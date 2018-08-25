package mvanbrummen.olifant.controllers

import mvanbrummen.olifant.models.DBStatement
import mvanbrummen.olifant.models.QueryStatement
import mvanbrummen.olifant.models.UpdateStatement
import tornadofx.Controller
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource


typealias Columns = List<List<String>>

class DBException(override val message: String) : RuntimeException(message)
data class DatabaseResult(val columns: Columns = emptyList(), val rowsAffected: Int = 0, val message: String? = null)

const val FALLBACK_ERROR_MESSAGE = "Error running query"

class DatabaseController : Controller() {

    fun executeStatements(ds: DataSource, statements: List<DBStatement>): List<DatabaseResult> {
        return statements.map {
            when (it) {
                is QueryStatement -> executeQuery(ds, it.queryString)
                is UpdateStatement -> executeUpdate(ds, it.queryString)
            }
        }
    }

    private fun executeQuery(ds: DataSource, queryString: String): DatabaseResult {
        val rows = mutableListOf<List<String>>()

        var conn: Connection? = null

        try {
            conn = ds.connection

            val statement = conn.prepareStatement(queryString)
            val rs = statement.executeQuery()

            val metaData = rs.metaData
            val columnCount = metaData.columnCount

            val columns = mutableListOf<String>()

            for (i in 1..columnCount) {
                columns.add(metaData.getColumnLabel(i))
            }

            rows.add(columns)

            while (rs.next()) {
                val row = mutableListOf<String>()

                for (i in 1..columnCount) {
                    val res = rs.getObject(i)

                    row.add(res?.toString() ?: "null")
                }

                rows.add(row)
            }

            return DatabaseResult(rows, rows.size - 1)
        } catch (e: SQLException) {
            log.info(e.message)

            return DatabaseResult(message = e.message)
        } catch (e: Exception) {
            log.info(e.message)

            throw DBException(e.message ?: FALLBACK_ERROR_MESSAGE)
        } finally {
            try {
                if (conn !== null) {
                    conn.close()
                }
            } catch (e: Exception) {
                log.info(e.message)
            }
        }
    }

    private fun executeUpdate(ds: DataSource, queryString: String): DatabaseResult {
        var conn: Connection? = null
        try {
            conn = ds.connection
            val statement = conn.prepareStatement(queryString)

            return DatabaseResult(emptyList(), statement.executeUpdate())
        } catch (e: SQLException) {
            log.info(e.message)

            return DatabaseResult(message = e.message)
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

    fun getDatabases(ds: DataSource): List<String> = fetch(ds,
            "SELECT datname FROM pg_database WHERE datistemplate = false;", "datname")

    fun getSchemas(databaseName: String, ds: DataSource): List<String> = fetch(
            ds, "SELECT schema_name FROM information_schema.schemata;", "schema_name", databaseName)

    fun getTables(schemaName: String, ds: DataSource): List<String> = fetch(ds,
            "SELECT table_name FROM information_schema.tables WHERE table_schema = '$schemaName'",
            "table_name")

    fun getRoles(ds: DataSource): List<String> = fetch(ds, "SELECT rolname FROM pg_roles;", "rolname")

    private fun fetch(ds: DataSource, query: String, columnName: String, catalog: String? = null): List<String> {
        var conn: Connection? = null

        try {
            conn = ds.connection

            if (catalog != null) {
                conn.catalog = catalog
            }

            val statement = conn.prepareStatement(query)
            val rs = statement.executeQuery()

            val roles = mutableListOf<String>()

            while (rs.next()) {
                val result = rs.getString(columnName)

                if (result != null) {
                    roles.add(result)
                }
            }

            return roles
        } catch (e: SQLException) {
            log.info(e.message)

            throw DBException(e.message ?: FALLBACK_ERROR_MESSAGE)
        } catch (e: Exception) {
            log.info(e.message)

            throw DBException(e.message ?: FALLBACK_ERROR_MESSAGE)
        } finally {
            try {
                if (conn !== null) {
                    conn.close()
                }
            } catch (e: Exception) {
                log.info(e.message)
            }
        }
    }
}
