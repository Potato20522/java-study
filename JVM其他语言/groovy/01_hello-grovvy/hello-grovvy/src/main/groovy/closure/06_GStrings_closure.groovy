package closure

def x0 = 1
def gs0 = "x0 = ${x0}"
assert gs0 == 'x0 = 1'

x0 = 2
//assert gs0 == 'x0 = 2'
//不相等，因为上述 GString 中的语法不表示闭包，而是创建 GString 时计算的表达式。


//GString闭包
def x = 1
def gs = "x = ${-> x}"
assert gs == 'x = 1'

x = 2
assert gs == 'x = 2'

