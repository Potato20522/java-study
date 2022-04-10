package com.atguigu.java;

/**
 * @author shkstart  shkstart@126.com
 * @create 2020  14:57
 */
public class LocalVarGC {
    public void localvarGC1() {
        byte[] buffer = new byte[10 * 1024 * 1024];//10MB
        System.gc();//没有回收buffer
    }

    public void localvarGC2() {
        byte[] buffer = new byte[10 * 1024 * 1024];
        buffer = null;
        System.gc();//回收了buffer
    }

    public void localvarGC3() {
        {
            byte[] buffer = new byte[10 * 1024 * 1024];
        }
        System.gc();//没有回收buffer，局部变量表的槽还在引用着buffer
    }

    public void localvarGC4() {
        {
            byte[] buffer = new byte[10 * 1024 * 1024];
        }
        int value = 10;//局部变量表的槽复用，所以这里回收了
        System.gc();
    }

    public void localvarGC5() {
        localvarGC1();//这里面的执行 System.gc()时，buffer没有被回收
        System.gc();//这里执行  System.gc() 时，buffer被回收
    }

    public static void main(String[] args) {
        LocalVarGC local = new LocalVarGC();
        local.localvarGC5();
    }
}
