package mvanbrummen.olifant.models;

import tornadofx.ViewModel
import tornadofx.getProperty
import tornadofx.property

class DatabaseConnectionInfo: ViewModel() {

    var connectionName by property<String>()
    fun connectionNameProperty() = getProperty(DatabaseConnectionInfo::connectionName)

    var username by property<String>()
    fun usernameProperty() = getProperty(DatabaseConnectionInfo::username)

    var password by property<String>()
    fun passwordProperty() = getProperty(DatabaseConnectionInfo::password)

    var host by property<String>()
    fun hostProperty() = getProperty(DatabaseConnectionInfo::host)

    var databaseName by property<String>()
    fun databaseNameProperty() = getProperty(DatabaseConnectionInfo::databaseName)

    var port by property<Int>()
    fun portProperty() = getProperty(DatabaseConnectionInfo::port)

    init {
        this.host = "localhost"
        this.port = 5432
        this.databaseName = "postgres"
    }

}