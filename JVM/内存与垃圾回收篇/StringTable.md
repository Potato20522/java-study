# String的基本特性

- String：字符串使用对""引起来表示。String对象实例化的方式：
  - String s1 = "atguigu"; //字面量的定义方式
  - String s2 = new String("hello");

- String声明为final的，不可被继承

- String实现了 Serializable接口：表示字符串是支持序列化的。

- String实现了 Comparable接口：表示String可以比较大小

- String在jdk8及以前内部定义了final char[] value用于存储字符串数据。

- jdk9时改为byte[]