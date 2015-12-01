package me.skyleft.utils;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangzongchao on 2015/11/30.
 */
public class JCUtil {

    private String jars = "";
    private String targetDir = "";



    public Ret doCompile(String javaFilePath, String otherSourcePath, String jarPath, String targetDir){
        try {
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
            JCUtil JCUtil = new JCUtil();
            boolean compilerResult = JCUtil.compiler("UTF-8", JCUtil.getJarFiles(jarPath), javaFilePath, otherSourcePath, targetDir, diagnostics);
            if (compilerResult) {
                return Ret.SUCCESS;
            } else {
                StringBuilder msg = new StringBuilder();
                for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                    msg.append(diagnostic.getMessage(null));
                }
                Ret ret = new Ret(false,msg.toString());
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Ret ret = new Ret(false,e.getMessage());
            return ret;
        }
    }



    public boolean isnull(String str) {
        if (null == str) {
            return false;
        } else if ("".equals(str)) {
            return false;
        } else if (str.equals("null")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 编译java文件
     *
     * @param encoding    编译编码
     * @param jars        需要加载的jar
     * @param filePath    文件或者目录（若为目录，自动递归编译）
     * @param sourceDir   java源文件存放目录
     * @param targetDir   编译后class类文件存放目录
     * @param diagnostics 存放编译过程中的错误信息
     * @return
     * @throws Exception
     */
    public boolean compiler(String encoding, String jars, String filePath, String sourceDir, String targetDir, DiagnosticCollector<JavaFileObject> diagnostics)
            throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        try {
            if (!isnull(filePath) && !isnull(sourceDir) && !isnull(targetDir)) {
                return false;
            }
            File sourceFile = new File(filePath);
            List<File> sourceFileList = new ArrayList<File>();
            this.targetDir = targetDir;
            getSourceFiles(sourceFile, sourceFileList);
            if (sourceFileList.size() == 0) {
                System.out.println(filePath + "目录下查找不到任何java文件");
                return false;
            }
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(sourceFileList);
            /**
             * 编译选项，在编译java文件时，编译程序会自动的去寻找java文件引用的其他的java源文件或者class。 -sourcepath选项就是定义java源文件的查找目录， -classpath选项就是定义class文件的查找目录。
             */
            Iterable<String> options = Arrays.asList("-encoding", encoding, "-classpath", jars, "-d", targetDir, "-sourcepath", sourceDir);
            CompilationTask compilationTask = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
            return compilationTask.call();
        } finally {
            fileManager.close();
        }
    }

    /**
     * 查找该目录下的所有的java文件
     *
     * @param sourceFile
     * @param sourceFileList
     * @throws Exception
     */
    private void getSourceFiles(File sourceFile, List<File> sourceFileList) throws Exception {
        if (sourceFile.exists() && sourceFileList != null) {
            if (sourceFile.isDirectory()) {
                File[] childrenFiles = sourceFile.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        if (pathname.isDirectory()) {
                            try {
                                new CopyDirectory().copyDirectiory(pathname.getPath(), targetDir + pathname.getPath().substring(pathname.getPath().indexOf("src") + 3, pathname.getPath().length()));
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            return true;
                        } else {
                            String name = pathname.getName().toLowerCase();
                            if (name.endsWith(".java") ? true : false) {
                                return true;
                            }
                            try {
                                new CopyDirectory().copyFile(pathname, new File(targetDir + pathname.getPath().substring(pathname.getPath().indexOf("src") + 3, pathname.getPath().length())));
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            return false;
                        }
                    }
                });
                for (File childFile : childrenFiles) {
                    getSourceFiles(childFile, sourceFileList);
                }
            } else {
                sourceFileList.add(sourceFile);
            }
        }
    }

    /**
     * 查找该目录下的所有的jar文件
     *
     * @param sourceFile
     * @param sourceFileList
     * @throws Exception
     */
    private String getJarFiles(String jarPath) throws Exception {
        File sourceFile = new File(jarPath);
        if (sourceFile.exists()) {
            if (sourceFile.isDirectory()) {
                File[] childrenFiles = sourceFile.listFiles(new FileFilter() {
                    public boolean accept(File pathname) {
                        if (pathname.isDirectory()) {
                            return true;
                        } else {
                            String name = pathname.getName();
                            if (name.endsWith(".jar") ? true : false) {
                                jars = jars + pathname.getPath() + ";";
                                return true;
                            }
                            return false;
                        }
                    }
                });
            }
        }
        return jars;
    }


    public static void main(String[] args) {
        try {
            String filePath = "E:\\logs\\src\\gen-java";
            String sourceDir = "E:\\logs\\src\\gen-java";
            String jarPath = "D:\\work\\gen-java-lib";
            String targetDir = "E:\\logs\\bin";
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
            JCUtil JCUtil = new JCUtil();
            boolean compilerResult = JCUtil.compiler("UTF-8", JCUtil.getJarFiles(jarPath), filePath, sourceDir, targetDir, diagnostics);
            if (compilerResult) {
                System.out.println("编译成功");
            } else {
                System.out.println("编译失败");
                for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
//                   System.out.format("%s[line %d column %d]-->%s%n", diagnostic.getKind(), diagnostic.getLineNumber(),   
//                   diagnostic.getColumnNumber(),   
//                   diagnostic.getMessage(null));   
                    System.out.println(diagnostic.getMessage(null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
