package mvanbrummen.olifant.controllers

import mvanbrummen.olifant.Styles
import mvanbrummen.olifant.controllers.PGSyntaxController.Patterns.KEYWORD_PATTERN
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder
import tornadofx.Controller


class PGSyntaxController : Controller() {

    object Patterns {
        val KEYWORDS = listOf(
                "ABORT", "ABS", "ABSOLUTE", "ACCESS", "ACTION", "ADA", "ADD", "ADMIN", "AFTER", "AGGREGATE", "ALIAS", "ALL",
                "ALLOCATE", "ALTER", "ANALYSE", "ANALYZE", "AND", "ANY", "ARE", "ARRAY", "AS", "ASC", "ASENSITIVE", "ASSERTION",
                "ASSIGNMENT", "ASYMMETRIC", "AT", "ATOMIC", "AUTHORIZATION", "AVG", "BACKWARD", "BEFORE", "BEGIN", "BETWEEN", "BIGINT",
                "BINARY", "BIT", "BITVAR", "BIT_LENGTH", "BLOB", "BOOLEAN", "BOTH", "BREADTH", "BY", "C", "CACHE", "CALL", "CALLED",
                "CARDINALITY", "CASCADE", "CASCADED", "CASE", "CAST", "CATALOG", "CATALOG_NAME", "CHAIN", "CHAR", "CHARACTER",
                "CHARACTERISTICS", "CHARACTER_LENGTH", "CHARACTER_SET_CATALOG", "CHARACTER_SET_NAME", "CHARACTER_SET_SCHEMA", "CHAR_LENGTH",
                "CHECK", "CHECKED", "CHECKPOINT", "CLASS", "CLASS_ORIGIN", "CLOB", "CLOSE", "CLUSTER", "COALESCE", "COBOL", "COLLATE",
                "COLLATION", "COLLATION_CATALOG", "COLLATION_NAME", "COLLATION_SCHEMA", "COLUMN", "COLUMN_NAME", "COMMAND_FUNCTION",
                "COMMAND_FUNCTION_CODE", "COMMENT", "COMMIT", "COMMITTED", "COMPLETION", "CONDITION_NUMBER", "CONNECT", "CONNECTION",
                "CONNECTION_NAME", "CONSTRAINT", "CONSTRAINTS", "CONSTRAINT_CATALOG", "CONSTRAINT_NAME", "CONSTRAINT_SCHEMA", "CONSTRUCTOR",
                "CONTAINS", "CONTINUE", "CONVERSION", "CONVERT", "COPY", "CORRESPONDING", "COUNT", "CREATE", "CREATEDB", "CREATEUSER",
                "CROSS", "CUBE", "CURRENT", "CURRENT_DATE", "CURRENT_PATH", "CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP",
                "CURRENT_USER", "CURSOR", "CURSOR_NAME", "CYCLE", "DATA", "DATABASE", "DATE", "DATETIME_INTERVAL_CODE",
                "DATETIME_INTERVAL_PRECISION", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DEFERRABLE", "DEFERRED",
                "DEFINED", "DEFINER", "DELETE", "DELIMITER", "DELIMITERS", "DEPTH", "DEREF", "DESC", "DESCRIBE", "DESCRIPTOR", "DESTROY",
                "DESTRUCTOR", "DETERMINISTIC", "DIAGNOSTICS", "DICTIONARY", "DISCONNECT", "DISPATCH", "DISTINCT", "DO", "DOMAIN", "DOUBLE",
                "DROP", "DYNAMIC", "DYNAMIC_FUNCTION", "DYNAMIC_FUNCTION_CODE", "EACH", "ELSE", "ENCODING", "ENCRYPTED", "END", "END-EXEC",
                "EQUALS", "ESCAPE", "EVERY", "EXCEPT", "EXCEPTION", "EXCLUSIVE", "EXEC", "EXECUTE", "EXISTING", "EXISTS", "EXPLAIN", "EXTERNAL",
                "EXTRACT", "FALSE", "FETCH", "FINAL", "FIRST", "FLOAT", "FOR", "FORCE", "FOREIGN", "FORTRAN", "FORWARD", "FOUND", "FREE",
                "FREEZE", "FROM", "FULL", "FUNCTION", "G", "GENERAL", "GENERATED", "GET", "GLOBAL", "GO", "GOTO", "GRANT", "GRANTED", "GROUP",
                "GROUPING", "HANDLER", "HAVING", "HIERARCHY", "HOLD", "HOST", "HOUR", "IDENTITY", "IGNORE", "ILIKE", "IMMEDIATE", "IMMUTABLE",
                "IMPLEMENTATION", "IMPLICIT", "IN", "INCREMENT", "INDEX", "INDICATOR", "INFIX", "INHERITS", "INITIALIZE", "INITIALLY",
                "INNER", "INOUT", "INPUT", "INSENSITIVE", "INSERT", "INSTANCE", "INSTANTIABLE", "INSTEAD", "INT", "INTEGER", "INTERSECT",
                "INTERVAL", "INTO", "INVOKER", "IS", "ISNULL", "ISOLATION", "ITERATE", "JOIN", "K", "KEY", "KEY_MEMBER", "KEY_TYPE",
                "LANCOMPILER", "LANGUAGE", "LARGE", "LAST", "LATERAL", "LEADING", "LEFT", "LENGTH", "LESS", "LEVEL", "LIKE", "LIMIT",
                "LISTEN", "LOAD", "LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "LOCATION", "LOCATOR", "LOCK", "LOWER", "M", "MAP", "MATCH",
                "MAX", "MAXVALUE", "MESSAGE_LENGTH", "MESSAGE_OCTET_LENGTH", "MESSAGE_TEXT", "METHOD", "MIN", "MINUTE", "MINVALUE",
                "MOD", "MODE", "MODIFIES", "MODIFY", "MODULE", "MONTH", "MORE", "MOVE", "MUMPS", "NAME", "NAMES", "NATIONAL", "NATURAL",
                "NCHAR", "NCLOB", "NEW", "NEXT", "NO", "NOCREATEDB", "NOCREATEUSER", "NONE", "NOT", "NOTHING", "NOTIFY", "NOTNULL", "NULL",
                "NULLABLE", "NULLIF", "NUMBER", "NUMERIC", "OBJECT", "OCTET_LENGTH", "OF", "OFF", "OFFSET", "OIDS", "OLD", "ON", "ONLY",
                "OPEN", "OPERATION", "OPERATOR", "OPTION", "OPTIONS", "OR", "ORDER", "ORDINALITY", "OUT", "OUTER", "OUTPUT", "OVERLAPS",
                "OVERLAY", "OVERRIDING", "OWNER", "PAD", "PARAMETER", "PARAMETERS", "PARAMETER_MODE", "PARAMETER_NAME",
                "PARAMETER_ORDINAL_POSITION", "PARAMETER_SPECIFIC_CATALOG", "PARAMETER_SPECIFIC_NAME", "PARAMETER_SPECIFIC_SCHEMA",
                "PARTIAL", "PASCAL", "PASSWORD", "PATH", "PENDANT", "PLACING", "PLI", "POSITION", "POSTFIX", "PRECISION", "PREFIX",
                "PREORDER", "PREPARE", "PRESERVE", "PRIMARY", "PRIOR", "PRIVILEGES", "PROCEDURAL", "PROCEDURE", "PUBLIC", "READ",
                "READS", "REAL", "RECHECK", "RECURSIVE", "REF", "REFERENCES", "REFERENCING", "REINDEX", "RELATIVE", "RENAME",
                "REPEATABLE", "REPLACE", "RESET", "RESTRICT", "RESULT", "RETURN", "RETURNED_LENGTH", "RETURNED_OCTET_LENGTH",
                "RETURNED_SQLSTATE", "RETURNS", "REVOKE", "RIGHT", "ROLE", "ROLLBACK", "ROLLUP", "ROUTINE", "ROUTINE_CATALOG",
                "ROUTINE_NAME", "ROUTINE_SCHEMA", "ROW", "ROWS", "ROW_COUNT", "RULE", "SAVEPOINT", "SCALE", "SCHEMA", "SCHEMA_NAME",
                "SCOPE", "SCROLL", "SEARCH", "SECOND", "SECTION", "SECURITY", "SELECT", "SELF", "SENSITIVE", "SEQUENCE", "SERIALIZABLE",
                "SERVER_NAME", "SESSION", "SESSION_USER", "SET", "SETOF", "SETS", "SHARE", "SHOW", "SIMILAR", "SIMPLE", "SIZE", "SMALLINT",
                "SOME", "SOURCE", "SPACE", "SPECIFIC", "SPECIFICTYPE", "SPECIFIC_NAME", "SQL", "SQLCODE", "SQLERROR", "SQLEXCEPTION",
                "SQLSTATE", "SQLWARNING", "STABLE", "START", "STATE", "STATEMENT", "STATIC", "STATISTICS", "STDIN", "STDOUT", "STORAGE",
                "STRICT", "STRUCTURE", "STYLE", "SUBCLASS_ORIGIN", "SUBLIST", "SUBSTRING", "SUM", "SYMMETRIC", "SYSID", "SYSTEM",
                "SYSTEM_USER", "TABLE", "TABLE_NAME", "TEMP", "TEMPLATE", "TEMPORARY", "TERMINATE", "THAN", "THEN", "TIME", "TIMESTAMP",
                "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TOAST", "TRAILING", "TRANSACTION", "TRANSACTIONS_COMMITTED",
                "TRANSACTIONS_ROLLED_BACK", "TRANSACTION_ACTIVE", "TRANSFORM", "TRANSFORMS", "TRANSLATE", "TRANSLATION", "TREAT", "TRIGGER",
                "TRIGGER_CATALOG", "TRIGGER_NAME", "TRIGGER_SCHEMA", "TRIM", "TRUE", "TRUNCATE", "TRUSTED", "TYPE", "UNCOMMITTED", "UNDER",
                "UNENCRYPTED", "UNION", "UNIQUE", "UNKNOWN", "UNLISTEN", "UNNAMED", "UNNEST", "UNTIL", "UPDATE", "UPPER", "USAGE", "USER",
                "USER_DEFINED_TYPE_CATALOG", "USER_DEFINED_TYPE_NAME", "USER_DEFINED_TYPE_SCHEMA", "USING", "VACUUM", "VALID", "VALIDATOR",
                "VALUE", "VALUES", "VARCHAR", "VARIABLE", "VARYING", "VERBOSE", "VERSION", "VIEW", "VOLATILE", "WHEN", "WHENEVER",
                "WHERE", "WITH", "WITHOUT", "WORK", "WRITE", "YEAR", "ZONE"
        )

        val KEYWORD_PATTERN = "\\b(" + KEYWORDS.joinToString("|") + "\\b)"
    }

    fun computeHighlighting(text: String): StyleSpans<List<String>> {
        val regex = Regex(KEYWORD_PATTERN)
        val matchResults = regex.findAll(text)
        val spansBuilder = StyleSpansBuilder<List<String>>()

        var lastKwEnd = 0

        matchResults.iterator().forEach { res ->
            val range = res.range

            spansBuilder.add(emptyList(), range.first - lastKwEnd)
            spansBuilder.add(listOf(Styles.keyword.name), range.last - range.first)
            lastKwEnd = range.last
        }

        spansBuilder.add(emptyList(), text.length - lastKwEnd)

        return spansBuilder.create()
    }

}