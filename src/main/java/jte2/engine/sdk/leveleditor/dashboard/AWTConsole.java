package jte2.engine.sdk.leveleditor.dashboard;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.handler.ArgsHandlerService;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.JTEArgumentParser;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineParser;
import jte2.engine.twilight.errors.NotImplementedException;
import jte2.engine.twilight.errors.SyntaxException;
import jte2.engine.twilight.material.Colour;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class AWTConsole extends JFrame implements EditorDashboard, ConsoleSession {
    private static final Logger logger = LogManager.getLogger(AWTConsole.class);
    @Getter
    private final ArgsHandlerService argsHandlerService;
    private final static String VERSION = "1.2";
    private final LineParser lineParser = new JTEArgumentParser();
    private final ColorPane textPane;
    private final JScrollPane scroll;

    private static int condition = JComponent.WHEN_FOCUSED;

    private final InputMap iMap;
    private final ActionMap aMap;

    public AWTConsole(){
        super("JTEEditor " + VERSION);
        textPane = new ColorPane();
        textPane.setPreferredSize(new Dimension(525, 600));
        textPane.setFont(new Font("Lucida Console", Font.BOLD, 12));
        scroll = new JScrollPane(textPane);
        textPane.setBackground(Color.BLACK);

        this.argsHandlerService = new ArgsHandlerService(lineParser);

        CommonsArgsProvider.registerCommons(this);

        scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        iMap = textPane.getInputMap(condition);
        aMap = textPane.getActionMap();

        textPane.getCaret().setVisible(false);

        textPane.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                scrollToBottom(scroll);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        textPane.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent arg0) {

            }

            @Override
            public void keyReleased(KeyEvent arg0) {

            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                Caret caret = textPane.getCaret();
                Point p = caret.getMagicCaretPosition();
                if(p != null) {
                    if(arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        textPane.setEditable(p.x > 25);
                    }else {
                        textPane.setEditable(true);
                    }
                }
            }
        });

        showInfo();

        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.Foreground, Colour.AQUA.getColor());

        int len = textPane.getDocument().getLength();
        textPane.setCaretPosition(len);
        textPane.setCharacterAttributes(aset, false);


        textPane.replaceSelection("\n</> ");
        textPane.setCaretPosition(textPane.getDocument().getLength());

        String enter = "enter";
        iMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), enter);
        aMap.put(enter, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Document document = textPane.getDocument();
                Element rootElem = document.getDefaultRootElement();
                int numLines = rootElem.getElementCount();
                Element lineElem = rootElem.getElement(numLines - 1);
                int lineStart = lineElem.getStartOffset();
                int lineEnd = lineElem.getEndOffset();
                String lineText;
                try {
                    lineText = document.getText(lineStart, lineEnd - lineStart);
                } catch (BadLocationException e) {
                    logger.error("Bad location sector for [" + lineStart + "] to [" + lineEnd + "]");
                    return;
                }

                if(lineText.length() != 5) { // TODO: find dynamic size for prefix

                    processLine(lineText);

                    StyleContext sc = StyleContext.getDefaultStyleContext();
                    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                            StyleConstants.Foreground, Colour.AQUA.getColor());

                    int len = textPane.getDocument().getLength();
                    textPane.setCaretPosition(len);
                    textPane.setCharacterAttributes(aset, false);

                }else {
                    StyleContext sc = StyleContext.getDefaultStyleContext();
                    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                            StyleConstants.Foreground, Colour.AQUA.getColor());

                    int len = textPane.getDocument().getLength();
                    textPane.setCaretPosition(len);
                    textPane.setCharacterAttributes(aset, false);

                }
                textPane.replaceSelection("\n</> ");
                textPane.setCaretPosition(textPane.getDocument().getLength());
            }
        });

        add(scroll);
        setSize(100, 600);
        pack();
        setVisible(false);
    }

    private void processLine(String text){
        try {
            int index = text.indexOf('>');
            if(index == -1) {
                throw new SyntaxException("Excepted char '>' to close prefix");
            }
            argsHandlerService.parseText(text.substring(index + 1), this);
        }catch (Exception e){
            logger.warn("External command exception [" + e.getMessage() + "]", e);
            JOptionPane.showMessageDialog(this,
                    "Error excepted while trying execute command: [" + text.trim()  + "]\nExternal command exception [" + e.getMessage() + "]\n",
                    "JTEEditor Console Warning",
                    JOptionPane.ERROR_MESSAGE);
            writeLine("External command exception [" + e.getMessage() + "]", Colour.RED);
        }
    }

    private void showInfo(){
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.Foreground, Colour.WHITE.getColor());

        int len = textPane.getDocument().getLength();
        textPane.setCaretPosition(len);
        textPane.setCharacterAttributes(aset, false);


        writeLine("UNIX Style Level Editor for Twilight Engine", Colour.WHITE, false);
        writeLine("JTELevelEditor " + VERSION, Colour.WHITE, false);
        writeLine("To create new scene use 'newmdl <name>'", Colour.ORANGE, false);
        writeLine("For more information on a specific command, type help", Colour.ORANGE, false);

        textPane.setCaretPosition(textPane.getDocument().getLength());
    }

    private void scrollToBottom(JScrollPane scrollPane) {
        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
    }

    @Override
    public void startDashboard() {
        setVisible(true);
    }

    @Override
    public void disableDashboard() {
        argsHandlerService.stop();

        EventQueue.invokeLater(() -> {
            WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);

            setVisible(false);
            dispose();
        });
    }

    @Override
    public void registerArg(String arg, Argument handler) {
        argsHandlerService.addHandle(arg, handler);
    }

    @Override
    public void unregisterArg(String arg) {
        argsHandlerService.removeHandle(arg);
    }

    @Override
    public String readLine() {
        throw NotImplementedException.NOT_IMPLEMENTED;
    }

    @Override
    public String readLine(long timeout) {
        throw NotImplementedException.NOT_IMPLEMENTED;
    }

    @Override
    public void writeLine(String text) {
        writeLine(text, Colour.AQUA);
    }

    @Override
    public void writeLine(String text, Colour colour) {
        writeLine(text, colour, true);
    }

    public void writeLine(String text, @NotNull Colour colour, boolean format) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.Foreground, colour.getColor());

        int len = textPane.getDocument().getLength();
        textPane.setCaretPosition(len);
        textPane.setCharacterAttributes(aset, false);

        if(format) {
            textPane.replaceSelection("\n</> " + text);
        }else {
            textPane.replaceSelection("\n" + text);
        }
        textPane.setCaretPosition(textPane.getDocument().getLength());
    }

    @Override
    public void disconnect() {
        throw NotImplementedException.NOT_IMPLEMENTED;
    }
}
