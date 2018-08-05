package mvanbrummen.olifant.views

import javafx.collections.FXCollections
import javafx.scene.layout.BorderPane
import tornadofx.*
import java.time.LocalDate

class MainView : View("OlifantDB") {

    override val root = BorderPane()

    private val persons = FXCollections.observableArrayList<Person>(
            Person(1, "Samantha Stuart", LocalDate.of(1981, 12, 4)),
            Person(2, "Tom Marks", LocalDate.of(2001, 1, 23)),
            Person(3, "Stuart Gills", LocalDate.of(1989, 5, 23)),
            Person(3, "Nicole Williams", LocalDate.of(1998, 8, 11))
    )

    init {
        with(root) {

            top = menubar {
                menu("File") {
                    menu("Connect") {
                        item("Facebook")
                        item("Twitter")
                    }
                    item("Save")
                    item("Quit")
                }
                menu("Edit") {
                    item("Copy")
                    item("Paste")
                }
            }

            left = vbox {
                label("Database list")
            }

            center = vbox {
                textarea {

                }
                tableview(persons) {
                    column("ID", Person::idProperty)
                    column("Name", Person::nameProperty)
                    column("Birthday", Person::birthdayProperty)
                }
            }
        }
    }
}

class Person(id: Int, name: String, birthday: LocalDate) {
    var id by property<Int>()
    fun idProperty() = getProperty(Person::id)

    var name by property<String>()
    fun nameProperty() = getProperty(Person::name)

    var birthday by property<LocalDate>()
    fun birthdayProperty() = getProperty(Person::birthday)


    init {
        this.id = id
        this.name = name
        this.birthday = birthday
    }
}