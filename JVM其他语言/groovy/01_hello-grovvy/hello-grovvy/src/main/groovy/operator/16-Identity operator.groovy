package operator

def list1 = ['Groovy 1.8', 'Groovy 2.0', 'Groovy 2.3']
def list2 = ['Groovy 1.8', 'Groovy 2.0', 'Groovy 2.3']
assert list1 == list2
assert !list1.is(list2)