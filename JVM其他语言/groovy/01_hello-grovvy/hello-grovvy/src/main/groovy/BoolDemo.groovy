def myBooleanVariable = true
boolean untypedBooleanVar = false
booleanField = true

/**Groovy Truth*/
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
