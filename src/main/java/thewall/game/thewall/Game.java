package thewall.game.thewall;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thewall.engine.twilight.TwilightApp;
import thewall.game.thewall.input.KeyboardCallback;

public class Game {
    @Getter
    private final static TheWall game = new TheWall();

    private final static Logger logger = LogManager.getLogger(Game.class);

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        TwilightApp.init();
        try{
            //game.getDebugConsole().showConsole();
            TwilightApp.startApp(game);
        }catch (Exception e){
            logger.fatal("Cannot start game, fatal runtime error", e);
            System.exit(1);
            return;
        }
        long endTime = System.currentTimeMillis();
        logger.info("Game started in " + (endTime - startTime) / 1000.0 + "s.");
        //game.input().getMouse().disableCursor();
        game.enableAutoWindowResizable();
        game.setKeyboardCallback(new KeyboardCallback());
        game.setFPSLimit(300);
        game.showImmediateGUI();
    }
}
