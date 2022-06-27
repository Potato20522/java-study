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

## 模式运算符

使用符号：~ 加斜杠包起来，来创建一个java.util.regex.Pattern.Pattern对象

```groovy
import java.util.regex.Pattern

def p = ~/foo/
assert p instanceof Pattern
```

除了使用斜杠包起来，还可以使用单引号、双引号、$、\$加大括号抱起来

```groovy
p = ~'foo'                                                        
p = ~"foo"                                                        
p = ~$/dollar/slashy $ string/$          //无需转义，直接使用 $和/                        
p = ~"${pattern}"  //还可使用GString
```

## 查找运算符

使用=~ 创建java.util.regex.Matcher对象

```groovy
def text = "some text to match"
def m = text =~ /match/                                           
assert m instanceof Matcher                                       
if (!m) {                                                         
    throw new RuntimeException("Oops, text not found!")
}
```

## 匹配运算符

使用==~ 来创建ava.util.regex.Matcher对象

```groovy
m = text ==~ /match/                                              
assert m instanceof Boolean                                       
if (m) {                                                          
    throw new RuntimeException("Should not reach that point!")
}
```

## 对比

当涉及单个完全匹配时，使用match，否则使用find

```groovy
assert 'two words' ==~ /\S+\s+\S+/
assert 'two words' ==~ /^\S+\s+\S+$/         
assert !(' leading space' ==~ /\S+\s+\S+/)   

def m1 = 'two words' =~ /^\S+\s+\S+$/
assert m1.size() == 1                          
def m2 = 'now three words' =~ /^\S+\s+\S+$/    
assert m2.size() == 0                          
def m3 = 'now three words' =~ /\S+\s+\S+/
assert m3.size() == 1                          
assert m3[0] == 'now three'
def m4 = ' leading space' =~ /\S+\s+\S+/
assert m4.size() == 1                          
assert m4[0] == 'leading space'
def m5 = 'and with four words' =~ /\S+\s+\S+/
assert m5.size() == 2                          
assert m5[0] == 'and with'
assert m5[1] == 'four words'
```

# 其他

## 展开运算符：*

Spread Operator

Groovy中的展开运算符（*.）很有意思，它用于展开/传播集合元素。

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

### null-safe

展开运算符是null-safe的，不会抛空指针异常

```groovy
cars = [
        new Car(make: 'Peugeot', model: '508'),
        null,
        new Car(make: 'Renault', model: 'Clio')]
assert cars*.make == ['Peugeot', null, 'Renault']
assert null*.make == null
```

### 适用于Iterable

展开运算符适用于任何实现了Iterable的对象

```groovy
class Component {
    Long id
    String name
}
class CompositeObject implements Iterable<Component> {
    def components = [
            new Component(id: 1, name: 'Foo'),
            new Component(id: 2, name: 'Bar')]

    @Override
    Iterator<Component> iterator() {
        components.iterator()
    }
}
def composite = new CompositeObject()
assert composite*.id == [1,2]
assert composite*.name == ['Foo','Bar']
```

### 嵌套

展开运算符可以嵌套

```groovy
class Make {
    String name
    List<Model> models
}

@Canonical
class Model {
    String name
}

def cars = [
    new Make(name: 'Peugeot',
             models: [new Model('408'), new Model('508')]),
    new Make(name: 'Renault',
             models: [new Model('Clio'), new Model('Captur')])
]

def makes = cars*.name
assert makes == ['Peugeot', 'Renault']

def models = cars*.models*.name
assert models == [['408', '508'], ['Clio', 'Captur']]
assert models.sum() == ['408', '508', 'Clio', 'Captur'] // flatten one level
assert models.flatten() == ['408', '508', 'Clio', 'Captur'] // flatten all levels (one in this case)
```

对于集合嵌套的情况，使用collectNested方法而不是 *.

```groovy
class Car {
    String make
    String model
}
def cars = [
   [
       new Car(make: 'Peugeot', model: '408'),
       new Car(make: 'Peugeot', model: '508')
   ], [
       new Car(make: 'Renault', model: 'Clio'),
       new Car(make: 'Renault', model: 'Captur')
   ]
]
def models = cars.collectNested{ it.model }
assert models == [['408', '508'], ['Clio', 'Captur']]
```

### 展开方法参数

有如下函数：

```groovy
int function(int x, int y, int z) {
    x*y+z
}
```

有一个变量：

```groovy
def args = [4,5,6]
```

使用展开运算符，调用该方法

```groovy
assert function(*args) == 26
```

还可以混着用：

```groovy
args = [4]
assert function(*args,5,6) == 26
```

### 用于列表中的元素

```groovy
def items = [4,5]                      
def list = [1,2,3,*items,6]            
assert list == [1,2,3,4,5,6]   
```

### 展开Map

```groovy
def m1 = [c:3, d:4]
def map = [a:1, b:2, *:m1]
assert map == [a:1, b:2, c:3, d:4]
//和位置有关
def m2 = [c:3, d:4]
def map2 = [a:1, b:2, *:m2, d: 8] //d: 8覆盖了前面的d:4
assert map2 == [a:1, b:2, c:3, d:8]
```

## 范围运算符 ..

Range operator

使用..可以定义一个范围：

```groovy
def range = 0..5    //groovy.lang.IntRange类型                                
assert (0..5).collect() == [0, 1, 2, 3, 4, 5]       
assert (0..<5).collect() == [0, 1, 2, 3, 4]         
assert (0<..5).collect() == [1, 2, 3, 4, 5]         
assert (0<..<5).collect() == [1, 2, 3, 4]           
assert (0..5) instanceof List                       
assert (0..5).size() == 6                           
```

不仅仅是数字，实现了Comparable接口的对象都可以使用范围运算符

```groovy
assert ('a'..'d').collect() == ['a','b','c','d']
```

## 飞船运算符<==>(compareTo)

Spaceship operator

`<==>`像不像一个宇宙飞船？相当于调用`compareTo`方法：

```groovy
assert (1 <=> 1) == 0
assert (1 <=> 2) == -1
assert (2 <=> 1) == 1
assert ('a' <=> 'z') == -1
```

## 下标运算符[下标]

Subscript operator

不仅仅是数组，List对象也可以使用中括号加下标访问了

```groovy
def list = [0,1,2,3,4]
assert list[2] == 2 //取值，实际上调用了getAt方法                        
list[2] = 4   //赋值，实际上调用了putAt方法                                    
assert list[0..2] == [0,1,4]                
list[0..2] = [6,6,6]                        
assert list == [6,6,6,3,4]
```

下标运算符中的取值，实际上调用了getAt方法 ，赋值，实际上调用了putAt方法，所以，只要对象中重写了这两个方法，也能对改对象使用下标运算符。

```groovy
class User {
    Long id
    String name
    def getAt(int i) {                                             
        switch (i) {
            case 0: return id
            case 1: return name
        }
        throw new IllegalArgumentException("No such element $i")
    }
    void putAt(int i, def value) {                                 
        switch (i) {
            case 0: id = value; return
            case 1: name = value; return
        }
        throw new IllegalArgumentException("No such element $i")
    }
}
def user = new User(id: 1, name: 'Alex')                           
assert user[0] == 1                                                
assert user[1] == 'Alex'                                           
user[1] = 'Bob'                                                    
assert user.name == 'Bob'         
```

## 安全索引运算符 ?[下标]

写法：问号+下标 ， 这样就可以应对null了

```groovy
tring[] array = ['a', 'b']
assert 'b' == array?[1]      // get using normal array index
array?[1] = 'c'              // set using normal array index
assert 'c' == array?[1]

array = null
assert null == array?[1]     // return null for all index values
array?[1] = 'c'              // quietly ignore attempt to set value
assert null == array?[1]

def personInfo = [name: 'Daniel.Sun', location: 'Shanghai']
assert 'Daniel.Sun' == personInfo?['name']      // get using normal map index
personInfo?['name'] = 'sunlan'                  // set using normal map index
assert 'sunlan' == personInfo?['name']

personInfo = null
assert null == personInfo?['name']              // return null for all map values
personInfo?['name'] = 'sunlan'                  // quietly ignore attempt to set value
assert null == personInfo?['name']
```

## 成员运算符 in

in相当于inCase方法，当用在列表上时，相当于调用列表的contains方法：

```groovy
def list = ['Grace','Rob','Emmy']
assert ('Emmy' in list)     
```

## ==运算符（调用了equals）

Identity operator

java中的==是比较对象是否是同一个，即内存地址

而groovy中的==直接是调用equals方法进行比较

groovy中想要比较对象是否是同一个，使用is关键字就行

```groovy
def list1 = ['Groovy 1.8','Groovy 2.0','Groovy 2.3']        
def list2 = ['Groovy 1.8','Groovy 2.0','Groovy 2.3']        
assert list1 == list2                                       
assert !list1.is(list2)   
```

## 强制转换运算符 as

Coercion operator

下面的强制转换，运行时会抛异常：**ClassCastException**

```groovy
Integer x = 123
String s = (String) x    
```

可以使用强制转换运算符 as 来代替

```groovy
Integer x = 123
String s = x as String      
```

当一个对象被强制转换为另一个对象时，除非目标类型与源类型相同，否则强制将**返回一个新对象**。强制规则因源类型和目标类型而异，如果未找到转换规则，则强制规则可能会失败。自定义转换规则可以通过以下方法实现：**asType**：

```groovy
class Identifiable {
    String name
}
class User {
    Long id
    String name
    def asType(Class target) {                                              
        if (target == Identifiable) {
            return new Identifiable(name: name)
        }
        throw new ClassCastException("User cannot be coerced into $target")
    }
}
def u = new User(name: 'Xavier')                                            
def p = u as Identifiable                                                   
assert p instanceof Identifiable                                            
assert !(p instanceof User)     
```

## 钻石运算符<>

Diamond operator

和java中一样，用于泛型

```groovy
List<String> strings = new LinkedList<>()
```

## call运算符

Call operator

```groovy
class MyCallable {
    int call(int x) { //隐式实现了java.util.concurrent.Callable
        2*x
    }
}

def mc = new MyCallable()
assert mc.call(2) == 4          
assert mc(2) == 4  //简化调用
```

# 运算符优先级

| Level | Operator(s)                                                  | Name(s)                                                      |
| :---- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| 1     | `new`  `()`                                                  | object creation, explicit parentheses                        |
|       | `()`  `{}`  `[]`                                             | method call, closure, literal list/map                       |
|       | `.`  `.&`  `.@`                                              | member access, method closure, field/attribute access        |
|       | `?.`  `*`  `*.`  `*:`                                        | safe dereferencing, spread, spread-dot, spread-map           |
|       | `~`  `!`  `(type)`                                           | bitwise negate/pattern, not, typecast                        |
|       | `[]`  `?[]`  `++`  `--`                                      | list/map/array (safe) index, post inc/decrement              |
| 2     | `**`                                                         | power                                                        |
| 3     | `++`  `--`  `+`  `-`                                         | pre inc/decrement, unary plus, unary minus                   |
| 4     | `*`  `/`  `%`                                                | multiply, div, remainder                                     |
| 5     | `+`  `-`                                                     | addition, subtraction                                        |
| 6     | `\<\<`  `>>`  `>>>`  `..`  `..\<`                            | left/right (unsigned) shift, inclusive/exclusive range       |
| 7     | `\<`  `<=`  `>`  `>=`  `in` &0#160; `!in`  `instanceof`  `!instanceof`  `as` | less/greater than/or equal, in, not in, instanceof, not instanceof, type coercion |
| 8     | `==`  `!=`  `<=>`  `===`  `!==`                              | equals, not equals, compare to, identical to, not identical to |
|       | `=~`  `==~`                                                  | regex find, regex match                                      |
| 9     | `&`                                                          | binary/bitwise and                                           |
| 10    | `^`                                                          | binary/bitwise xor                                           |
| 11    | `|`                                                          | binary/bitwise or                                            |
| 12    | `&&`                                                         | logical and                                                  |
| 13    | `||`                                                         | logical or                                                   |
| 14    | `? :`                                                        | ternary conditional                                          |
|       | `?:`                                                         | elvis operator                                               |
| 15    | `=`  `**=`  `*=`  `/=`  `%=`  `+=`  `-=`  `\<<=`  `>>=`  `>>>=`  `&=`  `^=`  `|=`   `?=` | various assignments                                          |

# 运算符重载

Groovy中支持运算符重载，其实也就是重写一下实现约定好的方法：

比如：加法重载

```groovy
class Bucket {
    int size

    Bucket(int size) { this.size = size }

    Bucket plus(Bucket other) {
        return new Bucket(this.size + other.size)
    }
    Bucket plus(int capacity) {
        return new Bucket(this.size + capacity)
    }
}

def b1 = new Bucket(4)
def b2 = new Bucket(11)
assert (b1 + b2).size == 15 //plus(Bucket other)

assert (b1 + 11).size == 15 //plus(int capacity)
```

