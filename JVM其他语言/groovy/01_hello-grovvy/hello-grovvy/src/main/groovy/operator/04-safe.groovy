package operator
//安全访问运算符
def person = getPerson() // 假设该方法可能返回null
def name = person?.name // 如果person不为null，那返回具体的值；如果为null，也不会抛出异常，而是返回null


