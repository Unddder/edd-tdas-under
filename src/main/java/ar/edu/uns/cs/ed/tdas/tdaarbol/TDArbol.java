package ar.edu.uns.cs.ed.tdas.tdaarbol;

import java.util.Iterator;

import ar.edu.uns.cs.ed.tdas.*;
import ar.edu.uns.cs.ed.tdas.excepciones.BoundaryViolationException;
import ar.edu.uns.cs.ed.tdas.excepciones.EmptyListException;
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
            elemento = elem; //c1
            padre = p; //c2
            hijos = new ListaDoblementeEnlazada<>(); //c3
            //c1+c2+c3 = O(1)
        }
        public E element(){return elemento;} //c1 = O(1)
        public TNodo<E> getPadre(){return padre;}//c1 = O(1)
        public PositionList<TNodo<E>> getHijos(){return hijos;}// c1 = O(1)
        
        public void setElemento(E e){elemento = e;}// c1 = O(1)
        public void setPadre(TNodo<E> p){padre = p;}// c1 = O(1)
        public void setHijos(PositionList<TNodo<E>> pl){hijos = pl;}// c1 = O(1)
    
    }

    private TNodo<E> checkPosition(Position<E> p){
        //evaluar cond: c1
        //T_max(c2, c4+c$+c6) = O(1)
        if(p == null)
            // arrojar excepción: c2
            throw new InvalidPositionException("Posición nula");
        try{
            //c3
            TNodo<E> nodo = (TNodo<E>) p;
            //evaluar cond = c4
            // T_max(c5,c6) = O(1) = c$
            if(nodo != raiz && nodo.getPadre() == null)
                throw new InvalidPositionException("Posición eliminada previamente"); //c5
            return nodo; //c6
        } 
        catch (ClassCastException e){
            //c7: solo alcanzable si falla el casteo
            throw new InvalidPositionException("Posición inválida");
        }
        //c1+c4+c$+c6 = c& = O(1)
    }
    
    
    protected TNodo<E> raiz;
    protected int tamaño;

    public TDArbol(){
        raiz = null; //c1
        tamaño = 0; //c2
        //c1+c2 = O(1)
    }

    public boolean isEmpty(){return size() == 0;} // c1 = O(1)
    public int size(){return tamaño;}// c1 = O(1)
    public void createRoot(E e) throws InvalidOperationException{
        if(!isEmpty()) throw new InvalidOperationException("Ya hay raiz");
        raiz = new TNodo<>(e, null);
        tamaño++;
    }
    public E replace(Position<E> v, E e) throws InvalidPositionException{
        TNodo<E> nodo = checkPosition(v); // c1
        E aux = nodo.element(); // c2
        nodo.setElemento(e); // c3
        return aux; //c4
        //c1+c2+c3+c4 = O(1)
    }

    public Position<E> root() throws EmptyTreeException{
        if(isEmpty()) throw new EmptyTreeException("Árbol vacío");//c1
        return raiz; //c2
        //c1+c2 = c$ = O(1)
    }

    public Position<E> parent(Position<E> v) throws InvalidPositionException, BoundaryViolationException{
        TNodo<E> nodo = checkPosition(v); // declarar variable y llamar a checkposition (de tiempo constante): c1
        // evaluar condición c2
        // TMax(T(c3),T(c4)) = Tmax(O(1),O(1)) = O(1) = c5
        if(v == raiz) throw new BoundaryViolationException("La posición pasada por parámetro corresponde a la raíz");//c3
        return nodo.getPadre(); //c4
        //c1+c2+c5 = c$ = O(1)
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
        return addChild(p, e, false);  
    }

    public Position<E> addLastChild(Position<E> p, E e) throws InvalidPositionException {
        return addChild(p, e, true);
    }

    private Position<E> addChild(Position<E> p, E e, boolean b){ // b addlast notb addfirst
        if(isEmpty()) throw new InvalidPositionException("Árbol vacío");
        TNodo<E> padre = checkPosition(p);
        TNodo<E> nuevo = new TNodo<E>(e, padre);
        if(b) padre.getHijos().addLast(nuevo);
        else padre.getHijos().addFirst(nuevo);
        tamaño++;
        return nuevo; 
    }

    public Position<E> addBefore(Position<E> p, Position<E> rb, E e){
        // llamada al metodo de O(n): c1*n
        //retorno: c2
        return add(p,rb,e,false);
        //c1*n + c2 = O(n)
    }

    public Position<E> addAfter(Position<E> p, Position<E> lb, E e) throws InvalidPositionException{
        // llamada al metodo de O(n): c1*n
        //retorno: c2
        return add(p,lb,e,true);
        //c1*n + c2 = O(n)
    }

    private Position<E> add(Position<E> p, Position<E> brother, E e, boolean after) throws InvalidPositionException{ //after -> addafter !after -> addbefore
        // evaluar condicion: c1
        // arrojar excepción: c2
        if(isEmpty()) throw new InvalidPositionException("Árbol vacío");
        //todas las siguientes declaraciones de variables: c3
        TNodo<E> hermano = checkPosition(brother);
        TNodo<E> padre = checkPosition(p);
        TNodo<E> nuevo = new TNodo<E>(e, padre);

        boolean encontre = false;
        Iterator<Position<TNodo<E>>> it = padre.getHijos().positions().iterator();
        
        //peor caso: el tamaño del arbol son todos los hijos de la raiz (salvo la raiz) : n-1
        //cant_iteraciones_while = n-1
        // evaluar condición: c4 (se evalua cada pasada del while)
        //tiempo_while: [c4(n-1)] * (c5+c6+c7+c8)= k(n-1) = O(n) = c$n
        while(it.hasNext() && !encontre){
            Position<TNodo<E>> posicion = it.next(); // c5
            if(posicion.element() ==  hermano){// evaluar condicion: c6
                if(after) padre.getHijos().addAfter(posicion, nuevo); // c7
                else padre.getHijos().addBefore(posicion, nuevo);//c8
                tamaño++; // c9
                encontre = true; // c10
            }
        }
        // c11
        if(!encontre) throw new InvalidPositionException("La posición a la cual se pide poner como hermano no corresponde al padre");
        // c12
        return nuevo;
        //c1+c2+c3+c$n+c11+c12 = k + c$n = O(n)
    }

    public void removeNode(Position<E> p) throws InvalidPositionException{
        //evaluar condición: c1
        if(size()==0)
            throw new InvalidPositionException("No se puede eliminar de un arbol vacio"); //arrojar excepción: c2 pero corta el flujo de ejecución así que no lo cuento
        
        //declaración de variable + asignación a objeto por llamado a un método de tiempo constante: c3
        TNodo<E> n = checkPosition(p);

        //T_try_catch(n) = max(T_try(n),T_catch(n)) = max(O(n), O(1)) = O(n) = c14*n
        try {
            //evaluar condición: c4
            //T_try(n) = max(T1(n),T2(n)) = max(O(1), O(n)) = O(n)
            if(n==raiz)// el nodo que se pretende eliminar es la raiz
            //T1(n)= max(T3,T4) = max(O(1),O(1)) = c5
                if(raiz.getHijos().size()==1)//la raiz tiene un solo hijo
                {
                    Position<TNodo<E>> rN=raiz.getHijos().first();
                    rN.element().setPadre(null);
                    raiz.getHijos().remove(rN);
                    raiz= rN.element();
                    //todas operaciones constantes T3(n) = O(1)
                }
                else
                    //T4 = max(T5,T6) = max(O(1),O(1)) = O(1)
                    if (tamaño==1)//el arbol tiene un unico nodo 
                        raiz=null;
                        //T5(n) = O(1)
                    else    // se quiere eliminar la raiz pero no es posible por la estructura del arbol
                        throw new InvalidPositionException("Solo se puede eliminar la raiz si es el unico elemento o si tiene un solo hijo");
                        //T6(n) = O(1)
                        
            else // Se quiere eliminar un nodo interno o un nodo hoja.
            //T2(n) = c6 + c8*n + c9 + c11*n + c12 + c13 = O(n)
            {   
                //declaración de variables y llamadas a métodos de tiempo constante = c6
                TNodo<E> padre=n.getPadre();
                PositionList<TNodo<E>> hPadre=padre.getHijos(); //hijos del padre (hermanos de n)
                PositionList<TNodo<E>> hN=n.getHijos();//hijos de n
                    
                //buscar a n dentro de los hijos del padre
                    Position<TNodo<E>>posDeN;
                    Position<TNodo<E>> cursor= hPadre.first();
                    //T_while(n) = cant_iteraciones(n) * c7
                    //peor caso: recorre toda la estructura (el nodo a eliminar es hijo de raiz, es el último hijo y es nodo externo)
                    //T_while(n) = (n-1) * c7 = c7n - c7 = O(n) = c8*n
                    while(cursor.element()!=n && cursor!=null){
                        //T7(n) = O(1) (pues si recurre toda la estructura (que es el peor de los casos) solo entra al else)
                        if (cursor==hPadre.last()) 
                            cursor=null;
                        else
                            cursor= hPadre.next(cursor);}
                    
                    //T8(n) = max(O(1), O(1)) = O(1) = c9
                    if(cursor!=null)	
                        posDeN= cursor; //O(1)
                    else
                        throw new InvalidPositionException("La estructura no corresponde a un arbol valido"); //O(1)
                    
                //si n tiene hijos, se recorren e insertan ordenados en el lugar del padre
                
                //T_while(n) = cant_iteraciones(n) * c10
                //peor caso: los hijos del nodo a borrar son todos los nodos externos
                //cant_iteraciones(n) = (n-1-k1) (se resta 1 por la raiz, y k1 son los hermanos del nodo a eliminar)
                //T_while(n) = c10(n-1-k1)= c10n - c10 - c10*k1 = O(n) = c11*n
                while(!hN.isEmpty())
                {
                    Position<TNodo<E>> hijoN=hN.first();
                    hPadre.addBefore(posDeN,hijoN.element());
                    hijoN.element().setPadre(padre);
                    hN.remove(hijoN);
                    //todas operaciones constantes: c10
                }
                //eliminamos a n de la lista
                hPadre.remove(posDeN); //c12
            }
            //decrementamos el tamaño de la estructura
            tamaño--; //c13
            } catch (EmptyListException | BoundaryViolationException e) { //t_catch(n)= O(1)
                throw new InvalidPositionException("La estructura no corresponde a un arbol valido");
            }
            //c1 + c14 * n + c13 = O(n) = T_removeNode(n)
    }


    public void removeInternalNode(Position<E> p) throws InvalidPositionException{
        //evaluar condición: c1
        if(!isInternal(p)) throw new InvalidPositionException("la posición pasada por parámetro no corresponde a un nodo interno");
        removeNode(p); //llamado a un método de tiempo lineal: c2*n
        //T_removeInternalNode(n) = c1 + c2*n = O(n)
    }
    public void removeExternalNode(Position<E> p) throws InvalidPositionException{
        //evaluar condición: c1
        if(!isExternal(p)) throw new InvalidPositionException("la posición pasada por parámetro no corresponde a un nodo interno");
        removeNode(p); //llamado a un método de tiempo lineal: c2*n
        //T_removeInternalNode(n) = c1 + c2*n = O(n)
    }

    //implementación de iteradores
    private void preOrden(TNodo<E> nodo, PositionList<Position<E>> lista){
        /*Tiempo de ejecución: Vemos que el algoritmo pasa una vez por cada nodo i y en el nodo toma un tiempo constante c2
        y luego ejecuta un bucle que realiza hi iteraciones, con hi = la cantidad de hijos del nodo i. */

        //T(n) = c1 + sumatoria desde i=1 hasta n de (c2 + c3hi) = c1 + c2n + c3(n-1) = O(n)
        lista.addLast(nodo);
        for(TNodo<E> h : nodo.getHijos()) 
            preOrden(h, lista);  
    }

    public Iterable<Position<E>> positions(){
        //declarar variable y llamar a constructor: c1
        PositionList<Position<E>> lista = new ListaDoblementeEnlazada<>();
        //evaluar condición: c2
        //llamar a método de tiempo lineal: c3*n
        if(!isEmpty()) preOrden(raiz, lista);
        //retornar: c4
        return lista;
        //c1+c2+c3*n+c4 = O(n)
    }

    public Iterator<E> iterator() {
        //declarar variable y llamar a constructor: c1
        PositionList<E> lista = new ListaDoblementeEnlazada<>();
        //llamar a positions (tiempo lineal): c2*n
        for(Position<E> p : positions())
            //c3
            lista.addLast(p.element());
            //retornar: c4
        return lista.iterator();
        //c1 + c2*n(c3) + c4 = c1 + c$*n + c4 = O(n)
    }
}