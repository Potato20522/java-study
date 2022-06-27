package operator

Integer x = 123
String s = x as String

class Identifiable {
    String name
}
class User3 {
    Long id
    String name
    def asType(Class target) {
        if (target == Identifiable) {
            return new Identifiable(name: name)
        }
        throw new ClassCastException("User cannot be coerced into $target")
    }
}
def u = new User3(name: 'Xavier')
def p = u as Identifiable
assert p instanceof Identifiable
assert !(p instanceof User3)