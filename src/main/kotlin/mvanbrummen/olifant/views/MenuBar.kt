package mvanbrummen.olifant.views

import javafx.application.Platform
import tornadofx.*

class MenuBar : View() {
    override val root = vbox {
        menubar {
            menu("File") {
                item("Preferences")
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