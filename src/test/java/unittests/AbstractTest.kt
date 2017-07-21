package unittests

import core.environment.DefaultEnvironment
import core.environment.Environment
import core.evaluator.Evaluator
import core.reader.StringReader
import core.scm.Symbol
import org.junit.Assert.assertEquals
import java.util.*

abstract class AbstractTest {

    private   val reader = StringReader()
    protected val eval = Evaluator()
    protected val env = DefaultEnvironment()

    init {
        /* Eval lib procedures */
        env.libraryProcedures.flatMap { reader.read(it)!! }.forEach { eval.macroexpandAndEvaluate(it, env) }
    }

    /* Helper method: evaluates first S-expression */
    protected fun eval(sexp: String, env: Environment) = eval.macroexpandAndEvaluate(reader.readFirst(sexp)!!, env)

    protected fun s(str: String) = Symbol.intern(str)

    protected fun assertAllEqual(expected: Any, forms: Array<String>, env: Environment) {
        Arrays.stream(forms).forEach { assertEquals(it, expected, eval(it, env)) }
    }
}
