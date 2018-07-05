package core.procedures.math.trigonometry

import core.procedures.AFn
import core.procedures.Arity.Exactly
import core.scm.BigComplex
import core.utils.Utils
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.asin
import kotlin.math.ln
import kotlin.math.sqrt

class Asin : AFn<Number?, Number>(name = "asin", isPure = true, arity = Exactly(1),
                                  mandatoryArgsTypes = arrayOf(Number::class.java)) {

    override operator fun invoke(arg: Number?) = when {
        !Utils.isFinite(arg) -> Double.NaN
        Utils.isZero(arg) -> 0L
        arg is BigComplex -> asin(arg)
        else -> asin(arg!!.toDouble()).let {
            when (it.isNaN()) {
                true -> asin(BigComplex(arg))
                else -> it
            }
        }
    }

    /* asin(a+bi) = asin(A) + ln(B + sqrt(B*B - 1))*i
     *
     * A = (sqrt((1+a)^2 + b^2) - sqrt((1-a)^2 + b^2))/2
     * B = (sqrt((1+a)^2 + b^2) + sqrt((1-a)^2 + b^2))/2
     **/
    private fun asin(c: BigComplex): Number {
        val r = c.re
        val i = c.im
        val signum = if (i.signum() == 0) {
            -r.signum()
        } else {
            i.signum()
        }
        val precision = Math.max(Utils.integerDigits(r), Utils.integerDigits(i))
        if (precision >= Utils.DEFAULT_SCALE) {
            val context = MathContext(precision)
            val b2 = i.multiply(i)
            val L = r.add(BigDecimal.ONE).multiply(r.add(BigDecimal.ONE)).add(b2).sqrt(context)
            val R = r.subtract(BigDecimal.ONE).multiply(r.subtract(BigDecimal.ONE)).add(b2).sqrt(context)
            val A = L.subtract(R).divide(Utils.TWO)
            val B = L.add(R).divide(Utils.TWO)

            val re = asin(A.toDouble())
            if (!Utils.isFinite(re)) {
                return re
            }
            val im = ln(B.add(B.multiply(B).subtract(BigDecimal.ONE).sqrt(context)).toDouble())
            if (!Utils.isFinite(im)) {
                return im
            }
            return BigComplex(re, signum * im)
        } else {
            val a = r.toDouble()
            if (!Utils.isFinite(a)) {
                return a
            }
            val b = i.toDouble()
            if (!Utils.isFinite(b)) {
                return b
            }

            val b2 = b * b
            val L = sqrt((1 + a) * (1 + a) + b2)
            val R = sqrt((1 - a) * (1 - a) + b2)
            val A = (L - R) / 2
            val B = (L + R) / 2

            val re = asin(A)
            if (!Utils.isFinite(re)) {
                return re
            }
            val im = ln(B + sqrt(B * B - 1))
            if (!Utils.isFinite(im)) {
                return im
            }
            return BigComplex(re, signum * im)
        }
    }
}
