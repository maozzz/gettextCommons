package ru.villex.gettext.plurals;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.tools.*;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by Alexey Matukhin.
 * Author: maoz
 * Date: 17.11.2018
 * Time: 19:30
 */
public class ExpressionPlurable implements Plurable {

    private static final ScriptEngine js = new ScriptEngineManager().getEngineByName("js");
    private String expression;

    public ExpressionPlurable(String expression) {
        this.expression = expression;
    }

    @Override
    public int pluralEval(long n) {
        try {
            Object eval = js.eval("n=" + n + ";" + expression);
            if (eval instanceof Boolean) {
                return (Boolean) eval ? 1 : 0;
            } else {
                return (Integer) eval;
            }
        } catch (Exception e) {
            return 0;
        }
    }


    public static Plurable generatePlurableClass(String expression) {
//        try {
//            String className = "Plurable";
//            // create an empty source file
//            Logger.getLogger(ExpressionPlurable.class.getSimpleName()).severe("Creating class for plurable");
//            File sourceFile = File.createTempFile(className, ".java");
//            Logger.getLogger(ExpressionPlurable.class.getSimpleName()).severe("Creating class for plurable");
//            sourceFile.deleteOnExit();
//
//            // generate the source code, using the source filename as the class name
//            className = sourceFile.getName().split("\\.")[0];
//            String sourceCode = " public class " + className + " implements " + Plurable.class.getName() + " {"
//                    + " public int pluralEval(long n) {"
//                    + " Object o = " + expression + (expression.endsWith(";") ? "" : ";")
//                    + " if (o instanceof Boolean) { return ((Boolean) o) ? 1 : 0;}else { return (Integer) o;} "
//                    + " }"
//                    + "} ";
//
//            FileWriter writer = new FileWriter(sourceFile);
//            Logger.getLogger(ExpressionPlurable.class.getSimpleName()).severe("Writing class for plurable");
//            writer.write(sourceCode);
//            writer.close();
//            Logger.getLogger(ExpressionPlurable.class.getSimpleName()).severe("Writed! Compile...");
//
//            // compile the source file
//            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
//            StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
//            File parentDirectory = sourceFile.getParentFile();
//            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(parentDirectory));
//            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
//            compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
//            fileManager.close();
//            Logger.getLogger(ExpressionPlurable.class.getSimpleName()).severe("Loading compiled class.");
//
//            // load the compiled class
//            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{parentDirectory.toURI().toURL()}, ClassLoader.getSystemClassLoader());
//            Class<?> plurable = classLoader.loadClass(className);
//
//            // return instance
//            Logger.getLogger(ExpressionPlurable.class.getSimpleName()).severe("Plurable ready");
//            return (Plurable) plurable.newInstance();
//        } catch (Exception ex) {
//            ex.printStackTrace();
            Logger.getLogger(ExpressionPlurable.class.getSimpleName()).info("Creating plurable for expr: " + expression);
            return new ExpressionPlurable(expression);
//        }
    }
}
