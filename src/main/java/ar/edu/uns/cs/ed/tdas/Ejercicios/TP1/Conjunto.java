package ar.edu.uns.cs.ed.tdas.Ejercicios.TP1;

public interface Conjunto<E>  {
    public int size();
    public int capacity();
    public boolean isEmpty(); 
    public E get(int i); //Requiere que la posición sea válida
    public void put(E elem); /*  Requiere que el conjunto no esté lleno y que el elemento no se
    encuentre en el conjunto. La comparación se realiza por equivalencia.*/
    public boolean pertenece(E elem);
    public Conjunto<E> interseccion(Conjunto<E> c); 
}
