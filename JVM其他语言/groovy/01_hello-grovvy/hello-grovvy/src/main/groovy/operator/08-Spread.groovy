package operator
//展开运算符（*.），它用于展开集合元素。
class Car {
    String make
    String model
}

def cars = [
        new Car(make: 'Peugeot', model: '508'),
        new Car(make: 'Renault', model: 'Clio')]
def makes = cars*.make // 相当于访问了每一个元素的make
assert makes == ['Peugeot', 'Renault'] // 结果还是一个列表
