package com.potato.temp;

import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Hello {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = Hello.class.getResourceAsStream("");
        byte[] bytes = IOUtils.toByteArray(inputStream);
        System.out.println(Arrays.toString(bytes));
        String s = new String(bytes);
        System.out.println(s);
    }
}
