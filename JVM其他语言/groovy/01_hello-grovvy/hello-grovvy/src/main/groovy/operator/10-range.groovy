package operator

def range = 0..5
assert (0..5).collect() == [0, 1, 2, 3, 4, 5]
assert (0..<5).collect() == [0, 1, 2, 3, 4]
assert (0<..5).collect() == [1, 2, 3, 4, 5]
assert (0<..<5).collect() == [1, 2, 3, 4]
assert (0..5) instanceof List
assert (0..5).size() == 6

