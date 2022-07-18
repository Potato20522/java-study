package dsl
class PizzaShop {
    def setSize(String size){}
    def setAddress(String addr){}
    def setPayment(String cardId){}
}

def pizzaShop = new PizzaShop()
pizzaShop.with {
    setSize "large"
    setAddress "XXX street"
    setPayment "WeChat"
}

def getPizza(Closure closure){
    def pizzaShop = new PizzaShop()
    closure.delegate = pizzaShop
    closure.run()
}

getPizza {
    setSize "large"
    setAddress "XXX street"
    setPayment "WeChat"
}