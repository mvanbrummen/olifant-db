package mvanbrummen.olifant.db

import javax.sql.DataSource

class DatabaseConnection {
    companion object {

        private val dataSources = mutableListOf<DataSource>()

        fun add(ds: DataSource) = dataSources.add(ds)

        fun getDataSource(): DataSource = dataSources.first()
    }

}