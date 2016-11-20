package my.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by bruce.ge on 2016/11/21.
 */
public class TextBox extends AnAction {
    public TextBox() {
        super("Text _Boxes");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project data = e.getData(PlatformDataKeys.PROJECT);
        String txt = Messages.showInputDialog(data, "What is your name?", "Input your name", Messages.getQuestionIcon());
        if (StringUtils.containsAny(txt, "hehe")) {
            Messages.showMessageDialog(data, "Hello, " + txt + "!\n hehe nimei", "Information", Messages.getInformationIcon());
        } else {
            Messages.showMessageDialog(data, "Hello, " + txt + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());
        }
    }
}
