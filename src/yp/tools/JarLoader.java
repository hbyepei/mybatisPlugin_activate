package yp.tools;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 把jar和配置文件加载到classpath中。
 *
 * @author yepei
 */
public final class JarLoader {
    private static URLClassLoader classloader;

    /**
     * 将指定目录下的所有jar包加入classPath
     *
     * @param files
     *
     * @throws Exception
     */
    public static void loadClasspath(File... files) throws Exception {
        if (classloader == null) {
            classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        }
        assert files != null && files.length > 0;
        for (File f : files) {
            addJar2ClassPath(f);
        }
    }

    public static void close() throws IOException {
        if (classloader != null) classloader.close();
    }

    /**
     * 递归加载指定文件或目录下的jar包至classPath。
     *
     * @param file 当前遍历文件
     */
    private static void addJar2ClassPath(File file) throws Exception {
        if (file.isDirectory()) {
            File[] tmps = file.listFiles();
            assert tmps != null;
            for (File tmp : tmps) {
                addJar2ClassPath(tmp);
            }
        } else {
            if (file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".zip")) {
                addUrl2ClassLoader(file.toURI().toURL());
            }
        }
    }

    /**
     * 根据url加载资源到classpath
     *
     * @param url
     *
     * @throws Exception
     */
    private static void addUrl2ClassLoader(URL url) throws Exception {
        Method addURLMethod = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        addURLMethod.setAccessible(true);
        addURLMethod.invoke(classloader, url);
    }
}
