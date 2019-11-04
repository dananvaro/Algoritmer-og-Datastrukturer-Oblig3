package no.oslomet.cs.algdat.Oblig3;

////////////////// ObligSBinTre /////////////////////////////////

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    public boolean fjern(T verdi) {
        //treet tar ike imot null verdier
        if (verdi == null) return false;

        Node<T> p = rot;
        Node <T> q = null;

        //prøver å finne verdien
        while (p != null)
        {
            //sammenlikner verdiene
            int cmp = comp.compare(verdi,p.verdi);
            //går til venstre dersom vi får mindre enn 0
            if (cmp < 0) { q = p; p = p.venstre; }
            //går til venstre dersom vi får mer enn 0
            else if (cmp > 0) { q = p; p = p.høyre; }
            //hvis ingen av de så har vi funnet verdien
            else break;
        }
        //dersom p er null så har vi ikke funnet verdien
        if (p == null) return false;

        //dersom noden har 0 eller 1 barn
        if (p.venstre == null || p.høyre == null)
        {
            //b er barnet til p
            Node b = null;

            if(p.venstre!=null){
                b = p.venstre;
            }
            else if(p.høyre!=null) {
                b = p.høyre;
            }
            //dersom det er rot så kommer vi inn her
            if (p == rot){
                System.out.println("i roten her");
                if(p.høyre==null && p.venstre==null){
                    rot = null;
                }else {
                    rot = b;
                    b.forelder = null;
                }
            }
            else if (p == q.venstre){
//                System.out.println(q.verdi + " " + b.verdi);
                q.venstre = b;
                p.forelder = q;
            }
            else {
                if(p.høyre==null && p.venstre==null){
                    p.forelder=null;
                }else{
                    b.forelder = q;
                }
                q.høyre = b;

            }
        }
        //dersom noden har 2 barn
        else {
            //s = forelder til r og r = nesteinorden av p
            Node<T> s = p, r = p.høyre;
            r = nesteInorden(p);
            s = r.forelder;


            //kopierer verdien slik at vi kan fjerne den nederste
            p.verdi = r.verdi;

            if (s != p){
                if(r.høyre == null){
                    r.forelder = null;
                }
                else {
                    r.høyre.forelder = s;
                }
                s.venstre = r.høyre;
            }
            else {
                //dersom p er roten
                if(r.forelder==rot){
                    if(r.høyre!=null){
                        r.høyre.forelder = r.forelder;
                    }else {
                        r.forelder=null;
                    }
                }
                else {
                    //flytter pekeren
                    r.forelder = s;
                }
                //flytter pekeren
                s.høyre = r.høyre;

            }
        }

        //minker antall
        antall--;
        return true;
    }

    public int fjernAlle(T verdi)
    {
        //dersom antall lik 0
        if (antall == 0){
            return 0;
        }
        //finner antallet av en verdi
        int stopp = antall(verdi);
        //hjelpeverdi som teller antall fjernet
        int antall = 0;
        if (stopp == 0) {
            return antall;
        }
        //fjerner ved hjelp av fjern metoden
        for(int i =0; i < stopp; i++){
            fjern(verdi);
            antall++;
        }
        //returnerer antallt som ble fjernet
        return antall;
    }

    @Override
    public int antall()
    {
    return antall;
    }

    public int antall(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    @Override
    public boolean tom()
    {
    return antall == 0;
    }

    @Override
    public void nullstill(){
        //dersom antall er 0
        if(antall == 0){
            return;
        }
        Node p = rot;
        //går igjennom alle erdiene i treet
        int stopp = antall;
        // går til den verdien som er lengst til venstre
        // slik at vi kan bruke nesteinorden metoden
        while (p.venstre != null){
            p = p.venstre;
        }
        while(stopp!=0){
            //fjerner verdi
            fjern((T)p.verdi);
            p.verdi = null;
            //går til neste verdi
            p =nesteInorden(p);
            stopp--;

        }
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
        if(tom())return "[]";

        Node<T> n = rot;
        ArrayDeque<Node<T>> stakk = new ArrayDeque<>();
        StringJoiner l = new StringJoiner(", ", "[", "]");

        while(n!= null || stakk.size()>0){
           while(n != null){
               stakk.addFirst(n);
               n = n.høyre;
           }
           n = stakk.pop();
           l.add(n.verdi.toString());
           n = n.venstre;
        }

        return l.toString();
    }

    public String høyreGren() {
        //dersom antall er lik 0
        if(antall==0){
            return "[]";
        }
        StringBuilder ut = new StringBuilder();
        //vi starter med rotverdien
        Node p = rot;
        ut.append("["+rot);
        //vi fortsetter helt il vi kommer til en bladnode
        while (p.høyre!=null || p.venstre!=null) {
            //vi går til høyre dersom det er mulig
            if(p.høyre!=null){
                p = p.høyre;
                ut.append(", "+p);
            }
            // ellers går vi til venstre
            else {
                p = p.venstre;
                ut.append(", "+p);
            }
        }
        //returnerer veien
        ut.append("]");
        return ut.toString();
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
