package unittests

import core.exceptions.IllegalSyntaxException
import core.exceptions.ReentrantDelayException
import core.exceptions.WrongTypeException
import core.scm.Keyword
import org.junit.Assert.*
import org.junit.Test

class DelayedTest : AbstractTest() {

    @Test
    fun testEvalDelay() {
        assertEquals(true,  eval("(promise?   (delay (* (+ 2 3) 4))))", env))
        assertEquals(false, eval("(procedure? (delay (* (+ 2 3) 4))))", env))
    }

    @Test
    fun testEvalDelayed() {
        try {
            eval("((delay (* (+ 2 3) 4)))", env)
            fail()
        } catch (e: IllegalSyntaxException) {
            assertTrue(e.message!!.contains("wrong type to apply"))
        }
    }

    @Test
    fun testEvalForce() {
        assertEquals(true, eval("(force (delay (= (+ 1 2) 3)))", env))
        assertEquals(5L, eval("(force (delay 1 2 3 4 5))", env))
    }

    @Test
    fun testReentrantDelay() {
        /* Check that re-entrant promises are not allowed
         * See http://lambda-the-ultimate.org/node/4686A
         */
        eval("(define x 0)", env)
        val conundrum = "(define p" +
                        "  (delay" +
                        "    (if (= x 5)" +
                        "      x" +
                        "      (begin" +
                        "        (set! x (+ x 1))" +
                        "        (force p)" +
                        "        (set! x (+ x 1))" +
                        "        x))))"
        eval(conundrum, env)
        try {
            eval("(force p)", env)
            fail()
        } catch (e: ReentrantDelayException) {
            assertTrue(e.message!!.startsWith("re-entrant delay:"))
        }
    }

    @Test
    fun testPromise() {
        assertEquals(false, eval("(let ((p (promise))) (realized? p))", env))
        assertEquals(false, eval("(let ((p (promise))) (future?   p))", env))
        assertEquals(false, eval("(let ((p (promise))) (delay?    p))", env))
        assertEquals(true,  eval("(let ((p (promise))) (promise?  p))", env))
        assertEquals(true,  eval("(let ((p (promise))) (deliver p 1) (realized? p))", env))
        assertEquals(12L,   eval("(let ((p (promise))) (deliver p (+ 1 2 3)) (+ @p (deref p)))", env))
        assertEquals(Keyword.intern("timeout"), eval("(deref (promise) 1 :timeout)", env))
    }

    @Test
    fun testDelay() {
        assertEquals(false, eval("(let ((d (delay (+ 1 2 3)))) (realized? d))", env))
        assertEquals(false, eval("(let ((d (delay (+ 1 2 3)))) (future?   d))", env))
        assertEquals(true,  eval("(let ((d (delay (+ 1 2 3)))) (delay?    d))", env))
        assertEquals(true,  eval("(let ((d (delay (+ 1 2 3)))) (promise?  d))", env))
        assertEquals(true,  eval("(let ((d (delay (+ 1 2 3)))) @d (realized? d))", env))
        assertEquals(6L,    eval("(let ((d (delay (+ 1 2 3)))) @d)", env))
    }

    @Test
    fun testFuture() {
        assertEquals(true,  eval("(let ((f (future (+ 1 2 3)))) (future?   f))", env))
        assertEquals(false, eval("(let ((f (future (+ 1 2 3)))) (delay?    f))", env))
        assertEquals(false, eval("(let ((f (future (+ 1 2 3)))) (promise?  f))", env))
        assertEquals(6L,    eval("(let ((f (future (+ 1 2 3)))) @f)", env))
        assertEquals(true,  eval("(let ((f (future (+ 1 2 3)))) @f (future-done? f))", env))
        assertEquals(true,  eval("(let ((f (future (sleep 5000)))) (future-cancel f) (future-cancelled? f))", env));
        assertEquals(Keyword.intern("timeout"), eval("(deref (future (sleep 1000000)) 1 :timeout)", env))
        try {
            eval("(realized? 123)", env)
            fail()
        } catch (e: WrongTypeException) {
            // success
        }
    }

    @Test
    fun testIsLazySeqRealized() {
        assertEquals(false, eval("(realized? (lazy-seq (range 5)))", env))
        assertEquals(false, eval("(let ((s (lazy-seq (range)))) (take 1 s) (realized? s))", env))
        assertEquals(true,  eval("(let ((s (lazy-seq (range)))) (str (take 1 s)) (realized? s))", env))
    }
}
