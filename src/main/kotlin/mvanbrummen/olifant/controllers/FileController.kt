package mvanbrummen.olifant.controllers

import tornadofx.Controller
import java.io.File

class FileController : Controller() {

    fun saveTextToFile(files: List<File>, text: String) {
        if (files.isNotEmpty()) files.first().writeText(text, Charsets.UTF_8)
    }

    fun readTextFromFile(files: List<File>): String? =
            if (files.isNotEmpty()) {
                val file = files.first()
                val fileText = file.readText(Charsets.UTF_8) // TODO read into a buffer instead

                fileText
            } else null
}