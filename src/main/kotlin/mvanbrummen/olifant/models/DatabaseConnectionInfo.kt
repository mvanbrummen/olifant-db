package mvanbrummen.olifant.models;

import tornadofx.getProperty
import tornadofx.property

class DatabaseConnectionInfo(username: String?,
                             password: String?,
                             host: String = "localhost",
                             port: Int = 5432,
                             databaseName: String = "postgres") {

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
        this.username = username
        this.password = password
        this.host = host
        this.port = port
        this.databaseName = databaseName
    }
}