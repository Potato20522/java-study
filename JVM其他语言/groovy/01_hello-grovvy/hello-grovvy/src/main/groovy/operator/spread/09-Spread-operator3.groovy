package operator.spread


/**5.展开运算符可以嵌套，集合嵌套使用collectNested方法*/

def cars = [
        [
                new Car(make: 'Peugeot', model: '408'),
                new Car(make: 'Peugeot', model: '508')
        ], [
                new Car(make: 'Renault', model: 'Clio'),
                new Car(make: 'Renault', model: 'Captur')
        ]
]
def models = cars.collectNested { it.model }
assert models == [['408', '508'], ['Clio', 'Captur']]