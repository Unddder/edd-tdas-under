package ar.edu.uns.cs.ed.tdas.tdagrafo;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidEdgeException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidVertexException;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdamapeo.TDAMapeo;

public class GrafoListaArcosDirigido<V,E> implements GraphD<V,E> {
    
    protected  class Vertice<V,E> extends TDAMapeo<Object,Object> implements Vertex<V> {
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
        private Vertice<V,E> origen,destino;
        private E rotulo;
        private Position<Arco<V,E>> posicionEnlOrigen, posicionEnlDestino;

        public Arco(E rotulo, Vertice<V,E> v1, Vertice<V,E> v2){
            this.rotulo = rotulo;
            origen = v1;
            destino = v2;
        }

        //setters y getters
        public E element(){return rotulo;}
        public Vertice<V,E> getOrigen(){return origen;}
        public Vertice<V,E> getDestino(){return destino;}
        public Position<Arco<V,E>> getPosicionEnListaArco(){return posicionEnListaArco;}
        public Position<Arco<V,E>> getPosicionEnlv1(){return posicionEnlOrigen;} 
        public Position<Arco<V,E>> getPosicionEnlv2(){return posicionEnlDestino;}
        public void setOrigen(Vertice<V,E> v){origen = v;}
        public void setDestino(Vertice<V,E> v){destino = v;}
        public void setPosicionEnlistaArco(Position<Arco<V,E>> p){posicionEnListaArco = p;}
        public void setPosicionEnlOrigen(Position<Arco<V,E>> p){posicionEnlOrigen = p;}
        public void setPosicionEnlDestino(Position<Arco<V,E>> p){posicionEnlDestino = p;}
        public void setRotulo(E rotulo){this.rotulo = rotulo;}
    }

    private Vertice<V,E> checkVertex(Vertex<V> v){
        if(v == null) throw new InvalidVertexException("Vertice nulo");
        try{return (Vertice<V,E>) v;}
        catch(ClassCastException e){throw new InvalidVertexException("Vertice invalido");}
    }

    private Arco<V,E> checkEdge(Edge<E> e){
        if(e == null) throw new InvalidEdgeException("Arco nulo");
        try{return (Arco<V,E>) e;}
        catch(ClassCastException ex){throw new InvalidEdgeException("Arco invalido");}
    }

    protected PositionList<Vertice<V,E>> nodos;
    protected PositionList<Arco<V,E>> arcos;

    public GrafoListaArcosDirigido(){
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
        arco.setPosicionEnlOrigen(vv.getAdyacentes().last());
        ww.getAdyacentes().addLast(arco);
        arco.setPosicionEnlDestino(ww.getAdyacentes().last());
        arcos.addLast(arco);
        arco.setPosicionEnlistaArco(arcos.last());
        return arco; 
    }

    public E removeEdge(Edge<E> e){
        Arco<V,E> ee = checkEdge(e);
        Vertice<V,E> vOrigen = ee.getOrigen(); Vertice<V,E> vDestino = ee.getDestino();
        vOrigen.getAdyacentes().remove(ee.getPosicionEnlv1());
        vDestino.getAdyacentes().remove(ee.getPosicionEnlv2());
        Position<Arco<V,E>> pee= ee.getPosicionEnListaArco();
        return arcos.remove(pee).element();
    }

    public Vertex<V> opposite(Vertex<V> v, Edge<E> e){
        Arco<V,E> ee = checkEdge(e);
        Vertice<V,E> vv = checkVertex(v);
        if(ee.getOrigen() != vv && ee.getDestino() != vv) throw new InvalidEdgeException("e no es un arco incidente de v");
        if(ee.getOrigen() == vv) return ee.getDestino();
        return ee.getOrigen(); 
    }

    public Vertex<V> [] endvertices(Edge<E> e){
        Arco<V,E> ee = checkEdge(e);
        Vertex<V>[] arr = (Vertex<V>[]) new Vertice[2];
        arr[0] = ee.getOrigen();
        arr[1] = ee.getDestino();
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
            if(e.getOrigen() == ww || e.getDestino() == ww)
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

    public Iterable<Edge<E>> succesorEdges(Vertex<V> v){
        Vertice<V,E> vv = checkVertex(v);
        PositionList<Edge<E>> pl = new ListaDoblementeEnlazada<Edge<E>>();
        for(Edge<E> e : incidentEdges(v)){
            Arco<V,E> ee = checkEdge(e);
            if(ee.getOrigen() == vv)
                pl.addLast(e);
        }
        return pl;
    }
}
