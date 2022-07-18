```c
/*
 * @file c语言实现回调函数
 * @detial 在java等更高级的语言中往往已经给我们封装好了回调函数的调用方式，直接用就可以了。
 * 而C语言中并没有这种直接可以操作的回调方式，我们用函数指针来实现回调原理。
 */
#include<stdio.h>

// 将函数名作为指针的格式为：int (*ptr)(char *p) 即：返回值(指针名)(参数列表)
typedef int (*callback)(char *str); // 回调函数的名称为 callback，参数是char *p

// functionA的格式符合 callback 的格式，因此可以看作是一个 callback类型
int functionA(char *str)
{
    printf("回调 functionA(char *str) 函数:%s!\n", str);
    return 0;
}

// functionB的格式符合 callback 的格式，因此也可以看作是一个 callback类型
int functionB(char *str)
{
    printf("回调 functionB(char *str) 函数:%s!\n", str);
    return 0;
}



// 调用回调函数，方式一：通过命名方式
int test1(callback p_callback, char *str)
{
    printf("test1:\n不调用回调函数打印:%s!\n", str);
    p_callback(str);
    return 0;
}

// 调用回调函数，方式二：直接通过函数指针
int test2(int (*ptr)(), char *str)
{
    printf("test2:\n不调用回调函数打印:%s!\n", str);
    (*ptr)(str);
}

int main()
{
    char *str = "hello world!";

    test1(functionA, str);
    test1(functionB, str);
    test2(functionA, str);
    test2(functionB, str);

    printf("test3:\n");
    callback test3 = functionB;
    test3(str);

    return 0;
}
```

可以很直观的看出，同步和异步就是调用方式的不同，这使得我们的编码方式也有所不同。

在异步调用下的代码逻辑相对而言不太直观，需要借助回调或事件通知，这在复杂逻辑下对编码能力的要求较高。而同步调用就是直来直去，等待执行完毕然后拿到结果紧接着执行下面的逻辑，对编码能力的要求较低，也更不容易出错。

所以你会发现有很多方法它是异步调用的方式，但是最终的使用还是异步转同步。

比如你向线程池提交一个任务，得到一个 future，此时是异步的，然后你在紧接着在代码里调用 `future.get()`，那就变成等待这个任务执行完成，这就是所谓的异步转同步，像 Dubbo RPC 调用同步得到返回结果就是这样实现的。