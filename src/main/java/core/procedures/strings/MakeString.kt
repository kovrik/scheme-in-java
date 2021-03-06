package core.procedures.strings

import core.procedures.AFn
import core.procedures.Arity.Range
import core.scm.MutableString
import core.scm.Type

class MakeString : AFn<Any?, MutableString>(name = "make-string", isPure = true, arity = Range(1, 2),
                                            mandatoryArgsTypes = arrayOf(Type.ExactNonNegativeInteger::class.java),
                                            restArgsType = Char::class.javaObjectType) {

    override operator fun invoke(args: Array<out Any?>): MutableString {
        val length = (args[0] as Number).toInt()
        val c = if (args.size == 1) Character.MIN_VALUE else args[1]
        return MutableString(length).apply { for (i in 0 until length) { append(c) } }
    }
}
