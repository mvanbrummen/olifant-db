package mvanbrummen.olifant.views

import com.zaxxer.hikari.HikariDataSource
import tornadofx.*

class NewServerView : Fragment("Create new connection") {
    override val root = Form()

    val databaseConnection = DatabaseConnection("", "")

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
            button("Connect") {

                action {
                    val ds = HikariDataSource().apply {
                        jdbcUrl = "jdbc:postgresql://${databaseConnection.hostProperty().get()}:${databaseConnection.portProperty().get()}/${databaseConnection.databaseNameProperty().get()}"
                        username = databaseConnection.usernameProperty().get()
                        password = databaseConnection.passwordProperty().get()
                        driverClassName = "org.postgresql.ds.PGSimpleDataSource"
                    }

                    val connection = ds.connection
                    val statement = connection.createStatement()
                    val resultSet = statement.execute("SELECT 1")
                    println("DB returned $resultSet")

                    println("Connected!")

                    connection.close()
                }

                disableProperty().bind(databaseConnection.usernameProperty().isNull.or(databaseConnection.passwordProperty().isNull))
            }
        }
    }

    class DatabaseConnection(username: String?,
                             password: String?,
                             host: String = "localhost",
                             port: Int = 5432,
                             databaseName: String = "postgres") {
        var username by property<String>()
        fun usernameProperty() = getProperty(DatabaseConnection::username)

        var password by property<String>()
        fun passwordProperty() = getProperty(DatabaseConnection::password)

        var host by property<String>()
        fun hostProperty() = getProperty(DatabaseConnection::host)

        var databaseName by property<String>()
        fun databaseNameProperty() = getProperty(DatabaseConnection::databaseName)

        var port by property<Int>()
        fun portProperty() = getProperty(DatabaseConnection::port)

        init {
            this.username = username
            this.password = password
            this.host = host
            this.port = port
            this.databaseName = databaseName
        }
    }
}