package mvanbrummen.olifant.models

sealed class DatabaseTreeItem(open val name: String)
object TreeRoot : DatabaseTreeItem("Dummy")

data class DatabaseConnection(override val name: String) : DatabaseTreeItem(name)
data class Database(override val name: String, val databaseConnectionName: String) : DatabaseTreeItem(name)
data class Schema(override val name: String, val databaseName: String) : DatabaseTreeItem(name)
data class Table(override val name: String, val schemaName: String) : DatabaseTreeItem(name)
data class Role(override val name: String, val databaseConnectionName: String) : DatabaseTreeItem(name)

class DatabaseRoot(val databaseConnectionName: String) : DatabaseTreeItem("Databases")
class RolesRoot(val databaseConnectionName: String) : DatabaseTreeItem("Roles")