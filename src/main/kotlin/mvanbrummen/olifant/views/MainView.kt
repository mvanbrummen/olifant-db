package mvanbrummen.olifant.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.text.Font
import mvanbrummen.olifant.controllers.DatabaseController
import mvanbrummen.olifant.controllers.DatabaseTreeContext
import mvanbrummen.olifant.db.DatabaseConnection
import org.postgresql.ds.PGSimpleDataSource
import tornadofx.*
import java.lang.System.exit

const val HEIGHT = 600.0
const val WIDTH = 950.0

class MainView : View("OlifantDB") {

    val dbController: DatabaseController by inject()

    val dbTreeView = find(DatabaseTreeView::class)

    val dbTreeContext = find(DatabaseTreeContext::class)

    val input = SimpleStringProperty()
    val data = FXCollections.observableArrayList<List<String>>()
    val tableview = tableview(data)

    init {
        if (app.config.string("connectionName") !== null) {
            val host = app.config.string("host")
            val port = app.config.int("port")
            val username = app.config.string("username")
            val pword = if (app.config.string("password") == null) "" else app.config.string("password")
            val databaseName = app.config.string("databaseName")

            DatabaseConnection.add(
                    PGSimpleDataSource().apply {
                        url = "jdbc:postgresql://$host:$port/$databaseName"
                        user = username
                        password = pword
                    }
            )
        }
    }

    override val root = borderpane {
        setPrefSize(WIDTH, HEIGHT)

        top = vbox {
            menubar {
                menu("File") {
                    item("Preferences")
                    item("Quit") {
                        action {
                            Platform.exit()
                            exit(0)
                        }
                    }
                }
                menu("Help") {
                    item("About") {
                    }
                }
            }


        }

        left = vbox {
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

        center = vbox {
            tabpane {
                tab("Query 1") {
                    vbox {
                        toolbar {
                            button("Execute", FontAwesomeIconView(FontAwesomeIcon.PLAY)) {

                                action {
                                    if (input.value.isNotBlank()) {
                                        runAsync {
                                            dbController.executeQuery(input.value)
                                        } ui { entries ->

                                            entries.first().forEachIndexed { colIndex, name ->
                                                tableview.column(name, String::class) {
                                                    value { row ->
                                                        SimpleStringProperty(row.value[colIndex])
                                                    }
                                                }
                                            }

                                            data.setAll(entries.drop(1))
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
                        textarea(input) {
                            font = Font.font("Monospaced", 14.0)
                        }
                    }
                }
            }
            this += tableview
        }

    }


}



