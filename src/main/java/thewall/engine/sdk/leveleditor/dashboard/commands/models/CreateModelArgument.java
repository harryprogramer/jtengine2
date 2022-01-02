package thewall.engine.sdk.leveleditor.dashboard.commands.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import thewall.engine.sdk.leveleditor.Editor;
import thewall.engine.sdk.leveleditor.dashboard.ConsoleSession;
import thewall.engine.sdk.leveleditor.dashboard.args.Argument;
import thewall.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import thewall.engine.twilight.material.Colour;
import thewall.engine.twilight.viewport.Node;

public class CreateModelArgument extends Argument {
    private final static Logger logger = LogManager.getLogger(CreateModelArgument.class);
    private final Editor editor;


    public CreateModelArgument(Editor editor) {
        this.editor = editor;
    }

    @Override
    public void handle(LineArgument arg, ConsoleSession session) {
        if(arg.getArgumentsSize() != 1){
            errorEcho("Syntax error, Excepted 1 argument (name)", session);
            return;
        }

        if(editor.getScene() != null){
            session.writeLine("Another model [" + editor.getScene().getName() + "] is already selected.", Colour.YELLOW);
            return;
        }

        Node node = new Node();
        node.setName(arg.getArguments()[0]);
        editor.setScene(node);
    }

    private void errorEcho(String text, @NotNull ConsoleSession session){
        logger.error(text);
        session.writeLine(text, Colour.RED);
    }
}
