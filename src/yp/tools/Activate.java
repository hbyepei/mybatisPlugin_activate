package yp.tools;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.io.IOException;

/**
 * Created by yepei.ye on 2017/5/17.
 * Description:
 */
public class Activate {
    /**
     * 此方法修改了用于激活的字字节码文件，替换了原有激活逻辑，并生成了新的javaUtils及ActivationDriver字节码文件
     * 生成的新文件位于当前目录的activate目录下，然后请执行命令(执行命令前请关闭idea)：
     * cd activate & jar uvf ../lib/mybatis_plus.jar com & cp -f ../lib/mybatis_plus.jar ~/Library/Application\ Support/IntelliJIdea2017.1/mybatis_plus/lib/
     *
     * 上述命令会将mybatsi插件目录中的mybatis_plus.jar文件下的对应class文件替换为新生成的class文件
     * 这些步骤可以制作成一个激活工具，
     *
     * 注意，在windows或linux中，idea的插件安装目录通常是：~/.IntellijIdea2017/config/plugins/xxx
     * 在mac中,idea的插件安装目录通常是: ~/Library/Application Support/IntelliJIdea2017.1/xxx
     *
     * 详细参考blog: http://www.cnblogs.com/yepei/p/6866732.html, 密码：520134
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        //更好的方法是，判断操作系统，然后获取插件目录，加载相应的jar文件
        CtClass driverClass = pool.get("com.seventh7.mybatis.ref.license.ActivationDriver");
        CtClass javaUtil = pool.get("com.seventh7.mybatis.util.JavaUtils");
        CtMethod activate = driverClass.getMethod("activate", "(Ljava/lang/String;)Lcom/seventh7/mybatis/ref/license/ActivationResult;");
        CtMethod refValid = javaUtil.getDeclaredMethod("refValid");
        try {
            refValid.setBody("{return true;}");
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        System.out.println(activate);
        try {
            activate.setBody("{com.seventh7.mybatis.ref.license.LicenseData licenseData = new com.seventh7.mybatis.ref.license.LicenseData(\"1\", \"2\");com.seventh7.mybatis.ref.license.ActivationResult res =com.seventh7.mybatis.ref.license.ActivationResult.success(licenseData); return res;}");
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
        try {
            driverClass.writeFile("activate");
            javaUtil.writeFile("activate");
        } catch (CannotCompileException | IOException e) {
            e.printStackTrace();
        }
    }
}
