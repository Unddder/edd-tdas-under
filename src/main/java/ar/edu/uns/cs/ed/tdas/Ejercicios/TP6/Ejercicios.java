package ar.edu.uns.cs.ed.tdas.Ejercicios.TP6;

import static org.junit.jupiter.api.Assumptions.abort;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.tdaarbol.Tree;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdamapeo.Map;
import ar.edu.uns.cs.ed.tdas.tdamapeo.TDAMapeo;

public class Ejercicios {
    public Map<Character, Integer> cantidadRepeticiones(Tree<Character> t){
    Map<Character,Integer> m = new TDAMapeo<>();
    for(Character c : t)
        if(m.get(c) == null)
            m.put(c, 1);
        else m.put(c, m.get(c)+1);
    return m;
    }

    public Iterable<Position<String>> ejercicio4(Tree<String> t, String s){
        PositionList<Position<String>> pl = new ListaDoblementeEnlazada<>();
        postOrdenShell(t, pl, s);
        return pl;
    }

    private void postOrdenShell(Tree<String> tree, PositionList<Position<String>> pl, String s){postOrden(tree,pl,tree.root(), s);}
    private void postOrden(Tree<String> tree, PositionList<Position<String>> pl, Position<String> nodo, String s){
        for(Position<String> hijo : tree.children(nodo)){
            postOrden(tree, pl, hijo, s);
        }
        if(nodo.element().equals(s)) pl.addLast(nodo);
    }

    public <E> int ejercicio5(Tree<E> a, E e){
        //tamaño de entrada n: a.size() = nodos del arbol a
        //c1
        int contador = 0;
        //llamado a positions: c2*n
        //recorrido exhaustivo: recorre toda la estructura
        //cant_iteraciones_for(n) = n
        //cuerpo del for: c3
        //T_for(n): c2*n + n*(c3+c4*n+c5) = c2*n + n*c3 + n^2 * c4 + n*c5
        for(Position<E> pos : a.positions())
            //c3
            if (pos.element().equals(e)){
                //c4*n
                a.removeNode(pos);
                //c5
                contador++;
            }
        //c6
        return contador;
        //c1 + c2*n + n*c3 + n^2 * c4 + n*c5 + c6 = c1 + c6 + (c2+c3+c5)*n + (n^2)*c4 = O(n^2)
    }

    public boolean ejercicio6(Tree<Integer> a, int n){
        //tamaño de entrada n: a.size()
        //llamado al iterador de a para el for each: c1*n
        //peor caso: recorre toda la estructura
        //cant_iteraciones(n) = n
        //T_for(n)= c1*n + c2*n
        for(Integer i : a)
            // comparación (que hasta la última iteración debe dar falso asi se sigue recorriendo la estructura) = c2
            if (i.equals(n)) return true;
            //luego en la ultima iteración no importa si i.equals(n) porque retorne verdadero o retorne falso cuesta el mismo tiempo c3
        return false;
        //T_ejercicio6(n) = c1*n + c2*n + c3 = O(n)
    }



}
