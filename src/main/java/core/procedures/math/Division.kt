package core.procedures.math

import core.procedures.AFn
import core.procedures.FnArgsBuilder
import core.scm.BigComplex
import core.scm.BigRatio
import core.utils.Utils

import java.math.BigDecimal
import java.math.BigInteger

class Division : AFn(FnArgsBuilder().min(1).rest(Number::class.java).build()) {

    companion object {
        /* Rolls back to DEFAULT_CONTEXT if result cannot be represented with UNLIMITED precision */
        fun safeBigDecimalDivision(num: BigDecimal, den: BigDecimal): BigDecimal {
            try {
                return num.divide(den, Utils.getMathContext(num, den))
            } catch (e: ArithmeticException) {
                return num.divide(den, Utils.DEFAULT_CONTEXT)
            }
        }
    }

    override val isPure: Boolean
        get() = true

    override val name: String
        get() = "/"

    override fun apply(vararg args: Any?): Number? {
        if (args.size == 1) {
            return apply(1L, args[0] as Number)
        }
        var result = args[0] as Number?
        for (d in 1..args.size - 1) {
            result = apply(result, args[d] as Number)
        }
        return result
    }

    private fun apply(numerator: Number?, denominator: Number?): Number? {
        if (numerator   == null) throw NullPointerException()
        if (denominator == null) throw NullPointerException()
        var numerator = numerator
        var denominator = denominator
        if (Utils.isZero(numerator)) {
            return Utils.inexactnessTaint(numerator, denominator)
        }
        /* Complex numbers*/
        if (numerator is BigComplex) {
            return numerator.divide(denominator)
        }
        if (denominator is BigComplex) {
            return BigComplex(numerator).divide(denominator)
        }
        /* Big Ratio numbers */
        if (numerator is BigRatio && denominator is BigRatio) {
            return numerator.divide(denominator)
        }
        if (numerator is BigRatio) {
            if (Utils.isExact(denominator)) {
                return numerator.divide(Utils.toBigInteger(denominator))
            } else {
                numerator = numerator.toDouble()
            }
        }
        if (denominator is BigRatio) {
            if (Utils.isExact(numerator)) {
                return denominator.reciprocal().multiply(Utils.toBigInteger(numerator))
            } else {
                denominator = denominator.toDouble()
            }
        }
        if (Utils.isExact(numerator) && Utils.isExact(denominator)) {
            return BigRatio.valueOf(Utils.toBigInteger(numerator), Utils.toBigInteger(denominator))
        }
        if (numerator is Float && denominator is Float) {
            val result = numerator.toFloat() / denominator.toFloat()
            if (java.lang.Float.isNaN(result) || java.lang.Float.isInfinite(result)) {
                return Utils.toBigDecimal(numerator).divide(Utils.toBigDecimal(denominator), Utils.DEFAULT_CONTEXT)
            }
            return result
        }
        if (numerator is Double || denominator is Double || numerator is Float || denominator is Float) {
            val result = numerator.toDouble() / denominator.toDouble()
            if (java.lang.Double.isNaN(result) || java.lang.Double.isInfinite(result)) {
                return Utils.toBigDecimal(numerator).divide(Utils.toBigDecimal(denominator), Utils.DEFAULT_CONTEXT)
            }
            return result
        }
        if (numerator is BigDecimal || denominator is BigDecimal) {
            return Utils.toBigDecimal(numerator).divide(Utils.toBigDecimal(denominator), Utils.DEFAULT_CONTEXT)
        }
        if (numerator is BigInteger || denominator is BigInteger) {
            return Utils.toBigInteger(numerator).divide(Utils.toBigInteger(denominator))
        }
        val f = numerator.toDouble()
        val s = denominator.toDouble()
        return f / s
    }
}
