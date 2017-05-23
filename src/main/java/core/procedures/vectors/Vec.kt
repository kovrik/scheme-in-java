package core.procedures.vectors

import core.exceptions.WrongTypeException
import core.procedures.AFn
import core.procedures.FnArgsBuilder
import core.scm.MutableVector

class Vec : AFn(FnArgsBuilder().min(1).max(1).build()) {

    override val name: String
        get() = "vec"

    override fun apply1(arg: Any?): MutableVector? {
        if (arg is Collection<*>) {
            return MutableVector(*arg.toTypedArray())
        }
        if (arg is CharSequence) {
            val size = arg.length
            val vector = MutableVector(size, null)
            for (i in 0..size - 1) {
                vector[i] = arg[i]
            }
            return vector
        }
        throw WrongTypeException(name, "List or Vector or String", arg)
    }
}