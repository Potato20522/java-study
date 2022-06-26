package number

def a = 1
assert a instanceof Integer

// Integer.MAX_VALUE
def b = 2147483647
assert b instanceof Integer

// Integer.MAX_VALUE + 1
def c = 2147483648
assert c instanceof Long

// Long.MAX_VALUE
def d = 9223372036854775807
assert d instanceof Long

// Long.MAX_VALUE + 1
def e = 9223372036854775808
assert e instanceof BigInteger

def decimal = 123.456
println decimal.getClass() // class java.math.BigDecimal

def a1 = 10g //BigInteger 使用G或g
def a2 = 10l //Long 使用L或l
def a3 = 10i //Integer 使用I或i
def a4 = 10.0g //igDecimal 使用G或g
def a5 = 10d //Double 使用D或d
def a6 = 10f //Float 使用F或f