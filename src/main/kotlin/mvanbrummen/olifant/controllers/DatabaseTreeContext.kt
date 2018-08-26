package mvanbrummen.olifant.controllers

import javafx.collections.FXCollections
import mvanbrummen.olifant.models.*
import tornadofx.Controller
import javax.sql.DataSource


class DatabaseTreeContext : Controller() {

    val databaseController: DatabaseController by inject()

    val databaseConnections = FXCollections.observableArrayList<DatabaseConnection>()

    fun addDatabaseConnectionTreeItem(connectionName: String, ds: DataSource) {
        databaseConnections.add(
                DatabaseConnection(
                        connectionName,
                        getDatabaseConnectionChildren(connectionName, ds)
                )
        )
    }

    fun getDatabaseConnectionChildren(connectionName: String, ds: DataSource) =
            listOf(
                    DatabaseRoot(connectionName, getDatabases(connectionName, ds)),
                    RolesRoot(connectionName, getRoles(connectionName, ds))
            )

    fun getDatabases(connectionName: String, ds: DataSource): List<Database> =
            databaseController.getDatabases(ds).map { Database(it, connectionName, getDatabaseChildren(it, ds)) }

    fun getRoles(connectionName: String, ds: DataSource): List<Role> =
            databaseController.getRoles(ds).map { Role(it, connectionName) }

    fun getDatabaseChildren(dbName: String, ds: DataSource): List<DatabaseTreeItem> {
        return listOf(getSchema(dbName, ds))
    }

    fun getSchema(dbName: String, ds: DataSource) =
            SchemaRoot(dbName, databaseController.getSchemas(dbName, ds).map {
                Schema(it, dbName, getSchemaChildren(it, ds))
            })

    fun getSchemaChildren(schemaName: String, ds: DataSource) =
            listOf(
                    TableRoot(schemaName, getTables(schemaName, ds)),
                    ViewRoot(schemaName),
                    FunctionRoot(schemaName),
                    SequenceRoot(schemaName)
            )

    fun getTables(schemaName: String, ds: DataSource) =
            databaseController.getTables(schemaName, ds).map { Table(it, schemaName) }


}
