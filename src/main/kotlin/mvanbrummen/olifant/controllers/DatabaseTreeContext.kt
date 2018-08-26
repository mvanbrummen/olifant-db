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
                DatabaseConnection
                (
                        connectionName,
                        listOf(
                                DatabaseRoot(connectionName, getDatabases(connectionName, ds)),
                                RolesRoot(connectionName, getRoles(connectionName, ds))
                        )
                )
        )
    }

    fun getDatabases(connectionName: String, ds: DataSource) =
            databaseController.getDatabases(ds).map { Database(it, connectionName) }

    fun getRoles(connectionName: String, ds: DataSource) =
            databaseController.getRoles(ds).map { Role(it, connectionName) }

}