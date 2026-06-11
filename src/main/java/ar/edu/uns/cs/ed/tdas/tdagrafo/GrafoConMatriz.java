package ar.edu.uns.cs.ed.tdas.tdagrafo;

import java.util.Iterator;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidEdgeException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidVertexException;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;

public class GrafoConMatriz<V,E> implements Graph<V,E> {
    protected  class Vertice<V> implements Vertex<V> {
        private Position<Vertex<V>> posicionEnListaVertices;
        private V rotulo;
        private int indice;

        public Vertice(V rotulo, int indice){
            this.rotulo = rotulo;
            this.indice = indice;
        }

        //setters y getters
        public V element(){return rotulo;}
        public void setRotulo(V nuevoRotulo){rotulo = nuevoRotulo;}
        public void setPosicionEnListaVertices(Position<Vertex<V>> p){posicionEnListaVertices = p;}
        public void setIndice(int i){indice = i;}
        public int getIndice(){return indice;}
        public Position<Vertex<V>> getPosicionEnListaVertices(){return posicionEnListaVertices;}
   }
    protected class Arco<V,E> implements Edge<E>{
        private Position<Edge<E>> posicionEnListaArco;
        private Vertice<V> v1,v2;
        private E rotulo;

        public Arco(E rotulo, Vertice<V> v1, Vertice<V> v2){
            this.rotulo = rotulo;
            this.v1 = v1;
            this.v2 = v2;
        }

        //setters y getters
        public E element(){return rotulo;}
        public Vertice<V> getV1(){return v1;}
        public Vertice<V> getV2(){return v2;}
        public Position<Edge<E>> getPosicionEnListaArco(){return posicionEnListaArco;}
        public void setV1(Vertice<V> v){v1 = v;}
        public void setV2(Vertice<V> v){v2 = v;}
        public void setPosicionEnlistaArco(Position<Edge<E>> p){posicionEnListaArco = p;}
        public void setRotulo(E rotulo){this.rotulo = rotulo;}
    }

    private Vertice<V> checkVertex(Vertex<V> v){
        if(v == null) throw new InvalidVertexException("Vertice nulo");
        try{return (Vertice<V>) v;}
        catch(ClassCastException e){throw new InvalidVertexException("Vertice invalido");}
    }

    private Arco<V,E> checkEdge(Edge<E> e){
        if(e == null) throw new InvalidEdgeException("Arco nulo");
        try{return (Arco<V,E>) e;}
        catch(ClassCastException ex){throw new InvalidEdgeException("Arco invalido");}
    }


    protected PositionList<Vertex<V>> vertices;
    protected PositionList<Edge<E>> arcos;
    protected Edge<E> [][] matriz;
    protected int cantidadVertices;


    private void agrandarMatriz(){
        Edge<E> [][] nuevo = (Edge<E>[][]) new Arco[matriz.length*2][matriz.length*2];
        for(int i=0; i<cantidadVertices; i++)
            for(int j=0; j<cantidadVertices; j++)
                nuevo[i][j] = matriz[i][j];
        matriz = nuevo;
    }
    public GrafoConMatriz(){ //10 por convención
        vertices = new ListaDoblementeEnlazada<Vertex<V>>();
        arcos = new ListaDoblementeEnlazada<Edge<E>>();
        matriz = (Edge<E> [][]) new Arco[10][10];
    }

    public Vertex<V> insertVertex(V x){
        if(cantidadVertices == matriz.length) agrandarMatriz();
        Vertice<V> vv = new Vertice<V>(x,cantidadVertices++);
        vertices.addLast(vv);
        vv.setPosicionEnListaVertices(vertices.last());
        return vv;
    }

    public Edge<E> insertEdge(Vertex<V> v, Vertex<V> w, E x){
        Vertice<V> vv = checkVertex(v);
        Vertice<V> ww = checkVertex(w);
        int fila = vv.getIndice();
        int col = ww.getIndice();
        Arco<V,E> arco = new Arco(x, vv,ww);
        matriz[fila][col] = matriz[col][fila] = arco;
        arcos.addLast(arco);
        arco.setPosicionEnlistaArco(arcos.last());
        return arco;
    }

    public Iterable<Vertex<V>> vertices(){
        PositionList<Vertex<V>> pl = new ListaDoblementeEnlazada<Vertex<V>>();
        for(Vertex<V> v : vertices){
            pl.addLast(v);
        }
        return pl;
    }

    public Iterable<Edge<E>> edges(){
        PositionList<Edge<E>> pl = new ListaDoblementeEnlazada<Edge<E>>();
        for(Edge<E> e : arcos)
            pl.addLast(e);
        return pl;
    }

    public Iterable<Edge<E>> incidentEdges(Vertex<V> v){
        Vertice<V> vv = checkVertex(v);
        int i = vv.getIndice();
        PositionList<Edge<E>> pl = new ListaDoblementeEnlazada<Edge<E>>();
        for(int j=0; j<cantidadVertices; j++)
            if(matriz[i][j] != null) pl.addLast(matriz[i][j]);
        return pl;
    }

    public Vertex<V> opposite(Vertex<V> v, Edge<E> e){
        Vertice<V> vv = checkVertex(v);
        Arco<V,E> ee = checkEdge(e);
        if(ee.getV1() == vv) return ee.getV2();
        if(ee.getV2() == vv) return ee.getV1();
        throw new InvalidEdgeException("el vertice no corresponde al arco");
    }
    public Vertex<V>[] endvertices(Edge<E> e){
        Vertex<V>[] arr = (Vertex<V>[]) new Vertice[2];
        Arco<V,E> ee = checkEdge(e);
        arr[0] = ee.getV1(); arr[1] = ee.getV2();
        return arr;
    }

    public boolean areAdjacent(Vertex<V> v, Vertex<V> w){
        int i = checkVertex(v).getIndice();
        int j = checkVertex(w).getIndice();
        return matriz[i][j] != null;
    }
    public E removeEdge(Edge<E> e){
        Arco<V,E> ee = checkEdge(e);
        int fila = ee.getV1().getIndice();
        int columna = ee.getV2().getIndice();
        matriz[fila][columna] = matriz[columna][fila] = null;
        arcos.remove(ee.getPosicionEnListaArco());
        return e.element();
    }

    public V removeVertex(Vertex<V> v){
        Vertice<V> vv= checkVertex(v);
        PositionList<Edge<E>> arcosABorrar = new ListaDoblementeEnlazada<Edge<E>>();
        for(Edge<E> e : incidentEdges(v)) arcosABorrar.addLast(e);
        for(Edge<E> e : arcosABorrar) removeEdge(e);
        
        int indiceV = vv.getIndice();
        int ultimoIndice = cantidadVertices - 1;

        if(indiceV != ultimoIndice){
            Vertice<V> ultimoVertice = null;
            Iterator<Vertex<V>> it = vertices.iterator();
            boolean encontreVertice = false;
            while(it.hasNext() && !encontreVertice){
                Vertice<V> vx = checkVertex(it.next());
                if(vx.getIndice() == ultimoIndice){
                    ultimoVertice = vx;
                    encontreVertice = true;
                }
            }
            for(int i = 0; i<cantidadVertices; i++){
                matriz[indiceV][i] = matriz[ultimoIndice][i];
                matriz[i][indiceV] = matriz[i][ultimoIndice];
            }
            ultimoVertice.setIndice(indiceV);
        }
        for(int i=0; i<cantidadVertices; i++){
            matriz[ultimoIndice][i] = matriz[i][ultimoIndice] = null;
        }
        vertices.remove(vv.getPosicionEnListaVertices());
        cantidadVertices--;
        return vv.element();
    }
    public E replace(Edge<E> e, E x){
        Arco<V,E> ee = checkEdge(e);
        E temp = ee.element();
        ee.setRotulo(x);
        return temp;
    }
    public V replace(Vertex<V> v, V x){
        Vertice<V> vv = checkVertex(v);
        V temp = vv.element();
        vv.setRotulo(x);
        return temp;
    }

    
}
