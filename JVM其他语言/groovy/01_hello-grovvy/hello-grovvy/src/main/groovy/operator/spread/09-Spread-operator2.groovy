package operator.spread

import groovy.transform.Canonical

/**4.展开运算符可以嵌套*/
class Make {
    String name
    List<Model> models
}

@Canonical
class Model {
    String name
}

def cars = [
        new Make(name: 'Peugeot',
                models: [new Model('408'), new Model('508')]),
        new Make(name: 'Renault',
                models: [new Model('Clio'), new Model('Captur')])
]

def makes = cars*.name
assert makes == ['Peugeot', 'Renault']

def models = cars*.models*.name
assert models == [['408', '508'], ['Clio', 'Captur']]
assert models.sum() == ['408', '508', 'Clio', 'Captur'] // flatten one level
assert models.flatten() == ['408', '508', 'Clio', 'Captur'] // flatten all levels (one in this case)