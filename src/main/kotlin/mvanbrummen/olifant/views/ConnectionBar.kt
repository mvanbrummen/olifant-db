package mvanbrummen.olifant.views

import javafx.scene.layout.HBox
import javafx.scene.paint.Paint
import tornadofx.Fragment
import tornadofx.label
import tornadofx.paddingLeft
import tornadofx.style

class ConnectionBar : Fragment() {
    override val root = HBox()

    init {
        with(root) {
            label("kgitforge on postgres@localhost") {
                style {
                    textFill = Paint.valueOf("#FFF")
                    paddingLeft = 5
                }
            }

            style {
                backgroundColor += Paint.valueOf("#41b2f4")
            }
        }
    }

}