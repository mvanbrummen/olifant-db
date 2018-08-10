package mvanbrummen.olifant.views

import javafx.beans.property.SimpleStringProperty
import mvanbrummen.olifant.Styles
import mvanbrummen.olifant.db.DatabaseConnection
import org.postgresql.ds.PGSimpleDataSource
import tornadofx.*

const val TEST_CONNECTION = "select 1"

class NewServerView : Fragment("Create new connection") {
    override val root = Form()

    private val databaseConnection = DatabaseConnectionInfo("", "")

    val labelText = SimpleStringProperty()
    val testConnectionLabel = label(labelText)

    init {
        title = "New Connection"

        with(root) {
            fieldset("Connection Information") {
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

            hbox {
                button("Connect") {
                    action {
                        val ds = getDataSource()

                        DatabaseConnection.add(ds)
                    }

                    disableProperty().bind(databaseConnection.usernameProperty().isNull.or(databaseConnection.passwordProperty().isNull))
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
                testConnectionLabel
            }
        }
    }

    private fun getDataSource() = PGSimpleDataSource().apply {
        url = "jdbc:postgresql://${databaseConnection.hostProperty().get()}:${databaseConnection.portProperty().get()}/${databaseConnection.databaseNameProperty().get()}"
        user = databaseConnection.usernameProperty().get()
        password = databaseConnection.passwordProperty().get()
    }

    class DatabaseConnectionInfo(username: String?,
                                 password: String?,
                                 host: String = "localhost",
                                 port: Int = 5432,
                                 databaseName: String = "postgres") {
        var username by property<String>()
        fun usernameProperty() = getProperty(DatabaseConnectionInfo::username)

        var password by property<String>()
        fun passwordProperty() = getProperty(DatabaseConnectionInfo::password)

        var host by property<String>()
        fun hostProperty() = getProperty(DatabaseConnectionInfo::host)

        var databaseName by property<String>()
        fun databaseNameProperty() = getProperty(DatabaseConnectionInfo::databaseName)

        var port by property<Int>()
        fun portProperty() = getProperty(DatabaseConnectionInfo::port)

        init {
            this.username = username
            this.password = password
            this.host = host
            this.port = port
            this.databaseName = databaseName
        }
    }
}