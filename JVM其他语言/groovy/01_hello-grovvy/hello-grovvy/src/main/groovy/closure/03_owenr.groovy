package closure

class Enclosing2 {
    void run() {
        def whatIsOwnerMethod = { getOwner() }
        assert whatIsOwnerMethod() == this
        def whatIsOwner = { owner } //闭包的owner和this相同，都是Enclosing2的当前实例
        assert whatIsOwner() == this
    }
}
class EnclosedInInnerClass2 {
    class Inner2 {
        Closure cl = { owner }//owner是Inner2的实例
    }
    void run() {
        def inner = new Inner2()
        assert inner.cl() == inner
    }
}
class NestedClosures2 {
    void run() {
        def nestedClosures = {
            def cl = { owner } //owner是nestedClosures这个闭包
            cl()
        }
        assert nestedClosures() == nestedClosures
    }
}