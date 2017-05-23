package core.procedures.meta

import core.exceptions.WrongTypeException
import core.procedures.AFn
import core.procedures.FnArgsBuilder
import core.scm.IMeta
import core.scm.Symbol

class WIthMeta : AFn(FnArgsBuilder().min(2).max(2).mandatory(arrayOf(IMeta::class.java, Map::class.java)).build()) {

    override val isPure: Boolean
        get() = false

    override val name: String
        get() = "with-meta"

    override fun apply2(arg1: Any?, arg2: Any?): Any {
        if (arg1 is Symbol) {
            return Symbol(arg1.name, arg2 as Map<*, *>?)
        }
        throw WrongTypeException(name, "IMeta", arg1)
    }
}