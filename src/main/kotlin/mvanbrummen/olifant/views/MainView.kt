package mvanbrummen.olifant.views

import javafx.geometry.Orientation
import javafx.scene.layout.Priority
import mvanbrummen.olifant.config.ConfigHelper
import mvanbrummen.olifant.db.DatabaseConnection
import tornadofx.*

const val HEIGHT = 600.0
const val WIDTH = 950.0

const val RESULT_TAB_INDEX = 0
const val MESSAGES_TAB_INDEX = 1

class MainView : View("OlifantDB") {

    val menuBar: MenuBar by inject()
    val objectExplorerView: ObjectExplorerView by inject()

    init {
        if (ConfigHelper.isConnectionSaved(app.config)) {
            DatabaseConnection.add(ConfigHelper.getSavedConnectionDataSource(app.config))
        }
    }

    override val root = borderpane {
        setPrefSize(WIDTH, HEIGHT)

        top = menuBar.root

        center = splitpane {
            this += objectExplorerView.root
            this += find(QueryPaneView::class)

            orientation = Orientation.HORIZONTAL

            setDividerPositions(0.25, 0.75)

            vboxConstraints { vGrow = Priority.ALWAYS }
        }
    }

}



