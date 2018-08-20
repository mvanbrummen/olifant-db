package mvanbrummen.olifant.controllers

import mvanbrummen.olifant.models.QueryStatement
import mvanbrummen.olifant.models.UpdateStatement
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class QueryParserControllerTest {

    private val controller = QueryParserController()

    @Test
    internal fun `should parse query statement when query string starts with select`() {
        val query = "select * from test"
        val res = controller.parseQuery("$query;")

        assertThat(res.size).isEqualTo(1)
        assertThat(res[0]).isInstanceOf(QueryStatement::class.java)
        assertThat(res[0].queryString).isEqualToIgnoringCase(query)
    }

    @Test
    internal fun `should parse update statement when query string starts with update`() {
        val query = "update test set test = 1"
        val res = controller.parseQuery("$query;")

        assertThat(res.size).isEqualTo(1)
        assertThat(res[0]).isInstanceOf(UpdateStatement::class.java)
        assertThat(res[0].queryString).isEqualToIgnoringCase(query)
    }

    @Test
    internal fun `should parse multiple statements when query string contains delimited sql`() {
        val query1 = "select * from test"
        val query2 = "update test set test = 1"
        val res = controller.parseQuery("$query1;$query2;")

        assertThat(res.size).isEqualTo(2)
        assertThat(res[0]).isInstanceOf(QueryStatement::class.java)
        assertThat(res[0].queryString).isEqualToIgnoringCase(query1)

        assertThat(res[1]).isInstanceOf(UpdateStatement::class.java)
        assertThat(res[1].queryString).isEqualToIgnoringCase(query2)
    }

    @Test
    internal fun `should ignore query when empty`() {
        val query = "select * from test"
        val res = controller.parseQuery(";;$query;;")

        assertThat(res.size).isEqualTo(1)
        assertThat(res[0].queryString).isEqualToIgnoringCase(query)
    }

}