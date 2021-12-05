declare var game: Game; 
declare function setTimeout(time: string, callback : string);

export interface Console {
    log(...data: any[]): void;
    info(...data: any[]) : void;
    warn(...data: any[]) : void;
    crit(...data: any[]) : void;
    debug(...data: any[]) : void;
    switchDebugConsole() : void;
}

interface VectorMovement {
    getVector: Vector3f;
    setVector(data: Vector3f) : void;
}

export interface Player extends VectorMovement {
    getName(): string;
    getCamera() : Camera;
}

export interface Entity extends VectorMovement {
    getEntityName: string;
    deleteEntity: void;
}

export interface Sound {
    getName: string;
    getID: number;
}

export interface Event {
    getName: string;
    getFunctionName: string;
}

export interface Callbacks {
    setKeyCallback(data: string);
    setWindowResizedCallback(data: string);
}

export interface Camera {
    setFOV(data: number);
    setYaw(data: number);
    setRoll(data: number);
    setPitch(data: number);
    getYaw : number;
    getRoll : number;
    getPitch : number;
    getFOV: number;
}

export interface Audio {
    play(data: string) : number;
    playVector(data: Vector3f) : number;
    stopMusic(data: number) : void;
    getPlayedSounds() : Sound[];
}

export interface Vector3f {
    x: number;
    y: number;
    z: number;
}

export interface Networking {
    /**
     * 
     * @param host host
     * @param port port
     * 
     * @returns return 0 if was error excepted, to check error {@link getConnectError}
     */
    connect(host: string, port: number) : number;
    disconnect(id : number) : void;
    isConnected(id : number) : boolean;
    sendMessage(msg : string) : void;
    recvMessage() : string;
    getConnectError(id: number) : string;
}


export interface Tasks {
    /**
     * @param task function name
     * @returns task id
     */
    runAsync(task : string) : number;
    /**
     * 
     * @param task function name
     * @param delay delay of start
     * @returns task id 
     */
    runAsyncDelayed(task : string, delay : number) : number;

    /**
     * 
     * @param id task id
     * 
     * @returns return true if task is still running
     */
    isTaskRunning(id : number) : boolean;

    runRepeatedTask(task : string, delay : number, period : number);
}

export interface FileIO {
    openFile(name : string) : number;
    writeToFile(context : string): number;
    flushFile(file : number) : void;
    readAllFile(file : number) : string;
    readLineFile(file : number) : string;
    hasNextLine(file : number) : boolean;
    closeFile(file : number) : void;
}

export interface Hardware {
    getGPU() : string;
    getCPU() : string;
    getRam() : string;
    getRamSizeGB() : number;
    getRamSizeMB() : number;
    getUsedRam() : number;
}

export interface Settings {
    getFPSLimit() : number;
    setFPSLimit() : number;
}

export interface Window {
    setFullScreen() : void;
    isFullScreened() : boolean;
    getWindowWidth() : number;
    getWindowHeight() : number;
    sendWindowFocusRequest() : void;
    hideWindow() : void;
    showWindow() : void;
    setWindowTitle() : void;
    getWindowTitle() : void;
    resizeWindow(width : number, height : number) : void;
}

export interface Chat {
    isAvailable() : boolean;
    sendMessage(data : string) : void;
    sendRawMessage(data : string) : void;
    sendMessageColour(data : string, colour : Vector3f) : void;
    sendRawMessageColour(data : string, colour : Vector3f) : void;
    tellMessage(data : string, player : string) : boolean;
    setChatCallback(functionName: string) : void;
}

export interface Math {
    randomNumber(rangeFrom : number, rangeTo: number) : number;
    randomNumberSeed(rangeFrom : number, rangeTo : number, seed : number) : number;
    sin(double : number) : number;
    cos(double : number) : number;
    toRadians(double : number) : number;
    sqrt(double : number) : number;
    log10(double : number) : number;
    log(double : number) : number;
    max(min : number, max : number) : number;
    min(min : number, max : number) : number;
    fma(arg1 : number, arg2 : number) : number;
    abs(arg : number) : number;
}



export interface Game {
    getGameTime() : number;
    getVersion(): string;
    getInterpretterEngine(): string;
    getScriptNativeAccessVersion(): string;
    getName(): string;
    getEntityById(): Entity;
    addEntity(data: Entity) : void;
    addEvent(eventType: string, listenerName: string);
    getPlayer(): void;
    console: Console;
    getEvents(): Event[];
    setEvent(data: string, functionName: string);
    audio : Audio;
    callbacks: Callbacks;
    exitGame(): void;
    hardware : Hardware;
    network : Networking;
    tasks : Tasks;
    settings : Settings;
    math : Math;
    window : Window;
}
