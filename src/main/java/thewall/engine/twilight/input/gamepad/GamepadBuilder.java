package thewall.engine.twilight.input.gamepad;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import thewall.engine.twilight.errors.InitializationException;

public abstract class GamepadBuilder implements Gamepad {
    private final static short MAX_CONTROLLERS_SUPPORTED = 16;
    private GamepadAxis lastAxis = createAxis(0, 0);

    private final GamepadNumber number;
    private String name;

    public GamepadBuilder(int gamepadNumber) {
        if(gamepadNumber == 0){
            throw new InitializationException("Controller is null");
        }

        if(gamepadNumber > MAX_CONTROLLERS_SUPPORTED){
            throw new IndexOutOfBoundsException("Max supported controller this same time is " + MAX_CONTROLLERS_SUPPORTED);
        }

        this.number = GamepadNumber.numberToGamepadNumber(gamepadNumber);
    }

    @Override
    public GamepadNumber getNumber() {
        return this.number;
    }

    @Override
    public String getGamepadName() {
        return "Controller #" + number.getRawNumber();
    }

    @Override
    public GamepadAxis getAxis1() {
        if(isGamepadStillPresent()) {
            GamepadAxis axis = getGamepadAxis().getAxis1();
            if (axis == null || (axis.getX() == 0 && axis.getY() == 0)) {
                return lastAxis == null ? createAxis(0, 0) : lastAxis;
            }
            lastAxis = axis;
            return axis;
        }else {
            return lastAxis;
        }
    }

    @Override
    public GamepadAxis getAxis2() {
        if(isGamepadStillPresent()) {
            GamepadAxis axis = getGamepadAxis().getAxis2();
            if (axis == null || (axis.getX() == 0 && axis.getY() == 0)) {
                return lastAxis == null ? createAxis(0, 0) : lastAxis;
            }
            lastAxis = axis;
            return axis;
        }else {
            return lastAxis;
        }
    }

    @Override
    public GamepadAxis getAxis3() {
        if(isGamepadStillPresent()) {
            GamepadAxis axis = getGamepadAxis().getAxis3();
            if (axis == null || (axis.getX() == 0 && axis.getY() == 0)) {
                return lastAxis == null ? createAxis(0, 0) : lastAxis;
            }
            lastAxis = axis;
            return axis;
        }else {
            return lastAxis;
        }
    }

    @Contract("_, _ -> new")
    protected static @NotNull GamepadAxis createAxis(int x, int y){
        return createAxis((float) x, (float) y);
    }

    @Contract("_, _ -> new")
    protected static @NotNull GamepadAxis createAxis(float x, float y){
        return new AxisBuilder(x, y);
    }

    protected static @NotNull GamepadAxisPack createAxisPack(GamepadAxis axis1, GamepadAxis axis2, GamepadAxis axis3){
        return new AxisPackBuilder(axis1, axis2, axis3);
    }

    private record AxisPackBuilder(GamepadAxis axis1,
                                   GamepadAxis axis2,
                                   GamepadAxis axis3) implements GamepadAxisPack {

        @Override
        public GamepadAxis getAxis1() {
            return axis1;
        }

        @Override
        public GamepadAxis getAxis2() {
            return axis2;
        }

        @Override
        public GamepadAxis getAxis3() {
            return axis3;
        }
    }

    private static final class AxisBuilder implements GamepadAxis {
        private final float x, y;

        public AxisBuilder(float x, float y){
            this.x = x;
            this.y = y;
        }

        public AxisBuilder(int x, int y){
            this.x = (float) x;
            this.y = (float) y;
        }

        @Override
        public float getX() {
            return x;
        }

        @Override
        public float getY() {
            return y;
        }
    }

    protected abstract GamepadAxisPack getGamepadAxis();
}
