package nl.eighttwo.flow;

public class Context<T> {
    private T context;

    public Context(T context) {
        this.context = context;
    }

    public T get() {
        return context;
    }
}
