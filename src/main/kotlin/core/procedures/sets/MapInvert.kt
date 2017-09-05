package core.procedures.sets

import core.procedures.AFn
import core.scm.MutableHashmap

class MapInvert : AFn<Any?, Map<*, *>>(name = "map-invert", isPure = true, minArgs = 1,
                                             mandatoryArgsTypes = arrayOf<Class<*>>(Map::class.java)) {

    override operator fun invoke(args: Array<out Any?>) = MutableHashmap().apply {
        (args[0]!! as Map<*, *>).forEach { k, v -> put(v, k) }
    }
}