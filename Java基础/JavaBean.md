# 什么是JavaBean

"java"表示咖啡，”bean“表示豆子，”JavaBean“就是咖啡豆啊，就这么简单。

JavaBean是一种Java类，所有的属性都是private的，通过setter和getter方法来实现对成员属性的访问。

## JavaBean用途

当前，最主要的是用来封装业务数据对象，比如DTO、Entity、DO、BO、VO都是JavaBean。

## JavaBean的发展历程

以前，JavaBean还用来封装GUI组件([Java桌面开发]()

- 1995年，java问世。

- 1996年，java1.0发布，12月就发布了java bean1.00-A，通过统一的规范可以设置对象的值(get,set方法),这是最初的java bean
- **Java GUI中的JavaBean**：当年java bean最初是用于java GUI开发中的，一个GUI组件对象，比如按钮，就是一个java bean。此时，除了上面提到的规范，还支持事件机制，自省/反射机制（便于查看bean的各种信息），后来，java gui编程凉凉了。
- **JSP中的JavaBean**，JSP开发中，通过java bean 来封装业务对象，其中 Servlet 充当Controller ,  jsp 充当 View ，Java bean 当然就是Model ，这就是java当中以前的**MVC**模式，业务逻辑， 页面显示， 和处理过程做了很好的分离，基于这个模型的扩展和改进，  很多Web开发框架开始如雨后春笋一样出现， 其中最著名的就是Struts， SpringMVC 了，Java Web开发迅速的繁荣了，当然JSP也凉凉了。
- 在实际企业开发中，需要实现**事务，安全，分布式、高可用**，javabean就不好用了，sun公司就开始往上面堆功能，这里java bean就复杂为EJB(Enterprise JavaBeans)，比如JDBC、JNDI、RMI、JMS、JTA等。
- EJB功能强大，但是太重太复杂了，此时出现DI(依赖注入),AOP(面向切面)技术，通过简单的java bean也能完成EJB的事情，这里的java bean简化为**POJO**（Plain Ordinary Java Object 简单的Java对象）;
- **Spring** 框架顺应了POJO的潮流， 提供了一个spring 的容器来管理这些POJO, 好玩的是也叫做bean 。看来我们的java bean 走了一圈**又回到了原点**：**属性私有且提供getter和setter方法**。 

对于一个Spting Bean 来说，如果你依赖别的Bean , 只需要声明即可， spring 容器负责把依赖的bean 给“注入进去“， 起初大家称之为控制反转(IoC)，后来 Martin flower 给这种方式起来个更好的名字，叫“**依赖注入**”。如果一个Bean 需要一些像事务，日志，安全这样的通用的服务， 也是只需要声明即可， spring 容器在运行时能够动态的“织入”这些服务， 这叫**AOP**。 



## 推荐阅读

javabean官方文档：https://docs.oracle.com/javase/tutorial/javabeans/quick/index.html

EJB官方文档：https://www.oracle.com/java/technologies/enterprise-javabeans-technology.html

[Java 帝国之Java bean (上)](https://mp.weixin.qq.com/s?__biz=MzAxOTc0NzExNg==&mid=2665513115&idx=1&sn=da30cf3d3f163d478748fcdf721b6414#rd)

[Java 帝国之Java bean（下)](https://mp.weixin.qq.com/s?__biz=MzAxOTc0NzExNg==&mid=2665513118&idx=1&sn=487fefb8fa7efd59de6f37043eb21799#rd)

# JavaBean命名规范

核心规范：

1、提供一个默认的无参构造函数。

2、需要被序列化并且实现了 Serializable 接口。

3、所有属性为private

4、提供getter和setter

解释

- 提供一个默认的无参构造函数。可以不提供，但是有时候，三方库通过无参构造来实例化对象，没有的化，就会抛异常。
- 需要被序列化并且实现了 Serializable 接口。可以不实现Serializable 接口，但是有些三方库，会报错。

- 类名大驼峰，即开头首字母大写，不要使用下划线

- 属性名小驼峰，不要使用下划线

- 方法名，根据属性，提供getter和setter，比如name属性对应：

  ```java
  priva String name;
  public void setName(){
      this.name = name
  }
  public String getName(){
      return name;
  }
  ```

  

# jackson序列化

## 一个正常的pojo类

```java
@Data
public class Person {
    private Long id;
    private String name;
}
```
序列化：
```java
Person person = new Person();
person.setId(1L);
person.setName("zhangsan");
String json = objectMapper.writeValueAsString(person);
System.out.println(json);
//{"id":1,"name":"zhangsan"}
```
反序列化：

```java
Person person2 = objectMapper.readValue(json, Person.class);
System.out.println(person2);
//Person(id=1, name=zhangsan)
```

## 没有getter和setter

```java
@ToString
public class PersonNoGetSet {
    public Long id;
    public String name;
}
```

序列化：

```java
PersonNoGetSet person = new PersonNoGetSet();
person.id=1L;
person.name = "zhangsan";
String json = objectMapper.writeValueAsString(person);
System.out.println(json);//{"id":1,"name":"zhangsan"}
```

反序列化：

```java
PersonNoGetSet person2 = objectMapper.readValue(json, PersonNoGetSet.class);
System.out.println(person2);//PersonNoGetSet(id=1, name=zhangsan)
```

## 布尔类型

```java
@Data
public class User1 {
    private boolean active;
    private boolean isApproved;
    private Boolean checked;
    private Boolean isNormal;
}
```
先看看序列化
```java
User1 user1 = new User1();
user1.setActive(true);
user1.setApproved(true);
user1.setChecked(true);
user1.setIsNormal(true);
String json = objectMapper.writeValueAsString(user1);
System.out.println(json);
//{"active":true,"checked":true,"isNormal":true,"approved":true}
//为啥isApproved变成了approved？
```

把上面输出的json字符串，再原样反序列化看看：

```java
User1 user11 = objectMapper.readValue(json, User1.class);
System.out.println(user11);
//User1(active=true, isApproved=true, checked=true, isNormal=true)
```

那么：如果json里是isApproved，反序列化时，会是什么样呢？

```json
{
    "active":true,
    "checked":true,
    "isNormal":true,
    "isApproved":true
}
```

```java
String json = "{\"active\":true,\"checked\":true,\"isNormal\":true,\"isApproved\":true}";
User1 user1 = objectMapper.readValue(json, User1.class);
System.out.println(user1);
```
直接抛异常：

```
com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field "isApproved" (class com.example.javabean.bool.User1), not marked as ignorable (4 known properties: "isNormal", "active", "checked", "approved"])

at [Source: (String)"{"active":true,"checked":true,"isNormal":true,"isApproved":true}"; line: 1, column: 64] (through reference chain: com.example.javabean.bool.User1["isApproved"])
```

说明：不认识json中的isApproved，正确做法是：要么传approved，要么把类中属性改为approved

那么，我要是偏偏不该，类里面就想用isApproved，json就传isApproved，要怎么实现？

