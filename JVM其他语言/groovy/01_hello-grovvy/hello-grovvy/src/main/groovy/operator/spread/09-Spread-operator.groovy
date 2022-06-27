package operator.spread
/**1.展开运算符*/
class Car {
    String make
    String model
}
def cars = [
        new Car(make: 'Peugeot', model: '508'),
        new Car(make: 'Renault', model: 'Clio')]
def makes = cars*.make //展开List，对每个元素进行同样调用
assert makes == ['Peugeot', 'Renault'] // 结果还是一个列表

/**2.展开运算符是null-safe的，不会抛空指针异常*/
cars = [
        new Car(make: 'Peugeot', model: '508'),
        null,
        new Car(make: 'Renault', model: 'Clio')]
assert cars*.make == ['Peugeot', null, 'Renault']
assert null*.make == null

/**3.展开运算符适用于任何实现了Iterable的对象*/
class Component {
    Long id
    String name
}
class CompositeObject implements Iterable<Component> {
    def components = [
            new Component(id: 1, name: 'Foo'),
            new Component(id: 2, name: 'Bar')]

    @Override
    Iterator<Component> iterator() {
        components.iterator()
    }
}
def composite = new CompositeObject()
assert composite*.id == [1,2]
assert composite*.name == ['Foo','Bar']

