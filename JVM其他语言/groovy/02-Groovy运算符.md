参考来源：https://www.jianshu.com/p/b25ff17d10a1

文档：http://www.groovy-lang.org/operators.html

与Java一样的就不展开了～

# 算术运算符

## 除号（/）的特殊行为

Groovy中的除`/`和Java的表现不一样：

- 当两个操作数中有一个是`float`或`double`时，结果是`double`
- 当两个操作数都是整型（`short`、`char`、`byte`、`int`、`long`或`BigInteger`）或者`BigDecimal`时，结果是`BigDecimal`
- 如果要像Java那样*取整*，需要调用`intdiv`方法

```groovy
def result = 1 / 3.0f // 当其中有一个是float或double时
println result.class  // 结果是:class java.lang.Double
println result        // 0.3333333333333333
println 4.intdiv(3)   // 结果为1,与Java一样

def newResult = 1 / 2   // 当两个操作数都是整型
println newResult.class // class java.math.BigDecimal
println newResult       // 结果是0.5
```

## 幂运算

Power operator

**表示幂运算，如`2 ** 3`表示为2的三次方。

```groovy
// base and exponent are ints and the result can be represented by an Integer
// 基数和指数都是int,所得结果可以用int表示，那么结果就是int类型
assert 2**3 instanceof Integer    //  8
assert 10**9 instanceof Integer   //  1_000_000_000

// the base is a long, so fit the result in a Long
// (although it could have fit in an Integer)
// 基数是long类型，所以结果也是long类型，尽管用int就足够表示
assert 5L**2 instanceof Long       //  25

// the result can't be represented as an Integer or Long, so return a BigInteger
// 当结果超过了int和long的表示范围,用BigInteger表示
assert 100**10 instanceof BigInteger   //  10e20
assert 1234**123 instanceof BigInteger //  170515806212727042875...

// the base is a BigDecimal and the exponent a negative int
// but the result can be represented as an Integer
assert 0.5**-2 instanceof Integer    //  4

// the base is an int, and the exponent a negative float
// but again, the result can be represented as an Integer
assert 1**-0.3f instanceof Integer    //  1

// the base is an int, and the exponent a negative int
// but the result will be calculated as a Double
// (both base and exponent are actually converted to doubles)
assert 10**-1 instanceof Double     //  0.1

// the base is a BigDecimal, and the exponent is an int, so return a BigDecimal
assert 1.2**10 instanceof BigDecimal //  6.1917364224

// the base is a float or double, and the exponent is an int
// but the result can only be represented as a Double value
assert 3.4f**5 instanceof Double     //  454.35430372146965
assert 5.6d**2 instanceof Double     //  31.359999999999996

// the exponent is a decimal value
// and the result can only be represented as a Double value
assert 7.8**1.9 instanceof Double     //  49.542708423868476
assert 2**0.1f instanceof Double     //  1.0717734636432956

```

# Elvis运算符

在Java中，我们有时会用三元运算符来简化代码，比如下边的例子：

```java
String name = getName(); // 假设这个方法可能返回空值，如果我们想在为空时赋上一个默认值

// 写法1，普通写法
if (name == null) {
    name = "unknow";
}

// 写法2，使用三元运算符
name = name ！= null ? name : "unknow";
```

在Groovy中，可以使用Elvis operator来进一步简化：

```groovy
def name = getName()
name = name ?: 'unknown' // 在Groovy真值中，非空也为true
println name
```

Elvis 运算符还可以与赋值结合使用：可以表示这个变量是可空的，给个默认值

```groovy
import groovy.transform.ToString

@ToString
class Element {
    String name
    int atomicNumber
}

def he = new Element(name: 'Helium')
he.with {
    name = name ?: 'Hydrogen'   // existing Elvis operator
    atomicNumber ?= 2           // new Elvis assignment shorthand
}
assert he.toString() == 'Element(Helium, 2)'
```

# 对象相关的运算符

## 安全访问运算符

Safe navigation operator

我们可以通过一个点`.`来访问一个对象的属性或方法，但很多时候我们拿到的变量可能是null。这时如果我们直接使用`.`去访问，就有可能抛出空指针异常。而Groovy的安全访问运算符`?.`可以很好地解决这个问题：

```groovy
def person = getPerson() // 假设该方法可能返回null
def name = person?.name // 如果person不为null，那返回具体的值；如果为null，也不会抛出异常，而是返回null
```

## 直接属性访问运算符

Direct field access operator

先看一个例子：

```groovy
class User {
    public final String name

    User(String name) { this.name = name }

    String getName() { "Name: ${name}" }
}

def user = new User('Bob')
assert user.name == 'Name: Bob' // 这里看似是访问属性name,但其实是调用getName方法
```

上面例子中，我们直接使用`user.name`其实相当于`user.getName()`。如果需要直接访问属性，需要使用`.@`这个运算符：

```groovy
// 接上边的例子
assert user.@name == 'Bob'
```

## 方法指针运算符

Method pointer operator

使用`.&`可以取一个方法的指针，而所谓的方法指针，其实是Groovy中的**闭包：groovy.lang.Closure**（后面会重点讲解）：

```groovy
def str = 'example of method reference'
def fun = str.&toUpperCase // 取String的toUpperCase方法指针
println fun.class // class org.codehaus.groovy.runtime.MethodClosure
def upper = fun() // 这里相当于调用了方法
assert upper == str.toUpperCase()
```

### 作为闭包

方法指针运算符可以作为闭包传入，类似于Java中的函数式接口

```groovy
def transform(List elements, Closure action) {                    
    def result = []
    elements.each {
        result << action(it)
    }
    result
}
String describe(Person p) {                                       
    "$p.name is $p.age"
}
def action = this.&describe                                       
def list = [
    new Person(name: 'Bob',   age: 42),
    new Person(name: 'Julia', age: 35)]                           
assert transform(list, action) == ['Bob is 42', 'Julia is 35']  
```

### 调用重载方法

方法指针运算符可以调用重载的方法

```groovy
def doSomething(String str) { str.toUpperCase() }    
def doSomething(Integer x) { 2*x }                   
def reference = this.&doSomething                    
assert reference('foo') == 'FOO'                     
assert reference(123)   == 246  
```

### 类似于方法引用

在 Groovy 3 开始，方法指针运算符还可以这么用，类似于java8中的方法引用：

**类名.&构造方法**

```groovy
def foo  = BigInteger.&new
def fortyTwo = foo('42')
assert fortyTwo == 42G
```

还可以：**类名.&实例方法**

```groovy
def instanceMethod = String.&toUpperCase
assert instanceMethod('foo') == 'FOO'
```

## 方法引用运算符

真正的方法引用来了，Groovy 3+ 中的 Parrot 解析器支持 Java 8+ 方法引用运算符。功能上于方法指针运算符有些重叠。

实际上，对于动态 Groovy，方法引用运算符只是方法指针运算符的别名。

对于静态 Groovy，和Java中的方法引用一样的，使用双冒号 ::

```groovy
import groovy.transform.CompileStatic
import static java.util.stream.Collectors.toList

@CompileStatic
void methodRefs() {
    assert 6G == [1G, 2G, 3G].stream().reduce(0G, BigInteger::add)                           

    assert [4G, 5G, 6G] == [1G, 2G, 3G].stream().map(3G::add).collect(toList())              

    assert [1G, 2G, 3G] == [1L, 2L, 3L].stream().map(BigInteger::valueOf).collect(toList())  

    assert [1G, 2G, 3G] == [1L, 2L, 3L].stream().map(3G::valueOf).collect(toList())          
}

methodRefs()
```

构造方法引用的例子

```groovy
@CompileStatic
void constructorRefs() {
    assert [1, 2, 3] == ['1', '2', '3'].stream().map(Integer::new).collect(toList())

    def result = [1, 2, 3].stream().toArray(Integer[]::new)
    assert result instanceof Integer[]
    assert result.toString() == '[1, 2, 3]'
}

constructorRefs()

```

# 正则表达式运算符

# 其他

## 展开运算符

Spread Operator

Groovy中的展开运算符（*.）很有意思，它用于展开集合元素。

```groovy
class Car {
    String make
    String model
}

def cars = [
        new Car(make: 'Peugeot', model: '508'),
        new Car(make: 'Renault', model: 'Clio')]
def makes = cars*.make // 相当于访问了每一个元素的make
assert makes == ['Peugeot', 'Renault'] // 结果还是一个列表

```

