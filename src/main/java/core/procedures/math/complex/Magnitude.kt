package core.procedures.math.complex

import core.procedures.AFn
import core.procedures.math.Abs
import core.scm.BigComplex

class Magnitude : AFn<Number?, Number>(name = "magnitude", isPure = true, minArgs = 1, maxArgs = 1,
                      mandatoryArgsTypes = arrayOf<Class<*>>(Number::class.java)) {

    override operator fun invoke(arg: Number?): Number {
        arg!!
        if (arg is BigComplex) return arg.magnitude()
        return Abs.abs(arg)
    }
}
