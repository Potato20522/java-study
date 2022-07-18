package dsl

def move(String dir){
    print "move $dir "
    this //类似于Java中的builder模式，方法最后return this
}

def turn(String dir){
    print "turn $dir"
    this
}


def jump(String speed,String dir){
    print "jump ${dir} ${speed}"
    this
}

//move("forward").turn("right").turn("right").move("back")
move "forward" turn "right" turn "right" move "back" // 1
println ''
//jump("fast","forward").move("back").move("forward")
jump "fast","forward" move "back" move "forward"      // 2

