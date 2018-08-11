package mvanbrummen.olifant.controllers

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import mvanbrummen.olifant.models.DatabaseConnection
import tornadofx.Controller
import javax.sql.DataSource


class DatabaseTreeContext : Controller() {

    val databaseController: DatabaseController by inject()
    val databaseConnections = SimpleListProperty<DatabaseConnection>()

    fun addDatabaseTreeItem(ds: DataSource) {
        runAsync {
            databaseController.getDatabases(ds)
        } ui {

            println("Refreshing db tree context..")
            databaseConnections.value = FXCollections.observableArrayList(
                    it.map { DatabaseConnection(it, emptySet()) }
            )
        }
    }

}