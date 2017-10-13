package yp.tools;
import java.io.File;

/**
 * Author:yepei@meituan.com
 * Date:2017/5/24
 * Time:22:04
 * ------------------------------------
 * Desc:
 */
public class Activator {
    public static ShellUtil.ShellInfo doActivate() {
        try {
            FileUtil.clean(false);
            File activateFile = FileUtil.genActiveFile();
            File targetJar = FileUtil.getPluginJar();
            ShellUtil.ShellInfo shellInfo = ShellUtil.updateJar(activateFile, targetJar);
            FileUtil.clean(true);
            return shellInfo;
        } catch (Exception e) {
            return new ShellUtil.ShellInfo(-1, e.getMessage());
        }
    }
}
