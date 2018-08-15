package mvanbrummen.olifant.controllers

import mvanbrummen.olifant.models.DBStatement
import mvanbrummen.olifant.models.QueryStatement
import mvanbrummen.olifant.models.UpdateStatement
import tornadofx.Controller

class QueryParserController : Controller() {

    fun parseQuery(text: String): List<DBStatement> {
        val statements = text.trim()
                .split(";")
                .map(String::trim)

        return statements
                .filter { s -> s.isNotEmpty() }
                .map { statement ->
                    if (statement.startsWith("select", true)) {
                        QueryStatement(statement)
                    } else {
                        UpdateStatement(statement)
                    }
                }
    }
}