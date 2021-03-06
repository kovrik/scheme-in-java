package core.procedures.math.trigonometry

import core.procedures.AFn
import core.procedures.Arity.Exactly
import core.procedures.math.Multiplication
import core.scm.Complex
import core.utils.Utils
import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.sin
import kotlin.math.sinh

class Sin : AFn<Number?, Number>(name = "sin", isPure = true, arity = Exactly(1),
                                 mandatoryArgsTypes = arrayOf(Number::class.java)) {

    override operator fun invoke(arg: Number?) = when {
        Utils.isZero(arg!!) -> 0L
        arg is Complex   -> sin(arg)
        else                -> sin(arg.toDouble())
    }

    companion object {

        private val multiplication = Multiplication()

        fun sin(c: Complex): Complex {
            val x = c.re.toDouble()
            val y = c.im.toDouble()
            return Complex(multiplication.invoke(sin(x), cosh(y)), multiplication.invoke(cos(x), sinh(y)))
        }
    }
}
