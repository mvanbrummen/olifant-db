package mvanbrummen.olifant.models

sealed class DatabaseTreeItem(open val name: String)
object TreeRoot : DatabaseTreeItem("Dummy")
data class DatabaseConnection(override val name: String, val databases: Set<Database>) : DatabaseTreeItem(name)
data class Database(override val name: String, val schemas: Set<Schema>) : DatabaseTreeItem(name)
data class Schema(override val name: String) : DatabaseTreeItem(name)
