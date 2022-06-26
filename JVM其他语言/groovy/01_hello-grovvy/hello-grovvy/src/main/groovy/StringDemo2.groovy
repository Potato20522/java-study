
def number = 1
def eagerGString = "value == ${number}" // 普通形式
def lazyGString = "value == ${-> number}" // 这是一个闭包

println eagerGString == "value == 1"
println lazyGString == "value == 1"

number = 2
println eagerGString == "value == 1" // eagerGString已经被固定下来了
println lazyGString == "value == 2" // lazyGString的值会被重新计算

def key = "a"
def m = ["${key}": "letter ${key}"] // key类型是一个GString
assert m["a"] == null // 用一个普通String类型的key去取值

char c1 = 'A' // 明确指定给一个字符变量
assert c1 instanceof Character

def c2 = 'B' as char // 用as关键字
assert c2 instanceof Character

def c3 = (char) 'C' // 强制类型转换
assert c3 instanceof Character