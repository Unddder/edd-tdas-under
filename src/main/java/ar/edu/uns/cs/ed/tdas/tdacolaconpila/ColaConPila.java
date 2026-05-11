package ar.edu.uns.cs.ed.tdas.tdacolaconpila;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyQueueException;
import ar.edu.uns.cs.ed.tdas.tdacola.Queue;
import java.util.Stack;

public class ColaConPila<E> implements Queue<E>{
    private Stack <E> pila;
    private Stack<E> cola;
    public ColaConPila(){
        pila = new Stack<E>();
        cola = new Stack<E>();
        }

	public int size() {return cola.size() + pila.size();};

	public boolean isEmpty() {return cola.isEmpty() && pila.isEmpty();}

	public E front(){
        if (isEmpty())
            throw new EmptyQueueException("La cola está vacía");
        if (cola.isEmpty()) {encolar();}
        return cola.peek();
    };
	
	public void enqueue(E element){
        pila.push(element);
    }
	
	public E dequeue(){
        if (isEmpty())
            throw new EmptyQueueException("La cola está vacía");
        if(cola.isEmpty()) {encolar();}
        return cola.pop();
    };

    private void encolar(){
        while(!pila.isEmpty()){
            cola.push(pila.pop());
        }
    }
}