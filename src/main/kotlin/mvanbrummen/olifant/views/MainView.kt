package mvanbrummen.olifant.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import mvanbrummen.olifant.controllers.DatabaseController
import tornadofx.*
import java.lang.System.exit

const val HEIGHT = 600.0
const val WIDTH = 950.0

class MainView : View("OlifantDB") {

    val dbController: DatabaseController by inject()

    val input = SimpleStringProperty()
    val data = FXCollections.observableArrayList<List<String>>()
    val tableview = tableview(data)

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
                button("Stop", FontAwesomeIconView(FontAwesomeIcon.STOP)) {
                    println("stopping query...")
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
                    }
                }
            }
        }

        center = vbox {
            tabpane {
                tab("Query 1") {
                    textarea(input)
                }
            }
            tableview
        }
    }

}

