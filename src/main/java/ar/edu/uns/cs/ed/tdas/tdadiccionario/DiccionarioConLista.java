package ar.edu.uns.cs.ed.tdas.tdadiccionario;
import ar.edu.uns.cs.ed.tdas.Entry;
import ar.edu.uns.cs.ed.tdas.Position;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidKeyException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidEntryException;
import java.util.Iterator;

public class DiccionarioConLista<K,V> implements Dictionary<K,V>{
    protected static class Entrada<K,V> implements Entry<K,V>{ 
		private K k;
		private V v;
		public Entrada(K key, V value){
			k = key;
			v = value;
		}

		public V getValue(){return v;}
		public K getKey(){return k;}

		protected void setKey(K key){k = key;}
		protected V setValue(V value){
			V old = v;
			v = value;
			return old;
		}
	}
    //Iterador de Entries
    private class EntryIterator implements Iterator<Entry<K,V>>{
        private Iterator<Position<Entrada<K,V>>> it = S.positions().iterator();
        public boolean hasNext(){
            return it.hasNext();
        }
        public Entry<K,V> next(){
            if(!hasNext())
                throw new java.util.NoSuchElementException("No hay más elementos"); 
            return it.next().element();
        }
        public void remove(){throw new UnsupportedOperationException();}
    }
    private class EntryIterable implements Iterable<Entry<K,V>>{
        public Iterator<Entry<K,V>> iterator(){return new EntryIterator();}
    }
    public Iterable<Entry<K,V>> entries(){return new EntryIterable();}

    protected ListaDoblementeEnlazada<Entrada<K,V>> S;

    public DiccionarioConLista(){
        S = new ListaDoblementeEnlazada<Entrada<K,V>>();
    }

    public int size(){return S.size();}
    public boolean isEmpty(){return size() == 0;}
    public Entry<K,V> find(K key){
        if(key == null) throw new InvalidKeyException("La clave pasada por parametro es nula");
        for(Position<Entrada<K,V>> p : S.positions())
            if(p.element().getKey().equals(key))
                return p.element();
        return null;
    }
    public Iterable<Entry<K,V>> findAll(K key){
        //evaluar condición: c1
        //arrojar excepción: c2, pero corta el flujo de ejecución por lo que no la tenemos en cuenta
        if(key == null) throw new InvalidKeyException("La clave pasada por parametro es nula");
        // declaración del objeto y llamado al constructor de tiempo constante = c3
        ListaDoblementeEnlazada<Entry<K,V>> nueva = new ListaDoblementeEnlazada<>();
        //cant iteraciones del for: n
        //tiempo del cuerpo del for: c4+c5
        // tiempo del for: n*(c4+c5)
        for(Position<Entrada<K,V>> p : S.positions()){
            //evaluar condición donde se rastrea un objeto y se llama a 3 metodos constantes: c4
            if(p.element().getKey().equals(key))
                //llamada a un metodo constante (en la DLL addLast es de O(1)) y pasar como parametro un metodo constante: c5
                nueva.addLast(p.element()); 
        }
        return nueva; // tiempo de retornar: c6
        //c1 + c3 + n*(c4+c5) + c6 = O(n)
    }

    public Entry<K,V> insert(K key, V value){
        if(key == null) throw new InvalidKeyException("La clave pasada por parametro es nula");
        Entrada<K,V> nueva = new Entrada<K,V>(key, value);
        S.addLast(nueva);
        return nueva;
    }
    public Entry<K,V> remove(Entry<K,V> e) throws InvalidKeyException{
        if(e == null || e.getKey() == null) throw new InvalidEntryException("La clave pasada por parametro es nula");
        for(Position<Entrada<K,V>> p : S.positions())
            if(p.element().equals(e)){
                Entrada<K,V> aux = p.element();
                S.remove(p);
                return aux;
            }
        throw new InvalidEntryException("la entrada pasada por parametro no está en el diccionario");
        //tiempo de ejecucion: O(N)
    }


}
