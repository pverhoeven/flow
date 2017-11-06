package nl.eighttwo.flow;

public abstract class Flow<T> {
    private Context<T> context;

    public Flow(T context) {
        this.context = new Context<>(context);
    }

    public void start() {
        Stap<T> stap = initieel();
        while(stap != null) {
            stap = stap.voerUit(this.context);
        }
    }

    abstract Actie<T> initieel();
}
