package mvanbrummen.olifant.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.geometry.Orientation
import javafx.scene.control.TabPane
import javafx.stage.FileChooser
import mvanbrummen.olifant.Styles
import mvanbrummen.olifant.controllers.DatabaseController
import mvanbrummen.olifant.controllers.FileController
import mvanbrummen.olifant.controllers.PGSyntaxController
import mvanbrummen.olifant.controllers.QueryParserController
import mvanbrummen.olifant.util.ObservableStringBuffer
import org.fxmisc.richtext.CodeArea
import org.fxmisc.richtext.LineNumberFactory
import tornadofx.*
import java.time.Duration

class QueryPaneView : View() {

    private val extensionFilters = arrayOf(FileChooser.ExtensionFilter("SQL Scripts (*.sql)", "*.sql"))

    val dbController: DatabaseController by inject()
    val queryParserController: QueryParserController by inject()
    val fileController: FileController by inject()
    val syntaxController: PGSyntaxController by inject()

    val connectionBar = find(ConnectionBar::class)

    val data = FXCollections.observableArrayList<List<String>>()
    val tableview = tableview(data)

    val dbMessages = ObservableStringBuffer()
    val codeArea = CodeArea()
    val resultTabPane = TabPane()

    override val root = splitpane {
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
                        button("", FontAwesomeIconView(FontAwesomeIcon.SAVE)) {
                            tooltip("Save file")

                            action {
                                val files = chooseFile("Save File", extensionFilters, FileChooserMode.Save)

                                fileController.saveTextToFile(files, codeArea.text)
                            }
                        }
                        button("", FontAwesomeIconView(FontAwesomeIcon.FILE)) {

                            tooltip("Open file")

                            action {
                                val files = chooseFile("Select File", extensionFilters, FileChooserMode.Single)

                                val text = fileController.readTextFromFile(files)

                                if (text != null) codeArea.replaceText(text)
                            }
                        }
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
                    codeArea.addClass(Styles.editorText)

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
}