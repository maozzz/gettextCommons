package ru.villex.gettext.test;

import org.junit.Assert;
import org.junit.Test;
import ru.villex.gettext.plurals.ExpressionPlurable;
import ru.villex.gettext.plurals.Plurable;

import javax.script.ScriptEngineManager;
import java.net.URL;
import java.net.URLClassLoader;

public class ExpressionPlurableClassLoaderTest {

    @Test
    public void testInitializationWithIsolatedContextClassLoader() throws Exception {
        final String className = ExpressionPlurable.class.getName();
        URL classes = ExpressionPlurable.class.getProtectionDomain().getCodeSource().getLocation();
        ClassLoader previous = Thread.currentThread().getContextClassLoader();

        try (URLClassLoader loader = new URLClassLoader(new URL[]{classes},
                ExpressionPlurable.class.getClassLoader()) {
            @Override
            protected synchronized Class<?> loadClass(String name, boolean resolve)
                    throws ClassNotFoundException {
                if (!className.equals(name)) {
                    return super.loadClass(name, resolve);
                }
                Class<?> loaded = findLoadedClass(name);
                if (loaded == null) {
                    loaded = findClass(name);
                }
                if (resolve) {
                    resolveClass(loaded);
                }
                return loaded;
            }
        }) {
            Thread.currentThread().setContextClassLoader(new ClassLoader(null) {
            });
            Assert.assertNull(new ScriptEngineManager().getEngineByName("js"));

            Class<?> isolatedClass = Class.forName(className, true, loader);
            Plurable plurable = (Plurable) isolatedClass.getConstructor(String.class)
                    .newInstance("(n != 1);");

            Assert.assertEquals(0, plurable.pluralEval(1));
            Assert.assertEquals(1, plurable.pluralEval(3));
        } finally {
            Thread.currentThread().setContextClassLoader(previous);
        }
    }
}
