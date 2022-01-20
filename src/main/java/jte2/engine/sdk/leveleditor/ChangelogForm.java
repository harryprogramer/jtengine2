package jte2.engine.sdk.leveleditor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChangelogForm extends JFrame {
    private final static Logger logger = LogManager.getLogger(ChangelogForm.class);
    private ChangelogManager.ChangeLog changeLog;
    private JButton confirmButton ;
    private JLabel textField;
    private JPanel panel;
    private JFrame frame;


    private ChangelogForm(ChangelogManager.@NotNull ChangeLog changeLog, ChangelogManager changelogManager) {
        this.changeLog = changeLog;
        this.setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                changeLog.hasRead = true;
                changeLog.readTime = dtf.format(now);
                try {
                    changelogManager.updateChangelog(changeLog);
                } catch (IOException ex) {
                    logger.warn("Cannot update changelog feedback, {}", ex.getMessage());
                }
                dispose();
            }
        });

        textField.setBorder(new EmptyBorder(0, 10, 0, 0));

        String stringBuilder = "<html>" +
                changeLog.content +
                "</html>";
        textField.setText(stringBuilder);
        setTitle(changeLog.title);
        pack();
    }

    public static void showChangelogForm(ChangelogManager.ChangeLog changeLog, ChangelogManager manager) {
        SwingUtilities.invokeLater(() -> {
            JFrame jFrame = new ChangelogForm(changeLog, manager);
            jFrame.setVisible(true);
            jFrame.setSize(800, 600);
        });
    }
}
