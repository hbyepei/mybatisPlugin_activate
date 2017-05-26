package yp.tools;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * Author:yepei@meituan.com
 * Date:2017/5/24
 * Time:21:47
 * ------------------------------------
 * Desc:
 */
public class Main extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        int activation = Messages.showYesNoDialog(project, "如果你已经安装了mybatis_plugin插件, 请点击'激 活' 按钮 来激活mybatis_plugin插件!", "激活", "激 活", "取 消", Messages.getQuestionIcon());
        if (activation == Messages.YES) {
            ShellUtil.ShellInfo shellInfo = Activator.doActivate();
            int code = shellInfo.code;
            if (code == 0) {
                Messages.showInfoMessage(shellInfo.message, "激活成功");
            } else {
                Messages.showErrorDialog(shellInfo.message, "激活失败");
            }
        }
    }
}
