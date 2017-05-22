package core.scm

import core.exceptions.WrongTypeException
import core.utils.Utils

/* Mutable Vector */
open class MutableVector : Vector {

    constructor() : super()

    constructor(size: Int, init: Any?) : super(size, init)

    constructor(elements: Array<Any?>) : super(elements)

    constructor(e: Any?) : super(e)

    operator fun set(index: Int, value: Any) {
        if (size <= index) throw IndexOutOfBoundsException(String.format("%s: value out of range: %s", name, index))
        array[index] = value
    }

    override fun getArray(): Array<Any?> {
        return array
    }

    override fun toArray(): Array<Any?> {
        return array
    }

    override fun assoc(key: Any, value: Any): Any {
        if (!Utils.isInteger(key)) {
            throw WrongTypeException(name, Int::class.java, key)
        }
        val i = (key as Number).toInt()
        set(i, value)
        return this
    }
}
