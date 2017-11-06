package nl.eighttwo.flow;

import java.util.ArrayList;
import java.util.List;

public abstract class Beslissing<V, T> implements Stap<T> {

    public static class Als<V, T> {
        private Expressie<V> expressie;
        private Stap<T> volgende;

        public Als(Expressie<V> expressie) {
            this.expressie = expressie;
        }

        public <S extends Stap<T>> S dan(S stap) {
            this.volgende = stap;
            return stap;
        }

        public Stap<T> getVolgendeStap() {
            return volgende;
        }

        public boolean isGelijkAan(V waarde) {
            return this.expressie.evalueer(waarde);
        }
    }


    private List<Als<V,T>> controles = new ArrayList<>();

    @Override
    public Stap<T> voerUit(Context<T> context) {
        Stap<T> volgende = null;
        V waarde = evalueer(context.get());
        for(Als als : controles) {
            if(als.isGelijkAan(waarde)) {
                volgende = als.getVolgendeStap();
                break;
            }
        }
       return volgende;
    }

    public Als als(Expressie<V> expressie) {
        Als als = new Als(expressie);
        controles.add(als);
        return als;
    }

    public Als als(V waarde) {
        Als als = new Als(context -> context.equals(waarde));
        controles.add(als);
        return als;
    }


    public abstract V evalueer(T context);

}
