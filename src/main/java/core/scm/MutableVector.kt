package core.scm

/* Mutable Vector */
open class MutableVector : Vector {

    companion object {
        val EMPTY = MutableVector(0, null)
    }

    constructor() : super()

    constructor(size: Int) : super(size)

    constructor(size: Int, init: Any?) : super(size, init)

    constructor(coll: Collection<Any?>) : super(coll)

    constructor(elements: Array<out Any?>) : super(elements)

    constructor(vector: Vector) : super(vector)

    operator fun set(index: Int, value: Any?) {
        if (size <= index) throw IndexOutOfBoundsException("$name: value out of range: $index")
        array[index] = value
    }

    override fun getArray() = array

    override fun toArray() = array

    override fun assoc(key: Any?, value: Any?) = apply {
        Type.assertType(name, key, Int::class.java)
        set((key as Number).toInt(), value)
    }
}

