package operator

class User {
    public final String name

    User(String name) { this.name = name }

    String getName() { "Name: ${name}" }
}

def user = new User('Bob')
assert user.name == 'Name: Bob' // 这里看似是访问属性name,但其实是调用getName方法
//我们直接使用user.name其实相当于user.getName()。如果需要直接访问属性，需要使用.@这个运算符：
assert user.@name == 'Bob'
