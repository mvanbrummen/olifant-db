package mvanbrummen.olifant.controllers

import org.junit.jupiter.api.Test

internal class PGSyntaxControllerTest {

    val controller = PGSyntaxController()

    @Test
    internal fun testComputeHighlighting() {

        val res = controller.computeHighlighting("SELECT * FROM some_schema.some_table WHERE 1 = 1;")
    }
}