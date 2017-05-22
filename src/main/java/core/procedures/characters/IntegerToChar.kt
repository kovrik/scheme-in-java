package core.procedures.characters

import core.procedures.AFn
import core.procedures.FnArgsBuilder

class IntegerToChar : AFn(FnArgsBuilder().min(1).max(1).mandatory(arrayOf<Class<*>>(Long::class.javaObjectType)).build()) {

    override val name: String
        get() = "integer->char"

    override fun apply1(arg: Any?): Char? {
        return (arg as Number).toLong().toChar()
    }
}