package closure.delegate
//delegate 默认是和owner一致，或者自定义delegate指向
class Enclosing3 {
    void run() {
        def cl = { getDelegate() }
        def cl2 = { delegate }
        assert cl() == cl2()
        assert cl() == this
        def enclosed = {//嵌套闭包
            { -> delegate }.call()
        }
        assert enclosed() == enclosed
    }
}

