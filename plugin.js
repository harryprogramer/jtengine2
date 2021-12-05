import * as twall from './twallscript';

const pluginInfo =  {"name": "Test Script", "version": "1.0"};

function log(data){
    twall.game.console.log(data);
}

function keyCallback(key, state){
    log("Key " + key + " state: " + state);
}

function onEnable(){
    twall.game.console.info("Starting...");
    log("Hardware info: " + twall.game.hardware.getCPU());
    twall.game.tasks.runRepeatedTask("loop", 0, 50);
    twall.game.callbacks.setKeyCallback("keyCallback");
}

function onDisable(){
    twall.game.console.switchDebugConsole();
    twall.game.exitGame();
}


function loop(){
    if(!twall.game.window.isFullScreened()){
        log("Window info: X: " + twall.game.window.getWindowWidth() + ", Y: " + twall.game.window.getWindowHeight());
    }

    log(twall.game.math.sqrt(360.0));
    
}