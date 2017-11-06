package nl.eighttwo.flow;

@FunctionalInterface
public interface Stap<T> {
    Stap<T> voerUit(Context<T> context);
}
