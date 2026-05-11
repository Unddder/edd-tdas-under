package ar.edu.uns.cs.ed.tdas.tdamapeo;
import ar.edu.uns.cs.ed.tdas.Entry;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import java.util.Iterator;


public class MapeoConLista<K,V> implements Map<K,V> {
    
    protected static class Entrada<K,V> implements Entry<K,V>{ //implementar iteradores
		private K k;
		private V v;
		public Entrada(K key, V value){ //tiempo O(1): operaciones primitivas 
			k = key;
			v = value;
		}

		public V getValue(){return v;} //tiempo O(1): operaciones primitivas
		public K getKey(){return k;} //tiempo O(1): operaciones primitivas

		protected void setKey(K key){k = key;} //tiempo O(1): operaciones primitivas
		protected V setValue(V value){ //tiempo O(1): operaciones primitivas
			V old = v;
			v = value;
			return old;
		}
	}


    //iterador de Keys
    private class KeyIterator implements Iterator<K>{
        // llamada a metodo entries: declara entryiterable: tiempo c1: O(1)
        // llamada a iterator: declara entryiterator: tiempo c2: O(1)
        private Iterator<Entry<K,V>> entries = entries().iterator();
        // invoca hasNext() que tiene tiempo c3: O(1)
        public boolean hasNext(){return entries.hasNext();}
        // invoca un Next() de tiempo c4, y getKey() de tiempo c5: O(1)
        public K next(){return entries.next().getKey();}
        //tiempo c6: O(1)
        public void remove(){throw new UnsupportedOperationException();}
    }
    private class KeyIterable implements Iterable<K>{
        public Iterator<K> iterator(){return new KeyIterator();}// tiempo c1: O(1)
    }
    public Iterable<K> keys(){return new KeyIterable();}// tiempo c1: O(1)

    //Iterador de Values
    private class ValueIterator implements Iterator<V>{
        //invoca a entries de tiempo c1: O(1)
        //invoca a iterator de tiempo c2: O(1)
        private Iterator<Entry<K,V>> entries = entries().iterator();
        //invoca un metodo de tiempo constante c3: O(1)
        public boolean hasNext(){return entries.hasNext();}
        //invoca a next de tiempo constante c4: O(1)
        //invoca a getvalue de tiempo constante c5: O(1)
        public V next(){return entries.next().getValue();}
        //arroja excepción de tiempo constante c6: O(1)
        public void remove(){throw new UnsupportedOperationException();}
    }
    private class ValueIterable implements Iterable<V>{
        //invoca al constructor de ValueIterator() y lo retorna, tiempo c1: O(1)
        public Iterator<V> iterator(){return new ValueIterator();}
    }
    public Iterable<V> values(){return new ValueIterable();}// invoca al constructor de valueIterable() y lo retorna, tiempo c1: O(1)
    
    //Iterador de Entries
    private class EntryIterator implements Iterator<Entry<K,V>>{ 
        // invoca el método positions de tiempo c1
        // invoca el método iterator que es un constructor de tiempo c2
        //c1+c2 = O(1)
        private Iterator<Position<Entrada<K,V>>> it = S.positions().iterator();
        public boolean hasNext(){ // metodo de tiempo c1: O(1)
            return it.hasNext();
        }
        public Entry<K,V> next(){
            // tiempo de !hasNext() = c1
            if(!hasNext())
                //tiempo del throw = c2
                throw new java.util.NoSuchElementException("No hay más elementos");
            // invoca un metodo de tiempo constante y llama a element también de tiempo constante, tiempo c3 
            return it.next().element();
            //T(next()) = c1 + max(c2,c3) = O(1)
        }
        public void remove(){throw new UnsupportedOperationException();} //tiempo c1 = O(1)
    }

    private class EntryIterable implements Iterable<Entry<K,V>>{
        // retorna una invocación a un constructor de tiempo c1: O(1);
        public Iterator<Entry<K,V>> iterator(){return new EntryIterator();}
    }

    public Iterable<Entry<K,V>> entries(){return new EntryIterable();} // retorna una invocación a un constructor de tiempo c1: O(1);


    protected ListaDoblementeEnlazada<Entrada<K,V>> S;
    public MapeoConLista(){
        // llamar al constructor de DLL de tiempo constante: c1
        // se lo asigna a S: tiempo c2
        // c1 + c2 = O(1)
        S = new ListaDoblementeEnlazada<Entrada<K,V>>();
    }

    // llama al size() de S que es un método de tiempo constante: c1
    // lo retorna: tiempo c2
    // c1 + c2 = O(1)
    public int size(){return S.size();} 
    
    // llamar a size: c1, tiempo de size: c2, comparación c3, retorno: c4
    //c1+c2+c3+c4 = O(1)
    public boolean isEmpty(){return size() == 0;}
    
    public V put(K key, V value){
        // cant_iteraciones del for: n -> (this.size())
        // tiempo del cuerpo del for: c1
        // T(for m;S) = c1 * n
        for(Position<Entrada<K,V>> p : S.positions())
            if(p.element().getKey().equals(key)){ //evaluar condicion = c1
                // element y value tiempo constante y asignacion a aux constante = c2
                V aux = p.element().getValue();
                //llamada a element y setvalue = c3
                p.element().setValue(value);
                // retornar = c4
                return aux;
                /*si se cumple la condición del if hay un return por lo que cortaría el flujo de ejecución
                entonces el peor de los casos es que no se cumpla la condición para todas las iteraciones */
            }
        S.addLast(new Entrada<K,V>(key, value)); // tiempo c5
        return null; // tiempo c6
        // c1 * n + c5 + c6 = O(n)
    }

    public V get(K key){
        // cant_iteraciones del for: n -> (this.size())
        // tiempo del cuerpo del for: c1
        // T(for m;S) = c1 * n
        for(Position<Entrada<K,V>> p : S.positions())
            if(p.element().getKey().equals(key)) // evaluar condicion: c1
                // invoca 2 metodos de tiempo constante y los retorna: c2
                return p.element().getValue();
                /*si se cumple la condición del if hay un return por lo que cortaría el flujo de ejecución
                entonces el peor de los casos es que no se cumpla la condición para todas las iteraciones */
        return null; // retornar: c3
        //  c1 * n + c3 = O(n)
    }

    public V remove(K key){
        // cant_iteraciones del for: n -> (this.size())
        // tiempo del cuerpo del for: c1
        // T(for m;S) = c1 * n
        for(Position<Entrada<K,V>> p : S.positions())
            // evaluar condicion: c1
            if(p.element().getKey().equals(key)){
                //llamar a element y value de tiempo constante y asignarselo a value: c2
                V value = p.element().getValue();
                S.remove(p); // seguir referencia de un objeto y llamar a un método de tiempo constante: c3
                return value; // retornar: c4
                /*si se cumple la condición del if hay un return por lo que cortaría el flujo de ejecución
                entonces el peor de los casos es que no se cumpla la condición para todas las iteraciones */
            }
        return null; // retornar: c5
        //c1 * n + c5 = O(n)
    }

}
