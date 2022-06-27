package operator

def list = [0,1,2,3,4]
assert list[2] == 2 //取值，实际上调用了getAt方法
list[2] = 4 //赋值，实际上调用了putAt方法
assert list[0..2] == [0,1,4]
list[0..2] = [6,6,6]
assert list == [6,6,6,3,4]

class User2 {
    Long id
    String name
    def getAt(int i) {
        switch (i) {
            case 0: return id
            case 1: return name
        }
        throw new IllegalArgumentException("No such element $i")
    }
    void putAt(int i, def value) {
        switch (i) {
            case 0: id = value; return
            case 1: name = value; return
        }
        throw new IllegalArgumentException("No such element $i")
    }
}
def user = new User2(id: 1, name: 'Alex')
assert user[0] == 1
assert user[1] == 'Alex'
user[1] = 'Bob'
assert user.name == 'Bob'