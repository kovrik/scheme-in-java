package core.procedures.delayed

import core.procedures.AFn
import core.scm.Promise

import java.util.concurrent.CompletableFuture

class Deliver : AFn<Any?, CompletableFuture<Any>?>(name = "deliver", minArgs = 2, maxArgs = 2,
                                                   mandatoryArgsTypes = arrayOf(Promise::class.java, Any::class.java)) {

    override operator fun invoke(arg1: Any?, arg2: Any?): Promise? = (arg1!! as Promise).apply {
        when {
            isDone || isCompletedExceptionally -> return null
            else -> complete(arg2)
        }
    }
}