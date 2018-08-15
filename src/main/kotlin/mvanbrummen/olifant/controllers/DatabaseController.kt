package mvanbrummen.olifant.controllers

import mvanbrummen.olifant.db.DatabaseConnection
import mvanbrummen.olifant.models.DBStatement
import mvanbrummen.olifant.models.QueryStatement
import mvanbrummen.olifant.models.UpdateStatement
import tornadofx.Controller
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource


typealias Columns = List<List<String>>

class DatabaseController : Controller() {

    data class DatabaseResult(val columns: Columns, val rowsAffected: Int)

    fun executeStatements(statements: List<DBStatement>): List<DatabaseResult> {
        return statements.map {
            when (it) {
                is QueryStatement -> executeQuery(it.queryString)
                is UpdateStatement -> executeUpdate(it.queryString)
            }
        }
    }

    private fun executeQuery(queryString: String): DatabaseResult {
        val conn = DatabaseConnection.getDataSource().connection
        val statement = conn.prepareStatement(queryString)
        val rs = statement.executeQuery()

        val rows = mutableListOf<List<String>>()
        val metaData = rs.metaData
        val columnCount = metaData.columnCount

        val columns = mutableListOf<String>()

        for (i in 1..columnCount) {
            try {
                columns.add(metaData.getColumnLabel(i))
            } catch (e: Exception) {
                log.info("ERROR: at index $i: " + e.message)
            }
        }

        rows.add(columns)

        while (rs.next()) {
            val row = mutableListOf<String>()

            for (i in 1..columnCount) {
                try {
                    row.add(rs.getObject(i).toString())
                } catch (e: Exception) {
                    log.info("ERROR: at index $i: " + e.message)
                    row.add("error")
                }
            }

            rows.add(row)
        }

        rows.forEach {
            it.forEach(::println)
        }

        conn.close()

        return DatabaseResult(rows, rows.size - 1)
    }

    private fun executeUpdate(queryString: String): DatabaseResult {
        var conn: Connection? = null
        try {
            conn = DatabaseConnection.getDataSource().connection
            val statement = conn.prepareStatement(queryString)

            return DatabaseResult(emptyList(), statement.executeUpdate())
        } catch (e: SQLException) {
            log.info(e.message)
        } catch (e: Exception) {
            log.info(e.message)
        } finally {
            try {
                if (conn !== null) {
                    conn.close()
                }
            } catch (e: Exception) {

            }
        }
        return DatabaseResult(emptyList(), 0)
    }

    fun executeStoredProc(): Nothing = TODO()

    fun getDatabases(ds: DataSource): List<String> {
        val queryString = "SELECT datname FROM pg_database WHERE datistemplate = false;"

        val conn = ds.connection
        val statement = conn.prepareStatement(queryString)
        val rs = statement.executeQuery()

        val databases = mutableListOf<String>()

        while (rs.next()) {
            val result = rs.getString("datname")

            if (result != null) {
                databases.add(result)
            }
        }

        return databases
    }

    fun getSchemas(databaseName: String, ds: DataSource): List<String> {
        val queryString = "SELECT schema_name FROM information_schema.schemata;"

        val conn = ds.connection

        conn.catalog = databaseName

        val statement = conn.prepareStatement(queryString)
        val rs = statement.executeQuery()

        val schemas = mutableListOf<String>()

        while (rs.next()) {
            val result = rs.getString("schema_name")

            if (result != null) {
                schemas.add(result)
            }
        }

        return schemas
    }

    fun getTables(schemaName: String, ds: DataSource): List<String> {
        val queryString = "SELECT table_name FROM information_schema.tables WHERE table_schema = '$schemaName'"

        val conn = ds.connection
        val statement = conn.prepareStatement(queryString)
        val rs = statement.executeQuery()

        val tables = mutableListOf<String>()

        while (rs.next()) {
            val result = rs.getString("table_name")

            if (result != null) {
                tables.add(result)
            }
        }

        return tables
    }

    fun getRoles(ds: DataSource): List<String> {
        val queryString = "SELECT rolname FROM pg_roles;"

        val conn = ds.connection
        val statement = conn.prepareStatement(queryString)
        val rs = statement.executeQuery()

        val roles = mutableListOf<String>()

        while (rs.next()) {
            val result = rs.getString("rolname")

            if (result != null) {
                roles.add(result)
            }
        }

        return roles
    }
}
