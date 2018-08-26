package mvanbrummen.olifant.views

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import mvanbrummen.olifant.controllers.DatabaseTreeContext
import tornadofx.*

class ObjectExplorerView : View() {
    val dbTreeContext: DatabaseTreeContext by inject()
    val dbTreeView: DatabaseTreeView by inject()

    override val root = vbox {
        toolbar {
            button("", FontAwesomeIconView(FontAwesomeIcon.PLUS)) {
                action {
                    find(NewServerView::class).openModal()
                }
            }
            button("", FontAwesomeIconView(FontAwesomeIcon.REFRESH)) {
                action {
                    println("Refreshing tree...")

                }
            }
        }

        val treeView = dbTreeView.root

        treeView.prefHeightProperty().bind(this.heightProperty())

        this += treeView
    }

}