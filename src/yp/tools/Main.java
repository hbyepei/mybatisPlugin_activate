package yp.tools;
import java.io.File;

/**
 * Created by yepei.ye on 2017/5/17.
 * Description:
 */
public class Main {
    /**
     * 此方法修改了用于激活的字字节码文件，替换了原有激活逻辑，并生成了新的javaUtils及ActivationDriver字节码文件
     *
     * @param args
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        doActivate();
    }

    private static void doActivate() throws Exception {
        PluginFileUtil.clean();
        File activateFile = PluginFileUtil.genActiveFile();
        File targetJar = PluginFileUtil.getPluginJar();
        PluginFileUtil.replaceFile(activateFile, targetJar);
        System.out.println("结束");
        PluginFileUtil.clean(new File(PluginFileUtil.getWorkDir() + "/out"));
        PluginFileUtil.clean(new File(PluginFileUtil.getWorkDir() + "/" + PluginFileUtil.activateDir));
    }
}
