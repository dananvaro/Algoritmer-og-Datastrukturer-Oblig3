package no.oslomet.cs.algdat.Oblig3;

////////////////// ObligSBinTre /////////////////////////////////

import java.util.*;

public class ObligSBinTre<T> implements Beholder<T>
{
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder)
        {
            this.verdi = verdi;
            venstre = v; høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString(){ return "" + verdi;}

    } // class Node

    private Node<T> rot;
    private int antall;
    private int endringer;

    private final Comparator<? super T> comp;

    public ObligSBinTre(Comparator<? super T> c)
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    @Override
    public boolean leggInn(T verdi)
    {
        // Skjekker om verdien er lik null
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        // Lager noder av type Node der p er roten
        Node<T> p = rot;
        Node<T> q = null;
        // cmp er en hjelpevariabel
        int cmp = 0;

        while (p != null)
        {
            // Setter q lik roten som er p
            q = p;
            //Skjekker verdien sp vi vet hvor den skal ligge
            cmp = comp.compare(verdi,p.verdi);

            //Hvis cmp er midre enn 0 skal den ligge på venstre side
            if (cmp < 0){
            p = p.venstre;
            }
            //Hvis cmp er storre enn er den lik hoyre
            else {
            p = p.høyre;
            }

        }

        //Lager en ny metode og setter den lik p
        p = new Node<T>(verdi,q);

        //Om q er lik null er p lik rot
        if (q == null){
            rot = p;
        }

        //Om cmp er mindre enn 0 er p lik ventstre
        else if (cmp < 0)
        {
            q.venstre = p;
        }

        //Ellers er den lik hoyre
        else {
            q.høyre = p;
        }

        //Plusser antall
        antall++;
        return true;
    }

    @Override
    public boolean inneholder(T verdi)
    {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null)
        {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    @Override
    public boolean fjern(T verdi)
    { throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi)
    {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public int antall()
    {
    return antall;
    }

    public int antall(T verdi)
    {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public boolean tom()
    {
    return antall == 0;
    }

    @Override
    public void nullstill(){
    throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> nesteInorden(Node<T> p)
    {
        if (p.høyre != null)
    {
        p = p.høyre;
        while (p.venstre != null){
            p = p.venstre;
        }
    }
    else
    {
        while (p.forelder != null && p == p.forelder.høyre){
            p = p.forelder;
        }
        p = p.forelder;
    }
        return p;
    }


    @Override
    public String toString()
    {
        if (tom()) return "[]";
        StringJoiner s = new StringJoiner(", ", "[", "]");
        Node<T> p = rot;

        while (p.venstre != null) p = p.venstre;
        while (p != null) {
            s.add(p.verdi.toString());
            p = nesteInorden(p);
        }
        return s.toString();
    }

    public String omvendtString()
    {
        if(tom()) return "[]";
        Node<T> p = rot;
        StringJoiner l = new StringJoiner(", ","[","]");
        ArrayDeque stakk = new ArrayDeque();
        while(p.venstre != null){
            p=p.venstre;
        }
        while(p != null){
            stakk.addFirst(p.verdi);
            p = nesteInorden(p);

        }
        for(Object c : stakk){
            l.add(stakk.pop().toString());
        }

        return  l.toString();

    }

    public String høyreGren()
    {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String lengstGren()
    {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String[] grener()
    {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String bladnodeverdier()
    {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public String postString()
    {
    throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public Iterator<T> iterator()
    {
    return new BladnodeIterator();
    }

    private class BladnodeIterator implements Iterator<T>
    {
        private Node<T> p = rot, q = null;
        private boolean removeOK = false;
        private int iteratorendringer = endringer;

    private BladnodeIterator()  // konstruktør
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public boolean hasNext()
    {
      return p != null;  // Denne skal ikke endres!
    }

    @Override
    public T next()
    {
      throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public void remove()
    {
        if (!removeOK) throw new IllegalStateException("Ulovlig kall på remove()");

        if (endringer != iteratorendringer) throw new ConcurrentModificationException("Treet er blitt endret");

        removeOK = false;

        Node<T> f = q.forelder;
        if (f == null) rot = null;
        else if (q == f.venstre) f.venstre = null;
        else f.høyre = null;

        antall--;
        endringer++;
        iteratorendringer++;
    }

  } // BladnodeIterator

} // ObligSBinTre
