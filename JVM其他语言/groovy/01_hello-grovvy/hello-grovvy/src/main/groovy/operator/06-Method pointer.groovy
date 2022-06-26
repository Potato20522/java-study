package operator
/**1.方法指针运算符：对象.&方法名 */
def str = 'example of method reference'
def fun = str.&toUpperCase // 取String的toUpperCase方法指针
println fun.class // class org.codehaus.groovy.runtime.MethodClosure
def upper = fun() // 这里相当于调用了方法
assert upper == str.toUpperCase()

/**2.作为闭包*/
//方法指针运算符可以作为闭包传入，类似于Java中的函数式接口
def transform(List elements, Closure action) {
    def result = []
    elements.each {
        result << action(it)
    }
    result
}
class Person{
    String name
    int age
}
String describe(Person p) {
    "$p.name is $p.age"
}
def action = this.&describe
def list = [
        new Person(name: 'Bob',   age: 42),
        new Person(name: 'Julia', age: 35)]
assert transform(list, action) == ['Bob is 42', 'Julia is 35']


/**3.调用重载方法*/
def doSomething(String str) { str.toUpperCase() }
def doSomething(Integer x) { 2*x }
def reference = this.&doSomething
assert reference('foo') == 'FOO'
assert reference(123)   == 246

/**4.类似于方法引用*/
def foo  = BigInteger.&new
def fortyTwo = foo('42')
assert fortyTwo == 42G

def instanceMethod = String.&toUpperCase
assert instanceMethod('foo') == 'FOO'
