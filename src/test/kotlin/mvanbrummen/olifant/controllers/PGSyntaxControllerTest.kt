package mvanbrummen.olifant.controllers

import mvanbrummen.olifant.Styles
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PGSyntaxControllerTest {

    private val controller = PGSyntaxController()

    @Test
    internal fun `should have no highlighting when string is empty`() {
        val res = controller.computeHighlighting("")

        assertThat(res.length()).isEqualTo(0)
        assertThat(res.getStyleSpan(0).style).doesNotContain(Styles.keyword.name)
    }

    @Test
    internal fun `should have no highlighting when there are no keywords`() {
        val res = controller.computeHighlighting("moocow")

        assertThat(res.getStyleSpan(0).style).doesNotContain(Styles.keyword.name)
    }

    @Test
    internal fun `should have no highlighting when there are nested keywords`() {
        val res = controller.computeHighlighting("mooSELECTmoo")

        assertThat(res.getStyleSpan(0).style).doesNotContain(Styles.keyword.name)
    }

    @Test
    internal fun `should have highlighting with a single keyword`() {
        val res = controller.computeHighlighting("SELECT")

        assertThat(res.getStyleSpan(0).length).isEqualTo(6)
        assertThat(res.getStyleSpan(0).style).containsExactly(Styles.keyword.name)
    }

    @Test
    internal fun `should have highlighting with a single keyword with mixed case`() {
        val res = controller.computeHighlighting("sElECt")

        assertThat(res.getStyleSpan(0).length).isEqualTo(6)
        assertThat(res.getStyleSpan(0).style).containsExactly(Styles.keyword.name)
    }

    @Test
    internal fun `should have highlighting with multiple keywords`() {
        val res = controller.computeHighlighting("SELECT * FROM test WHERE")

        assertThat(res.spanCount).isEqualTo(5)
        assertThat(res.getStyleSpan(0).style).containsExactly(Styles.keyword.name)
        assertThat(res.getStyleSpan(0).length).isEqualTo(6)

        assertThat(res.getStyleSpan(1).style).doesNotContain(Styles.keyword.name)

        assertThat(res.getStyleSpan(2).style).containsExactly(Styles.keyword.name)
        assertThat(res.getStyleSpan(2).length).isEqualTo(4)

        assertThat(res.getStyleSpan(3).style).doesNotContain(Styles.keyword.name)

        assertThat(res.getStyleSpan(4).style).containsExactly(Styles.keyword.name)
        assertThat(res.getStyleSpan(4).length).isEqualTo(5)
    }
}