
def colors = [red: '#FF0000', green: '#00FF00', blue: '#0000FF']

assert colors['red'] == '#FF0000'
assert colors.green  == '#00FF00'

colors['pink'] = '#FF00FF'
colors.yellow  = '#FFFF00'

assert colors.pink == '#FF00FF'
assert colors['yellow'] == '#FFFF00'

assert colors instanceof java.util.LinkedHashMap

/**与变量名一样时*/
def keyVal = 'name'
def persons = [keyVal: 'Guillaume'] // 此处的key是字符串keyVal而不是name
assert !persons.containsKey('name')
assert persons.containsKey('keyVal')
//如果要使用一个变量作为key，需要用括号括住：
def keyVal2 = 'name'
def persons2 = [(keyVal2): 'Guillaume'] // 相当于[ 'name' : 'Guillaume' ]

assert persons2.containsKey('name')
assert !persons2.containsKey('keyVal')


/**访问不存在的key*/
assert colors.unknown == null
def emptyMap = [:]
assert emptyMap.anyKey == null

/**数字作为key*/
def numbers = [1: 'one', 2: 'two']
assert numbers[1] == 'one'
