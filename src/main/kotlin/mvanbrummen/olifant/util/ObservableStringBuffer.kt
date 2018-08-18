package mvanbrummen.olifant.util

import javafx.beans.binding.StringBinding

class ObservableStringBuffer : StringBinding() {

    private val buffer = mutableListOf<String>()

    override fun computeValue(): String {
        return buffer.joinToString("\n")
    }

    fun set(content: String) {
        with(buffer) {
            clear()
            add(content)
        }
        invalidate()
    }

    fun append(text: String) {
        buffer.add(text)
        invalidate()
    }
}