package mvanbrummen.olifant.controllers

import mvanbrummen.olifant.db.DatabaseConnection
import tornadofx.Controller
import javax.sql.DataSource


class DatabaseController : Controller() {

    fun executeQuery(queryString: String): List<List<String>> {
        println("Executing $queryString")

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
                println("ERROR: at index $i: " + e.message)
                println(metaData.getColumnLabel(i) + "\n\n")
            }
        }

        rows.add(columns)

        while (rs.next()) {
            val row = mutableListOf<String>()

            for (i in 1..columnCount) {
                try {
                    row.add(rs.getObject(i).toString())
                } catch (e: Exception) {
                    println("ERROR: at index $i: " + e.message)
                    println(metaData.getColumnLabel(i) + "\n\n")
                    row.add("error")
                }
            }

            rows.add(row)
        }

        rows.forEach {
            it.forEach(::println)
        }

        conn.close()

        return rows
    }

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
}
