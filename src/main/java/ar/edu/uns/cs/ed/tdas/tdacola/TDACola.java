package ar.edu.uns.cs.ed.tdas.tdacola;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyQueueException;;

public class TDACola<E> implements Queue<E>{
    private E[] datos;
    private int f; //frente
    private int tam; //tamaño

    public TDACola(){
        datos = (E[]) new Object[10]; //tamaño por defecto
        f=0;
        tam=0;
    }

    public void enqueue(E elem){
        if (tam == datos.length){
            E[] nueva = (E[]) new Object[datos.length * 2];
            for(int i=0; i < tam; i++)
                nueva[i] = datos[(i + f) % datos.length];
            datos = nueva;
            f=0;
        }
        datos[(f + tam) % datos.length] = elem;
        tam++;
    }

    public E dequeue() throws EmptyQueueException{
        if(isEmpty()) throw new EmptyQueueException("La cola está vacía");
        E temp = datos[f];
        datos[f] = null;
        tam--;
        f = (f+1)%datos.length;
        return temp;
    }

    public E front() throws EmptyQueueException{
        if(isEmpty()) throw new EmptyQueueException("La cola está vacía");
        return datos[f];
    }
    public boolean isEmpty(){return tam == 0;}
    public int size(){return tam;}
}
