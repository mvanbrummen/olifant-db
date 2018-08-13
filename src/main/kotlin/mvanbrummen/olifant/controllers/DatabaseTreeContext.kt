package mvanbrummen.olifant.controllers

import javafx.collections.FXCollections
import mvanbrummen.olifant.models.Database
import mvanbrummen.olifant.models.DatabaseConnection
import tornadofx.Controller
import javax.sql.DataSource


class DatabaseTreeContext : Controller() {

    val databaseController: DatabaseController by inject()
    val databaseConnections = FXCollections.observableArrayList<DatabaseConnection>()

    fun addDatabaseTreeItem(connectionName: String, ds: DataSource) {
        runAsync {
            databaseController.getDatabases(ds)
        } ui {
            databaseConnections.setAll(FXCollections.observableArrayList(
                    DatabaseConnection(connectionName,
                            it.map { Database(it, emptySet()) }.toSet()
                    )
            )
            )
        }
    }

}