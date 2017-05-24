package yp.tools;
import yp.tools.enums.OS;

/**
 * Author:yepei@meituan.com
 * Date:2017/5/24
 * Time:22:02
 * ------------------------------------
 * Desc:
 */
public class SystemUtil {
    public static OS getSystem() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("mac")) return OS.mac;
        if (os.toLowerCase().contains("windows")) return OS.windows;
        return OS.linux;
    }
    public static String getWorkDir() {
        return System.getProperty("user.dir");
    }
}
