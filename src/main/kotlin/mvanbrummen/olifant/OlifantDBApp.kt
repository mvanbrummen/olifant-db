package mvanbrummen.olifant

import javafx.application.Application
import mvanbrummen.olifant.views.MainView
import tornadofx.App

class OlifantDBApp: App(MainView::class, Styles::class)

fun main(args: Array<String>) {
    Application.launch(OlifantDBApp::class.java, *args)
}
