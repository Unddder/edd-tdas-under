package ar.edu.uns.cs.ed.tdas.tdaarbol;

import java.util.Iterator;

import ar.edu.uns.cs.ed.tdas.*;
import ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyTreeException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidOperationException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;

public class TDArbol<E> implements Tree<E> {

    protected static class TNodo<E> implements Position<E>{
        private E elemento;
        private TNodo<E> padre;
        private PositionList<TNodo<E>> hijos;

        public TNodo(E elem, TNodo<E> p){
            elemento = elem;
            padre = p;
            hijos = new ListaDoblementeEnlazada<>();
        }
        public E element(){return elemento;}
        public TNodo<E> getPadre(){return padre;}
        public PositionList<TNodo<E>> getHijos(){return hijos;}
        
        public void setElemento(E e){elemento = e;}
        public void setPadre(TNodo<E> p){padre = p;}
        public void setHijos(PositionList<TNodo<E>> pl){hijos = pl;}
    
    }

    private TNodo<E> checkPosition(Position<E> p){
        if(p == null)
            throw new InvalidPositionException("Posición nula");
        try{
            TNodo<E> nodo = (TNodo<E>) p;
            if(nodo != raiz && nodo.getPadre() == null)
                throw new InvalidPositionException("Posición eliminada previamente");
            return nodo;
        } 
        catch (ClassCastException e){
            throw new InvalidPositionException("Posición inválida");
        }
    }
    
    
    protected TNodo<E> raiz;
    protected int tamaño;

    public TDArbol(){
        raiz = null;
        tamaño = 0;
    }

    public boolean isEmpty(){return size() == 0;}
    public int size(){return tamaño;}
    public void createRoot(E e) throws InvalidOperationException{
        if(!isEmpty()) throw new InvalidOperationException("Ya hay raiz");
        raiz = new TNodo<>(e, null);
        tamaño++;
    }
    public E replace(Position<E> v, E e) throws InvalidPositionException{
        TNodo<E> nodo = checkPosition(v);
        E aux = nodo.element();
        nodo.setElemento(e);
        return aux;
    }

    public Position<E> root() throws EmptyTreeException{
        if(isEmpty()) throw new EmptyTreeException("Árbol vacío");
        return raiz;
    }

    public Position<E> parent(Position<E> v) throws InvalidPositionException, BoundaryViolationException{
        TNodo<E> nodo = checkPosition(v);
        if(v == raiz) throw new BoundaryViolationException("La posición pasada por parámetro corresponde a la raíz");
        return nodo.getPadre();
    }

    public Iterable<Position<E>> children(Position<E> p) throws InvalidPositionException{
        TNodo<E> nodo = checkPosition(p);
        PositionList<Position<E>> lista = new ListaDoblementeEnlazada<>();
        for(TNodo<E> hijo : nodo.getHijos()) lista.addLast(hijo);
        return lista;
    }
    
    public boolean isInternal(Position<E> p) throws InvalidPositionException{
        TNodo<E> nodo = checkPosition(p);
        return !nodo.getHijos().isEmpty();
    }
    public boolean isExternal(Position<E> p) throws InvalidPositionException{return !isInternal(p);}
    public boolean isRoot(Position<E> v) throws InvalidPositionException{return checkPosition(v) == raiz;}
    
    public Position<E> addFirstChild(Position<E> p, E e) throws InvalidPositionException {
        if(isEmpty()) throw new InvalidPositionException("Árbol vacío");
        TNodo<E> padre = checkPosition(p);
        TNodo<E> nuevo = new TNodo<E>(e, padre);
        padre.getHijos().addFirst(nuevo);
        tamaño++;
        return nuevo;   
    }
    public Position<E> addLastChild(Position<E> p, E e) throws InvalidPositionException {
        if(isEmpty()) throw new InvalidPositionException("Árbol vacío");
        TNodo<E> padre = checkPosition(p);
        TNodo<E> nuevo = new TNodo<E>(e, padre);
        padre.getHijos().addLast(nuevo);
        tamaño++;
        return nuevo;   
    }
    public Position<E> addBefore(Position<E> p, Position<E> rb, E e){
        if(isEmpty()) throw new InvalidPositionException("Árbol vacío");

        TNodo<E> hermanoDerecho = checkPosition(rb);
        TNodo<E> padre = checkPosition(p);
        TNodo<E> nuevo = new TNodo<E>(e, padre);

        boolean encontre = false;
        Iterator<Position<TNodo<E>>> it = padre.getHijos().positions().iterator();
        
        while(it.hasNext() && !encontre){
            Position<TNodo<E>> posicion = it.next();
            if(posicion.element() ==  hermanoDerecho){
                padre.getHijos().addBefore(posicion, nuevo);
                tamaño++;
                encontre = true;
            }
        }
        if(!encontre) throw new InvalidPositionException("La posición a la cual se pide poner como hermano no corresponde al padre");
        return nuevo;
    }

    public Position<E> addAfter(Position<E> p, Position<E> lb, E e) throws InvalidPositionException{
        if(isEmpty()) throw new InvalidPositionException("Árbol vacío");

        TNodo<E> hermanoIzquierdo = checkPosition(lb);
        TNodo<E> padre = checkPosition(p);
        TNodo<E> nuevo = new TNodo<E>(e, padre);

        boolean encontre = false;
        Iterator<Position<TNodo<E>>> it = padre.getHijos().positions().iterator();
        
        while(it.hasNext() && !encontre){
            Position<TNodo<E>> posicion = it.next();
            if(posicion.element() ==  hermanoIzquierdo){
                padre.getHijos().addAfter(posicion, nuevo);
                tamaño++;
                encontre = true;
            }
        }
        if(!encontre) throw new InvalidPositionException("La posición a la cual se pide poner como hermano no corresponde al padre");
        return nuevo;
    }

    public void removeExternalNode(Position<E> p) throws InvalidPositionException{
        if(isEmpty() || !isExternal(p)) throw new InvalidPositionException("Árbol vacío o el nodo no es externo");
        TNodo<E> nodo = checkPosition(p);
        
        //caso arbol de un solo nodo
        if(nodo == raiz){
            raiz = null;
            tamaño = 0;
            nodo.setPadre(null);
            return;
        }
        for(Position<TNodo<E>> posicion : nodo.getPadre().getHijos().positions())
            if(posicion.element() == nodo){
                nodo.getPadre().getHijos().remove(posicion);
                tamaño--;
                nodo.setPadre(null);
                return;
            }
    }
    
    public void removeInternalNode(Position<E> p){
        if(isEmpty())
            throw new InvalidPositionException("el árbol está vacío.");
        TNodo<E> nodo = checkPosition(p);

        if(!isInternal(p) || (isRoot(p) && nodo.getHijos().size() > 1))
          throw new InvalidPositionException("la posición pasada por parámetro no corresponde a un nodo interno o corresponde a la raíz (con más de un hijo)"); 

        if(isRoot(p)){ // tiene un solo hijo porque si no hubiera lanzado la excepción arriba
            TNodo<E> hijo = nodo.getHijos().first().element();
            raiz = hijo;
            hijo.setPadre(null);
            nodo.setHijos(null);
            tamaño--;
            return;
        }

        //busco la posición del nodo
        Position<TNodo<E>> posicionDelNodo = null; // debo inicializarla para que compile el addAfter de abajo
        boolean encontre = false;
        Iterator<Position<TNodo<E>>> it = nodo.getPadre().getHijos().positions().iterator(); //busco entre a nodo entre los hijos de su padre
        while(it.hasNext() && !encontre){
            Position<TNodo<E>> pos = it.next();
            if(pos.element() == nodo){
                posicionDelNodo = pos;
                encontre = true;
            }
        }

        PositionList<TNodo<E>> hijosDeNodo = nodo.getHijos();
        TNodo<E> padre = nodo.getPadre();
        
        Position<TNodo<E>> actual = posicionDelNodo;

        for(Position<TNodo<E>> posicion : hijosDeNodo.positions()){
            TNodo<E> nodoHijo = posicion.element();
            padre.getHijos().addAfter(actual, nodoHijo);
            nodoHijo.setPadre(padre);
            actual = padre.getHijos().next(actual); 
            // actualizo asi el siguiente nodo se agrega después del nodo agregado ahora y no siempre al lado del nodo a borrar, de esta forma no queda en orden invertido
        }
        padre.getHijos().remove(posicionDelNodo);
        nodo.setHijos(null);
        nodo.setPadre(null);
        tamaño--;
    }

    public void removeNode(Position<E> p){
        if(isEmpty() || (isRoot(p) && checkPosition(p).getHijos().size() > 1))
            throw new InvalidPositionException("la posición pasada por parámetro es inválida o corresponde a la raíz (con más de un hijo), o el árbol está vacío.");
        if(isInternal(p)) removeInternalNode(p);
        else removeExternalNode(p);
    }


    //implementación de iteradores
    private void preOrden(TNodo<E> nodo, PositionList<Position<E>> lista){
    lista.addLast(nodo);
    for(Position<TNodo<E>> p : nodo.getHijos().positions())
        preOrden(p.element(), lista);  
    }

    public Iterable<Position<E>> positions(){
        PositionList<Position<E>> lista = new ListaDoblementeEnlazada<>();
        if(!isEmpty()) preOrden(raiz, lista);
        return lista;
    }

    public Iterator<E> iterator() {
        PositionList<E> lista = new ListaDoblementeEnlazada<>();
        for(Position<E> p : positions())
            lista.addLast(p.element());
        return lista.iterator();
    }
}















