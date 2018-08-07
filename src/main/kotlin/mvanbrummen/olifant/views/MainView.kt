package mvanbrummen.olifant.views

import javafx.application.Platform
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.layout.BorderPane
import mvanbrummen.olifant.controllers.DatabaseController
import tornadofx.*
import java.lang.System.exit

class MainView : View("OlifantDB") {

    override val root = BorderPane()

    val input = SimpleStringProperty()

    val dbController: DatabaseController by inject()

    val data = FXCollections.observableArrayList<List<String>>()
    val tableview = tableview(data)

    init {
        with(root) {

            top = vbox {
                menubar {
                    menu("File") {
                        item("New") {
                            action {
                                find(NewServerView::class).openWindow()
                            }
                        }
                        item("Run") {
                            action {

                            }
                        }
                        item("Quit") {
                            action {
                                Platform.exit()
                                exit(0)
                            }
                        }
                    }

                }

                toolbar {
                    button("> Execute") {
                        action {
                            runAsync {
                                dbController.executeQuery(input.value)
                            } ui { entries ->
                                // Generate columns based on the first row
                                entries.first().forEachIndexed { colIndex, name ->
                                    tableview.column(name, String::class) {
                                        value { row ->
                                            SimpleStringProperty(row.value[colIndex])
                                        }
                                    }
                                }
                                // Assign the extracted entries to our list, skip first row
                                data.setAll(entries.drop(1))
                            }
                        }
                    }
                    button("Stop") {

                    }
                }
            }


            left = vbox {
                label("Database list")
            }

            center = vbox {
                textarea(input)
                tabpane {
                    tab("Query 1") {
                        tableview
                    }
                }
            }
        }
    }
}

