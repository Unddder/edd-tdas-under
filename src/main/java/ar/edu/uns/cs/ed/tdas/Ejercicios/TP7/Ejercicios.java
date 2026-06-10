package ar.edu.uns.cs.ed.tdas.Ejercicios.TP7;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyTreeException;
import ar.edu.uns.cs.ed.tdas.tdaarbolbinario.BinaryTree;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;

public class Ejercicios<E>  {
    public Iterable<Character> ejercicio3(BinaryTree<Character> a){
        PositionList<Character> lista = new ListaDoblementeEnlazada<>();
        ordenar(a,lista,a.root());
        return lista;
    }

    private void ordenar(BinaryTree<Character> a, PositionList<Character> lista, Position<Character> p){
        if(a.isExternal(p))
            lista.addLast(p.element());
        else{
            lista.addLast('(');
            ordenar(a,lista,a.left(p));
            lista.addLast(p.element());
            ordenar(a, lista, a.right(p));
            lista.addLast(')');
        }
    }

    public void completarDerechos(E r, BinaryTree<E> t){
        //tamaño de la entrada n: t.Size()
        //analizar condicion c1, tirar excepción: c2 corta el flujo de ejecucion
        if(t.isEmpty()) throw new EmptyTreeException("árbol vacío");
        //llamar a t.positions(): c3*n
        /*peor caso: el arbol tiene solo hijos izquierdos (aunque el recorrido sea exhaustivo,
        si todos los nodos tienen hijo izquierdo y no derecho la constante c5 también se aplica
        (salvo para la única hoja del árbol la cual claramente no tiene hijo izquierdo)*/
        //cant_iteraciones_for(n) = n
        //T_for(n) = n(c4+c5) - c5   (se resta un c5 porque en la ultima iteracion p no tiene hijo izquierdo)
        for(Position<E> p : t.positions()){
            //analizar condición: c4
            if(t.hasLeft(p) && !t.hasRight(p))
                //llamar a método de tiempo constante: c5
                t.addRight(p, r);
        }
        //T_CompletarDerechos(n) = c1+c3*n+ n*(c4+c5) -c5
        //= c1 + c3*n + c4*n + c5*n - c5 = O(n)
    }
}
 