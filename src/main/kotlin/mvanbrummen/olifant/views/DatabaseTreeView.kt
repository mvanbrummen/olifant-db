package mvanbrummen.olifant.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.event.EventHandler
import javafx.scene.control.TreeItem
import mvanbrummen.olifant.controllers.DatabaseTreeContext
import mvanbrummen.olifant.models.*
import tornadofx.*

class DatabaseTreeView : View() {

    val dbTreeContext: DatabaseTreeContext by inject()

    override val root =

            vbox {
                treeview<DatabaseTreeItem>(TreeItem(TreeRoot)) {

                    isShowRoot = false

                    cellFormat {
                        text = it.name

                        graphic = when (it) {
                            is DatabaseConnection -> FontAwesomeIconView(FontAwesomeIcon.DATABASE)
                            is Database -> FontAwesomeIconView(FontAwesomeIcon.DATABASE)
                            is Schema -> FontAwesomeIconView(FontAwesomeIcon.TABLE)
                            is Table -> FontAwesomeIconView(FontAwesomeIcon.TABLE)
                            else -> kotlin.error("Invalid value type")
                        }

                        onMouseClicked = when (it) {
                            TreeRoot -> EventHandler { mouseEvent ->
                                println("Tree root clicked: " + it.name)
                            }
                            is DatabaseConnection -> EventHandler { mouseEvent ->
                                println("DB connection clicked: " + it.name)
                                
                                // TODO shouldnt be called after already fetched from DB
                                dbTreeContext.addDatabaseTreeItem(it.name, mvanbrummen.olifant.db.DatabaseConnection.getDataSource())
                            }
                            is Database -> EventHandler { mouseEvent ->
                                println("DB clicked: " + it.name)

                                dbTreeContext.addSchemaTreeItem(it.name, mvanbrummen.olifant.db.DatabaseConnection.getDataSource())
                            }
                            is Schema -> EventHandler { mouseEvent ->
                                println("Schema clicked: " + it.name)

                                dbTreeContext.addTableTreeItem(it.name, mvanbrummen.olifant.db.DatabaseConnection.getDataSource())
                            }
                            is Table -> EventHandler { mouseEvent ->
                                println("Table clicked: " + it.name)
                            }
                        }
                    }
                    populate { parent ->
                        val value = parent.value
                        when (value) {
                            TreeRoot -> dbTreeContext.databaseConnections
                            is DatabaseConnection -> dbTreeContext.databases.filter { it.databaseConnectionName == value.name }
                            is Database -> dbTreeContext.schemas.filter { it.databaseName == value.name }
                            is Schema -> dbTreeContext.tables.filter { it.schemaName == value.name }
                            is Table -> emptyList()
                        }
                    }
                }
            }
}