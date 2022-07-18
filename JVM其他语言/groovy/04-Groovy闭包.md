对比其他语言，比如js的闭包

文档：http://www.groovy-lang.org/closures.html

# 定义闭包

闭包（Closure）是很多编程语言中很重要的概念，那么Groovy中闭包是什么，官方定义是“**Groovy中的闭包是一个开放，匿名的代码块，可以接受参数，返回值并分配给变量**”，简而言之，他说一个匿名的代码块，可以接受参数，有返回值。可以理解为闭包是匿名函数、这个函数可以赋值给一个变量。

```groovy
{ [closureParameters -> ] statements }
```

其中[closureParameters->]代表参数们，多参数用逗号分割，用->隔开参数与内容，没有参数可以不写->

groovy的函数，可以不用写返回值类型，默认最后一句代码的直接结果就是作为函数的返回值。

## 闭包的写法

```groovy
//执行一句话
{printf 'Hello World'}

//闭包有默认参数it，且不用声明
{println it}

//闭包有默认参数it，声明了也无所谓
{it->println it}

//name的自定义的参数名
{name->println name}

//多个参数的闭包
{ String x, int y ->                                
    println "hey ${x} the value is ${y}"
}

//可以包含多个语句
{ reader ->                                         
    def line = reader.readLine()
    line.trim()
}
```

## Closure对象

每次定义的闭包是一个groovy.lang.Closure对象，我们可以把一个闭包赋值给一个变量

```groovy
def listener = { e -> println "Clicked on $e.source" }      
assert listener instanceof Closure
Closure callback = { println 'Done!' }                      
Closure<Boolean> isTextFile = {
    File it -> it.name.endsWith('.txt')                     
}
```

## 调用闭包

执行闭包对象有两种，一是直接用**括号+参数**，二是调用**call方法**

```groovy
def code = { 123 }
assert code() == 123 //方式一：括号+参数
assert code.call() == 123 //方式二：call方法

def isOdd = { int i -> i%2 != 0 }                           
assert isOdd(3) == true                                     
assert isOdd.call(2) == false                               

def isEven = { it%2 == 0 }                                  
assert isEven(3) == false                                   
assert isEven.call(2) == true  
```

# 参数

## 正常参数

可选类型、可选默认值

```groovy
def closureWithOneArg = { str -> str.toUpperCase() }
assert closureWithOneArg('groovy') == 'GROOVY'

def closureWithOneArgAndExplicitType = { String str -> str.toUpperCase() }
assert closureWithOneArgAndExplicitType('groovy') == 'GROOVY'

def closureWithTwoArgs = { a,b -> a+b }
assert closureWithTwoArgs(1,2) == 3

def closureWithTwoArgsAndExplicitTypes = { int a, int b -> a+b }
assert closureWithTwoArgsAndExplicitTypes(1,2) == 3

def closureWithTwoArgsAndOptionalTypes = { a, int b -> a+b }
assert closureWithTwoArgsAndOptionalTypes(1,2) == 3

def closureWithTwoArgAndDefaultValue = { int a, int b=2 -> a+b }
assert closureWithTwoArgAndDefaultValue(1) == 3
```

## 隐式参数it

当闭包未显式定义参数列表（使用 ）时，会有一个叫做it的隐式参数

```groovy
def greeting = { "Hello, $it!" }
assert greeting('Patrick') == 'Hello, Patrick!'
```

等价于下面：

```groovy
def greeting = { it -> "Hello, $it!" }
assert greeting('Patrick') == 'Hello, Patrick!'
```

如果要声明一个不接受参数且必须限制为没有参数的调用的闭包**，则必须使用**显式空参数列表声明它：

```groovy
def magicNumber = { -> 42 }

// this call will fail because the closure doesn't accept any argument
magicNumber(11)
```

## 可变参数

和Java中的可变参数一样

```groovy
def concat1 = { String... args -> args.join('') }           
assert concat1('abc','def') == 'abcdef'                     
def concat2 = { String[] args -> args.join('') }            
assert concat2('abc', 'def') == 'abcdef'

def multiConcat = { int n, String... args ->                
    args.join('')*n
}
assert multiConcat(2, 'abc','def') == 'abcdefabcdef'
```

# 委托

闭包和lambda不一样，委托delegate是闭包中的一个关键概念，这一点lambda中没有。闭包的委托在DSL中有广泛应用。

## 闭包内this，owner，delegate对象

在闭包内部，有三个内置对象this，owner，delegate，我们可以直接this，owner，delegate调用，或者用get方法：

- getThisObject() 等于 this
- getOwner() 等于 owner
- getDelegate() 等于delegate

那么这三个对象，分别指代的是哪个对象，是否和java的this关键字一样，我们先做文字解释：

- **this** 对应于定义闭包的那个类，如果在内部类中定义，指向的是内部类
- **owenr** 对应于定义闭包的那个类或者闭包，如果在闭包中定义，对应闭包，否则同this一致
- **delegate** 默认是和owner一致，或者自定义delegate指向



### **this**

```groovy

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

```



```groovy
class Person {
    String name
    int age
    String toString() { "$name is $age years old" }

    String dump() {
        def cl = {
            String msg = this.toString()               
            println msg
            msg
        }
        cl()
    }
}
def p = new Person(name:'Janice', age:74)
assert p.dump() == 'Janice is 74 years old'
```

### **owenr**

```groovy
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
```

### **delegate委托**

闭包可以设置delegate对象，**设置delegate的意义就是将闭包和一个具体的对象关联起来**

```groovy
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
```

## **更改委托对象**

```groovy
class Person {
    String name
}
class Thing {
    String name
}

def p = new Person(name: 'Norman')
def t = new Thing(name: 'Teapot')
```

定义一个闭包

```groovy
def upperCasedName = { delegate.name.toUpperCase() }
//def upperCasedName = { name.toUpperCase() } //delegate可以省略
```

更改闭包的委托对象：

```groovy
upperCasedName.delegate = p
assert upperCasedName() == 'NORMAN'
upperCasedName.delegate = t
assert upperCasedName() == 'TEAPOT'
```

和如下的写法很相似：

```groovy
def target = p
def upperCasedNameUsingVar = { target.name.toUpperCase() }
assert upperCasedNameUsingVar() == 'NORMAN'
```

## 委托策略

在闭包中可以访问被代理对象的属性和方法，那么，如果闭包所在的类或闭包中和被代理的类中有相同名称的方法，到底要调用哪个方法，其实这个问题groovy肯定考虑到了，为我们设定了几个代理的策略：

- Closure.OWNER_FIRST是默认策略。优先在owner寻找，owner没有再delegate
- Closure.DELEGATE_FIRST：优先在delegate寻找，delegate没有再owner
- Closure.OWNER_ONLY：只在owner中寻找
- Closure.DELEGATE_ONLY：只在delegate中寻找
- Closure.TO_SELF：暂时没有用到，哎不知道啥意思

```groovy
class Person {
    String name
    def pretty = { "My name is $name" }             
    String toString() {
        pretty()
    }
}
class Thing {
    String name                                     
}

def p = new Person(name: 'Sarah')
def t = new Thing(name: 'Teapot')

assert p.toString() == 'My name is Sarah'           
p.pretty.delegate = t                               
assert p.toString() == 'My name is Sarah'  
```

还可以关闭委托策略

```groovy
p.pretty.resolveStrategy = Closure.DELEGATE_FIRST
assert p.toString() == 'My name is Teapot'
```

如果委托之一（或所有者）**没有**这样的方法或属性，则可以说明“委托优先”和“仅委托”或“所有者优先”和“仅所有者”之间的区别：

```groovy
class Person {
    String name
    int age
    def fetchAge = { age }
}
class Thing {
    String name
}

def p = new Person(name:'Jessica', age:42)
def t = new Thing(name:'Printer')
def cl = p.fetchAge
cl.delegate = p
assert cl() == 42
cl.delegate = t
assert cl() == 42
cl.resolveStrategy = Closure.DELEGATE_ONLY
cl.delegate = p
assert cl() == 42
cl.delegate = t
try {
    cl()
    assert false
} catch (MissingPropertyException ex) {
    // "age" is not defined on the delegate
}
```

# GStrings 中的闭包

```groovy
def x = 1
def gs = "x = ${x}"
assert gs == 'x = 1'

x = 2
assert gs == 'x = 2'
//不相等，因为上述 GString 中的语法不表示闭包，而是创建 GString 时计算的表达式。
```

GString中的闭包按照如下：

```groovy
def x = 1
def gs = "x = ${-> x}"
assert gs == 'x = 1'

x = 2
assert gs == 'x = 2'
```

再看看下面的两个例子：

```groovy
class Person {
    String name
    String toString() { name }          
}
def sam = new Person(name:'Sam')        
def lucy = new Person(name:'Lucy')      
def p = sam                             
def gs = "Name: ${p}"                   
assert gs == 'Name: Sam'                
p = lucy                                
assert gs == 'Name: Sam'                
sam.name = 'Lucy'                       
assert gs == 'Name: Lucy'     
```



```groovy
class Person {
    String name
    String toString() { name }
}
def sam = new Person(name:'Sam')
def lucy = new Person(name:'Lucy')
def p = sam
// Create a GString with lazy evaluation of "p"
def gs = "Name: ${-> p}" //显式声明空参数列表来使用闭包
assert gs == 'Name: Sam'
p = lucy
assert gs == 'Name: Lucy'
```

# 闭包中的索引

**左边参数：curry**

```groovy
def nCopies = { int n, String str -> str*n }    
def twice = nCopies.curry(2)                    
assert twice('bla') == 'blabla'                 
assert twice('bla') == nCopies(2, 'bla')  
```



**右边参数：rcurry**

```groovy
def blah = nCopies.rcurry('bla')                
assert blah(2) == 'blabla'                      
assert blah(2) == nCopies(2, 'bla')   
```



如果闭包接受超过 2 个参数，则可以使用以下命令设置任意参数：**ncurry**

```groovy
def volume = { double l, double w, double h -> l*w*h }      
def fixedWidthVolume = volume.ncurry(1, 2d)                 
assert volume(3d, 2d, 4d) == fixedWidthVolume(3d, 4d)       
def fixedWidthAndHeight = volume.ncurry(1, 2d, 4d)          
assert volume(3d, 2d, 4d) == fixedWidthAndHeight(3d)        
```