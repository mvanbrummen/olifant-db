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
                            is DatabaseConnection -> FontAwesomeIconView(FontAwesomeIcon.SERVER)
                            is Database -> FontAwesomeIconView(FontAwesomeIcon.DATABASE)
                            is Schema -> FontAwesomeIconView(FontAwesomeIcon.TABLE)
                            is Table -> FontAwesomeIconView(FontAwesomeIcon.TABLE)
                            TreeRoot -> FontAwesomeIconView(FontAwesomeIcon.DATABASE)
                            is DatabaseRoot -> FontAwesomeIconView(FontAwesomeIcon.DATABASE)
                            is RolesRoot -> FontAwesomeIconView(FontAwesomeIcon.USER)
                            is Role -> FontAwesomeIconView(FontAwesomeIcon.USER)
                            is SchemaRoot -> FontAwesomeIconView(FontAwesomeIcon.TABLE)
                            else -> FontAwesomeIconView(FontAwesomeIcon.CIRCLE)
                        }

                        onMouseClicked = when (it) {
                            TreeRoot -> EventHandler { mouseEvent ->
                                println("Tree root clicked: " + it.name)
                            }
                            is DatabaseConnection -> EventHandler { mouseEvent ->
                                println("DB connection clicked: " + it.name)

                            }
                            is Database -> EventHandler { mouseEvent ->
                                println("DB clicked: " + it.name)


                            }
                            is Schema -> EventHandler { mouseEvent ->
                                println("Schema clicked: " + it.name)


                            }
                            is Table -> EventHandler { mouseEvent ->
                                println("Table clicked: " + it.name)
                            }
                            is RolesRoot -> EventHandler { mouseEvent ->
                                println("Role clicked: " + it.name)

                                dbTreeContext.addRoleTreeItem(it.databaseConnectionName, mvanbrummen.olifant.db.DatabaseConnection.getDataSource())
                            }
                            is DatabaseRoot -> EventHandler { mouseEvent ->
                                println("DB Root clicked: " + it.name)

                                dbTreeContext.addDatabaseTreeItem(it.name, mvanbrummen.olifant.db.DatabaseConnection.getDataSource())
                            }
                            is SchemaRoot -> EventHandler { mouseEvent ->
                                println("Schema Root clicked: " + it.name)

                                dbTreeContext.addSchemaTreeItem(it.databaseName, mvanbrummen.olifant.db.DatabaseConnection.getDataSource())
                            }
                            is TableRoot -> EventHandler { mouseEvent ->
                                println("Table Root clicked: " + it.name)

                                dbTreeContext.addTableTreeItem(it.schemaName, mvanbrummen.olifant.db.DatabaseConnection.getDataSource())
                            }
                            else -> EventHandler {

                            }
                        }
                    }
                    populate { parent ->
                        val value = parent.value
                        when (value) {
                            TreeRoot -> dbTreeContext.databaseConnections
                            is DatabaseConnection -> listOf(DatabaseRoot(value.name), RolesRoot(value.name))
                            is Database -> listOf(SchemaRoot(value.name))
                            is SchemaRoot -> dbTreeContext.schemas.filter { it.databaseName == value.databaseName }
                            is Schema -> listOf(TableRoot(value.name), ViewRoot(value.name), SequenceRoot(value.name), FunctionRoot(value.name))
                            is Table -> emptyList()
                            is DatabaseRoot -> dbTreeContext.databases.filter { it.databaseConnectionName == value.databaseConnectionName }
                            is RolesRoot -> dbTreeContext.roles.filter { it.databaseConnectionName == value.databaseConnectionName }
                            is Role -> emptyList()
                            is TableRoot -> dbTreeContext.tables.filter { it.schemaName == value.schemaName }
                            else -> emptyList()
                        }
                    }
                }
            }
}