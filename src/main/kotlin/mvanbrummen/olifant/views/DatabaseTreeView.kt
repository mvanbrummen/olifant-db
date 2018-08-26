package mvanbrummen.olifant.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.scene.control.TreeItem
import mvanbrummen.olifant.controllers.DatabaseTreeContext
import mvanbrummen.olifant.models.*
import tornadofx.View
import tornadofx.cellFormat
import tornadofx.populate
import tornadofx.treeview

class DatabaseTreeView : View() {

    val dbTreeContext: DatabaseTreeContext by inject()

    override val root = treeview<DatabaseTreeItem> {
        root = TreeItem(TreeRoot(dbTreeContext.databaseConnections))

        isShowRoot = false

        cellFormat {
            text = it.name

            graphic = when (it) {
                is DatabaseConnection -> FontAwesomeIconView(FontAwesomeIcon.SERVER)
                is Database -> FontAwesomeIconView(FontAwesomeIcon.DATABASE)
                is Schema -> FontAwesomeIconView(FontAwesomeIcon.TABLE)
                is Table -> FontAwesomeIconView(FontAwesomeIcon.TABLE)
                is TreeRoot -> FontAwesomeIconView(FontAwesomeIcon.DATABASE)
                is DatabaseRoot -> FontAwesomeIconView(FontAwesomeIcon.DATABASE)
                is RolesRoot -> FontAwesomeIconView(FontAwesomeIcon.USER)
                is Role -> FontAwesomeIconView(FontAwesomeIcon.USER)
                is SchemaRoot -> FontAwesomeIconView(FontAwesomeIcon.TABLE)
                else -> FontAwesomeIconView(FontAwesomeIcon.CIRCLE)
            }
        }

        populate { it.value.children }
    }
}