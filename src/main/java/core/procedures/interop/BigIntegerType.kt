package core.procedures.interop

import core.procedures.AFn
import core.procedures.FnArgsBuilder
import core.utils.Utils

import java.math.BigInteger

class BigIntegerType : AFn(FnArgsBuilder().min(1).max(1).build()) {

    override val isPure: Boolean
        get() = true

    override val name: String
        get() = "bigint"

    override fun apply1(arg: Any?): BigInteger {
        if (arg is Number) {
            return Utils.toBigInteger((arg as Number?)!!)
        }
        return BigInteger(arg!!.toString())
    }
}