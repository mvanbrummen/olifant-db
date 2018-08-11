package mvanbrummen.olifant.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.scene.control.TreeItem
import tornadofx.View
import tornadofx.cellFormat
import tornadofx.populate
import tornadofx.treeview

class DatabaseTreeView : View() {
    data class Department(val name: String)
    data class Person(val name: String, val department: String)

    val persons = listOf(
            Person("postgres", "gitforge"),
            Person("users", "gitforge"),
            Person("postgres", "micro_machines"),
            Person("accounts", "gitforge"),
            Person("pg_active", "system"),
            Person("entitlements", "micro_machines"),
            Person("repository", "gitforge")
    )

    val departments = persons.groupBy { Department(it.department) }

    override val root = treeview<Any> {
        root = TreeItem("Everest", FontAwesomeIconView(FontAwesomeIcon.TABLE))
        cellFormat {
            text = when (it) {
                is String -> it
                is Department -> it.name
                is Person -> it.name
                else -> kotlin.error("Invalid value type")
            }
            graphic = when (it) {
                is String -> FontAwesomeIconView(FontAwesomeIcon.SERVER)
                is Department -> FontAwesomeIconView(FontAwesomeIcon.FOLDER)
                is Person -> FontAwesomeIconView(FontAwesomeIcon.TABLE)
                else -> kotlin.error("Invalid value type")
            }
        }
        populate { parent ->
            val value = parent.value
            when {
                parent == root -> departments.keys
                value is Department -> departments[value]
                else -> null
            }
        }
    }

}
