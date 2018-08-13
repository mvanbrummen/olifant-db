package mvanbrummen.olifant.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.scene.control.TreeItem
import mvanbrummen.olifant.controllers.DatabaseTreeContext
import mvanbrummen.olifant.models.*
import tornadofx.*

class DatabaseTreeView : View() {

    val dbTreeContext: DatabaseTreeContext by inject()

    val dbConnections = dbTreeContext.databaseConnections

    override val root =

            vbox {
                treeview<DatabaseTreeItem>(TreeItem(TreeRoot)) {

                    isShowRoot = false

                    cellFormat {
                        text = it.name

                        graphic = when (it) {
                            is DatabaseConnection -> FontAwesomeIconView(FontAwesomeIcon.SERVER)
                            is Database -> FontAwesomeIconView(FontAwesomeIcon.FOLDER)
                            is Schema -> FontAwesomeIconView(FontAwesomeIcon.TABLE)
                            else -> kotlin.error("Invalid value type")
                        }
                    }
                    populate { parent ->
                        val value = parent.value
                        when (value) {
                            TreeRoot -> dbConnections
                            is DatabaseConnection -> value.databases
                            is Database -> value.schemas
                            is Schema -> emptyList()
                        }
                    }
                }
            }
}