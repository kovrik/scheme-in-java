package core.procedures.hashmaps

import core.procedures.AFn
import core.scm.Hashmap
import core.utils.Utils

class Zipmap : AFn<Any?, Map<*, *>>(name = "zipmap", isPure = true, minArgs = 2, maxArgs = 2) {

    override operator fun invoke(arg1: Any?, arg2: Any?) = Hashmap(Utils.toSequence(arg1).zip(
                                                                        Utils.toSequence(arg2)).toMap().toMutableMap())
}