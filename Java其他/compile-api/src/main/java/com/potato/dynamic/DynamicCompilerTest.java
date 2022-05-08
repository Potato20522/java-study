package com.potato.dynamic;
 
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
 
public class DynamicCompilerTest {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        StringBuilder src = new StringBuilder();
        src.append("package com.potato.dynamic;");
        src.append("public class DynaClass {\n");
        src.append("    public String toString() {\n");
        src.append("        return \"Hello, I am \" + ");
        src.append("this.getClass().getSimpleName();\n");
        src.append("    }\n");
        src.append("}\n");
 
        String fullName = "com.potato.dynamic.DynaClass";
 
        DynamicCompiler compiler = new DynamicCompiler();
        Class clz = compiler.compileAndLoad(fullName, src.toString());
 
        System.out.println(clz.getConstructor().newInstance());
        compiler.closeFileManager();
    }
}