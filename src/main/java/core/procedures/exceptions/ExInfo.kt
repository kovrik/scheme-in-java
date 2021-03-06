package core.procedures.exceptions

import core.exceptions.ExInfoException
import core.procedures.AFn
import core.procedures.Arity.Range

class ExInfo : AFn<Any?, ExInfoException>(name = "ex-info", isPure = true, arity = Range(2, 3),
                                          mandatoryArgsTypes = arrayOf(String::class.java, Map::class.java),
                                          restArgsType = Throwable::class.java) {

    override operator fun invoke(args: Array<out Any?>) = ExInfoException(args[0] as String,
                                                                          args[1] as Map<*, *>,
                                                                          args.getOrNull(2) as? Throwable)
}

