package core.procedures.math

import core.procedures.AFn
import core.procedures.FnArgs
import core.scm.BigRatio
import core.utils.Utils
import java.math.BigDecimal

class Exp : AFn(FnArgs(min = 1, max = 1, mandatory = arrayOf<Class<*>>(Number::class.java))) {

    override val isPure = true
    override val name = "exp"
    override operator fun invoke(arg: Any?) = exp(arg as Number?)

    companion object {

        val E = BigDecimal("2.71828182845904523536028747135266249775724709369995")

        fun exp(number: Number?): Number? {
            number!!
            when {
                Utils.isZero(number) -> return Utils.inexactnessTaint(1L, number)
                number is Double || number is Float -> {
                    when {
                        Utils.isNegativeInfinity(number) -> return 0L
                        !Utils.isFinite(number)          -> return number
                        else                             -> return Math.exp(number.toDouble())
                    }
                }
                number is Long || number is Int || number is Short || number is Byte -> return Math.exp(number.toDouble())
                number is BigRatio && number.isOne -> return Math.exp(1.0)
                else -> return Expt.expt(E, number)
            }
        }
    }
}
