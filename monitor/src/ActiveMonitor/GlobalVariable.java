package ActiveMonitor;

public abstract class GlobalVariable {
    public final String name;

    public GlobalVariable(String name) {
        this.name = name;
    }
    public abstract int getValue();
}
