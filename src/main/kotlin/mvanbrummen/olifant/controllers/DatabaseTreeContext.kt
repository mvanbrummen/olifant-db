package mvanbrummen.olifant.controllers

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import mvanbrummen.olifant.models.Database
import mvanbrummen.olifant.models.DatabaseConnection
import mvanbrummen.olifant.models.Schema
import tornadofx.Controller


class DatabaseTreeContext : Controller() {

    val databaseConnections = SimpleListProperty<DatabaseConnection>()

    init {
        databaseConnections.value = FXCollections.observableArrayList(
                DatabaseConnection(
                        "Test Connection",
                        setOf(
                                Database(
                                        "kgitforge",
                                        setOf(
                                                Schema("gitforge")
                                        )
                                )
                        )
                ),
                DatabaseConnection(
                        "Test Connection 2",
                        setOf(
                                Database(
                                        "postgres",
                                        setOf(
                                                Schema("test_schema")
                                        )
                                )
                        )
                )
        )
    }

}