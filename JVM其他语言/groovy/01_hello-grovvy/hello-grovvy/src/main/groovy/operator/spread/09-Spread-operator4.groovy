package operator.spread


/**6.展开运算符可以展开方法参数*/
int function(int x, int y, int z) {
        x*y+z
}
def args = [4,5,6]
assert function(*args) == 26
args = [4]
assert function(*args,5,6) == 26

/**7.用于列表中的元素*/
def items = [4,5]
def list = [1,2,3,*items,6]
assert list == [1,2,3,4,5,6]

/**8.展开Map*/
def m1 = [c:3, d:4]
def map = [a:1, b:2, *:m1]
assert map == [a:1, b:2, c:3, d:4]
//和位置有关
def m2 = [c:3, d:4]
def map2 = [a:1, b:2, *:m2, d: 8] //d: 8覆盖了前面的d:4
assert map2 == [a:1, b:2, c:3, d:8]