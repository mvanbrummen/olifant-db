package mvanbrummen.olifant.controllers

import javafx.collections.FXCollections
import mvanbrummen.olifant.models.Database
import mvanbrummen.olifant.models.DatabaseConnection
import mvanbrummen.olifant.models.Schema
import mvanbrummen.olifant.models.Table
import tornadofx.Controller
import javax.sql.DataSource


class DatabaseTreeContext : Controller() {

    val databaseController: DatabaseController by inject()

    val databaseConnections = FXCollections.observableArrayList<DatabaseConnection>()
    val databases = FXCollections.observableArrayList<Database>()
    val schemas = FXCollections.observableArrayList<Schema>()
    val tables = FXCollections.observableArrayList<Table>()

    fun addDatabaseConnectionTreeItem(connectionName: String) {
        databaseConnections.setAll(DatabaseConnection(connectionName))
    }

    //TODO fix re-rendering of all below

    fun addDatabaseTreeItem(connectionName: String, ds: DataSource) {
        runAsync {
            databaseController.getDatabases(ds)
        } ui {
            addDatabaseConnectionTreeItem(connectionName) // TODO remove
            databases.setAll(FXCollections.observableArrayList(
                    it.map { Database(it, connectionName) }.toSet())
            )
        }
    }

    fun addSchemaTreeItem(databaseName: String, ds: DataSource) {
        runAsync {
            databaseController.getSchemas(databaseName, ds)
        } ui {
            schemas.setAll(FXCollections.observableArrayList(
                    it.map { Schema(it, databaseName) }.toSet())
            )
        }

    }

    fun addTableTreeItem(schemaName: String, ds: DataSource) {
        runAsync {
            databaseController.getTables(schemaName, ds)
        } ui {
            tables.setAll(FXCollections.observableArrayList(
                    it.map { Table(it, schemaName) }.toSet())
            )
        }

    }
}