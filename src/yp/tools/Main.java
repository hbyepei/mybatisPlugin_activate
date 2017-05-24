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
        int activation = Messages.showYesNoDialog(project, "If you have installed mybatis_plugin, please presson 'Activate' button to Activate it!", "Activation", "Activate", "Cancel", Messages.getQuestionIcon());
        if (activation == Messages.YES) {
            ShellUtil.ShellInfo shellInfo = Activator.doActivate();
            int code = shellInfo.code;
            if (code == 0) {
                Messages.showInfoMessage(shellInfo.message, "Success");
            } else {
                Messages.showErrorDialog(shellInfo.message, "Failed");
            }
        }
    }
}
