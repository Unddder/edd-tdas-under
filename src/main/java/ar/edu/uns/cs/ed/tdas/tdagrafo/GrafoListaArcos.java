package ar.edu.uns.cs.ed.tdas.tdagrafo;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidEdgeException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidVertexException;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;


public class GrafoListaArcos<V,E> implements Graph<V,E> {

    protected  class Vertice<V,E> implements Vertex<V> {
        private Position<Vertice<V,E>> posicionEnListaVertices;
        private V rotulo;
        private PositionList<Arco<V,E>> adyacentes;

        

        public Vertice(V rotulo){
            this.rotulo = rotulo;
            adyacentes = new ListaDoblementeEnlazada<Arco<V,E>>();
        }

        //setters y getters
        public V element(){return rotulo;}
        public void setRotulo(V nuevoRotulo){rotulo = nuevoRotulo;}
        public void setPosicionEnListaVertices(Position<Vertice<V,E>> p){posicionEnListaVertices = p;}
        public void setAdyacentes(PositionList<Arco<V,E>> pl){adyacentes = pl;}
        public PositionList<Arco<V,E>> getAdyacentes(){return adyacentes;}
        public Position<Vertice<V,E>> getPosicionEnListaVertices(){return posicionEnListaVertices;}
   }
    protected class Arco<V,E> implements Edge<E>{
        private Position<Arco<V,E>> posicionEnListaArco;
        private Vertice<V,E> v1,v2;
        private E rotulo;
        private Position<Arco<V,E>> posicionEnlv1, posicionEnlv2;

        public Arco(E rotulo, Vertice<V,E> v1, Vertice<V,E> v2){
            this.rotulo = rotulo;
            this.v1 = v1;
            this.v2 = v2;
        }

        //setters y getters
        public E element(){return rotulo;}
        public Vertice<V,E> getV1(){return v1;}
        public Vertice<V,E> getV2(){return v2;}
        public Position<Arco<V,E>> getPosicionEnListaArco(){return posicionEnListaArco;}
        public Position<Arco<V,E>> getPosicionEnlv1(){return posicionEnlv1;} 
        public Position<Arco<V,E>> getPosicionEnlv2(){return posicionEnlv2;}
        public void setV1(Vertice<V,E> v){v1 = v;}
        public void setV2(Vertice<V,E> v){v2 = v;}
        public void setPosicionEnlistaArco(Position<Arco<V,E>> p){posicionEnListaArco = p;}
        public void setPosicionEnlv1(Position<Arco<V,E>> p){posicionEnlv1 = p;}
        public void setPosicionEnlv2(Position<Arco<V,E>> p){posicionEnlv2 = p;}
        public void setRotulo(E rotulo){this.rotulo = rotulo;}
    }

    private Vertice<V,E> checkVertex(Vertex<V> v){
        if(v == null)
            throw new InvalidVertexException("Vertice nulo");

        try{
            return (Vertice<V,E>) v;
        }
        catch(ClassCastException e){
            throw new InvalidVertexException("Vertice invalido");
        }
    }

    private Arco<V,E> checkEdge(Edge<E> e){
        if(e == null)
            throw new InvalidEdgeException("Arco nulo");

        try{
            return (Arco<V,E>) e;
        }
        catch(ClassCastException ex){
            throw new InvalidEdgeException("Arco invalido");
        }
    }

    protected PositionList<Vertice<V,E>> nodos;
    protected PositionList<Arco<V,E>> arcos;

    public GrafoListaArcos(){
        nodos = new ListaDoblementeEnlazada<Vertice<V,E>>();
        arcos = new ListaDoblementeEnlazada<Arco<V,E>>();   
        }

    public Iterable<Vertex<V>> vertices(){
        PositionList<Vertex<V>> lista = new ListaDoblementeEnlazada<Vertex<V>>();
        for(Vertex<V> v: nodos){
            lista.addLast(v);
        }
        return lista;
    }

    public Iterable<Edge<E>> edges(){
        PositionList<Edge<E>> lista = new ListaDoblementeEnlazada<Edge<E>>();
        for(Edge<E> e: arcos){
            lista.addLast(e);
        }
        return lista;
    }

    public Iterable<Edge<E>> incidentEdges(Vertex<V> v) {
        PositionList<Edge<E>> lista = new ListaDoblementeEnlazada<Edge<E>>();
        Vertice<V,E> vert = checkVertex(v);
        for( Edge<E> e : vert.getAdyacentes() )
            lista.addLast(e);
        return lista;
    }

    public Vertex<V> insertVertex(V x){
        Vertice<V,E> v = new Vertice<V,E>(x);
        nodos.addLast(v);
        v.setPosicionEnListaVertices(nodos.last());
        return v;
    }

    public Edge<E> insertEdge(Vertex<V> v, Vertex<V> w, E x){
        Vertice<V,E> vv =  checkVertex(v);
        Vertice<V,E> ww = checkVertex(w);
        Arco<V,E> arco = new Arco<V,E>(x, vv, ww);
        vv.getAdyacentes().addLast(arco);
        arco.setPosicionEnlv1(vv.getAdyacentes().last());
        ww.getAdyacentes().addLast(arco);
        arco.setPosicionEnlv2(ww.getAdyacentes().last());
        arcos.addLast(arco);
        arco.setPosicionEnlistaArco(arcos.last());
        return arco; 
    }

    public E removeEdge(Edge<E> e){
        Arco<V,E> ee = checkEdge(e);
        Vertice<V,E> v1 = ee.getV1(); Vertice<V,E> v2 = ee.getV2();
        v1.getAdyacentes().remove(ee.getPosicionEnlv1());
        v2.getAdyacentes().remove(ee.getPosicionEnlv2());
        Position<Arco<V,E>> pee= ee.getPosicionEnListaArco();
        return arcos.remove(pee).element();
    }

    public Vertex<V> opposite(Vertex<V> v, Edge<E> e){
        Arco<V,E> ee = checkEdge(e);
        Vertice<V,E> vv = checkVertex(v);
        if(ee.getV1() != vv && ee.getV2() != vv) throw new InvalidEdgeException("e no es un arco incidente de v");
        if(ee.getV1() == vv) return ee.getV2();
        return ee.getV1(); 
    }

    public Vertex<V> [] endvertices(Edge<E> e){
        Arco<V,E> ee = checkEdge(e);
        Vertex<V>[] arr = (Vertex<V>[]) new Vertice[2];
        arr[0] = ee.getV1();
        arr[1] = ee.getV2();
        return arr;
    }


    public V replace(Vertex<V> v, V x){
        Vertice<V,E> vv = checkVertex(v);
        V temp = vv.element();
        vv.setRotulo(x);
        return temp;
    }
    public E replace(Edge<E> e, E x){
        Arco<V,E> ee = checkEdge(e);
        E temp = ee.element();
        ee.setRotulo(x);
        return temp;
    }
    public boolean areAdjacent(Vertex<V> v,Vertex<V> w){
        Vertice<V,E> vv = checkVertex(v);
        Vertice<V,E> ww = checkVertex(w);
        for(Arco<V,E> e : vv.getAdyacentes())
            if(e.getV1() == ww || e.getV2() == ww)
                return true;
        return false;
    }

    public V removeVertex(Vertex<V> v) {
        Vertice<V,E> vv = checkVertex(v);
        Position<Vertice<V,E>> pos = vv.getPosicionEnListaVertices();
        for(Arco<V,E> e : vv.getAdyacentes())
            removeEdge(e);
        return nodos.remove(pos).element();
    }
} 

