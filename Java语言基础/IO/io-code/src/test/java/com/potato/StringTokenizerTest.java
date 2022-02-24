package com.potato;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.StringTokenizer;

public class StringTokenizerTest {
    @Test
    void a() {
        String str = "runoob,google,taobao,facebook,zhihu";
        // 以 , 号为分隔符来分隔字符串
        StringTokenizer st=new StringTokenizer(str,",");
        while(st.hasMoreTokens()) {
            System.out.println(st.nextToken());
        }
        String[] str2 = str.split(",");
        System.out.println(Arrays.toString(str2));
    }

    @Test
    void test02() {
        System.out.println("使用第一种构造函数：");
        StringTokenizer st1 = new StringTokenizer("Hello Runoob How are you", " ");
        while (st1.hasMoreTokens())
            System.out.println(st1.nextToken());

        System.out.println("使用第二种构造函数：");
        StringTokenizer st2 = new StringTokenizer("JAVA : Code : String", " :");
        while (st2.hasMoreTokens())
            System.out.println(st2.nextToken());

        System.out.println("使用第三种构造函数：");
        StringTokenizer st3 = new StringTokenizer("JAVA : Code : String", " :",  true);
        while (st3.hasMoreTokens())
            System.out.println(st3.nextToken());
    }
}
