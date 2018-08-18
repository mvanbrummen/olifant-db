package mvanbrummen.olifant.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.geometry.Orientation
import javafx.scene.layout.Priority
import mvanbrummen.olifant.Styles
import mvanbrummen.olifant.config.ConfigHelper
import mvanbrummen.olifant.controllers.DatabaseController
import mvanbrummen.olifant.controllers.DatabaseTreeContext
import mvanbrummen.olifant.controllers.PGSyntaxController
import mvanbrummen.olifant.controllers.QueryParserController
import mvanbrummen.olifant.db.DatabaseConnection
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import tornadofx.*
import java.time.Duration

const val HEIGHT = 600.0
const val WIDTH = 950.0

class MainView : View("OlifantDB") {

    val dbController: DatabaseController by inject()
    val syntaxController: PGSyntaxController by inject()
    val queryParserController: QueryParserController by inject()

    val dbTreeView = find(DatabaseTreeView::class)
    val connectionBar = find(ConnectionBar::class)
    val menuBar: MenuBar by inject()

    val dbTreeContext = find(DatabaseTreeContext::class)

    val data = FXCollections.observableArrayList<List<String>>()
    val tableview = tableview(data)

    val codeArea = CodeArea()

    init {
        if (ConfigHelper.isConnectionSaved(app.config)) {
            DatabaseConnection.add(ConfigHelper.getSavedConnectionDataSource(app.config))
        }
    }

    override val root = borderpane {
        setPrefSize(WIDTH, HEIGHT)

        top = menuBar.root

        center = splitpane {
            vbox {
                toolbar {
                    button("", FontAwesomeIconView(FontAwesomeIcon.PLUS)) {
                        action {
                            find(NewServerView::class).openModal()
                        }
                    }
                    button("", FontAwesomeIconView(FontAwesomeIcon.REFRESH)) {
                        action {
                            println("Refreshing tree...")


                            // TODO make it render correctly
                            dbTreeContext.clear()
                        }
                    }
                }

                this += dbTreeView.root
            }
            splitpane {
                tabpane {
                    tab("Query 1") {
                        vbox {
                            toolbar {
                                button("", FontAwesomeIconView(FontAwesomeIcon.PLAY)) {

                                    shortcut("F5")

                                    tooltip("Execute query")

                                    action {
                                        val codeAreaText = codeArea.text

                                        if (codeAreaText.isNotBlank()) {
                                            runAsync {
                                                val statements = queryParserController.parseQuery(codeAreaText)

                                                dbController.executeStatements(statements)
                                            } ui { entries ->

                                                // TODO handle collection of results

                                                entries.first().columns.first().forEachIndexed { colIndex, name ->
                                                    tableview.column(name, String::class) {
                                                        value { row ->
                                                            SimpleStringProperty(row.value[colIndex])
                                                        }
                                                    }
                                                }

                                                data.setAll(entries.first().columns.drop(1))
                                            }
                                        }
                                    }
                                }
                                button("", FontAwesomeIconView(FontAwesomeIcon.STOP)) {
                                    println("stopping query...")
                                }
                                separator {}
                                button("", FontAwesomeIconView(FontAwesomeIcon.COPY))
                                button("", FontAwesomeIconView(FontAwesomeIcon.PASTE))
                                separator {}
                                button("", FontAwesomeIconView(FontAwesomeIcon.SAVE))
                                button("", FontAwesomeIconView(FontAwesomeIcon.FILE))
                            }
                            this += connectionBar

                            val subscribe = codeArea
                                    .multiPlainChanges()
                                    .successionEnds(Duration.ofMillis(500))
                                    .subscribe { change ->
                                        codeArea.setStyleClass(0, codeArea.length, Styles.keyword.name)
                                        codeArea.setStyleSpans(0, syntaxController.computeHighlighting(codeArea.text))
                                    }

                            codeArea.paragraphGraphicFactory = LineNumberFactory.get(codeArea)

                            codeArea.prefHeightProperty().bind(this.heightProperty())

                            this += codeArea

                            // subscribe.unsubscribe() TODO
                        }
                    }
                }
                this += tableview

                orientation = Orientation.VERTICAL
            }
            orientation = Orientation.HORIZONTAL

            setDividerPositions(0.25, 0.75)

            vboxConstraints { vGrow = Priority.ALWAYS }
        }
    }

}



