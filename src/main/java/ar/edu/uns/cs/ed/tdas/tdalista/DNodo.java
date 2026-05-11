package ar.edu.uns.cs.ed.tdas.tdalista;
import  ar.edu.uns.cs.ed.tdas.Position;

public class DNodo<E> implements Position<E> {
    private E elemento;
    private DNodo<E> siguiente;
    private DNodo<E> anterior;

    public DNodo(E item, DNodo<E> sig, DNodo<E> ant){
        elemento = item; siguiente = sig; anterior = ant;
    }


    public void setElemento(E elem){elemento = elem;}
    public void setSiguiente(DNodo<E> elem){siguiente = elem;}
    public void setAnterior(DNodo<E> elem){anterior = elem;}

    public E element(){return elemento;}
    public DNodo<E> getSiguiente(){return siguiente;}
    public DNodo<E> getAnterior(){return anterior;}
    
}
