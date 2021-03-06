package core.procedures.sets

import core.exceptions.WrongTypeException
import core.procedures.AFn
import core.procedures.Arity.Exactly
import core.scm.MutableSet

class SetProc : AFn<Any?, Set<*>>(name = "set", isPure = true, arity = Exactly(1)) {

    override operator fun invoke(arg: Any?) = when (arg) {
        is Collection<*> -> MutableSet(arg)
        is CharSequence  -> MutableSet<Any?>(arg.length).apply { addAll(arg.asSequence()) }
        else -> throw WrongTypeException(name, "List or Vector or Set or String", arg)
    }
}
