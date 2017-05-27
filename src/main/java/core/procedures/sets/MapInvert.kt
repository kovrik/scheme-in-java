package core.procedures.sets

import core.procedures.AFn
import core.procedures.FnArgsBuilder

class MapInvert : AFn(FnArgsBuilder().min(1).mandatory(arrayOf<Class<*>>(Map::class.java)).build()) {

    override val isPure = true
    override val name = "map-invert"

    override operator fun invoke(vararg args: Any?): Map<Any?, Any?>? {
        val result = HashMap<Any?, Any?>()
        for ((key, value) in args[0] as Map<Any?, Any?>) {
            result.put(value, key)
        }
        return result
    }
}
