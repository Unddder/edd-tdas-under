package ar.edu.uns.cs.ed.tdas.tdapila;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyStackException;

public class TDAPila<E> implements Stack<E> {
    private E[] pila;
    private int tope;
    public TDAPila(int i){
        pila = (E[]) new Object[i];;
        tope = 0;
    }
    public TDAPila(){
        pila = (E[]) new Object[10];
        tope = 0;
    }
    public void push(E e){
        if(size() == pila.length){   
            E[] nueva = (E[]) new Object[tope+1];
            for(int i=0; i<tope; i++)
                nueva[i] = pila[i];
            pila = nueva;
        } 
            pila[tope++] = e;
    }
    public E pop() throws EmptyStackException{
        if(isEmpty()) 
            throw new EmptyStackException("La pila está vacía");
        E temp = pila[tope - 1]; 
        pila[tope - 1] = null;
        tope--;
        return temp;
    }
    public boolean isEmpty(){
        return tope == 0;
    }
    public E top() throws EmptyStackException{
        if(isEmpty()) 
            throw new EmptyStackException("La pila está vacía");
        return pila[tope - 1];
    }
    public int size(){
        return tope;
    }

}