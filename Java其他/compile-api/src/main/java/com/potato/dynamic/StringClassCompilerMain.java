package com.potato.dynamic;
import javax.tools.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
public class StringClassCompilerMain {
    public static void main(String[] args) {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        StandardJavaFileManager standardJavaFileManager = javaCompiler.getStandardFileManager(null, null, null);
        JavaFileObject testFile = generateTest();
        Iterable<? extends JavaFileObject> classes = Arrays.asList(testFile);
        JavaCompiler.CompilationTask task = javaCompiler.getTask(null, standardJavaFileManager, collector, null, null, classes);
        if(task.call()){
            System.out.println("success");
        }else{
            System.out.println("failure!");
        }
 
        List<Diagnostic<? extends JavaFileObject>> diagnostics = collector.getDiagnostics();
        for (Diagnostic<? extends JavaFileObject> diagnostic: diagnostics){
            System.out.println("line:"+ diagnostic.getLineNumber());
            System.out.println("msg:"+ diagnostic.getMessage(Locale.ENGLISH));
            System.out.println("source:"+ diagnostic.getSource());
 
        }
    }
 
    private static JavaFileObject generateTest() {
        String contents = "package com.potato.dynamic;\n" +
                "\n" +
                "public class Hello {\n" +
                "    public static void main(String[] args) {\n" +
                "        System.out.println(\"hello world2\");\n" +
                "    }\n" +
                "}";
        StringObject so = null;
        try {
            so = new StringObject("com.potato.dynamic.Hello", contents);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return so;
 
    }
}