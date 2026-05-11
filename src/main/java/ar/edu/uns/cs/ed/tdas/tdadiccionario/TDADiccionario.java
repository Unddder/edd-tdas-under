package ar.edu.uns.cs.ed.tdas.tdadiccionario;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.Entry;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidKeyException;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidEntryException;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
public class TDADiccionario<K,V> implements Dictionary<K,V> {
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

    private int n;
	private Dictionary<K,V>[] A;
	int N = 13;

	public TDADiccionario(){
		n = 0;
		A = (Dictionary<K,V> []) new DiccionarioConLista[N];
		for(int i=0; i<N; i++)
			A[i] = new DiccionarioConLista<K,V>();
	}

	private int h(K k){
		int i = k.hashCode();
		if(i<0) i = -i;
		return i % N;
	}

    public int size(){return n;}
	public boolean isEmpty(){return n==0;}

    public Entry<K,V> find(K key){
        if(key == null) throw new InvalidKeyException("La clave pasada por parametro es nula");
        return A[h(key)].find(key);}

    public Iterable<Entry<K,V>> findAll(K key){
        //peor caso: todas las entradas estan en el bucket A[h(key)]: O(n)
        if(key == null) throw new InvalidKeyException("La clave pasada por parametro es nula");
        return A[h(key)].findAll(key);
    }

    public Entry<K,V> insert(K key, V value){
        if(key == null) throw new InvalidKeyException("La clave pasada por parametro es nula");
        Entry<K,V> e = A[h(key)].insert(key, value);
        n++;
        return e;
    }

    public Entry<K,V> remove(Entry<K,V> e) throws InvalidKeyException{
        //evaluar condicion: c1
        //arrojar excepción: c2 pero corta el flujo de ejecución, no lo tengo en cuenta
        if(e == null || e.getKey() == null) throw new InvalidEntryException("La clave pasada por parametro es nula");
        //declaración de objeto, indexar un arreglo, seguir referencia de e, 
        // llamar a un metodo de tiempo constante y llamar a otro de tiempo lineal: c3*n 
        Entry<K,V> eliminado = A[h(e.getKey())].remove(e);
        //decrementar: c4
        n--;
        //retornar: c5
        return eliminado;
        //c1 + c2 + c3*n + c4 + c5 = O(n)
    }

    public Iterable<Entry<K,V>> entries(){
    PositionList<Entry<K,V>> lista = new ListaDoblementeEnlazada<>();
    for(int i = 0; i < N; i++)
        for(Entry<K,V> e : A[i].entries())
            lista.addLast(e);
    return lista;
    }

    public Iterable<Entry<K,V>> eliminarTodas(K c, V v) throws InvalidKeyException{
        //evaluar condicion: c1
        //arrojar excepción: c2, pero corta el flujo de ejecución entonces no la tnego en cuenta
        if(c == null) throw new InvalidKeyException("Clave Inválida");
        //invoca al constructor de tiempo constante y se lo asigna al objeto declarado: c3
        PositionList<Entry<K,V>> pl = new ListaDoblementeEnlazada<>();
        //llamada al método findAll(c) de tiempo lineal = n
        //peor caso: todas las entradas tienen la clave c
        //cant iteraciones del for = n
        //tiempo del for: n*c4 + (n(n+1))/2
        for(Entry<K,V> e : findAll(c))
            //tiempo de evaluar la condición: c4
            if(e.getValue().equals(v))
                //rastrear un objeto, llamar a un metodo de tiempo constante y pasarle por parametro
                //un llamado a un metodo de tiempo lineal: c5*n, pero entonces cada iteración n disminuiria
                //primero iteraría n veces, despues n-1,..., 1 vez entonces es la sumatoria desde i=0 hasta n de i
                // esto es (n(n+1)/2)
                pl.addLast(remove(e));
        //seguir la referencia de un objeto y retornar: c6
        return pl;
        //c1 + c3 + n + n*c4 + n*(n(n+1))/2 + c6
        //c1 + c3 + n + c4*n + (n^2 + n)/2 + c6 = O(n^2)
    }
}
