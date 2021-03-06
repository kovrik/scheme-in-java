package core.procedures.strings

import core.exceptions.WrongTypeException
import core.procedures.AFn
import core.procedures.Arity.Exactly

import java.util.regex.Pattern

class Replace : AFn<Any?, String>(name = "replace", isPure = true, arity = Exactly(3)) {

    override operator fun invoke(arg1: Any?, arg2: Any?, arg3: Any?): String {
        val chars = arg1 as? CharSequence ?: throw WrongTypeException(name, "String", arg1)
        if (arg2 is Char && arg3 is Char) {
            return chars.toString().replace((arg2 as Char?)!!, (arg3 as Char?)!!)
        }
        if (arg2 is CharSequence && arg3 is CharSequence) {
            return chars.toString().replace(arg2.toString(), arg3.toString())
        }
        // TODO arg2=string/arg3=function of match
        return (arg2 as Pattern).matcher(chars).replaceAll((arg3 as CharSequence).toString())
    }
}
