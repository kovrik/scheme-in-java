package core.procedures.seqs

import core.procedures.AFn
import core.scm.ThunkSeq
import core.utils.Utils

class Concat : AFn<Any?, Any?>(name = "concat") {

    override operator fun invoke(args: Array<out Any?>) = ThunkSeq(args.map { Utils.toSequence(it) }.reduce { f, s -> f.plus(s) })
}