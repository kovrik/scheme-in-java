package core.procedures.lists

import core.exceptions.WrongTypeException
import core.procedures.AFn
import core.procedures.cons.Car
import core.procedures.cons.Cdr
import core.procedures.predicates.Predicate
import core.utils.Utils
import core.Writer
import core.procedures.Arity.Exactly

class MemberProc(override val name: String, private inline val predicate: AFn<Any?, Boolean>) :
        AFn<Any?, Any?>(isPure = true, arity = Exactly(2),
                        mandatoryArgsTypes = arrayOf(Any::class.java, List::class.java)) {

    private val car = Car()
    private val cdr = Cdr()

    override operator fun invoke(arg1: Any?, arg2: Any?): Any? {
        val list = arg2!! as List<*>
        if (list.isEmpty()) {
            return false
        }
        var p = 0
        var cons: Any? = list
        while (cons is List<*> && !cons.isEmpty()) {
            if (Utils.toBoolean(predicate(arg1, car(cons)))) {
                return cons
            }
            cons = cdr(cons)
            p += 1
        }
        /* Not found */
        if (p == list.size) {
            if (!Predicate.isProperList(list)) {
                throw WrongTypeException("$name: wrong type argument in position $p (expecting list): ${Writer.write(list)}")
            }
            return false
        }
        throw WrongTypeException("$name: wrong type argument in position ${p+1} (expecting list): ${Writer.write(list)}")
    }
}
