package mvanbrummen.olifant.db

import javax.sql.DataSource

object DatabaseConnection {
    private val dataSources = mutableListOf<DataSource>()

    fun add(ds: DataSource) = dataSources.add(ds)

    fun getDataSource(): DataSource = dataSources.first()

}