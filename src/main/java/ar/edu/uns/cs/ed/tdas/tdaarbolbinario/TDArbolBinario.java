package ar.edu.uns.cs.ed.tdas.tdaarbolbinario;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyListException;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyTreeException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidOperationException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidPositionException;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import java.util.Iterator;

public class TDArbolBinario<E> implements BinaryTree<E> {
    protected static class BTNodo<E> implements Position<E>{
        public E element;
        public BTNodo<E> padre,izq,der;
        public BTNodo(E e){
            element = e; padre = null; izq = null; der = null;
        }
        public E element(){return element;}
        public BTNodo<E> getPadre(){return padre;}
        public BTNodo<E> getIzq(){return izq;}
        public BTNodo<E> getDer(){return der;}

        public void setElement(E e){element = e;}
        public void setPadre(BTNodo<E> p){padre = p;}
        public void setIzq(BTNodo<E> i){izq = i;}
        public void setDer(BTNodo<E> d){der = d;}
    }

    private BTNodo<E> checkPosition(Position<E> p) throws InvalidPositionException{
        if(p == null) throw new InvalidPositionException("posicion pasada por parametro nula");
        try{
            BTNodo<E> nodo = (BTNodo<E>) p;
            if(nodo != raiz &&  nodo.getPadre() == null)
                throw new InvalidPositionException("p eliminada previamente");
            return nodo;
        }
        catch(ClassCastException e){
            throw new InvalidPositionException("p no es una posición de este árbol");
        }
    }
    
    protected BTNodo<E> raiz;
    protected int tamaño;

    public TDArbolBinario(){
        tamaño = 0;
        raiz = null;
    }
    public int size(){return tamaño;}
    public boolean isEmpty(){return size() == 0;}

    public E replace(Position<E> v, E e) throws InvalidPositionException {
        BTNodo<E> nodo = checkPosition(v);
        E aux = nodo.element();
        nodo.setElement(e);
        return aux;
    }

    public Position<E> root() throws EmptyTreeException{
        if(isEmpty()) throw new EmptyTreeException("árbol vacío");
        return raiz;
    }
    public Position<E> parent(Position<E> v) throws InvalidPositionException, BoundaryViolationException{
        BTNodo<E> nodo = checkPosition(v);
        if(nodo==raiz) throw new BoundaryViolationException("la posición corresponde al nodo padre del arbol");
        return nodo.getPadre();
    }

    public Position<E> left(Position<E> v)throws InvalidPositionException, BoundaryViolationException{
        BTNodo<E> nodo = checkPosition(v);
        if (nodo.getIzq() == null) throw new BoundaryViolationException("v no tiene hijo izquierdo");
        return nodo.getIzq();
    }
    public boolean hasLeft(Position<E> v) throws InvalidPositionException{return checkPosition(v).getIzq() != null;}

    public Position<E> right(Position<E> v)throws InvalidPositionException, BoundaryViolationException{
        BTNodo<E> nodo = checkPosition(v);
        if (nodo.getDer() == null) throw new BoundaryViolationException("v no tiene hijo derecho");
        return nodo.getDer();
    }
    public boolean hasRight(Position<E> v) throws InvalidPositionException{return checkPosition(v).getDer() != null;}
    public Iterable<Position<E>> children(Position<E> v) throws InvalidPositionException{
        PositionList<Position<E>> hijos = new ListaDoblementeEnlazada<>();
        if(hasLeft(v)) hijos.addLast(left(v));
        if(hasRight(v)) hijos.addLast(right(v));
        return hijos;
    }
    public boolean isInternal(Position<E> v)throws InvalidPositionException {return hasLeft(v) || hasRight(v);}
    public boolean isExternal(Position<E> v)throws InvalidPositionException{return !isInternal(v);}
    public boolean isRoot(Position<E> v) throws InvalidPositionException{return checkPosition(v) == raiz;}

    public void createRoot(E e) throws InvalidOperationException{
        if(!isEmpty()) throw new InvalidOperationException("ya hay raiz");
        BTNodo<E> nuevo = new BTNodo<E>(e);
        raiz = nuevo;
        tamaño = 1;
    }

    public Position<E> addLeft(Position<E> v, E r) throws InvalidPositionException, InvalidOperationException{
        if(isEmpty()) throw new InvalidPositionException("lista vacía");
        if(hasLeft(v)) throw new InvalidOperationException("la posición pasada por parametro ya tiene un hijo izquierdo");
        BTNodo<E> nodo = checkPosition(v);
        BTNodo<E> izquierdo = new BTNodo<E>(r);
        nodo.setIzq(izquierdo);
        izquierdo.setPadre(nodo);
        tamaño++;
        return izquierdo; 
    }

    public Position<E> addRight(Position<E> v, E r) throws InvalidPositionException, InvalidOperationException{
        if(isEmpty()) throw new InvalidPositionException("lista vacía");
        if(hasRight(v)) throw new InvalidOperationException("la posición pasada por parametro ya tiene un hijo derecho");
        BTNodo<E> nodo = checkPosition(v);
        BTNodo<E> derecho = new BTNodo<E>(r);
        nodo.setDer(derecho);
        derecho.setPadre(nodo);
        tamaño++;
        return nodo.getDer();
    }

    public Iterator<E> iterator(){
        PositionList<E> pl = new ListaDoblementeEnlazada<>();
        for(Position<E> p : positions())
            pl.addLast(p.element());
        return pl.iterator();
    }
    public Iterable<Position<E>> positions(){
        PositionList<Position<E>> pl = new ListaDoblementeEnlazada<>();
        if(!isEmpty()) 
            preOrden(pl, root());
        return pl;
    }

    private void preOrden(PositionList<Position<E>> pl, Position<E> pos){
        pl.addLast(pos);
        for(Position<E> hijo : children(pos))
            preOrden(pl, hijo);
    }

    public Position<E> addFirstChild(Position<E> p, E e) throws InvalidPositionException{return addLeft(p, e);}
    public Position<E> addLastChild(Position<E> p, E e) throws InvalidPositionException{return addRight(p, e);}

    public Position<E> addAfter(Position<E> p, Position<E> lb, E e)throws InvalidPositionException{
        checkPosition(p);
        if(left(p) == lb)
            if(!hasRight(p))
                return addRight(p, e);
            else throw new InvalidPositionException("p ya tiene asignado un hijo derecho");
        else throw new InvalidPositionException("la posición lb no corresponde al hijo izquierdo de p");
    }
    public Position<E> addBefore(Position<E> p, Position<E> rb, E e)throws InvalidPositionException{
        checkPosition(p);
        if(right(p) == rb)
            if(!hasLeft(p))
                return addLeft(p, e);
            else throw new InvalidPositionException("p ya tiene asignado un hijo izquierdo");
        else throw new InvalidPositionException("la posición rb no corresponde al hijo derecho de p");
    }

    public void attach(Position<E> r, BinaryTree<E> T1, BinaryTree<E> T2)throws InvalidPositionException { //clona T1 y T2 
        if(isInternal(r)) throw new InvalidPositionException("la posición pasada por parámetro no corresponde a una hoja");
        BTNodo<E> nodo = checkPosition(r);
        tamaño+=T1.size()+T2.size();
        if(!T1.isEmpty()){
            BTNodo<E> raizT1 = (BTNodo<E>) T1.root();
            raizT1 = clonar(raizT1, nodo);
            nodo.setIzq(raizT1);
        }
        if(!T2.isEmpty()){
            BTNodo<E> raizT2 = (BTNodo<E>) T2.root();
            raizT2 = clonar(raizT2, nodo);
            nodo.setDer(raizT2);
        }
    }

    private BTNodo<E> clonar(BTNodo<E> nodo, BTNodo<E> padre){
        if(nodo == null) return null;
        BTNodo<E> nuevo = new BTNodo<E>(nodo.element());
        nuevo.setPadre(padre);

        BTNodo<E> hi = clonar(nodo.getIzq(), nuevo);
        BTNodo<E> hd = clonar(nodo.getDer(), nuevo);

        nuevo.setIzq(hi);
        nuevo.setDer(hd);
        return nuevo;
    }

    public void removeNode(Position<E> p) throws InvalidPositionException{   
        if(size()==0)
            throw new InvalidPositionException("No se puede eliminar de un arbol vacio");
        BTNodo<E> n = checkPosition(p);
        if(hasLeft(p) && hasRight(p)) throw new InvalidPositionException("p tiene 2 hijos");
        BTNodo<E> hijo = (n.getIzq() != null) ? n.getIzq() : n.getDer();
        if(hijo != null) hijo.setPadre(n.getPadre());
        if(n == raiz) raiz = hijo;
        else{
            BTNodo<E> padre = n.getPadre();
            if(n == padre.getIzq())
                padre.setIzq(hijo);
            else padre.setDer(hijo);
        }
        tamaño--;
        n.setElement(null);
        n.setIzq(null);
        n.setDer(null);
        n.setPadre(null);
    }

    public void removeInternalNode(Position<E> p) throws InvalidPositionException{
        if(!isInternal(p)) throw new InvalidPositionException("p no corresponde a un nodo interno");
        removeNode(p);
    }
    public void removeExternalNode(Position<E> p) throws InvalidPositionException{
        if(!isExternal(p)) throw new InvalidPositionException("p no corresponde a un nodo externo");
        removeNode(p);
    }
}
