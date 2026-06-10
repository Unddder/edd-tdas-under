import java.security.InvalidKeyException;

import javax.swing.text.Position;

import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import ar.edu.uns.cs.ed.tdas.tdaarbolbinario.BinaryTree;
import ar.edu.uns.cs.ed.tdas.tdadiccionario.TDADiccionario;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;

public class parcial{
    public int todas(K key) throws InvalidKeyException{
        if(key == null) throw new InvalidKeyException(); //clave nula
        TDADiccionario<K,V> d = A[h(key)];
        return d.findAll(key).size();
    }

    private int h(K key){
        int i = key.hashcode();
        if(i<0) i=-i;
        return i % N;
    }

    public class TDADiccionario<K,V> implements Dictionary<K,V>{
        protected static class Entrada<K,V> implements Entry<K,V>{
            //...
        }
        //...
        PositionList<Entry<K,V>> S;
        public Iterable<Entry<K,V>> findAll(K clave){
            PositionList<Entry<K,V>> nueva = new ListaDoblementeEnlazada<>();
            for(Entry<K,V> e : S)
                if(e.getKey().equals(clave))
                    nueva.addLast(e);
            return nueva;
        }
    }

    //ejercicio2
    public Mapeo<Character,Integer> eiminarHojas(BinaryTree<Character> arbol, Position<Character> p){
        if(p == null) throw new InvalidKeyException("la clave es nula");
        Map<Characte,Integer> m = new Mapeo<>();
        PositionList<Position<Character>> pl = new ListaDLL<>();
        posOrden(pl,p,arbol);
        for(Position<Character> pos : pl.positions())
            if(arbol.isExternal(pos)){
                if(m.find(pos.element()) == null)
                    m.put(pos.element(), 1);
                else m.put(pos.element(), m.get(pos.element()) + 1);
                arbol.remove(pos);
            }
        return m;
    }
    private void posOrden(PositionList<Character,Integer> pl, Position<Character> p, BinaryTree<Character> a){
        if(a.hasLeft(p))
            posOrden(pl,a.left(p),a);
        if(a.hasRight(p))
            posOrden(pl,a.right(p),a);
        pl.addLast(p);
    }

    //ej 3
    public boolean eliminarUltimo(Position<E> p) throws InvalidPositionException{
        if(isRoot(p)) throw new InvalidPositionException("p corresponde a raiz");
        TNodo<E> nodo = checkPosition(p);
        if(nodo==nodo.getPadre().getHijos().last().element()){
            if(isInternal(p)){
                PositionList<TNodo<E>> hijosDeNodo = nodo.getHijos();
                for(Position<TNodo<E>> pos : hijosDeNodo.positions()){
                    nodo.getPadre().getHijos().addLast(pos.element());
                    pos.element().setPadre(nodo.getPadre());
                }   
            }
            nodo.getPadre().getHijos().remove(nodo.getPadre().getHijos().last());
            nodo.setPadre(null);
            return true;
        }
        return false;
    }




    //parcial 2
    public int removeMasivo(Iterable<K> it){
        int contador = 0;
        for(K clave : it){
            if(clave == null) throw newInvalidKeyException("it contiene claves inválidas");
            boolean encontre = false;
            Iterator<Position<Entrada<K,V>>> bucketIt = buckets[h(clave)].positions().iterator();
            Position<Entrada<K,V>> cursor = (bucketIt.hasNext()) ? bucketIt.next() : null;
            while(!encontre && cursor != null){
                if(cursor.element().getKey().equals(clave)){
                    buckets[h(clave)].remove(cursor);
                    encontre = true; contador++;
                }
                cursor = (bucketIt.hasNext()) ? bucketIt.next() : null;
            }
        }
        return contador;
    }

    public void Ejercicio2(BinaryTree<Character> A, Character c1, Character c2){
        if(A.isEmpty()) return;
        PositionList<Position<Character>> pl = new ListaDoblementeEnlazada<>();
        pos(pl,A.root(), A);
        for(Position<Character> p : pl)
            if(!A.hasLeft(p) && A.hasRight(p) && p.element().equals(c1))
                A.addLeft(c2, p);
    }
    private void pos(PositionList<Position<Character>> pl, Position<Character> p, BinaryTree<Character> arbol){
        if(arbol.hasLeft(p)) pos(pl,arbol.left(p),arbol);
        if(arbol.hasRight(p)) pos(pl,arbol.right(p),arbol);
        pl.addLast(p);
    }   

    public boolean eliminarMedio(Position<E> p){
        if(p == root()) throw new InvalidPositionException("posicion corresponde a la raiz");
        TNodo<E> nodo = checkPosition(p);
        PositionList<TNodo<E>> hermanos = nodo.getPadre().getHijos();
        if(nodo == hermanos.last().element() || nodo == hermanos.first().element())
            return false;
        Iterator<Position<TNodo<E>>> it = hermanos.positions().iterator();
        Position<TNodo<E>> posDeNodo = null;
        Position<TNodo<E>> cursor = it.next();
        while(posDeNodo == null){
            if(cursor.element() == nodo)
                posDeNodo = cursor;
            cursor = it.next();
        }
        Position<TNodo<E>> aux = posDeNodo;
        for(Position<TNodo<E>> posHijo : nodo.getHijos().positions()){
            hermanos.addAfter(aux,posHijo.element());
            posHijo.element().setPadre(nodo.getPadre());
            aux = hermanos.next(aux);
        }
        nodo.setPadre(null);
        hermanos.remove(posDeNodo);
        tamaño--;
        return true;
    }
}


