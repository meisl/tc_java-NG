package plugins;


public abstract class AbstractTypedConstant {
    private final int intValue;
    protected AbstractTypedConstant(int intValue) {
        this.intValue = intValue;
    }
    public int intValue() {
        return this.intValue;
    }
}
