package core.procedures.math

import core.procedures.AFn
import core.scm.BigRatio
import core.scm.Type
import core.utils.Utils

import java.math.BigDecimal
import java.math.BigInteger

class Denominator : AFn<Number?, Number>(name = "denominator", isPure = true, minArgs = 1, maxArgs = 1,
                        mandatoryArgsTypes = arrayOf<Class<*>>(Type.Rational::class.java)) {

    private val toExact = ToExact()

    override operator fun invoke(arg: Number?) = denominator(arg!!)

    private fun denominator(o: Any): Number {
        val isIntegerOrRatio = o is BigRatio || Utils.isInteger(o)
        val exact = if (isIntegerOrRatio) (o as Number) else toExact(o as Number)
        if (exact is BigRatio) {
            if (!isIntegerOrRatio) {
                return BigDecimal(exact.denominator).setScale(1, Utils.ROUNDING_MODE)
            }
            return exact.denominator
        }
        return when (exact) {
            is Long, is Int, is Byte, is Short -> 1L
            is Double, is Float    -> 1.0
            is BigInteger          -> BigInteger.ONE
            is BigDecimal          -> when (exact.scale()) {
                0    -> BigDecimal.ONE
                else -> BigDecimal.ONE.setScale(1, Utils.ROUNDING_MODE)
            }
            else -> 1L
        }
    }
}