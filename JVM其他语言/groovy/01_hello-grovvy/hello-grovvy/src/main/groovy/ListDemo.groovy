def numbers = [1, 2, 3]
println numbers.getClass() // class java.util.ArrayList

def arrayList = [1, 2, 3] // 默认类型
assert arrayList instanceof ArrayList

def linkedList = [2, 3, 4] as LinkedList // 使用as操作符
assert linkedList instanceof LinkedList

LinkedList otherLinked = [3, 4, 5] // 显式指明类型
assert otherLinked instanceof LinkedList

//访问list中的元素
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