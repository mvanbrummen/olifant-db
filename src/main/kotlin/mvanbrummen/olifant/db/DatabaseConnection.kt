package mvanbrummen.olifant.db

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

class DatabaseConnection {
    companion object {

        private val dataSources = mutableMapOf<String, DataSource>()

        fun add(connectionName: String, ds: DataSource) = dataSources.put(connectionName, ds)

        fun getDataSource(connectionName: String): DataSource? = dataSources[connectionName]

        fun createDataSource(host: String, port: Int, username: String, password: String?, databaseName: String) = HikariDataSource().apply {
            jdbcUrl = "jdbc:postgresql://$host:$port/$databaseName"
            this.username = username
            this.password = password ?: ""
        }
    }
}