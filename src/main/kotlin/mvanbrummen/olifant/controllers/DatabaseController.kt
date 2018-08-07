package mvanbrummen.olifant.controllers

import com.zaxxer.hikari.HikariDataSource
import tornadofx.Controller


class DatabaseController : Controller() {

    val ds = HikariDataSource().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        username = "postgres"
        password = "postgres"
    }

    fun executeQuery(queryString: String): List<List<String>> {
        println("Executing $queryString")

        val conn = ds.connection
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
}
