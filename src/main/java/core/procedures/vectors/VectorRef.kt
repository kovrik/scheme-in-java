package core.procedures.vectors

import core.procedures.AFn
import core.procedures.FnArgsBuilder
import core.scm.Type
import core.scm.MutableVector

class VectorRef : AFn(FnArgsBuilder().min(2).max(2)
        .mandatory(arrayOf(MutableVector::class.java, Type.ExactNonNegativeInteger::class.java)).build()) {

    override val isPure: Boolean
        get() = true

    override val name: String
        get() = "vector-ref"

    override fun apply2(arg1: Any?, arg2: Any?): Any? {
        val vec = arg1 as MutableVector?
        val pos = (arg2 as Number).toLong()
        if (pos >= vec!!.size) {
            throw IndexOutOfBoundsException(String.format("%s: value out of range: %s", name, pos))
        }
        return vec[pos.toInt()]
    }
}