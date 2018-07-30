package core.procedures.math.trigonometry

import core.procedures.AFn
import core.procedures.Arity.Exactly
import core.scm.BigComplex
import core.utils.Utils

import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.sin
import kotlin.math.sinh

class Sinh : AFn<Number?, Number>(name = "sinh", isPure = true, arity = Exactly(1),
                                  mandatoryArgsTypes = arrayOf(Number::class.java)) {

    override operator fun invoke(arg: Number?) = when {
        Utils.isZero(arg) -> 0L
        arg is BigComplex -> sinh(arg)
        else              -> sinh(arg!!.toDouble())
    }

    companion object {
        /* sinh(x + yi) = sinh(x)*cos(y) + cosh(x)*sin(y)*i */
        internal fun sinh(c: BigComplex): Number {
            val x = c.re.toDouble()
            val y = c.im.toDouble()
            val re = sinh(x) * cos(y)
            if (!re.isFinite()) {
                return Double.NaN
            }
            val im = cosh(x) * sin(y)
            if (!im.isFinite()) {
                return Double.NaN
            }
            return BigComplex(re, im)
        }
    }
}
