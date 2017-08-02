package core.procedures.math

import core.procedures.AFn
import core.scm.BigRatio
import core.scm.Type
import core.utils.Utils

import java.math.BigDecimal
import java.math.BigInteger

class LCM : AFn<Any?, Number>(name = "lcm", isPure = true, restArgsType = Type.Rational::class.java) {

    override operator fun invoke(args: Array<out Any?>): Number = when {
        args.isEmpty() -> 1L
        args.size == 1 -> Abs.abs(args[0]!! as Number)
        else           -> args.fold(args[0]!! as Number) { r, n -> lcm(r, n!! as Number) }
    }

    private fun lcm(first: BigRatio, second: BigRatio) = BigRatio.valueOf(Companion.lcm(first.numerator, second.numerator),
                                                                          GCD.gcd(first.denominator, second.denominator))

    private fun lcm(a: BigDecimal, b: BigDecimal): Number = when {
        a.signum() == 0 && b.signum() == 0 -> BigDecimal.ZERO
        maxOf(a.scale(), b.scale()) == 0   -> BigDecimal(Companion.lcm(a.toBigInteger(), b.toBigInteger()))
        else -> ToInexact.toInexact(lcm(ToExact.toExact(a) as BigDecimal, ToExact.toExact(b) as BigDecimal))
    }

    private fun lcm(a: Long, b: Long) = when {
        a.toInt() == 0 && b.toInt() == 0 -> 0L
        else -> a / GCD.gcd(a, b) * b
    }

    private fun lcm(a: Double, b: Double) = when {
        a.toInt() == 0 && b.toInt() == 0 -> 0.0
        else -> a / GCD.gcd(a, b).toDouble() * b
    }

    private fun lcm(first: Number, second: Number): Number {
        val (f, s) = Utils.upcast(first, second)
        return when {
            f is Double     && s is Double     -> lcm(f, s)
            f is Float      && s is Float      -> lcm(f.toDouble(), s.toDouble())
            f is BigRatio   && s is BigRatio   -> lcm(f, s)
            f is BigDecimal && s is BigDecimal -> lcm(f, s)
            f is BigInteger && s is BigInteger -> Companion.lcm(f, s)
            else                               -> lcm(f.toLong(), s.toLong())
        }
    }

    companion object {
        internal fun lcm(first: BigInteger, second: BigInteger) = first.multiply(second.divide(GCD.gcd(first, second)))
    }
}
