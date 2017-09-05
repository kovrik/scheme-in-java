package core.scm.specialforms

import core.environment.Environment
import core.Evaluator
import core.Reflector
import core.exceptions.IllegalSyntaxException
import core.scm.Cons
import core.scm.Symbol

object Try : SpecialForm("try") {

    private  val CATCH = Symbol.intern("catch")
    internal val FINALLY = Symbol.intern("finally")

    private val reflector = Reflector()

    override fun eval(form: List<Any?>, env: Environment, evaluator: Evaluator): Any? {
        if (form.isEmpty()) {
            return null
        }
        var hadCatch = false
        val catches = LinkedHashMap<Class<*>, Any?>()
        var catchBindings: MutableMap<Class<*>, Symbol> = HashMap()
        var fin: Any? = null
        val expressions = ArrayList<Any?>()
        /* Init and check syntax */
        for (i in 1 until form.size) {
            val expr = form[i]
            if (expr is List<*> && !expr.isEmpty()) {
                val op = expr[0]
                if (op == FINALLY) {
                    if (i != form.size - 1) {
                        throw IllegalSyntaxException("try: finally clause must be last in try expression")
                    }
                    if (expr.size > 1) {
                        fin = Cons.list<Any?>(Begin).apply { addAll(expr.subList(1, expr.size)) }
                    }
                    continue
                } else if (op == CATCH) {
                    if (expr.size < 3) {
                        throw IllegalSyntaxException("catch: bad syntax in form: $expr")
                    }
                    hadCatch = true
                    if (catches.isEmpty()) {
                        catchBindings = HashMap()
                    }
                    val clazz = reflector.getClazz(expr[1].toString())
                    val catchExpr = when {
                        expr.size > 3 -> Cons.list<Any?>(Begin).apply { addAll(expr.subList(3, expr.size)) }
                        else -> null
                    }
                    catches.put(clazz, catchExpr)
                    val sym = expr[2] as? Symbol ?:
                              throw IllegalSyntaxException("catch: bad binding form, expected Symbol, actual: ${expr[2]}")
                    catchBindings.put(clazz, sym)
                    continue
                }
            }
            if (hadCatch) {
                throw IllegalSyntaxException("try: only catch or finally clause can follow catch in try expression")
            }
            expressions.add(expr)
        }
        /* Now Evaluate everything */
        try {
            var result: Any? = null
            expressions.forEach { result = evaluator.eval(it, env) }
            return result
        } catch (e: Throwable) {
            /* Check if we had catch block for that type of exception (OR for any superclass) */
            for (clazz in catches.keys) {
                if (clazz.isAssignableFrom(e.javaClass)) {
                    /* Bind exception */
                    env.put(catchBindings[clazz], e)
                    /* Evaluate corresponding catch block */
                    return evaluator.eval(catches[clazz], env)
                }
            }
            /* Unexpected exception, re-throw it */
            throw e
        } finally {
            /* And finally, evaluate finally block (if present) */
            fin?.let { evaluator.eval(fin, env) }
        }
    }
}
