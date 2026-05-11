package ar.edu.uns.cs.ed.tdas.Ejercicios.TP1;
public class ConjuntoArreglo<E> implements Conjunto<E>{
    private E[] arr;
    public ConjuntoArreglo(int i){
        arr = (E[]) new Object[i];
    }

    public int size(){
        int contador = 0;
        for(int i=0; i<arr.length;i++)
            if(arr[i]!=null)
                contador++;
        return contador;
    }

    public int capacity(){
        return arr.length;
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public E get(int i){ //req i válido
        return arr[i];
    }

    public void put(E elem){
/*Requiere que el conjunto no esté lleno y que el elemento no se
encuentre en el conjunto. La comparación se realiza por equivalencia. */
        int pos = -1;
        for(int i=0; i<arr.length && pos == -1; i++)
            if(pos == -1 && arr[i] == null)
                pos = i;
        arr[pos] = elem;
    }

    public boolean pertenece(E elem){
        boolean esta = false;
        for(int i=0; i<arr.length && !esta; i++)
            esta = elem.equals(arr[i]);
        return esta;
    }

    public Conjunto<E> interseccion(Conjunto<E> c){
        Conjunto<E> nuevo = new ConjuntoArreglo<>(arr.length);
        for(int i=0; i<arr.length; i++)
            if(arr[i] != null && c.pertenece(arr[i]))
                nuevo.put(arr[i]);
        return nuevo;
    } 
}
