package mvanbrummen.olifant.views

import mvanbrummen.olifant.Styles
import mvanbrummen.olifant.util.ObservableStringBuffer
import tornadofx.*

data class DBMessage(val message: String) : FXEvent()

class MessagesTextArea : Fragment() {

    val dbMessages = ObservableStringBuffer()

    override val root = textarea {
        subscribe<DBMessage> { event ->
            dbMessages.append(event.message)
        }

        textProperty().bind(dbMessages)

        isEditable = false

        addClass(Styles.dbMessages)
    }

}