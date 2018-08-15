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

class SchemaRoot(val databaseName: String): DatabaseTreeItem("Schemas")

class TableRoot(val schemaName: String): DatabaseTreeItem("Tables")
class ViewRoot(val schemaName: String): DatabaseTreeItem("Views")
class SequenceRoot(val schemaName: String): DatabaseTreeItem("Sequences")
class FunctionRoot(val schemaName: String): DatabaseTreeItem("Functions")