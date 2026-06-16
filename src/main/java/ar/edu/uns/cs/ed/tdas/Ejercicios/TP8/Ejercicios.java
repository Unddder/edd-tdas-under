package ar.edu.uns.cs.ed.tdas.Ejercicios.TP8;

import java.util.Iterator;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.tdacola.Queue;
import ar.edu.uns.cs.ed.tdas.tdacola.TDACola;
import ar.edu.uns.cs.ed.tdas.tdagrafo.Edge;
import ar.edu.uns.cs.ed.tdas.tdagrafo.Graph;
import ar.edu.uns.cs.ed.tdas.tdagrafo.GraphD;
import ar.edu.uns.cs.ed.tdas.tdagrafo.Vertex;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdamapeo.Map;
import ar.edu.uns.cs.ed.tdas.tdamapeo.TDAMapeo;

public class Ejercicios {
    private static final Object ESTADO = new Object();
    private static final Object VISITADO = new Object();
    private static final Object NOVISITADO = new Object();
 
    public <V,E>  boolean esConexo (Graph<V,E> g){
        Vertex<V> verticeCualquiera = null; boolean primerVertice = false;
        Iterator<Vertex<V>> it = g.vertices().iterator();
        if(!it.hasNext()) return true;
        while(!primerVertice && it.hasNext()){
            verticeCualquiera = it.next();
            primerVertice = true;
        }
        for(Vertex<V> w : g.vertices())
            w.put(ESTADO, NOVISITADO);
        dfsEJ3(g, verticeCualquiera);
        for(Vertex<V> w : g.vertices())
            if(w.get(ESTADO) == NOVISITADO) 
                return false;
        return true;
    }

    private <V,E> void dfsEJ3 (Graph<V,E> g, Vertex<V> v){
        v.put(ESTADO, VISITADO);
        for(Edge<E> e : g.incidentEdges(v)){
            Vertex<V> w = g.opposite(v, e);
            if(w.get(ESTADO) == NOVISITADO)
                dfsEJ3(g, w);
        }
    }

    public <V,E> int ejercicio4(Graph<V,E> g, Vertex<V> v1, Vertex<V> v2){
        for(Vertex<V> v : g.vertices())
            v.put(ESTADO, NOVISITADO);

        Map<Vertex<V>,Integer> m = new TDAMapeo<Vertex<V>, Integer>();
        m.put(v1,0);

        Queue<Vertex<V>> cola = new TDACola<>();
        cola.enqueue(v1);
        v1.put(ESTADO, VISITADO);

        while(!cola.isEmpty()){
            Vertex<V> u = cola.dequeue();
            if(u == v2)
                return m.get(u);
            for(Edge<E> e : g.incidentEdges(u)){
                Vertex<V> w = g.opposite(u, e);
                if(w.get(ESTADO) == NOVISITADO){
                    w.put(ESTADO,VISITADO);
                    m.put(w,m.get(u) + 1);
                    cola.enqueue(w);
                }
            }
        }
        return -1; //inalcanzable, solo puede pasar si es inconexo pero ya tira la excepción arriba, es para que compile
    }

    public <V,E> boolean ejercicio5(Graph<V,E> g, Vertex<V> v1, Vertex<V> v2){
        for(Vertex<V> v : g.vertices())
            v.put(ESTADO, NOVISITADO);
        PositionList<Vertex<V>> camino = new ListaDoblementeEnlazada<Vertex<V>>();
        boolean hayCamino = dfsCamino(g,v1,v2,camino);
        if(hayCamino){
            System.out.println("El camino de v1 a v2 es: ");
            for(Vertex<V> v : camino)
                System.out.print(v.element().toString() + " ");
        }
        else System.out.println("no hay camino de v1 a v2");
        return hayCamino;
    }

    private <V,E> boolean dfsCamino(Graph<V,E> g, Vertex<V> origen, Vertex<V> destino, PositionList<Vertex<V>> camino){
        origen.put(ESTADO,VISITADO);
        camino.addLast(origen);

        if(origen == destino) return true;

        else{
            Iterable<Edge<E>> adyacentes = g.incidentEdges(origen);
            for(Edge<E> e : adyacentes){
                Vertex<V> w = g.opposite(origen, e);
                if (w.get(ESTADO) == NOVISITADO){
                    boolean encontre = dfsCamino(g, w, destino, camino);
                    if (encontre) return true;
                }
            }
        }
        camino.remove(camino.last());
        return false;
    }

    /*Ejercicio 6: Dado un grafo G pesado con arcos conteniendo números reales y 
    dos vértices v1 y v2, escriba un método que encuentre el camino de costo 
    mínimo entre v1 y v2. El método debe imprimir el camino y el costo del mismo. */

    private static class Camino<V>{
        protected float peso;
        protected PositionList<Vertex<V>> nodos;
        public Camino(){
            peso = 0.0f;
            nodos = new ListaDoblementeEnlazada<>();
        }
        public float getPeso(){return peso;}
        public PositionList<Vertex<V>> getNodos(){return nodos;}
        public void setPeso(float f){peso = f;}
        public void setNodos(PositionList<Vertex<V>> pl){nodos = pl;}
        public String toString(){
            String s = "Peso del camino mas corto: " + peso + 
            "camino: ";
            for(Vertex<V> v : nodos)
                s += v.element().toString() + " "; 
            return s;
        }
    } 


    public <V> String ejercicio6(GraphD<V,Float> g, Vertex<V> v1, Vertex<V> v2){
        for(Vertex<V> w : g.vertices())
            w.put(ESTADO, NOVISITADO);
        Camino<V> camMinimo = new Camino<>();
        Camino<V> camActual = new Camino<>();
        camMinimo.setPeso(Float.MAX_VALUE);
        return dfsEj6(g,v1,v2,camActual,camMinimo).toString(); 
    } 
    private <V> Camino<V> dfsEj6(GraphD<V,Float> g, Vertex<V> origen, Vertex<V> destino, Camino<V> ca, Camino<V> cm){
        origen.put(ESTADO,VISITADO);
        ca.getNodos().addLast(origen);
        if(origen == destino)
            if(ca.getPeso() < cm.getPeso()){
                cm.setNodos(new ListaDoblementeEnlazada<>());
                for(Vertex<V> w : ca.getNodos())
                    cm.getNodos().addLast(w);
                cm.setPeso(ca.getPeso());
            }
        else{
            for(Edge<Float> arco : g.succesorEdges(origen)){
                Vertex<V> w = g.opposite(origen, arco);
                if(w.get(ESTADO) == NOVISITADO){
                    ca.setPeso(ca.getPeso() + arco.element());
                    dfsEj6(g, w, destino, ca, cm);
                    ca.setPeso(ca.getPeso() - arco.element());
                }
            }
        }
        ca.getNodos().remove(ca.getNodos().last());
        origen.put(ESTADO, NOVISITADO);
        return cm;
    }

    /* Implemente un recorrido primero en anchura (Breadth-First Search) a partir de un vértice v. 
    Si v es el número 1 en la visita, utilice el número de visita para encontrar aquellos nodos 
    cuyo número de visita es menor a un valor k designado por el cliente. El programa debe ser eficiente.
    */
   public <V,E> Iterable<Vertex<V>> ejercicio7(Graph<V,E> g, Vertex<V> v, int k){
        PositionList<Vertex<V>> resultado = new ListaDoblementeEnlazada<>();
        Queue<Vertex<V>> cola = new TDACola<>();
        for(Vertex<V> w : g.vertices())
            w.put(ESTADO, NOVISITADO);
        cola.enqueue(v);
        int contador = 1;
        v.put(ESTADO, VISITADO);
        while(!cola.isEmpty() && contador <= k){
            Vertex<V> w = cola.dequeue();
            resultado.addLast(w);
            for(Edge<E> arco : g.incidentEdges(w)){
                Vertex<V> x = g.opposite(w, arco);
                if(x.get(ESTADO) == NOVISITADO){
                    x.put(ESTADO, VISITADO);
                    cola.enqueue(x);
                }
            }
            contador++;
        }
        return resultado;
   }
}
