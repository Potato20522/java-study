package operator

// base and exponent are ints and the result can be represented by an Integer
// 基数和指数都是int,所得结果可以用int表示，那么结果就是int类型
assert 2**3 instanceof Integer    //  8
assert 10**9 instanceof Integer   //  1_000_000_000

// the base is a long, so fit the result in a Long
// (although it could have fit in an Integer)
// 基数是long类型，所以结果也是long类型，尽管用int就足够表示
assert 5L**2 instanceof Long       //  25

// the result can't be represented as an Integer or Long, so return a BigInteger
// 当结果超过了int和long的表示范围,用BigInteger表示
assert 100**10 instanceof BigInteger   //  10e20
assert 1234**123 instanceof BigInteger //  170515806212727042875...

// the base is a BigDecimal and the exponent a negative int
// but the result can be represented as an Integer
assert 0.5**-2 instanceof Integer    //  4

// the base is an int, and the exponent a negative float
// but again, the result can be represented as an Integer
assert 1**-0.3f instanceof Integer    //  1

// the base is an int, and the exponent a negative int
// but the result will be calculated as a Double
// (both base and exponent are actually converted to doubles)
assert 10**-1 instanceof Double     //  0.1

// the base is a BigDecimal, and the exponent is an int, so return a BigDecimal
assert 1.2**10 instanceof BigDecimal //  6.1917364224

// the base is a float or double, and the exponent is an int
// but the result can only be represented as a Double value
assert 3.4f**5 instanceof Double     //  454.35430372146965
assert 5.6d**2 instanceof Double     //  31.359999999999996

// the exponent is a decimal value
// and the result can only be represented as a Double value
assert 7.8**1.9 instanceof Double     //  49.542708423868476
assert 2**0.1f instanceof Double     //  1.0717734636432956
