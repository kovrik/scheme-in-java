package core.procedures.interop

import core.procedures.AFn
import core.procedures.FnArgsBuilder
import core.utils.Utils

class BooleanType : AFn(FnArgsBuilder().min(1).max(1).build()) {

    override val isPure = true
    override val name = "boolean"

    override operator fun invoke(arg: Any?): Boolean? {
        /* Have to box it */
        when {
            Utils.toBoolean(arg) -> return java.lang.Boolean.TRUE
            else -> return java.lang.Boolean.FALSE
        }
    }
}
