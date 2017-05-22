package core.reader

import core.exceptions.IllegalSyntaxException
import core.procedures.AFn
import core.scm.Cons
import core.scm.Keyword
import core.scm.MutableVector
import core.scm.Symbol
import core.scm.specialforms.Quasiquote
import core.scm.specialforms.Quote
import core.scm.specialforms.Unquote
import core.scm.specialforms.UnquoteSplicing
import core.utils.Utils.getRadixByChar
import core.utils.Utils.isValidForRadix
import core.utils.Utils.preProcessNumber
import java.io.*
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

open class Reader : IReader {

    internal lateinit var reader: PushbackReader

    internal constructor()

    constructor(inputStream: InputStream) {
        this.reader = PushbackReader(BufferedReader(InputStreamReader(inputStream)), 1)
    }

    companion object {

        private val DOT = Symbol.intern(".")

        private val DEREF = Symbol.intern("deref")

        private val LINE_BREAKS = "\n\r"
        private val WHITESPACES = LINE_BREAKS + "\u000B \t"
        // <delimiter> --> <whitespace> | ( | ) | " | ;
        private val DELIMITERS = WHITESPACES + ":;(){}[],\"\u0000\uffff"

        /* Allowed escape sequences. See: https://docs.oracle.com/javase/tutorial/java/data/characters.html */
        private val ESCAPED = HashMap<Char, Char>()

        init {
            ESCAPED.put('t', '\t')
            ESCAPED.put('b', '\b')
            ESCAPED.put('n', '\n')
            ESCAPED.put('r', '\r')
            ESCAPED.put('"', '\"')
            ESCAPED.put('\\', '\\')
        }

        val NAMED_CHARS: MutableMap<String, Char> = HashMap()

        init {
            NAMED_CHARS.put("newline", '\n')
            NAMED_CHARS.put("space", ' ')
            NAMED_CHARS.put("tab", '\t')
            NAMED_CHARS.put("return", '\r')
            NAMED_CHARS.put("backspace", '\b')
            NAMED_CHARS.put("alarm", '\u0007')
            NAMED_CHARS.put("vtab", '\u000B')
            NAMED_CHARS.put("esc", '\u001B')
            NAMED_CHARS.put("escape", '\u001B')
            NAMED_CHARS.put("delete", '\u007F')
            NAMED_CHARS.put("null", Character.MIN_VALUE)
            NAMED_CHARS.put("nul", Character.MIN_VALUE)
        }

        private fun isValid(i: Int): Boolean {
            return i > Character.MIN_VALUE.toInt() && i < Character.MAX_VALUE.toInt()
        }

        private fun isLineBreak(c: Char): Boolean {
            return LINE_BREAKS.indexOf(c) > -1
        }

        @JvmStatic fun isRadix(c: Char): Boolean {
            return "bodxBODX".indexOf(c) > -1
        }

        @JvmStatic fun isExact(c: Char): Boolean {
            return c == 'e' || c == 'E'
        }

        @JvmStatic fun isInexact(c: Char): Boolean {
            return c == 'i' || c == 'I'
        }

        @JvmStatic fun isExactness(c: Char): Boolean {
            return isExact(c) || isInexact(c)
        }
    }

    override fun read(): List<Any> {
        val tokens = ArrayList<Any>()
        try {
            var token = nextToken()
            while (token != null || tokens.isEmpty()) {
                when {
                    token != null -> tokens.add(token)
                }
                token = nextToken()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return tokens
    }

    @Throws(IOException::class)
    private fun readUntilDelimiter(): String {
        val token = StringBuilder()
        var i = reader.read()
        while (isValid(i) && DELIMITERS.indexOf(i.toChar()) < 0) {
            token.append(i.toChar())
            i = reader.read()
        }
        reader.unread(i.toChar().toInt())
        return token.toString()
    }

    /* Skip all null tokens and return the first non-null */
    @Throws(IOException::class)
    private fun nextNonNullToken(): Any {
        var token = nextToken()
        while (token == null) {
            /* Read */
            token = nextToken()
        }
        return token
    }

    /**
     * Read next token
     */
    @Throws(IOException::class)
    fun nextToken(): Any? {
        val i = reader.read()
        if (!isValid(i)) {
            return null
        }
        var c = i.toChar()
        /* Skip whitespaces until line break */
        if (Character.isWhitespace(c)) {
            while (isValid(c.toInt()) && Character.isWhitespace(c) && !isLineBreak(c)) {
                c = reader.read().toChar()
            }
        }
        /* Check if there is anything to read */
        if (!isValid(c.toInt()) || isLineBreak(c)) {
            return null
        }
        /* Decimal number */
        if (c != '#' && isValidForRadix(c, 10)) {
            /* Read identifier, not a number */
            val number = c + readUntilDelimiter()
            /* Now check if it IS a valid number */
            return preProcessNumber(number, null, 10)
        }
        when (c) {
            '\'' -> return readQuote(c)
            '`' -> return readQuote(c)
            ',' -> return readQuote(c)
            '@' -> return readDeref()
            '#' -> return readHash()
            '(' -> return readList(true, ')')
            '{' -> return readHashmap()
            '[' -> return readVector(']')
            ';' -> return readComment()
            '"' -> return readString()
            ':' -> return readKeyword()
            ')' -> throw IllegalSyntaxException("read: unexpected list terminator: " + c)
            '}' -> throw IllegalSyntaxException("read: unexpected terminator: " + c)
            ']' -> throw IllegalSyntaxException("read: unexpected vector terminator: " + c)
            else -> {
                val s = c + readUntilDelimiter()
                /* Read true and false as #t and #f */
                when (s) {
                    "true"  -> return true
                    "false" -> return false
                    else    -> return Symbol.intern(s)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun readHash(): Any {
        val c = reader.read().toChar()
        if (c == '(') {
            /* Read Quoted vector #(...) */
            val vector = readVector(')')
            if (vector.isEmpty()) {
                return vector
            }
            return Cons.list<AFn>(Quote.QUOTE_SYMBOL, vector)
        } else if (c == '{') {
            return readSet()
        } else if (c == '\\') {
            return readCharacter()
        } else if (c == 't' || c == 'T') {
            return java.lang.Boolean.TRUE
        } else if (c == 'f' || c == 'F') {
            return java.lang.Boolean.FALSE
        } else if (c == '"') {
            return readRegex()
        } else if (isRadix(c) || isExactness(c)) {
            /* Read identifier, not a number */
            val number = "#" + c + readUntilDelimiter()
            /* Read radix and/or exactness and a number */
            var radix: Char? = null
            var exactness: Char? = null
            var restNumber = number
            while (restNumber.length > 1 && restNumber[0] == '#') {
                val ch = restNumber[1]
                if (isExactness(ch)) {
                    if (exactness != null) {
                        throw IllegalSyntaxException("read: bad number: " + number)
                    }
                    exactness = ch
                    restNumber = restNumber.substring(2)
                    continue
                }
                if (isRadix(ch)) {
                    if (radix != null) {
                        throw IllegalSyntaxException("read: bad number: " + number)
                    }
                    radix = ch
                    restNumber = restNumber.substring(2)
                    continue
                }
                break
            }

            if (restNumber.isEmpty() || "+" == restNumber || "-" == restNumber) {
                throw IllegalSyntaxException("read: bad number: " + number)
            }

            /* Check if this is a proper number */
            val result = preProcessNumber(restNumber, exactness, getRadixByChar(radix)) as? Number ?: throw IllegalSyntaxException("read: bad number: " + number)
            return result
        }
        /* Bad hash syntax: read token and throw exception */
        val token = StringBuilder("#")
        if (isValid(c.toInt())) {
            token.append(c)
        }
        if (!Character.isWhitespace(c)) {
            token.append(readUntilDelimiter())
        }
        throw IllegalSyntaxException("read: bad syntax: " + token.toString())
    }

    /**
     * Read a quoted form abbreviation

     * Syntax:
     * <quote>            -> '<form>
     * <quasiquote>       -> `<form>
     * <unquote>          -> ,<form>
     * <unquote-splicing> -> ,@<form>
    </form></unquote-splicing></form></unquote></form></quasiquote></form></quote> */
    @Throws(IOException::class)
    private fun readQuote(c: Char): List<*> {
        var symbol: Symbol? = null
        if (c == '\'') {
            symbol = Quote.QUOTE_SYMBOL
        } else if (c == '`') {
            symbol = Quasiquote.QUASIQUOTE_SYMBOL
        } else if (c == ',') {
            val next = reader.read().toChar()
            if (next == '@') {
                symbol = UnquoteSplicing.UNQUOTE_SPLICING_SYMBOL
            } else {
                reader.unread(next.toInt())
                symbol = Unquote.UNQUOTE_SYMBOL
            }
        }
        return Cons.list<Any>(symbol, nextNonNullToken())
    }

    /**
     * Read a comment

     * Syntax:
     * <comment> --> ;  <all subsequent characters up to a line break>
    </all></comment> */
    @Throws(IOException::class)
    private fun readComment(): String? {
        var i = reader.read()
        while (isValid(i) && !isLineBreak(i.toChar())) {
            /* Read everything until line break */
            i = reader.read()
        }
        /* Comments are ignored, return null */
        return null
    }

    /**
     * Read a String
     * Always returns immutable String

     * Syntax:
     * <string> --> "<string element>*"
     * <string element> --> <any character other than></any>" or \> | \" | \\
    </string></string></string> */
    @Throws(IOException::class)
    private fun readString(): String {
        val string = StringBuilder()
        var i = reader.read()
        var c = i.toChar()
        while (isValid(i) && c != '"') {
            /* Escaping */
            if (c == '\\') {
                val next = reader.read().toChar()
                /* Unicode followed by a hexadecimal number */
                if (next == 'u' || next == 'U') {
                    reader.unread(next.toInt())
                    val chr = readCharacter()
                    if (chr == next) {
                        throw IllegalSyntaxException("read: no hex digit following \\u in string")
                    }
                    string.append(chr)
                } else {
                    /* Check that escape sequence is valid */
                    val character = ESCAPED[next] ?: throw IllegalSyntaxException(String.format("read: unknown escape sequence \\%s in string", next))
                    string.append(character)
                }
            } else {
                string.append(c)
            }
            i = reader.read()
            c = i.toChar()
        }
        /* Always intern Strings read by Reader */
        return string.toString().intern()
    }

    @Throws(IOException::class)
    private fun readRegex(): Pattern {
        val regex = StringBuilder()
        var i = reader.read()
        var c = i.toChar()
        while (isValid(i) && c != '"') {
            regex.append(c)
            if (c == '\\') {
                regex.append(reader.read().toChar())
            }
            i = reader.read()
            c = i.toChar()
        }
        return Pattern.compile(regex.toString())
    }

    /**
     * Read a Character

     * Syntax:
     * <character> --> #\ <any character> | #\ <character name>
     * <character name> --> space | newline
    </character></character></any></character> */
    @Throws(IOException::class)
    private fun readCharacter(): Char {
        val first = reader.read()
        var rest = readUntilDelimiter()
        if (rest.isEmpty()) {
            return first.toChar()
        }
        /* Check if it is a codepoint */
        var radix = 16
        var isCodepoint = first.toChar() == 'u' || first.toChar() == 'U'
        if (Character.isDigit(first.toChar())) {
            radix = 8
            rest = first.toChar() + rest
            isCodepoint = true
        }
        if (!isValidForRadix(rest[0], radix)) {
            isCodepoint = false
        }
        if (isCodepoint) {
            val codepoint = preProcessNumber(rest, 'e', radix) as? Number ?: throw IllegalSyntaxException("read: no hex digit following \\u in string")
            return codepoint.toInt().toChar()
        }
        /* Must be a named char */
        val character = first.toChar() + rest
        if ("linefeed" == character) {
            return NAMED_CHARS["newline"]!!
        }
        val namedChar = NAMED_CHARS[character] ?: throw IllegalSyntaxException("read: bad character constant: #\\" + character)
        return namedChar
    }

    /**
     * Read list

     * Syntax:
     * <list> -> (<list_contents>)
    </list_contents></list> */
    @Throws(IOException::class)
    private fun readList(allowImproperList: Boolean, terminator: Char): Cons<Any> {
        var list: Cons<Any> = Cons.EMPTY
        /* Remember position of a dot (if we meet it) */
        var dotPos = -1
        var i = reader.read()
        var c = i.toChar()
        while (isValid(i) && c != terminator) {
            /* Skip whitespaces */
            while (Character.isWhitespace(c)) {
                c = reader.read().toChar()
            }
            if (c == terminator) {
                break
            }
            reader.unread(c.toInt())
            val token = nextToken()
            /* Check if current token is a dot */
            if (DOT == token) {
                if (!allowImproperList || dotPos > -1) {
                    throw IllegalSyntaxException("read: illegal use of '.'")
                }
                /* Remember the dot position */
                dotPos = list.size
                /* Dot Special Form is allowed as the first element of a list */
                if (dotPos == 0) {
                    list = Cons.list<Any>(DOT)
                }
            } else if (token != null) {
                /* List is empty so far */
                if (list.isEmpty()) {
                    /* Initialize list with the first element (can't modify EMPTY) */
                    list = Cons.list(token)
                } else {
                    /* Add list element */
                    list.add(token)
                }
            }
            i = reader.read()
            c = i.toChar()
        }
        /* Was it a proper list or dot is the first element? */
        if (dotPos < 1) {
            return list
        }
        /* Process improper list */
        if (dotPos != list.size - 1) {
            throw IllegalSyntaxException("read: illegal use of '.'")
        }
        /* Convert list into cons */
        return list.toCons()
    }

    /**
     * Read vector

     * Syntax:
     * <vector> -> #(<vector_contents>)
    </vector_contents></vector> */
    @Throws(IOException::class)
    private fun readVector(terminator: Char): MutableVector {
        /* Improper lists are not allowed */
        return MutableVector(*readList(false, terminator).toTypedArray())
    }

    /**
     * Read hashmap

     * Syntax:
     * <hashmap> -> {<key1> <value1>, ..., <keyN> <valueN>}
    </valueN></keyN></value1></key1></hashmap> */
    @Throws(IOException::class)
    private fun readHashmap(): Map<Any?, Any?> {
        val hashmap= HashMap<Any?, Any?>()
        var i = reader.read()
        var c = i.toChar()
        while (isValid(i) && c != '}') {
            /* Skip whitespaces and commas */
            while (Character.isWhitespace(c) || c == ',') {
                c = reader.read().toChar()
            }
            if (c == '}') break
            reader.unread(c.toInt())
            val key = nextToken()

            /* Skip whitespaces and commas */
            c = reader.read().toChar()
            while (Character.isWhitespace(c) || c == ',') {
                c = reader.read().toChar()
            }
            if (c == '}') break
            reader.unread(c.toInt())
            val value = nextToken()

            hashmap.put(key, value)
            i = reader.read()
            c = i.toChar()
        }
        return hashmap
    }

    /**
     * Read set

     * Syntax:
     * <set> -> #{<value1>, ..., <valueN>}
    </valueN></value1></set> */
    @Throws(IOException::class)
    private fun readSet(): Set<Any?> {
        val set = HashSet<Any?>()
        var i = reader.read()
        var c = i.toChar()
        while (isValid(i) && c != '}') {
            /* Skip whitespaces and commas */
            while (Character.isWhitespace(c)) {
                c = reader.read().toChar()
            }
            if (c == '}') break
            reader.unread(c.toInt())
            set.add(nextToken())
            i = reader.read()
            c = i.toChar()
        }
        return set
    }

    /**
     * Read keyword

     * Syntax:
     * <keyword> -> :<token>
    </token></keyword> */
    @Throws(IOException::class)
    private fun readKeyword(): Keyword {
        val s = readUntilDelimiter()
        if (s.isEmpty()) {
            throw IllegalSyntaxException("read: illegal use of :")
        }
        return Keyword.intern(s)
    }

    /**
     * Deref shortcut

     * \@f -> (deref f)
     */
    @Throws(IOException::class)
    private fun readDeref(): List<Any> {
        return Cons.list(DEREF, nextNonNullToken())
    }

}