package mvanbrummen.olifant.views

import javafx.application.Platform
import mvanbrummen.olifant.config.ConfigHelper
import mvanbrummen.olifant.db.DatabaseConnection
import tornadofx.*

class MenuBar : View() {

    override val root = vbox {
        menubar {
            menu("File") {
                item("Preferences")
                item("New Query...") {
                    action {
                        // TODO move to the tree view and do properly
                        if (ConfigHelper.isConnectionSaved(app.config)) {
                            DatabaseConnection.add("test", ConfigHelper.getSavedConnectionDataSource(app.config))

                            fire(TabEvent("New Query ", ConfigHelper.getSavedConnectionDataSource(app.config)))
                        }
                    }
                }
                item("Quit") {
                    action {
                        Platform.exit()
                        System.exit(0)
                    }
                }
            }
            menu("Help") {
                item("About") {
                }
            }
        }
    }
}