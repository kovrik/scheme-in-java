package core.procedures.vectors

import core.procedures.AFn
import core.scm.Vector

class VectorToList : AFn<Vector, List<*>>(name = "vector->list", isPure = true, minArgs = 1, maxArgs = 1,
                                          mandatoryArgsTypes = arrayOf(Vector::class.java)) {

    override operator fun invoke(arg: Vector) = arg.getArray().toList()
}
