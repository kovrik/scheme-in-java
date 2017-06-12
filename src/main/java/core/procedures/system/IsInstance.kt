package core.procedures.system

import core.procedures.AFn

class IsInstance : AFn<Any?, Boolean>(name = "instance?", isPure = true, minArgs = 2, maxArgs = 2,
                       mandatoryArgsTypes = arrayOf(Class::class.java, Any::class.java)) {

    override operator fun invoke(arg1: Any?, arg2: Any?) = (arg1 as Class<*>).isAssignableFrom(arg2!!.javaClass)
}
