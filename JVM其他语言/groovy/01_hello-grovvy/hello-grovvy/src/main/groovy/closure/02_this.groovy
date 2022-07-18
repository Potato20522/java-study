package closure

class Enclosing {
    void run() {
        def whatIsThisObject = { getThisObject() } //定义闭包
        assert whatIsThisObject() == this
        def whatIsThis = { this }
        assert whatIsThis() == this
    }
}

class EnclosedInInnerClass {
    class Inner {
        Closure cl = { this }
    }

    void run() {
        def inner = new Inner() //当前类的实例是inner对象
        assert inner.cl() == inner
    }
}

class NestedClosures {
    void run() {
        def nestedClosures = {
            def cl = { this }
            cl()
        }
        assert nestedClosures() == this //this是指当前类的实例，而不是闭包
    }
}

