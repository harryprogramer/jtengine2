package jte2.engine.sdk.leveleditor.dashboard.commands.models;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import jte2.engine.sdk.leveleditor.Editor;
import jte2.engine.sdk.leveleditor.dashboard.ConsoleSession;
import jte2.engine.sdk.leveleditor.dashboard.args.Argument;
import jte2.engine.sdk.leveleditor.dashboard.args.parser.LineArgument;
import jte2.engine.twilight.material.Colour;
import jte2.engine.twilight.viewport.Node;

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

    @Override
    public String getHelpDescription() {
        return null;
    }

    private void errorEcho(String text, @NotNull ConsoleSession session){
        logger.error(text);
        session.writeLine(text, Colour.RED);
    }
}
