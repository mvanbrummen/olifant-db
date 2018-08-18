package mvanbrummen.olifant.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.geometry.Orientation
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import mvanbrummen.olifant.Styles
import mvanbrummen.olifant.config.ConfigHelper
import mvanbrummen.olifant.controllers.DatabaseController
import mvanbrummen.olifant.controllers.DatabaseTreeContext
import mvanbrummen.olifant.controllers.PGSyntaxController
import mvanbrummen.olifant.controllers.QueryParserController
import mvanbrummen.olifant.db.DatabaseConnection
import mvanbrummen.olifant.util.ObservableStringBuffer
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import tornadofx.*
import java.time.Duration

const val HEIGHT = 600.0
const val WIDTH = 950.0

const val RESULT_TAB_INDEX = 0
const val MESSAGES_TAB_INDEX = 1

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

    val dbMessages = ObservableStringBuffer()

    val codeArea = CodeArea()

    val resultTabPane = TabPane()

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

                val treeView = dbTreeView.root

                treeView.prefHeightProperty().bind(this.heightProperty())

                this += treeView
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
                                        val codeAreaText = if (codeArea.selectedText.isNotEmpty()) codeArea.selectedText else codeArea.text

                                        if (codeAreaText.isNotBlank()) {
                                            runAsync {
                                                val statements = queryParserController.parseQuery(codeAreaText)

                                                dbController.executeStatements(statements)
                                            } ui { entries ->

                                                entries.forEach {
                                                    if (it.rowsAffected > 0) {
                                                        dbMessages.append("${it.rowsAffected} row(s) affected.")
                                                    }
                                                    if (it.message != null) {
                                                        dbMessages.append(it.message)
                                                    }
                                                }

                                                val lastEntryCols = entries.last().columns

                                                if (lastEntryCols.isNotEmpty()) {
                                                    lastEntryCols.first().forEachIndexed { colIndex, name ->
                                                        tableview.column(name, String::class) {
                                                            value { row ->
                                                                SimpleStringProperty(row.value[colIndex])
                                                            }
                                                        }
                                                    }

                                                    data.setAll(entries.last().columns.drop(1))

                                                    resultTabPane.selectionModel.select(RESULT_TAB_INDEX)
                                                } else {
                                                    resultTabPane.selectionModel.select(MESSAGES_TAB_INDEX)
                                                }
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
                                separator {}
                                button("", FontAwesomeIconView(FontAwesomeIcon.TIMES)) {
                                    tooltip("Clear editor")

                                    action {
                                        codeArea.clear()
                                    }
                                }
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
                this += resultTabPane.apply {
                    tab("DB Results") {
                        this += tableview
                    }
                    tab("Messages") {
                        textarea {
                            textProperty().bind(dbMessages)

                            isEditable = false

                            addClass(Styles.dbMessages)
                        }
                    }
                }

                orientation = Orientation.VERTICAL
            }
            orientation = Orientation.HORIZONTAL

            setDividerPositions(0.25, 0.75)

            vboxConstraints { vGrow = Priority.ALWAYS }
        }
    }

}



