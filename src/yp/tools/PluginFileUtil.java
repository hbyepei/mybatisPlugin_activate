package yp.tools;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import yp.tools.enums.OS;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yepei.ye on 2017/5/17.
 * Description:
 */
public class PluginFileUtil {
    private static final String pluginDirName = "mybatis_plus";
    public static final String activateDir = "activate";

    /**
     * 生成破解之后的class文件目录
     *
     * @return
     * @throws Exception
     */
    public static File genActiveFile() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        //更好的方法是，判断操作系统，然后获取插件目录，加载相应的jar文件
        ExtClasspathLoader.loadClasspath(getPluginJar());

        CtClass driverClass = pool.get("com.seventh7.mybatis.ref.license.ActivationDriver");
        CtClass javaUtil = pool.get("com.seventh7.mybatis.util.JavaUtils");
        CtMethod activateMethod = driverClass.getMethod("activate", "(Ljava/lang/String;)Lcom/seventh7/mybatis/ref/license/ActivationResult;");
        CtMethod refValid = javaUtil.getDeclaredMethod("refValid");
        refValid.setBody("{return true;}");
        activateMethod.setBody("{com.seventh7.mybatis.ref.license.LicenseData licenseData = new com.seventh7.mybatis.ref.license.LicenseData(\"1\", \"2\");com.seventh7.mybatis.ref.license.ActivationResult res =com.seventh7.mybatis.ref.license.ActivationResult.success(licenseData); return res;}");
        //生成破解之后的class文件
        driverClass.writeFile(activateDir);
        javaUtil.writeFile(activateDir);
        ExtClasspathLoader.close();
        return new File(getActivateOut());
    }

    public static void replaceFile(File activateDir, File targetJar) throws IOException, InterruptedException {
        String jarCmd = Paths.get(System.getProperty("java.home")).getParent().resolve("bin/jar").toString();
        String cmd = jarCmd + " uvf '" + targetJar.getAbsolutePath() + "' -C '" + activateDir.getAbsolutePath() + "' com";
        String[] cmds = getSystem() == OS.windows ? new String[]{"cmd", "/k", cmd} : new String[]{"/bin/sh", "-c", cmd};
        callShell(cmds);
    }

    public static File getPluginJar() throws FileNotFoundException, AccessDeniedException {
        File f = getPluginDir().resolve("lib/" + pluginDirName + ".jar").toFile();
        return checkAccess(f);
    }

    public static void clean() {
        clean(new File(getActivateOut()));
    }

    public static void clean(File f) {
        if (f == null || !f.exists()) return;
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
                clean(file);
            }
        }
        f.delete();
    }

    private static OS getSystem() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("mac")) return OS.mac;
        if (os.toLowerCase().contains("windows")) return OS.windows;
        return OS.linux;
    }

    private static Path getPluginDir() throws FileNotFoundException, AccessDeniedException {
        String home = System.getProperty("user.home");
        OS os = getSystem();
        if (os == OS.mac) home += "/Library/Application Support";
        File f = new File(home);

        File[] subFiles = f.listFiles((dir, name) -> {
            String lowerName = name.toLowerCase();
            boolean nameOk = lowerName.startsWith("intellijidea") || lowerName.startsWith("intellij idea") || lowerName.startsWith(".intellijidea") || lowerName.startsWith(".intellij idea");
            return nameOk && dir.isDirectory();
        });
        if (subFiles == null || subFiles.length == 0) throw new FileNotFoundException("没找到idea配置目录");
        List<File> fileList = Arrays.asList(subFiles);
        fileList.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
        Path pluginDir = fileList.get(0).toPath();
        if (os != OS.mac) {
            pluginDir = pluginDir.resolve("config/plugins");
        }
        File mybatisDir = pluginDir.resolve(pluginDirName).toFile();
        return checkAccess(mybatisDir).toPath();
    }

    private static File checkAccess(File f) throws FileNotFoundException, AccessDeniedException {
        if (f == null) throw new NullPointerException();
        String path = f.getAbsolutePath();
        if (!f.exists()) {
            throw new FileNotFoundException("文件不存在:" + path);
        }
        if (!f.canRead()) {
            throw new AccessDeniedException("无读权限:" + path);
        }
        if (!f.canWrite()) {
            throw new AccessDeniedException("无写权限:" + path);
        }
        return f;
    }

    private static void callShell(String[] cmd) throws IOException, InterruptedException {
        System.out.println("激活前请关闭正在使用的IDE!!!");
        System.out.println("正在激活中，请稍后...");
        Process process = Runtime.getRuntime().exec(cmd);
        String info = readString(process.getInputStream());
        String error = readString(process.getErrorStream());

        int exitCode = process.waitFor();
        //        process.destroy();
        if (0 != exitCode) {
            throw new RuntimeException("调用shell失败. 错误码 :" + exitCode + ", 原因:" + System.lineSeparator() + cmd + System.lineSeparator() + error);
        } else {
            System.out.println(info);
            System.out.println("激活成功!");
        }
    }

    private static String readString(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder line = new StringBuilder();
        String temp = "";
        while (temp != null) {
            temp = reader.readLine();
            if (temp != null) line.append(temp).append(System.lineSeparator());
        }
        reader.close();
        return line.toString();
    }

    public static String getWorkDir() {
        return System.getProperty("user.dir");
    }

    private static String getActivateOut() {
        return getWorkDir() + "/" + activateDir;
    }
}
