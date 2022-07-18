Domain-Specific Languages：领域特定语言

常用于聚焦指定的领域或问题，这就要求 DSL 具备强大的表现力，同时在使用起来要简单。由于其使用简单的特性，DSL 通常不会像 Java，C++等语言将其应用于一般性的编程任务。

由语言的难易程度，做一个简单的排序：配置文件 < DSL < 编程语言(eg:Java,C++...)

对于 Groovy 来说，一个伟大的 DSL 产物就是新一代构建工具——Gradle。

来源：https://juejin.cn/post/6972423148742377508

# 命令链

在之前的学习中，注意到Groovy 中**调用方法可以省略掉括号**。比如：

```groovy
println("hello,Groovy")
println "hello,Groovy"
```

这种灵活的处理方式进而引申出了 Groovy 的命令链接特性。

```groovy
def move(String dir){
    print "move $dir "
    this //类似于Java中的builder模式，方法最后return this
}

def turn(String dir){
    print "turn $dir"
    this
}


def jump(String speed,String dir){
    print "jump ${dir} ${speed}"
    this
}

//move("forward").turn("right").turn("right").move("back")
move "forward" turn "right" turn "right" move "back" // 1
println ''
//jump("fast","forward").move("back").move("forward")
jump "fast","forward" move "back" move "forward"      // 2
```

第一条语句调用没有逗号。Groovy 首先会认为我们调用了一个 `move("forward")` 方法，该方法调用返回同样支持调用 `move`，`turn`，`jump`，等方法的对象实例自身 `this` 。随后，进一步调用它的 `turn("right")` 方法。以此类推，一条连贯的命令链接就出来了。

第二条语句调用多了一个逗号，其原因是：`jump` 方法接收两个参数，代表跳跃的速度 `speed` 和跳跃的方向 `dir`。

# 利用闭包委托创建上下文

设计上下文 ( Context ) 也是 DSL 的特点。比如："Venti latte with two extra shots!" 。这是星巴克的 DSL，尽管我们全局都没有提到咖啡两字，但是服务员照样会为我们提供一份超大杯拿铁 —— 但是在蜜雪冰城可就不一定了。每种 DSL 都依附于各自的上下文环境，或者称上下文驱动。

下面是一个订购 Pizza 的代码：

```groovy
class PizzaShop {
    def setSize(String size){}
    def setAddress(String addr){}
    def setPayment(String cardId){}
}

def pizzaShop = new PizzaShop()
pizzaShop.setSize("large")
pizzaShop.setAddress("XXX street")
pizzaShop.setPayment("WeChat")
```

由于缺少上下文，`pizzaShop` 引用会被反复调用。在 Groovy 中，对于这样的方法可以使用 `with` 进行梳理：

```groovy
pizzaShop.with {
    setSize "large"
    setAddress "XXX street"
    setPayment "WeChat"
}
```

实例 `pizaaShop` 在此处充当了上下文，它使得代码风格变得更加紧凑了。

另一个例子，用户不想主动创建一个 `PizzaShop` 实例 ( 因为创建一个实例或许需要很多的额外配置，假定我们遵循 "约定大于配置" 的原则 )，他们的目的仅仅是获得一个披萨。如果希望创建一个隐式的上下文对象，不妨试着利用 Groovy 闭包的委托功能：

```groovy
// 缺点是编写代码时，IntelliJ 无法对动态委托的闭包给出代码提示。
getPizza {
    setSize "large"
    setAddress "XXX street"
    setPayment "WeChat"
}

def getPizza(Closure closure){
    def pizzaShop = new PizzaShop()
    closure.delegate = pizzaShop
    closure.run()
}
```

## 巧用 Groovy 脚本聚合和方法拦截

利用 Groovy DSL 的能力，我们还可以自行组织配置文件的格式，下面通过一个例子一步一步实现。每行配置需要两项：配置名和值。我们可以将它写成这样：

```groovy
// 把它看作是一项配置 -> size = "large", 以此类推。
size "large"  	
payment "WeChat"
address "XXXStreet"
```

每行配置项在 Groovy 中可以视作是调用了 `k(v)` 方法 ( 比如配置项中的 `size "large"` 相当于调用了 `size("large")`)。为了避免报错，我们可能会想到提前实现和配置同名的方法

```groovy
// 该 config 没有 def 关键字，表示它是个脚本内的全局变量。
config = [:]
def size(String size){
	config["size"] = size
}
// 类似的还有 payment,address ...
```

假如设定的配置项非常多的话，要填充完这些方法可要好一阵时间。实际上，这对于 Groovy 来说根本没有必要。回顾前几章 MOP 的内容，我们只需要在 `methodMissing` 方法中将这些同名方法合成出来即可，如下面的代码块所示。同时，定义一个 `acceptOrder` "上下文"，它负责遍历 `config` 项的内容并输出到控制台：