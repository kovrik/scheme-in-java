package core.procedures.system

import core.procedures.AFn
import core.procedures.Arity.Exactly
import core.scm.Type
import java.security.SecureRandom

class CryptoRandomBytes : AFn<Number?, ByteArray>(name = "crypto-random-bytes", isPure = false, arity = Exactly(1),
                                                  restArgsType = Type.ExactNonNegativeInteger::class.java) {

    private val secureRandom = SecureRandom()

    override operator fun invoke(arg: Number?) = ByteArray(arg!!.toInt()).apply { secureRandom.nextBytes(this) }
}
