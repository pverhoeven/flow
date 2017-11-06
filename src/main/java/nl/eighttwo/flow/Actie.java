package nl.eighttwo.flow;

public abstract class Actie<T> implements Stap<T> {
    private Stap<T> volgende;

    @Override
    public Stap<T> voerUit(Context<T> context) {
        voerUit(context.get());
        return this.volgende;
    }

    public <S extends Stap<T>> S en(final S stap) {
        this.volgende = stap;
        return stap;
    }

    public <S extends Beslissing<?,T>> S en(final S stap) {
        this.volgende = stap;
        return stap;
    }

    public abstract void voerUit(T context);
}
