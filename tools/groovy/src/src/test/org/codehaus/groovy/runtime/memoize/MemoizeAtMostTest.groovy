package org.codehaus.groovy.runtime.memoize

/**
 * @author Vaclav Pech
 */
public class MemoizeAtMostTest extends AbstractMemoizeTestCase {

    Closure buildMemoizeClosure(Closure cl) {
        cl.memoizeAtMost(100)
    }

    public void testZeroCache() {
        def flag = false
        Closure cl = {
            flag = true
            it * 2
        }
        Closure mem = cl.memoizeAtMost(0)
        [1, 2, 3, 4, 5, 6].each {mem(it)}
        assert flag
        flag = false
        assertEquals(12, mem(6))
        assert flag
    }

    public void testLRUCache() {
        def flag = false
        Closure cl = {
            flag = true
            it * 2
        }
        Closure mem = cl.memoizeAtMost(3)
        [1, 2, 3, 4, 5, 6].each { mem(it) }
        assert flag
        flag = false
        assert 8 == mem(4)
        assert 10 == mem(5)
        assert 12 == mem(6)
        assert !flag
        assert 6 == mem(3)
        assert flag
        flag = false
        assert 10 == mem(5)
        assert 12 == mem(6)
        assert 6 == mem(3)
        assert !flag
        assert 8 == mem(4)
        assert flag

        flag = false
        assert 10 == mem(5)
        assert flag
    }
}
