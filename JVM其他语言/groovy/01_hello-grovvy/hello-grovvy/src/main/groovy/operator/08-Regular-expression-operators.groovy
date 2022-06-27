package operator

import java.util.regex.Matcher
import java.util.regex.Pattern
//正则表达式相关的运算符
/**1.模式运算符*/
def p = ~/foo/
assert p instanceof Pattern

p = ~'foo'
p = ~"foo"
p = ~$/dollar/slashy $ string/$  //特殊符号不需要转义 dollar/slashy $ string
p = ~"${pattern}" //支持GString

/**2.查找运算符*/
def text = "some text to match"
def m = text =~ /match/
assert m instanceof Matcher
if (!m) {
    throw new RuntimeException("Oops, text not found!")
}

/**3.匹配运算符*/
m = text ==~ /match/
assert m instanceof Boolean
if (m) {
    throw new RuntimeException("Should not reach that point!")
}

assert 'two words' ==~ /\S+\s+\S+/
assert 'two words' ==~ /^\S+\s+\S+$/
assert !(' leading space' ==~ /\S+\s+\S+/)

def m1 = 'two words' =~ /^\S+\s+\S+$/
assert m1.size() == 1
def m2 = 'now three words' =~ /^\S+\s+\S+$/
assert m2.size() == 0
def m3 = 'now three words' =~ /\S+\s+\S+/
assert m3.size() == 1
assert m3[0] == 'now three'
def m4 = ' leading space' =~ /\S+\s+\S+/
assert m4.size() == 1
assert m4[0] == 'leading space'
def m5 = 'and with four words' =~ /\S+\s+\S+/
assert m5.size() == 2
assert m5[0] == 'and with'
assert m5[1] == 'four words'