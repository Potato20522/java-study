来源：https://blog.csdn.net/weixin_40765637/article/details/102985239

# 代码生成转换

@ToString：生成toString方法，将bean转成可阅读字符串。
@EqualsAndHashCode：生成equals 和 hashcode 方法。
@TupleConstructor：生成构造函数。
@Canonical：结合了@ToString， @EqualsAndHashCode和@TupleConstructor 注释。
@InheritConstructor：生成构造匹配的超级构造函数为您服务。当覆盖异常类时，这特别有用。
@Category：简化的Groovy类的创建，这消除了将每个方法的第一个参数作为接收者的需要。
@IndexedProperty：生成索引getter / setter方法为列表/数组类型的属性。
@Lazy：实现领域的延迟初始化。
@Newify：用于把替代语法构造对象。
@Sortable：实现属性排序方法。
@Builder：创建帮助写类流利的API调用。如连续调用set注入属性值。
@AutoImplement：自动实现接口或父类方法，并提供默认返回值。

# 类设计

@BaseScript：在脚本中使用表示脚本应该从自定义脚本基类扩展而不是groovy.lang.Script。
@Delegate：实现委托设计模式。例如属性的委托就是把属性的调用委托给属性自己，而不是类。
@Immutable：@ToString@EqualsAndHashCode@TupleConstructor@MapConstructor@ImmutableBase@ImmutableOptions
@PropertyOptions@KnownImmutable的注解。该@Immutable元注释简化了一成不变的类的创建。不可变的类很有用，因为它们通常更易于推理，并且本质上是线程安全的。
@ImmutableBase：生成的不可变类，将自动设为final。
@PropertyOptions：此批注允许您指定在类构造期间要由转换使用的自定义属性处理程序。
@VisibilityOptions：此批注允许您为另一个转换生成的结构指定自定义可见性。
@ImmutableOptions：不可变属性。
@KnownImmutable：它只是一个标记注释。您可以使用注释对类进行注释（包括Java类），并且它们将被视为不可变类中的成员可接受的类型。
@Memoized：简化了允许方法调用的结果高速缓存的实施。
@TailRecursive：被用于在一个方法的结束时自动转换一个递归调用到同一代码的等效迭代版本。这避免了由于过多的递归调用而导致的堆栈溢出。
@Singleton：可以用来实现一个类Singleton设计模式。

# 日志

@Log：可用的第一个日志，依赖于JDK日志框架的注释
@Commons：log类由工程类获得
@Log4j：使用Apache Log4j 1.x框架。
@Log4j2：使用Apache Log4j 2.x框架
@Slf4j：使用Java的简单日志记录（SLF4J）框架

# 声明式并发

@Synchronized：在一个类似的方式向synchronized关键字，但对更安全的并发不同对象的锁。它可以应用于任何方法或静态方法
@WithReadLock ：读锁
@WithWriteLock：写锁

# 自动克隆和扩展

@AutoClone：实现复制方法 clone()
@AutoExternalize：实现writeExternal和readExternal方法

# 线程中断

@ThreadInterrupt：中断线程，通过在代码中的关键位置添加线程中断。
@TimedInterrupt：这是线程运行时长。
@ConditionalInterrupt：使用自定义策略中断脚本。

# 编译器指令

@Field：在脚本运行时，改变属性作用范围。
@PackageScope：创建包私有字段而不是属性（私有字段+ getter / setter）。
@AutoFinal：指示编译器自动插入注释节点内许多地方final修饰符。
@AnnotationCollector：允许创建元注释
@TypeChecked：在您的Groovy代码上激活编译时类型检查
@CompileStatic：在您的Groovy代码上激活静态编译
@CompileDynamic：在Groovy代码的某些部分上禁用静态编译。
@DelegatesTo：它旨在记录代码并在使用类型检查或静态编译的情况下为编译器提供帮助。
@SelfType：而是与特征一起使用的标记接口。

# 摇摆模式

@Bindable：它将常规属性转换为绑定属性（根据JavaBeans规范）
@ListenerList：删除和获得听众的名单一类，只是通过注释集合属性生成代码
@Vetoable：注释可以放在一个类上，这意味着所有属性都将转换为受约束的属性，也可以放在单个属性上。

# 测试辅助

@NotYetImplemented：用于反转JUnit 3/4测试用例的结果。
@ASTTest：旨在帮助调试其他AST转换或Groovy编译器本身