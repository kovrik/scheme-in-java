package core.scm

import core.exceptions.WrongTypeException
import core.procedures.AFn
import core.utils.Utils
import core.writer.Writer
import java.util.*

// TODO implement List instead?
/* Immutable Vector */
open class Vector : AFn<Number?, Any?>, Collection<Any?>, IAssoc {

    companion object {
        /* Scheme Vector syntax */
        //  private static final String OPEN = "#(";
        //  private static final String CLOSE = ")";
        /* Alternative Clojure-style Vector syntax */
        private val OPEN = "["
        private val CLOSE = "]"
    }

    override val name = "vector"

    /* Contents of Vector: plain Java array */
    internal val array: Array<Any?>

    constructor() : super(minArgs = 1, maxArgs = 1, mandatoryArgsTypes = arrayOf<Class<*>>(Type.ExactNonNegativeInteger::class.java)) {
        array = arrayOfNulls<Any>(0)
    }

    constructor(size: Int, init: Any?) {
        array = arrayOfNulls<Any>(size)
        array.fill(init)
    }

    constructor(elements: Array<out Any?>) : super(minArgs = 1, maxArgs = 1,
                                                   mandatoryArgsTypes = arrayOf<Class<*>>(Type.ExactNonNegativeInteger::class.java)) {
        array = Arrays.copyOf(elements, elements.size)
    }

    override val size: Int
        get() = array.size

    override fun contains(element: Any?) = array.contains(element)

    operator fun get(index: Int) = getArray()[index]

    override operator fun invoke(arg: Number?) = arg!!.toInt().let {
        when {
            it < array.size -> array[it]
            else -> throw IndexOutOfBoundsException("$name: value out of range: $it")
        }
    }

    override fun toString() = when {
        getArray().isEmpty() -> OPEN + CLOSE
        else -> StringBuilder(OPEN).apply {
            val lastIndex = getArray().size - 1
            for (i in 0..lastIndex) {
                val e = getArray()[i]
                append(if (e === this@Vector) "(this vector)" else Writer.write(e))
                if (i != lastIndex) {
                    append(' ')
                }
            }
            append(CLOSE)
        }.toString()
    }

    override fun iterator() = array.asList().iterator()

    override fun isEmpty() = (size == 0)

    open fun getArray(): Array<Any?> = array.copyOf()

    open fun toArray(): Array<Any?> = array.copyOf()

    override fun containsAll(elements: Collection<*>) = elements.all { contains(it) }

    override fun hashCode() = getArray().hashCode()

    override fun equals(other: Any?) = other is Vector && Arrays.equals(getArray(), other.getArray())

    override fun containsKey(key: Any?) = when {
        Utils.isInteger(key) -> size > (key as Number).toInt()
        else -> throw WrongTypeException(name, Int::class.java, key)
    }

    override fun getEntry(key: Any?) = when {
        Utils.isInteger(key) -> (key as Number).toInt().let { MapEntry(it, get(it)) }
        else -> throw WrongTypeException(name, Int::class.java, key)
    }

    override fun assoc(key: Any?, value: Any?): Any = throw UnsupportedOperationException("assoc is not supported for immutable vector")
}
