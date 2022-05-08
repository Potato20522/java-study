package com.potato.dynamic;
 
import javax.tools.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
/**
 * 运行时编译
 */
public class DynamicCompiler {
    private JavaFileManager fileManager;
 
    public DynamicCompiler() {
        this.fileManager = initManger();
    }
 
    private JavaFileManager initManger() {
        if (fileManager != null) {
            return fileManager;
        } else {
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector diagnosticCollector = new DiagnosticCollector();
            fileManager = new ClassFileManager(javaCompiler.getStandardFileManager(diagnosticCollector, null, null));
            return fileManager;
        }
    }
 
    /**
     * 编译源码并加载，获取Class对象
     * @param fullName
     * @param sourceCode
     * @return
     * @throws ClassNotFoundException
     */
    public Class compileAndLoad(String fullName, String sourceCode) throws ClassNotFoundException {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        List<JavaFileObject> javaFileObjectList = new ArrayList<>();
        javaFileObjectList.add(new CharSequenceJavaFileObject(fullName, sourceCode));
        boolean result = javaCompiler.getTask(null, fileManager, null, null, null, javaFileObjectList).call();
        if (result) {
            return this.fileManager.getClassLoader(null).loadClass(fullName);
        } else {
            return Class.forName(fullName);
        }
    }
 
    /**
     * 关闭fileManager
     * @throws IOException
     */
    public void closeFileManager() throws IOException {
        this.fileManager.close();
    }
 
}