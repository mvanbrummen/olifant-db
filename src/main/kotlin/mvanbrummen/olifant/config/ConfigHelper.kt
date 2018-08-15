package mvanbrummen.olifant.config

import mvanbrummen.olifant.models.DatabaseConnectionInfo
import org.postgresql.ds.PGSimpleDataSource
import tornadofx.ConfigProperties
import javax.sql.DataSource

object ConfigHelper {

    val CONNECTION_NAME_KEY = "connectionName"
    val HOST_KEY = "host"
    val PORT_KEY = "port"
    val USERNAME_KEY = "username"
    val PASSWORD_KEY = "password"
    val DATABASE_NAME_KEY = "databaseName"

    fun isConnectionSaved(config: ConfigProperties) = (config.string(CONNECTION_NAME_KEY) !== null)

    fun getSavedConnectionName(config: ConfigProperties) = config.string(CONNECTION_NAME_KEY)

    fun getSavedConnectionDataSource(config: ConfigProperties): DataSource {
        val host = config.string(HOST_KEY)
        val port = config.int(PORT_KEY)
        val username = config.string(USERNAME_KEY)
        val pword = if (config.string(PASSWORD_KEY) == null) "" else config.string(PASSWORD_KEY)
        val databaseName = config.string(DATABASE_NAME_KEY)

        return PGSimpleDataSource().apply {
            url = "jdbc:postgresql://$host:$port/$databaseName"
            user = username
            password = pword
        }
    }

    fun saveConnection(config: ConfigProperties, databaseConnection: DatabaseConnectionInfo) {
        with(config) {
            set(CONNECTION_NAME_KEY to databaseConnection.connectionName)
            set(HOST_KEY to databaseConnection.host)
            set(PORT_KEY to databaseConnection.port)
            set(USERNAME_KEY to databaseConnection.username)

            if (databaseConnection.password !== null) {
                set(PASSWORD_KEY to databaseConnection.password)
            }

            set(DATABASE_NAME_KEY to databaseConnection.databaseName)
            save()
        }
    }

}