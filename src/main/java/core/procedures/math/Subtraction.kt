package core.procedures.math

import core.procedures.AFn
import core.scm.BigComplex
import core.scm.BigRatio
import core.utils.Utils

import java.math.BigDecimal
import java.math.BigInteger

class Subtraction : AFn<Any?, Number?>(name = "-", isPure = true, minArgs = 1, restArgsType = Number::class.java) {

    override operator fun invoke(args: Array<out Any?>) = when (args.size) {
        1 -> when {
            args[0] == null -> null
            args[0] is Long -> try {
                Math.negateExact(args[0] as Long) as Number
            } catch (e: ArithmeticException) {
                -(args[0] as Long).toBigInteger()
            }
            args[0] is Double -> -(args[0] as Double)
            args[0] is BigDecimal -> -(args[0] as BigDecimal)
            args[0] is BigInteger -> -(args[0] as BigInteger)
            args[0] is BigRatio -> -(args[0] as BigRatio)
            args[0] is Float -> -(args[0] as Float)
            args[0] is Int -> try {
                Math.negateExact(args[0] as Int) as Number
            } catch (e: ArithmeticException) {
                Math.negateExact((args[0] as Int).toLong())
            }
            else -> subtract(0L, args[0] as Number)
        }
        else -> args.reduce { f, s -> subtract(f!! as Number, s!! as Number) } as Number
    }

    private fun subtract(first: Number, second: Number): Number? {
        val (f, s) = Utils.upcast(first, second)
        return when {
            /* Special cases */
            Utils.isPositiveInfinity(f) && Utils.isNegativeInfinity(s) -> Double.NaN
            Utils.isPositiveInfinity(s) && Utils.isNegativeInfinity(f) -> Double.NaN
            !Utils.isFinite(f)                 -> f
            !Utils.isFinite(s)                 -> s
            Utils.isZero(s)                    -> Utils.inexactnessTaint(f, s)
            f is BigComplex && s is BigComplex -> f - s
            f is BigRatio   && s is BigRatio   -> f - s
            f is BigDecimal && s is BigDecimal -> f - s
            f is BigInteger && s is BigInteger -> f - s
            f is Double     && s is Double     -> f - s
            f is Float      && s is Float      -> f - s
            else -> {
                val fl = f.toLong()
                val sl = s.toLong()
                return try {
                    Math.subtractExact(fl, sl)
                } catch (e: ArithmeticException) {
                    fl.toBigInteger() - sl.toBigInteger()
                }
            }
        }
    }
}
