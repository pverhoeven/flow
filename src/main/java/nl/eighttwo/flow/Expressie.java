package nl.eighttwo.flow;

@FunctionalInterface
public interface Expressie<T> {
    boolean evalueer(T context);
}

