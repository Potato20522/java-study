package operator

def result = 1 / 3.0f // 当其中有一个是float或double时
println result.class  // 结果是:class java.lang.Double
println result        // 0.3333333333333333
println 4.intdiv(3)   // 结果为1,与Java一样

def newResult = 1 / 2   // 当两个操作数都是整型
println newResult.class // class java.math.BigDecimal
println newResult       // 结果是0.5
