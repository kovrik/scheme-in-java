package core.procedures.vectors

import core.procedures.AFn
import core.scm.Vector

class VectorImmutable : AFn() {

    override val name: String
        get() = "vector-immutable"

    override fun apply(args: Array<Any?>): Vector {
        return Vector(*args)
    }
}