package operator
//在Java中：
//String name = getName(); // 假设这个方法可能返回空值，如果我们想在为空时赋上一个默认值
// 写法1，普通写法
//if (name == null) {
//    name = "unknow";
//}
// 写法2，使用三元运算符
//name = name != null ? name : "unknow";

def name = getName()
name = name ?: 'unknown' // 在Groovy真值中，非空也为true
println name
