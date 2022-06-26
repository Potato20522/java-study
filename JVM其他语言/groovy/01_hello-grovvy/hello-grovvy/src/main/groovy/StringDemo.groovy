def name1 = 'yjiyjgie'
println name1.class // class java.lang.String

def strippedFirstNewline = '''line one
line two
line three
'''
// 可以写成下面这种形式，可读性更好
def strippedFirstNewline2 = '''\
line one
line two
line three
'''

assert strippedFirstNewline == strippedFirstNewline2

def name = 'Guillaume' // a plain string
def greeting = "Hello ${name}" // name变量的值会被替换进去

assert greeting == 'Hello Guillaume'


def person = [name: 'Guillaume', age: 36]
println "$person.name is $person.age years old"

// 注意
def number = 3.14
println "$number.toString()" // 这里会报异常,因为相当于"${number.toString}()"
println "${number.toString()}" // 这样就正常了