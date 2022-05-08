package com.potato;

import com.sun.tools.javac.main.Main;

import javax.tools.ToolProvider;

/**
 *  javac 等价的写法
 */
public class JavacMain {
    public static void main(String[] args) {
        Main compiler = new Main("javac");
        compiler.compile(new String[]{"src/main/java/com/potato/Hello.java", "-d", "target"});
    }
}