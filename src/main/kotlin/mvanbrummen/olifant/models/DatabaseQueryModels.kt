package mvanbrummen.olifant.models

sealed class DBStatement(open val queryString: String)
data class QueryStatement(override val queryString: String) : DBStatement(queryString)
data class UpdateStatement(override val queryString: String) : DBStatement(queryString)
