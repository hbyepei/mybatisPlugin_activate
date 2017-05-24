package yp.tools;
import yp.tools.enums.OS;

import java.io.*;
import java.nio.file.Paths;

/**
 * Author:yepei@meituan.com
 * Date:2017/5/24
 * Time:21:52
 * ------------------------------------
 * Desc:
 */
public class ShellUtil {
    public static class ShellInfo {
        int code;
        String message;

        public ShellInfo(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    public static ShellInfo updateJar(File activatedJar, File targetJar) throws IOException, InterruptedException {
        String jarCmd = Paths.get(System.getProperty("java.home")).getParent().resolve("bin/jar").toString();
        String cmd = jarCmd + " uvf '" + targetJar.getAbsolutePath() + "' -C '" + activatedJar.getAbsolutePath() + "' com";
        String[] cmds = SystemUtil.getSystem() == OS.windows ? new String[]{"cmd", "/k", cmd} : new String[]{"/bin/sh", "-c", cmd};
        return callShell(cmds);
    }

    public static ShellInfo callShell(String[] cmd) throws IOException, InterruptedException {
        String sep = System.lineSeparator();
        StringBuilder sb = new StringBuilder("激活前请关闭正在使用的IDE!!!").append(sep).append("正在激活中，请稍后...").append(sep);

        Process process = Runtime.getRuntime().exec(cmd);
        String info = readString(process.getInputStream());
        String error = readString(process.getErrorStream());

        int exitCode = process.waitFor();
        String message = exitCode == 0 ? sb.append(info).append(sep).append("激活成功").toString() : sb.append(error).append(sep).append("激活失败").toString();
        return new ShellInfo(exitCode, message);
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
}
