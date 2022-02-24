**Java StringTokenizer** 属于 java.util 包，用于分隔字符串。

**StringTokenizer 构造方法：**

- **1. StringTokenizer(String str)** ：构造一个用来解析 str 的 StringTokenizer 对象。java 默认的分隔符是空格("")、制表符(\t)、换行符(\n)、回车符(\r)。
- **2. StringTokenizer(String str, String delim)** ：构造一个用来解析 str 的 StringTokenizer 对象，并提供一个指定的分隔符。
- **3. StringTokenizer(String str, String delim, boolean returnDelims)** ：构造一个用来解析 str 的 StringTokenizer 对象，并提供一个指定的分隔符，同时，指定是否返回分隔符。

**StringTokenizer 常用方法：**

- **1. int countTokens()**：返回nextToken方法被调用的次数。
- **2. boolean hasMoreTokens()**：返回是否还有分隔符。
- **3. boolean hasMoreElements()**：判断枚举 （Enumeration） 对象中是否还有数据。
- **4. String nextToken()**：返回从当前位置到下一个分隔符的字符串。
- **5. Object nextElement()**：返回枚举 （Enumeration） 对象的下一个元素。
- **6. String nextToken(String delim)**：与 4 类似，以指定的分隔符返回结果。

```java
String str = "runoob,google,taobao,facebook,zhihu";
// 以 , 号为分隔符来分隔字符串
StringTokenizer st=new StringTokenizer(str,",");
while(st.hasMoreTokens()) {
    System.out.println(st.nextToken());
}
```



```java
System.out.println("使用第一种构造函数：");
StringTokenizer st1 = new StringTokenizer("Hello Runoob How are you", " ");
while (st1.hasMoreTokens())
    System.out.println(st1.nextToken());

System.out.println("使用第二种构造函数：");
StringTokenizer st2 = new StringTokenizer("JAVA : Code : String", " :");
while (st2.hasMoreTokens())
    System.out.println(st2.nextToken());

System.out.println("使用第三种构造函数：");
StringTokenizer st3 = new StringTokenizer("JAVA : Code : String", " :",  true);
while (st3.hasMoreTokens())
    System.out.println(st3.nextToken());
```



**与split()的区别**

String.Split（）使用正则表达式，而StringTokenizer的只是使用逐字分裂的字符。
如果不用正则表达式（StringTokenizer也不能使用正则表达式），StringTokenizer在截取字符串中的效率最高。



**split、substring、stringtokenizer性能对比**

https://blog.csdn.net/qq_21251983/article/details/53004660

结论：

- 在字符串长度较短时,也就是我们绝大多数情况下接触到的,长度在几十、几百以内,split、substring、stringtokenizer 的性能差距是在纳秒级别,可随意选择.

- 在字符串长度在几百以上,几十万以下这个级别,选择使用split和StringTokenizer(substring的耗时比其它两者要高出一到数个数量级),其中整体来看StringTokenizer性能上略占优势,但是由于本身耗时就在1毫秒之内,所以优势几乎可以忽略.从代码简洁易读的角度来看,应当优先采用split.

- 在处理很长的字符串时,尤其是长度超过百万甚至是千万级别,性能的差距已经达到了毫秒级甚至秒级,应当优先选择StringTokenizer.
- 

**关于JDK不提倡使用StringTokenizer**

JDK中关于`StringTokenizer`的描述中提到,不建议使用这个类来进行字符串分割,提倡使用的是 `java.lang.String`的 `split`方法.

```
StringTokenizer is a legacy class that is retained for compatibility reasons although its use is discouraged in new code.
It is recommended that anyone seeking this functionality use the split method of String or the java.util.regex package instead.
12
```

官方并没有说明原因.

个人猜测可能是如下原因:

1. 最主要的原因.`substring`对于多个分隔符,比如`","`、`";"`、`"#"`等同时做分隔符时,代码就会变得相当冗杂.stringtokenizer虽然是支持多个分隔符,但是分隔符必须为单个字符,也就是说,`",;#"`会被认为是3个单字符的分隔符,但是不能看做一个整体,更别说单字符与多字符的分隔符同时存在的情况.但是`split`则完全不存在前两者的问题,由于使用的是正则表达式,所以`split`支持任何格式、任何数量长度的分隔符.
2. 在绝大多数场景下,我们处理的都是很短的字符串,此时`split`和`StringTokenizer`的性能差距可以忽略.
3. `split`使用最为简单,作为`String`自身提供的方法,直接调用就可返回结果,不像其它两个还需要额外的处理.

既然这样那JDK为何还要提供`StringTokenizer`这么一个实现?主要是因为,[Java](https://so.csdn.net/so/search?q=Java&spm=1001.2101.3001.7020)对正则表达式的支持以及`split`方法都是在JDK1.4才提供的,而`StringTokenizer`是作为早期的JDK(从1.0就已经存在了)的一个”临时性的”分割字符串的解决方案.

