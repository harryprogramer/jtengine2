package thewall.engine.twilight.debugger.console;

import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;
import thewall.engine.twilight.scheduler.TEngineThreadFactory;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

public final class DebugConsole {
    @Getter
    private static DebugConsole console = new DebugConsole();

    private static final Logger logger = LogManager.getLogger(DebugConsole.class);

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(TEngineThreadFactory.getInstance());
    private volatile List<LogData> logQueue = new ArrayList<>();
    private static boolean isGLFWInit = false;

    @Data
    static private class LogData {
        private final String text;
        private final LogLevel logLevel;
        private final boolean isRaw;
    }

    public enum LogLevel {
        INFO,
        WARN,
        FATAL,
        DEBUG
    }

    private boolean isWindowShowed = false;
    private final JFrame jFrame;
    private final JTextPane jTextField;
    private boolean debug = false;
    private boolean isLoggerEnabled = false;

    private static final String welcomeMessage ="Welcome to gowno\n";

    @SneakyThrows
    private DebugConsole(){
        jFrame = new JFrame("The Wall Console");
        jFrame.setSize(1280, 720);
        jTextField = new JTextPane();
        DefaultCaret caret = (DefaultCaret) jTextField.getCaret();
        JScrollPane scroll = new JScrollPane(jTextField);
        jFrame.add(scroll, BorderLayout.CENTER);
        jFrame.setFocusableWindowState(false);
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/fonts/font.ttf")));
        //jTextField.setFont(new Font ("Windows Command Prompt", Font.BOLD, 20));
        //jFrame.setLocationRelativeTo(null);
        jTextField.setBackground(new Color(8,46,56));
        //jTextField.setEditable(false);
        //jFrame.add(jTextField);
        startLogging();
    }

    public void showConsole(){
        logger.info("Showing up debug console");
        showOnScreen(1, jFrame);
    }

    public boolean isLogging(){
        return isLoggerEnabled;
    }

    @SneakyThrows
    public void startLogging(){
        if(isLoggerEnabled){
            throw new IllegalStateException("Logging is already working");
        }
        startLoggerQueue();
    }

    private void loggerPulse(){
        if(isLoggerEnabled && isLoggerReady()) {
            for (Iterator<LogData> it = logQueue.iterator(); it.hasNext(); ) {
                LogData logData = it.next();
                if(logData.isRaw()){
                    appendToPane(logData.getText(), Color.WHITE);
                }else {
                    log(logData.getText(), logData.getLogLevel());
                }
                it.remove();
            }
        }
    }
    private boolean isLoggerReady(){
        return jFrame.isShowing();
    }

    private void startLoggerQueue(){
        isLoggerEnabled = true;
        executor.scheduleAtFixedRate(() -> {
            try{
                loggerPulse();
            }catch (Exception e){
                logger.warn("Debug console error", e);
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    private void stopLogging(){
        logger.info("Stopping logging for debug console");
        if(!isLoggerEnabled){
            throw new IllegalStateException("Logger is already switch off");
        }
        isLoggerEnabled = false;
        executor.shutdown();
    }


    @Deprecated
    public void fatal(String text){
        invokeLog(text, LogLevel.FATAL);
    }

    @Deprecated
    public void warn(String text){
        invokeLog(text, LogLevel.WARN);
    }

    @Deprecated
    public void info(String text){
        invokeLog(text, LogLevel.INFO);
    }

    public void setDebugLogging(boolean isLogging){
        debug = isLogging;
    }

    @Deprecated
    public void debug(String text){
        invokeLog(text, LogLevel.DEBUG);
    }

    @Deprecated
    private void invokeLog(String text, LogLevel logLevel){
        if(!executor.isShutdown()) {
            executor.schedule(() -> logQueue.add(new LogData(text, logLevel, false)), 5, TimeUnit.MILLISECONDS);
        }
    }

    private void log(String text, @NotNull LogLevel logLevel){
        if(!isGLFWInit){
            if(!GLFW.glfwInit()){
                throw new IllegalStateException("glfw init error");
            }
            isGLFWInit = true;
        }
        try {
            EventQueue.invokeAndWait(() -> {
                jTextField.setEditable(true);
                Color color = null;
                String level = "N/A";
                switch (logLevel){
                    case INFO -> {color = Color.GREEN; level = "INFO";}

                    case WARN -> {color = Color.YELLOW; level = "WARN";}

                    case FATAL -> {color = Color.RED; level = "CRIT";}

                    case DEBUG -> {color = Color.CYAN; level = "DEBUG";}
                }
                /*
                if(jTextField.getDocument().getLength() > 20000) {
                    try {
                        String content = jTextField.getDocument().getText(0, jTextField.getDocument().getLength());
                        int lastLineBreak = content.lastIndexOf('\n');
                        jTextField.getDocument().remove(lastLineBreak, jTextField.getDocument().getLength() - lastLineBreak);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }

                 */


                if(logLevel != LogLevel.DEBUG){
                    appendToPane(String.format("[%s] [%s] %s", new DecimalFormat("##.###").format(GLFW.glfwGetTime()) , level, text), color);
                }else {
                    if(debug){
                        appendToPane(String.format("[%s] [%s] %s", new DecimalFormat("##.###").format(GLFW.glfwGetTime()) , level, text), color);
                    }
                }
                jTextField.setEditable(false);

                //jTextField.append(String.format("\n [%s] [%s]: %s", GLFW.glfwGetTime() ,new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()), text));
                /*
                if(jTextField.getDocument().getLength() > 5000){
                    jTextField.setText(null);
                }

                 */
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void clearConsole(){
        jTextField.setText(null);
    }

    public void closeConsole(){
        EventQueue.invokeLater(() -> {
            WindowEvent wev = new WindowEvent(jFrame, WindowEvent.WINDOW_CLOSING);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);

            // this will hide and dispose the frame, so that the application quits by
            // itself if there is nothing else around.
            jFrame.setVisible(false);
            jFrame.dispose();
            stopLogging();
        });
    }

    private void showOnScreen( int screen, JFrame frame )
    {

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gd = ge.getScreenDevices();
        int width = 0, height = 0;
        if( screen > -1 && screen < gd.length ) {
            width = gd[screen].getDefaultConfiguration().getBounds().width;
            height = gd[screen].getDefaultConfiguration().getBounds().height;
            frame.setLocation(
                    ((width / 2) - (frame.getSize().width / 2)) + gd[screen].getDefaultConfiguration().getBounds().x,
                    ((height / 2) - (frame.getSize().height / 2)) + gd[screen].getDefaultConfiguration().getBounds().y
            );
            //GLFW.glfwFocusWindow(Game.getDisplayManager().getWindow());

        } else {
            logger.warn("Second monitor for debug console not found, displaying in current...");
        }
        frame.setVisible(true);
        isWindowShowed = true;
    }


    private void appendToPane(String msg, Color c)
    {
        MutableAttributeSet sc = new SimpleAttributeSet();
        sc.addAttribute(StyleConstants.Foreground, c);

        sc.addAttribute(StyleConstants.FontFamily, "Console");
        sc.addAttribute(StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        StyleConstants.setFontSize(sc, 15);

        jTextField.setCharacterAttributes(sc, false);
        jTextField.replaceSelection(msg + "\n");
    }
}
