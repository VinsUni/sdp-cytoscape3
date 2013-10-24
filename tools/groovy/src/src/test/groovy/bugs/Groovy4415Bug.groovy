package groovy.bugs

class Groovy4415Bug extends GroovyTestCase {
    void testCompilerShouldFindMethod() {
        // interface defined in Groovy
        assertScript '''
            public interface MyInterface<T> { T getId() }
            public class A implements MyInterface<Long> {
                Long id
            }
            def o = new A(id:666)
            assert o.id == 666
        '''

        // the bug report is when the interface is written in Java
        assertScript '''
            public class A implements groovy.bugs.Groovy4415BugSupport<Long> {
                Long id
            }
            def o = new A(id:666)
            assert o.id == 666
        '''
    }

    void testGroovy4645() {
        assertScript '''
            interface CovariantReturns {
                Foo getGood()

                Object getBad()
            }

            class Foo {}

            class CovariantReturnsImpl implements CovariantReturns {
                //getter is generated by groovy as required by the interface
                final Foo good

                //this variable is defined as the subtype of Object
                final Foo bad
            
                /*
                I would expect that the following method would be generated by groovy
                which is using Java's covariant returns feature.  Including the method below
                will allow the groovy compiler to succeed but is annoying that it has to be explicitly
                defined in code.

                public Foo getBad() { return bad }
                */
            }
            new CovariantReturnsImpl()
        '''
    }
}

