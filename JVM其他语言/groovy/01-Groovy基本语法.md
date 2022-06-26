详细见官方文档：http://www.groovy-lang.org/syntax.html

# 基本语法

## 分号

**分号** 可以不写，然后可以这样：

```groovy
println "Hello World" println "Hello World" println "Hello World"
```

输出：

```
Hello World
Hello World
Hello World
```

## 注释和

注释和 Java 一样，有单行注释、多行注释、文档注释。但是groovy有一种特殊的注释，可以在运行时保留注释。Groovy 从 3.0.0 开始就支持 Runtime Groovydoc，即 Groovydoc 可以在运行时保留。
运行时 Groovydoc 在默认情况下处于禁用状态。可以通过添加JVM选项来启用它：

```cmd
-Dgroovy.attach.runtime.groovydoc=true
```

Runtime Groovydoc以/**@ 开头，并以\*/ 结尾，例如：

```groovy
/**@*/
```

```groovy
/**@
 * Some class groovydoc for Foo
 */
class Foo {
    /**@
     * Some method groovydoc for bar
     */
    void bar() {
    }
}
```

获取类和方法上的的运行时 groovydoc:

```groovy
assert Foo.class.groovydoc.content.contains('Some class groovydoc for Foo') 
assert Foo.class.getMethod('bar', new Class[0]).groovydoc.content.contains('Some method groovydoc for bar') 
```

**Shebang Line** 

这种注释通常用于UNIX系统的脚本中，可以直接从注释中的命令行来运行脚本，在环境变量PATH中设置了groovy后，就可以这样：

```groovy
#!/usr/bin/env groovy
println "Hello from the shebang line"
```



## 标识符

**普通标识符**  Identifiers

以英文字母、下划线、$开头，不可以用数字开头

注意：在点号之后，可以使用关键字作为标识符的，虽然不推荐这么做：

```groovy
foo.as
foo.assert
foo.break
foo.case
foo.catch
```



**带引号的标识符**  Quoted identifiers

Groovy在点表达式（dotted expression）后面可以使用引号标识符，比如`persion.name`可以表示为`persion.'name'`或`persion."name"`。而引号中可以包含普通标识符中不支持的字符，比如空格、中档线`-`这些：

```groovy
def map = [:]

map."an identifier with a space and double quotes" = "ALLOWED"
map.'with-dash-signs-and-single-quotes' = "ALLOWED"

assert map."an identifier with a space and double quotes" == "ALLOWED"
assert map.'with-dash-signs-and-single-quotes' == "ALLOWED"
```

其实，Groovy支持多种字符串的字面量表达形式，这些都是可以出现在点号后面的：

```groovy
map.'single quote'
map."double quote"
map.'''triple single quote'''
map."""triple double quote"""
map./slashy string/
map.$/dollar slashy string/$
```

更方便的是，Groovy中的`GString`支持*插值*，也可以用在点号后面的：

```groovy
def firstname = "Homer"
map."Simson-${firstname}" = "Homer Simson" // 会被插值成map."Simson-Homer"

assert map.'Simson-Homer' == "Homer Simson"
```





## 关键字

关键字：

| as     | assert  | break      | case       |
| ------ | ------- | ---------- | ---------- |
| catch  | class   | const      | continue   |
| def    | default | do         | else       |
| enum   | extends | false      | Finally    |
| for    | goto    | if         | implements |
| import | in      | instanceof | interface  |
| new    | pull    | package    | return     |
| super  | switch  | this       | throw      |
| throws | trait   | true       | try        |
| while  |         |            |            |



# 数据类型

可以和Java一样显式定义基本数据类型的变量，也可以使用def关键字来定义，进行自动推断类型

def关键字定义变量时，后面可以加as来指定其类型

## 字符串

在Groovy中字符串有两种类型，一种是Java原生的`java.lang.String`；另一种是`groovy.lang.GString`，又叫**插值字符串**(interpolated strings)。

### 单引号字符串

Single quoted string

在Groovy中，使用单引号括住的字符串就是`java.lang.String`，不支持插值：

```groovy
def name = 'yjiyjgie'
println name.class // class java.lang.String
```

### 三单引号字符串

Triple single quoted string

使用三单引号括住字符串支持多行，也是`java.lang.String`实例，在第一个`'''`起始处加一个反斜杠`\`可以在新一行开始文本：



```groovy
def strippedFirstNewline = '''line one
line two
line three
'''
// 可以写成下面这种形式，可读性更好
def strippedFirstNewline2 = '''\
line one
line two
line three
'''

assert strippedFirstNewline == strippedFirstNewline2 //等价写法
```

### 双引号字符串

Double quoted string

如果双引号括住的字符串中没有插值表达式（interpolated expression），那它就是`java.lang.String`；如是有插值表达式，那它就是`groovy.lang.GString`：

```groovy
def normalStr = "yjiyjige" // 这是一个java.lang.String
def interpolatedStr = "my name is ${normalStr}" // 这是一个groovy.lang.GString
```

### 字符串插值

String interpolation

在Groovy所有的字符串字面量表示中，除了单引号字符串和三单引号字符串，其他形式都支持字符串插值。字符串插值，也即将占位表达式中的结果最终替换到字符串相应的位置中：

```groovy
def name = 'Guillaume' // a plain string
def greeting = "Hello ${name}" // name变量的值会被替换进去

assert greeting == 'Hello Guillaume'
```

当使用点号表达式时，可以只用`$`代替`${}`：

```groovy
def person = [name: 'Guillaume', age: 36]
println "$person.name is $person.age years old"


// 注意
def number = 3.14 //java.math.BigDecimal
println "$number.toString()" 
// 这里会报异常,因为相当于"${number.toString}()"
//No such property: toString for class: java.math.BigDecimal

println "${number.toString()}" // 这样就正常了
println "$number" //这样也行
```



插值占位符中还支持闭包，而闭包的一个好处是**惰性求值**（lazy evaluation）：

```groovy
def number = 1
def eagerGString = "value == ${number}" // 普通形式
def lazyGString = "value == ${-> number}" // 这是一个闭包

println eagerGString == "value == 1"
println lazyGString == "value == 1"

number = 2
println eagerGString == "value == 1" // eagerGString已经被固定下来了
println lazyGString == "value == 2" // lazyGString的值会被重新计算
```

当一个方法的需要一个`java.lang.String`变量，而我们传递的是一个`groovy.lang.GString`实例时，`GString`的`toString`方法会被自动调用，看起来像我们可以直接将一个`GString`赋值给一个`String`变量一样。

注意：`GString`与`String`的hashCode是不一样的，即使他们最终结果一样。所以，在Map中，不应该用`GString`去做元素的Key，而又使用普通的`String`去取值：

```groovy
def key = "a"
def m = ["${key}": "letter ${key}"] // key类型是一个GString
assert m["a"] == null // 用一个普通String类型的key去取值
```

### 三双引号字符串

Triple double quoted string

类似于三单引号字符串，但支持字符串插值

### 斜线字符串

Slashy string

除了使用引号来括住字符串，还可以使用`/`。它一般用来定义正则表达式：

```groovy
def fooPattern = /.*foo.*/
assert fooPattern == '.*foo.*'

def foo = /"yjiyjige"/ // 可以在斜线表达式中,直接使用引号
println foo // 结果是“yjiyjige”
```

### 美元斜线字符串

Dollar slashy string

这种字符串使用`$/`开始，使用`/$`结束，其中的转义字符为`$`

```groovy
def name = "Guillaume"
def date = "April, 1st"

def dollarSlashy = $/
    Hello $name,
    today we're ${date}.

    $ dollar sign
    $$ escaped dollar sign
    \ backslash
    / forward slash
    $/ escaped forward slash
    $/$ escaped dollar slashy string delimiter
/$

assert [
        'Guillaume',
        'April, 1st',
        '$ dollar sign',
        '$ escaped dollar sign',
        '\\ backslash',
        '/ forward slash',
        '$/ escaped forward slash',
        '/$ escaped dollar slashy string delimiter'
].each { dollarSlashy.contains(it) }
```

### 字符串小结

| String name          | String syntax | Interpolated | Multiline | Escape character |
| -------------------- | ------------- | ------------ | :-------: | ---------------- |
| Single-quoted        | `'…'`         |              |           | `\`              |
| Triple-single-quoted | `'''…'''`     |              |     √     | `\`              |
| Double-quoted        | `"…"`         | √            |           | `\`              |
| Triple-double-quoted | `"""…"""`     | √            |     √     | `\`              |
| Slashy               | `/…/`         | √            |     √     | `\`              |
| Dollar slashy        | `$/…/$`       | √            |     √     | `$`              |

### 字符Characters

在Groovy中并没有明确的字符字面量表示形式（因为单引号表示字符串），所以必须明确指明：

```groovy
char c1 = 'A' // 明确指定给一个字符变量
assert c1 instanceof Character

def c2 = 'B' as char // 用as关键字
assert c2 instanceof Character

def c3 = (char) 'C' // 强制类型转换
assert c3 instanceof Character
```

## 数字

数字可以使用java的那几种基本数据类型，也可以使用def来定义

### 自动调整类型

当使用`def`指明整数字面量时，变量的类型会根据数字的大小自动调整：

```groovy
def a = 1
assert a instanceof Integer

// Integer.MAX_VALUE
def b = 2147483647
assert b instanceof Integer

// Integer.MAX_VALUE + 1
def c = 2147483648
assert c instanceof Long

// Long.MAX_VALUE
def d = 9223372036854775807
assert d instanceof Long

// Long.MAX_VALUE + 1
def e = 9223372036854775808
assert e instanceof BigInteger
```

为了精确地计算小数，在Groovy中使用`def`声明的小数是`BigDecimal`类型的：

```groovy
def decimal = 123.456
println decimal.getClass() // class java.math.BigDecimal
```

### 二进制八进制十六进制

二进制：ob开头

```groovy
int xInt = 0b10101111
assert xInt == 175

short xShort = 0b11001001
assert xShort == 201 as short

byte xByte = 0b11
assert xByte == 3 as byte

long xLong = 0b101101101101
assert xLong == 2925l

BigInteger xBigInteger = 0b111100100001
assert xBigInteger == 3873g

int xNegativeInt = -0b10101111
assert xNegativeInt == -175
```

八进制：0开头

```groovy
int xInt = 077
assert xInt == 63

short xShort = 011
assert xShort == 9 as short

byte xByte = 032
assert xByte == 26 as byte

long xLong = 0246
assert xLong == 166l

BigInteger xBigInteger = 01111
assert xBigInteger == 585g

int xNegativeInt = -077
assert xNegativeInt == -63
```

十六进制：0x开头

```groovy
int xInt = 0x77
assert xInt == 119

short xShort = 0xaa
assert xShort == 170 as short

byte xByte = 0x3a
assert xByte == 58 as byte

long xLong = 0xffff
assert xLong == 65535l

BigInteger xBigInteger = 0xaaaa
assert xBigInteger == 43690g

Double xDouble = new Double('0x1.0p0')
assert xDouble == 1.0d

int xNegativeInt = -0x77
assert xNegativeInt == -119
```

### 下划线分隔数字

```groovy
long creditCardNumber = 1234_5678_9012_3456L
long socialSecurityNumbers = 999_99_9999L
double monetaryAmount = 12_345_132.12
long hexBytes = 0xFF_EC_DE_5E
long hexWords = 0xFFEC_DE5E
long maxLong = 0x7fff_ffff_ffff_ffffL
long alsoMaxLong = 9_223_372_036_854_775_807L
long bytes = 0b11010010_01101001_10010100_10010010
```

### 运算

除了+、-、*、/、%之外还可以使用两个乘号表示幂运算

```groovy
// base and exponent are ints and the result can be represented by an Integer
assert    2    **   3    instanceof Integer    //  8
assert   10    **   9    instanceof Integer    //  1_000_000_000

// the base is a long, so fit the result in a Long
// (although it could have fit in an Integer)
assert    5L   **   2    instanceof Long       //  25

// the result can't be represented as an Integer or Long, so return a BigInteger
assert  100    **  10    instanceof BigInteger //  10e20
assert 1234    ** 123    instanceof BigInteger //  170515806212727042875...

// the base is a BigDecimal and the exponent a negative int
// but the result can be represented as an Integer
assert    0.5  **  -2    instanceof Integer    //  4

// the base is an int, and the exponent a negative float
// but again, the result can be represented as an Integer
assert    1    **  -0.3f instanceof Integer    //  1

// the base is an int, and the exponent a negative int
// but the result will be calculated as a Double
// (both base and exponent are actually converted to doubles)
assert   10    **  -1    instanceof Double     //  0.1

// the base is a BigDecimal, and the exponent is an int, so return a BigDecimal
assert    1.2  **  10    instanceof BigDecimal //  6.1917364224

// the base is a float or double, and the exponent is an int
// but the result can only be represented as a Double value
assert    3.4f **   5    instanceof Double     //  454.35430372146965
assert    5.6d **   2    instanceof Double     //  31.359999999999996

// the exponent is a decimal value
// and the result can only be represented as a Double value
assert    7.8  **   1.9  instanceof Double     //  49.542708423868476
assert    2    **   0.1f instanceof Double     //  1.0717734636432956
```

## 布尔

```groovy
def myBooleanVariable = true //java.lang.Boolean
boolean untypedBooleanVar = false
booleanField = true
```

Groovy中的布尔非常灵活从，可以对各种表达式进行布尔运算，专业术语叫做：Groovy Truth

```groovy
//1.boolean表达式
assert true
assert !false

//2.集合和数组
assert [1, 2, 3]  //有内容就是true
assert ![]  //没有内容就是false

//3.正则表达式，至少匹配到一个就是True
assert ('a' =~ /a/)
assert !('a' =~ /b/)

//4.迭代和枚举，有元素就是true，没有元素就是false
assert [0].iterator()
assert ![].iterator()
Vector v = [0] as Vector
Enumeration enumeration = v.elements()
assert enumeration
enumeration.nextElement()
assert !enumeration

//5.Maps，有元素就是true
assert ['one' : 1]
assert ![:]

//6.字符串，非空的就是true
assert 'a'
assert !''
def nonEmpty = 'a'
assert "$nonEmpty"
def empty = ''
assert !"$empty"

//7.数字非0就是true
assert 1
assert 3.5
assert !0

//8.对象引用，非空对象就是true
assert new Object()
assert !null

//9.自定义：重写asBoolean来自定义true
class Color {
    String name

    boolean asBoolean(){
        name == 'green'
    }
}
assert new Color(name: 'green')
assert !new Color(name: 'red')
```

## 列表List

默认情况下Groovy的列表使用的是`java.util.ArrayList`，用中括号`[]`括住，使用逗号分隔：

```groovy
def numbers = [1, 2, 3]
println numbers.getClass() // class java.util.ArrayList
```

如果要使用其它类型的列表（如：`LinkedList`）可以使用`as`操作符或显式分配给一个指定类型的变量：

```groovy
def arrayList = [1, 2, 3] // 默认类型
assert arrayList instanceof java.util.ArrayList

def linkedList = [2, 3, 4] as LinkedList // 使用as操作符
assert linkedList instanceof java.util.LinkedList

LinkedList otherLinked = [3, 4, 5] // 显式指明类型
assert otherLinked instanceof java.util.LinkedList
```

可以使用下标访问List中的元素，正数负数下标都可以。**使用<<来给List追加元素**

```groovy
def letters = ['a', 'b', 'c', 'd']

assert letters[0] == 'a'     
assert letters[1] == 'b'

assert letters[-1] == 'd'    
assert letters[-2] == 'c'

letters[2] = 'C'             
assert letters[2] == 'C'

letters << 'e'               
assert letters[ 4] == 'e'
assert letters[-1] == 'e'

assert letters[1, 3] == ['b', 'd']         
assert letters[2..4] == ['C', 'd', 'e']  
```

嵌套List

```groovy
def multi = [[0, 1], [2, 3]]     
assert multi[1][0] == 2       
```

## 数组

在Groovy中，没有数组的字面量定义方式。和特定类型列表的定义方式一样，我们需要使用`as`操作符或显式地分配给一个数组类型的变量：

```groovy
String[] arrStr = ['Ananas', 'Banana', 'Kiwi'] // 显式指明类型
assert arrStr instanceof String[]
assert !(arrStr instanceof List)

def numArr = [1, 2, 3] as int[] // 使用as操作符
assert numArr instanceof int[]
assert numArr.size() == 3
```

访问数组中和元素和List一样

**Java 风格的数组初始化**

Groovy 一直支持使用方括号的文字列表/数组定义，并避免使用 Java 样式的大括号，以免与闭包定义冲突。但是，在数组类型声明之后立即出现大括号的情况下，闭包定义没有歧义，因此Groovy 3及更高版本支持Java数组初始化表达式的变体。

```groovy
def primes = new int[] {2, 3, 5, 7, 11}
assert primes.size() == 5 && primes.sum() == 28
assert primes.class.name == '[I'

def pets = new String[] {'cat', 'dog'}
assert pets.size() == 2 && pets.sum() == 'catdog'
assert pets.class.name == '[Ljava.lang.String;'

// traditional Groovy alternative still supported
String[] groovyBooks = [ 'Groovy in Action', 'Making Java Groovy' ]
assert groovyBooks.every{ it.contains('Groovy') }
```

## Map

默认是java中的LinkedHashMap类型

```groovy
def colors = [red: '#FF0000', green: '#00FF00', blue: '#0000FF']   

assert colors['red'] == '#FF0000'    
assert colors.green  == '#00FF00'    

colors['pink'] = '#FF00FF'           
colors.yellow  = '#FFFF00'           

assert colors.pink == '#FF00FF'
assert colors['yellow'] == '#FFFF00'

assert colors instanceof java.util.LinkedHashMap
```

在上边的例子中，虽然没有明确的使用字符串`’red‘`、`’green‘`，但Groovy会自动把那些key转化为字符串。并且，在默认情况下，初始化映射时，key也不会去使用已经存在的变量：

```groovy
def keyVal = 'name'
def persons = [keyVal: 'Guillaume'] // 此处的key是字符串keyVal而不是name

assert !persons.containsKey('name')
assert persons.containsKey('keyVal')
```

如果要使用一个变量作为key，需要用括号括住：

```groovy
def keyVal = 'name'
def persons = [(keyVal): 'Guillaume'] // 相当于[ 'name' : 'Guillaume' ]

assert persons.containsKey('name')
assert !persons.containsKey('keyVal')
```

访问不存在的key时，返回null

```groovy
assert colors.unknown == null

def emptyMap = [:]
assert emptyMap.anyKey == null
```

在上面的示例中使用了String最为key，还可以使用其他类型，比如数字：

```groovy
def numbers = [1: 'one', 2: 'two']

assert numbers[1] == 'one'
```



