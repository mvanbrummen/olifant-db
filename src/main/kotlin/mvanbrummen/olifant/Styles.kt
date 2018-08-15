package mvanbrummen.olifant

import javafx.scene.paint.Color
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import javafx.scene.text.FontWeight
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val welcomeScreen by cssclass()
        val content by cssclass()
        val heading by cssclass()
        val errorText by cssclass()
        val successText by cssclass()
        val keyword by cssclass()
    }

    init {
        welcomeScreen {
            padding = box(10.px)
            backgroundColor += LinearGradient(0.0, 0.0, 0.0, 1.0, true, CycleMethod.NO_CYCLE, Stop(0.0, c("#028aff")), Stop(1.0, c("#003780")))
            heading {
                fontSize = 3.em
                textFill = Color.WHITE
                fontWeight = FontWeight.BOLD
            }
            content {
                padding = box(25.px)
                button {
                    fontSize = 22.px
                }
            }
        }
        errorText {
            textFill = Color.RED
        }
        successText {
            textFill = Color.GREEN
        }
        keyword {
            fill = Color.MAROON
            fontWeight = FontWeight.BOLD
        }
    }
}