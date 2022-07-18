package closure.delegate

class Person {
    String name
}

class Thing {
    String name
}
//先定义两个对象
def p = new Person(name: 'Norman')
def t = new Thing(name: 'Teapot')

def upperCasedName = { delegate.name.toUpperCase() }
//def upperCasedName = { name.toUpperCase() } //delegate可以省略

upperCasedName.delegate = p
assert upperCasedName() == 'NORMAN'
upperCasedName.delegate = t
assert upperCasedName() == 'TEAPOT'