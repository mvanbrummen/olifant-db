package mvanbrummen.olifant.views

import javafx.beans.property.SimpleStringProperty
import mvanbrummen.olifant.Styles
import mvanbrummen.olifant.config.ConfigHelper
import mvanbrummen.olifant.controllers.DatabaseTreeContext
import mvanbrummen.olifant.db.DatabaseConnection
import mvanbrummen.olifant.models.DatabaseConnectionInfo
import tornadofx.*

const val TEST_CONNECTION = "select 1"

class NewServerView : Fragment("Create new connection") {
    override val root = Form()

    private val databaseConnection: DatabaseConnectionInfo by inject()

    private val databaseTreeContext: DatabaseTreeContext by inject()

    val labelText = SimpleStringProperty()
    val testConnectionLabel = label(labelText)

    init {
        title = "New Connection"

        with(root) {
            fieldset("Connection Information") {
                field("Connection Name") {
                    textfield().bind(databaseConnection.connectionNameProperty())
                }

                field("Host") {
                    textfield().bind(databaseConnection.hostProperty())
                }

                field("Port") {
                    textfield().bind(databaseConnection.portProperty())
                }

                field("DB Name") {
                    textfield().bind(databaseConnection.databaseNameProperty())
                }

                field("User Name") {
                    textfield().bind(databaseConnection.usernameProperty())
                }

                field("Password") {
                    passwordfield().bind(databaseConnection.passwordProperty())
                }

            }

            vbox {
                hbox {
                    button("Connect") {
                        action {
                            val ds = getDataSource()

                            try {
                                val conn = ds.connection
                                conn.createStatement().executeQuery(TEST_CONNECTION)
                                conn.close()

                                DatabaseConnection.add(databaseConnection.connectionName, ds)

                                databaseTreeContext.addDatabaseConnectionTreeItem(databaseConnection.connectionName)

                                ConfigHelper.saveConnection(app.config, databaseConnection)

                                close()
                            } catch (e: Exception) {
                                log.info(e.message)

                                with(testConnectionLabel) {
                                    removeClass(Styles.successText)
                                    addClass(Styles.errorText)
                                }
                                labelText.value = "Failed to connect"
                            }
                        }

                        disableProperty().bind(databaseConnection.usernameProperty().isNull.or(databaseConnection.hostProperty().isNull
                                .or(databaseConnection.portProperty().isNull.or(databaseConnection.connectionNameProperty().isNull))))
                    }
                    button("Test connection") {
                        action {
                            val ds = getDataSource()

                            labelText.value = try {
                                val conn = ds.connection
                                conn.createStatement().executeQuery(TEST_CONNECTION)
                                conn.close()

                                with(testConnectionLabel) {
                                    removeClass(Styles.errorText)
                                    addClass(Styles.successText)
                                }

                                "Successfully connected!"
                            } catch (e: Exception) {
                                with(testConnectionLabel) {
                                    removeClass(Styles.successText)
                                    addClass(Styles.errorText)
                                }
                                "Failed to connect: ${e.message}"
                            }
                        }
                    }
                }
                this += testConnectionLabel
            }
        }
    }

    private fun getDataSource() = DatabaseConnection.createDataSource(
            databaseConnection.host,
            databaseConnection.port,
            databaseConnection.username,
            databaseConnection.password,
            databaseConnection.databaseName
    )
}