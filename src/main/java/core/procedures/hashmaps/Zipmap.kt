package core.procedures.hashmaps

import core.procedures.AFn
import core.procedures.Arity.Exactly
import core.scm.MutableHashmap
import core.scm.Type
import core.utils.Utils

class Zipmap : AFn<Any?, Map<*, *>>(
    name = "zipmap", isPure = true, arity = Exactly(2),
    mandatoryArgsTypes = arrayOf(Type.Seqable::class.java, Type.Seqable::class.java)
) {

    override operator fun invoke(arg1: Any?, arg2: Any?) =
        Utils.toSequence(arg1).zip(Utils.toSequence(arg2)).toMap(MutableHashmap())
}
