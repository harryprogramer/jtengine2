package thewall.engine.twilight.input.gamepad;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;

public enum GamepadNumber {
    JOYSTICK_1(1),
    JOYSTICK_2(2),
    JOYSTICK_3(3),
    JOYSTICK_4(4),
    JOYSTICK_5(5),
    JOYSTICK_6(6),
    JOYSTICK_7(7),
    JOYSTICK_8(8),
    JOYSTICK_9(9),
    JOYSTICK_10(10),
    JOYSTICK_11(11),
    JOYSTICK_12(12),
    JOYSTICK_13(13),
    JOYSTICK_14(14),
    JOYSTICK_15(15),
    JOYSTICK_16(16)
    ;

    private final static Logger logger = LogManager.getLogger(GamepadNumber.class);
    private final int number;

    GamepadNumber(int number){
        this.number = number;
    }

    public final int getRawNumber(){
        return number;
    }

    @Contract(pure = true)
    public static GamepadNumber numberToGamepadNumber(int number){
        switch (number){
            case 1 -> {
                return GamepadNumber.JOYSTICK_1;
            }

            case 2 -> {
                return GamepadNumber.JOYSTICK_2;
            }

            case 3 -> {
                return GamepadNumber.JOYSTICK_3;
            }

            case 4 -> {
                return GamepadNumber.JOYSTICK_4;
            }

            case 5 -> {
                return GamepadNumber.JOYSTICK_5;
            }

            case 6 -> {
                return GamepadNumber.JOYSTICK_6;
            }

            case 7 -> {
                return GamepadNumber.JOYSTICK_7;
            }

            case 8 -> {
                return GamepadNumber.JOYSTICK_8;
            }

            case 9 -> {
                return GamepadNumber.JOYSTICK_9;
            }

            case 10 -> {
                return GamepadNumber.JOYSTICK_10;
            }

            case 11 -> {
                return GamepadNumber.JOYSTICK_11;
            }

            case 12 -> {
                return GamepadNumber.JOYSTICK_12;
            }

            case 13 -> {
                return GamepadNumber.JOYSTICK_13;
            }

            case 14 -> {
                return GamepadNumber.JOYSTICK_14;
            }

            case 15 -> {
                return GamepadNumber.JOYSTICK_15;
            }

            case 16 -> {
                return GamepadNumber.JOYSTICK_16;
            }

            default -> {
                logger.warn("Unknown or invalid gamepad number [" + number + "]");
                return null;
            }
        }
    }
}
