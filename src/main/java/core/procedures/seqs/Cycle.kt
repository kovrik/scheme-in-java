package core.procedures.seqs

import core.procedures.AFn
import core.scm.LazySeq
import core.utils.Utils

class Cycle : AFn<Any?, Any?>(name = "cycle", minArgs = 1, maxArgs = 1) {

    override operator fun invoke(arg: Any?): Sequence<Any?> {
        val seq = Utils.toSequence(arg)
        if (!seq.iterator().hasNext()) return emptySequence()
        return LazySeq(generateSequence(seq) { seq }.flatten())
    }
}