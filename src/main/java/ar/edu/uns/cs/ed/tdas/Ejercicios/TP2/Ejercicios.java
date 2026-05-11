package ar.edu.uns.cs.ed.tdas.Ejercicios.TP2;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
public class Ejercicios {
    public Queue<Integer> elementosImpares(Queue<Integer> cola){
        Queue<Integer> nueva = new LinkedList<Integer>();
        int n = cola.size();
        for(int i=0; i<n; i++){
            Integer actual = cola.remove();
            if(actual % 2 != 0 && !cola.isEmpty())
                nueva.add(actual);
        }
        return nueva;          
    }

    public <E> Stack<E> intercalar(Stack<E> p1, Stack<E> p2){
        /*preguntar por qué debo agregar <E> antes del tipo de retorno
        preguntar si intercala correctamente las pilas */ 
        Stack<E> nueva = new Stack<E>();
        while(!p1.isEmpty() || !p2.isEmpty()){
            if (!p1.isEmpty())
                nueva.push(p1.pop());
            if(!p2.isEmpty())
                nueva.push(p2.pop());
        }
        return nueva;
    } 

    public int mayor(Queue<Integer> q){
        Queue<Integer> temp = new LinkedList<>();
        int mayor = q.element();
        while(!q.isEmpty()){
            if(q.element() > mayor)
                mayor = q.element();
            temp.add(q.remove());
        }
        while(!temp.isEmpty())
            q.add(temp.remove());
        return mayor;
    }

}
