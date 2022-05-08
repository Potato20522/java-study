package com.potato.dynamic;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * JavaFileObject获取java源程序
 * 开发者希望生成 Calculator 的一个测试类，而不是手工编写。使用 compiler API，可以将内存中的一段字符串，编译成一个 CLASS 文件。
 * 定制 JavaFileObject 对象：
 */
public class StringObject extends SimpleJavaFileObject {
    private String content = null;
 
    protected StringObject(String className, String contents) throws URISyntaxException {
        super(new URI(className), Kind.SOURCE);
        this.content = contents;
    }
 
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return content;
    }
}
