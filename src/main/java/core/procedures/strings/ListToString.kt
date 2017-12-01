package core.procedures.strings

import core.exceptions.WrongTypeException
import core.procedures.AFn
import core.scm.Type

class ListToString : AFn<List<*>?, String>(name = "list->string", isPure = true, minArgs = 1, maxArgs = 1,
                                           mandatoryArgsTypes = arrayOf(Type.ProperList::class.java)) {

    override operator fun invoke(arg: List<*>?) = when {
        arg!!.isEmpty() -> ""
        else -> StringBuilder(arg.size).apply {
            for (c in arg) {
                append(c as? Char ?: throw WrongTypeException(name, "Character", c))
            }
        }.toString()
    }
}
