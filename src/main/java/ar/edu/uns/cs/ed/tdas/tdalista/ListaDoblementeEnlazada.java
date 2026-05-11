package ar.edu.uns.cs.ed.tdas.tdalista;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyListException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;

public class ListaDoblementeEnlazada<E> implements PositionList<E> {
    //at. instancia
    protected DNodo<E> header;
    protected DNodo<E> trailer;
    protected int tamaño;

    //constructor
    public ListaDoblementeEnlazada(){
        header = new DNodo<E>(null, null, null);
        trailer = new DNodo<E>(null, null, header);
        header.setSiguiente(trailer);
        tamaño = 0;
    }

    private DNodo<E> checkPosition(Position<E> p){
        if(p == null)
            throw new InvalidPositionException("Posición nula");
        try{
            DNodo<E> nodo = (DNodo<E>) p;
            if(nodo.getSiguiente() == null)
                throw new InvalidPositionException("Posición eliminada previamente");
            return nodo;
        } 
        catch (ClassCastException e){
            throw new InvalidPositionException("Posición inválida");
        }
    }

    //consultas
    public int size(){return tamaño;}
    public boolean isEmpty(){return tamaño == 0;}
    public Position<E> first(){
        if(isEmpty()) throw new EmptyListException("Lista vacía");
        return header.getSiguiente();
    }
    public Position<E> last(){
        if (isEmpty()) throw new EmptyListException("Lista vacía");
        return trailer.getAnterior();
    }
    public Position<E> next(Position<E> p){
        if(isEmpty()) throw new InvalidPositionException("Lista vacía");
        DNodo<E> este = checkPosition(p);
        if(este.getSiguiente() == trailer)
            throw new BoundaryViolationException("la posición pasada por parámetro corresponde al último elemento");
        return este.getSiguiente();
    }
    public Position<E> prev(Position<E> p){
        if(isEmpty()) throw new InvalidPositionException("Lista vacía");
        DNodo<E> este = checkPosition(p);
        if(este.getAnterior() == header) 
            throw new BoundaryViolationException("la posición pasada por parámetro corresponde al primer elemento");
        return este.getAnterior();
    }

    //comandos
    public void addFirst(E elem){
        DNodo<E> siguiente = header.getSiguiente();
        DNodo<E> este = new DNodo<E>(elem,siguiente,header);
        header.setSiguiente(este);
        siguiente.setAnterior(este);
        tamaño++;
    }
    public void addLast(E elem){
        DNodo<E> anterior = trailer.getAnterior();
        DNodo<E> este = new DNodo<E>(elem,trailer,anterior);
        anterior.setSiguiente(este);
        trailer.setAnterior(este);
        tamaño++;
    }
    public void addAfter(Position<E> p, E elem){//anterior -> este -> siguiente
        if(isEmpty()) throw new InvalidPositionException("Lista vacía");
        DNodo<E> anterior = checkPosition(p);
        DNodo<E> siguiente = anterior.getSiguiente();
        DNodo<E> este = new DNodo<E>(elem, siguiente, anterior);
        siguiente.setAnterior(este);
        anterior.setSiguiente(este);
        tamaño++;
    }
    public void addBefore(Position<E> p, E elem){
        if(isEmpty()) throw new InvalidPositionException("Lista vacía");
        DNodo<E> siguiente = checkPosition(p);
        DNodo<E> anterior = siguiente.getAnterior();
        DNodo<E> este = new DNodo<E>(elem,siguiente,anterior);
        anterior.setSiguiente(este);
        siguiente.setAnterior(este);
        tamaño++;
    }
    public E remove(Position<E> p){//anterior -> siguiente          (este) queda desvinculado
        if (isEmpty()) throw new InvalidPositionException("Lista vacía");
        DNodo<E> este = checkPosition(p);
        DNodo<E> anterior = este.getAnterior();
        DNodo<E> siguiente = este.getSiguiente();
        E elem = p.element();
        este.setAnterior(null); este.setSiguiente(null);
        siguiente.setAnterior(anterior);
        anterior.setSiguiente(siguiente);
        tamaño--;
        return elem;
    }
    public E set(Position<E> p, E elem){
        if (isEmpty()) throw new InvalidPositionException("Lista vacía");
        DNodo<E> este = checkPosition(p);
        E elemAnterior = p.element();
        este.setElemento(elem);
        return elemAnterior;
    }

    private class PositionIterator implements Iterator<Position<E>> {
        private Position<E> cursor;
        private Position<E> recent;
        public PositionIterator(){
            cursor = (isEmpty()) ? null : first();
            recent = null;
        }
        
        public boolean hasNext(){return cursor != null;}
        public Position<E> next() throws NoSuchElementException{
            if(cursor == null) throw new NoSuchElementException("No hay siguiente");
            recent = cursor;
            if(cursor == last())
                cursor = null;
            else cursor = ListaDoblementeEnlazada.this.next(cursor); 
            /*UTILIZA EL METODO NEXT DEFINIDO PARA POSITIONS, TIENEN 
            MISMO NOMBRE POSITION <E> next(POSITION<E>) y éste método*/
            return recent;
        }
    }
    
    private class PositionIterable implements Iterable<Position<E>>{
        public Iterator<Position<E>> iterator(){ return new PositionIterator();}
    }

    public Iterator<E> iterator(){
        return new ElementIterator<>(this);
    }

    public Iterable<Position<E>> positions(){
        return new PositionIterable();
    }


    //EJERCICIO 2
    public void ejercicio2(E e1, E e2){
        if(isEmpty()){
            addFirst(e2);
            addFirst(e1);
        }
        else{
            addAfter(first(), e1);
            addBefore(last(), e2);
        }
    }
}

