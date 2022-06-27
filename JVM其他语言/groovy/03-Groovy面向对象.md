# 注意点

和Java一样的就不讲了，有点注意，groovy中默认访问修饰符就是public

groovy中的类型是动态的，基本数据类型可以在运行时动态转换为包装类型，比如：

```groovy
class Foo {
    static int i
}

assert Foo.class.getDeclaredField('i').type == int.class           
assert Foo.i.class != int.class && Foo.i.class == Integer.class   
```

在字节码中变量i是基本数据类型int

但在运行时，又变成了包装类型Integer

而且**不用担心变成包装类型后，会有拆箱和装箱的性能损耗**，groovy会有特殊处理进行优化，这里就不提了，详细可以看官方文档：https://docs.groovy-lang.org/latest/html/documentation/core-differences-java.html



# 属性get和set

```groovy
class Person {
    String name                             
    int age                                 
}
```

groovy会自动生成这两个字段的get和set方法，以及将这两个字段变成private的，可以使用jclasslib查看字节码以验证。



final修饰的就不会生成set方法

```groovy
class Person {
    final String name                   
    final int age                       
    Person(String name, int age) {
        this.name = name                
        this.age = age                  
    }
}
```



这样定义，可以直接访问属性名，当作方法调用

```groovy
class Person {
    String name
    void name(String name) {
        this.name = "Wonder $name"      
    }
    String title() {
        this.name                       
    }
}
def p = new Person()
p.name = 'Diana' //调用setName()                       
assert p.name == 'Diana' //调用getName()               
p.name('Woman') //调用name()                        
assert p.title() == 'Wonder Woman'      
```



properties这个委托属性可以查看类的字段

```groovy
class Person {
    String name
    int age
}
def p = new Person()
assert p.properties.keySet().containsAll(['name','age'])
```