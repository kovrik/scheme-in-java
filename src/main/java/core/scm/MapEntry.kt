package core.scm

import core.writer.Writer

// TODO implement proper interfaces, refactor
class MapEntry(key: Any?, value: Any?) : MutableVector(key, value) {

    constructor(entry: Map.Entry<Any?, Any?>) : this(entry.key, entry.value)

    val key: Any?
        get() = get(0)

    val value: Any?
        get() = get(1)

    override val name: String
        get() = "map entry"

    override fun toString(): String {
        return "[" + Writer.write(key) + " " + Writer.write(value) + "]"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as MapEntry?
        if (if (key != null) key != that!!.key else that!!.key != null) return false
        return if (value != null) value == that.value else that.value == null
    }

    override fun hashCode(): Int {
        var result = if (key != null) key!!.hashCode() else 0
        result = 31 * result + if (value != null) value!!.hashCode() else 0
        return result
    }
}