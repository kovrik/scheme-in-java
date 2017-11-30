package core.procedures.math

import core.procedures.AFn
import core.scm.BigComplex
import core.scm.BigRatio
import core.utils.Utils
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.ln
import kotlin.math.pow

class Log : AFn<Number?, Number>(name = "log", isPure = true, minArgs = 1, maxArgs = 1,
                                 mandatoryArgsTypes = arrayOf<Class<*>>(Number::class.java)) {

    companion object {
        /* If number has 307 digits or less, then can use ln(double) */
        private val MAX_DIGITS = 307
        private val VALUE = ln(10.0.pow(MAX_DIGITS.toDouble()))
    }

    override operator fun invoke(arg: Number?) = when {
        Utils.isZero(arg) && Utils.isExact(arg) -> throw ArithmeticException("log: undefined for 0")
        Utils.isOne(arg)  && Utils.isExact(arg) -> 0L
        arg is BigComplex                       -> arg.log()
        arg is BigRatio                         -> logBig(arg.toBigDecimal())
        arg is BigDecimal                       -> logBig(arg)
        arg is BigInteger                       -> logBig(BigDecimal(arg))
        else                                    -> ln(arg!!.toDouble())
    }

    /* Natural logarithm for Big numbers (greater than Double.MAX_VALUE) */
    private fun logBig(number: BigDecimal) = when {
        number.toDouble().isFinite() -> ln(number.toDouble())
        else -> {
            val digits = integerDigits(number)
            val num = number.movePointLeft(digits - digits.rem(MAX_DIGITS))
            (digits / MAX_DIGITS) * VALUE + ln(num.toDouble())
        }
    }

    /* Return number of digits of a given BigDecimal number */
    private fun integerDigits(n: BigDecimal) = if (n.signum() == 0) 1 else n.precision() - n.scale()
}
