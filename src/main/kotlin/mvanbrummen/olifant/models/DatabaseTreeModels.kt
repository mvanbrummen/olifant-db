package mvanbrummen.olifant.models

sealed class DatabaseTreeItem(open val name: String, open val children: List<DatabaseTreeItem> = emptyList())
data class TreeRoot(override val children: List<DatabaseConnection>) : DatabaseTreeItem("Dummy", children)

data class DatabaseConnection(
        override val name: String,
        override val children: List<DatabaseTreeItem> = listOf(DatabaseRoot(name), RolesRoot(name))
) : DatabaseTreeItem(name, children)


data class Database(
        override val name: String,
        val databaseConnectionName: String,
        override val children: List<DatabaseTreeItem>
) : DatabaseTreeItem(name, children)

data class Role(override val name: String, val databaseConnectionName: String) : DatabaseTreeItem(name)

data class Schema(
        override val name: String,
        val databaseName: String,
        override val children: List<DatabaseTreeItem>
) : DatabaseTreeItem(name, children)

data class Table(override val name: String, val schemaName: String) : DatabaseTreeItem(name)

data class DatabaseRoot(
        val databaseConnectionName: String,
        override val children: List<Database> = emptyList()
) : DatabaseTreeItem("Databases", children)

data class RolesRoot(
        val databaseConnectionName: String,
        override val children: List<Role> = emptyList()
) : DatabaseTreeItem("Roles", children)

data class SchemaRoot(
        val databaseName: String,
        override val children: List<DatabaseTreeItem>
) : DatabaseTreeItem("Schemas", children)


data class TableRoot(
        val schemaName: String,
        override val children: List<DatabaseTreeItem>
) : DatabaseTreeItem("Tables", children)

data class ViewRoot(val schemaName: String) : DatabaseTreeItem("Views")
data class SequenceRoot(val schemaName: String) : DatabaseTreeItem("Sequences")
data class FunctionRoot(val schemaName: String) : DatabaseTreeItem("Functions")