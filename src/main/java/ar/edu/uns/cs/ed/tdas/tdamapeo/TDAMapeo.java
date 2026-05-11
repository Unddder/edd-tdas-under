package ar.edu.uns.cs.ed.tdas.tdamapeo;
import ar.edu.uns.cs.ed.tdas.Entry;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.excepciones.InvalidKeyException;
public class TDAMapeo<K,V> implements Map<K,V>{
	private int n;
	private Map<K,V>[] A;
	int N = 13;

	public TDAMapeo(){
		//asignar de dos valores y llamada a un constructor de tiempo constante: c1
		n = 0;
		A = (Map<K,V> []) new MapeoConLista[N];
		//cant iteraciones = N
		// cuerpo del for = c2
		// tiempo del for: N*c2 pero N es una constante fija (en este caso 13) entonces c2*N se reduce a c2
		for(int i=0; i<N; i++)
			A[i] = new MapeoConLista<K,V>(); // indexar arreglo, invocar a un constructor de tiempo constante: c3

		//c1+c2+c3 = O(1)
	}

	private int h(K k){
		int i = k.hashCode();
		if(i<0) i = -i;
		return i % N;
	}

	public V put(K k, V v){
		// tiempo de evaluar la condición: c1
		// tiempo de arrojar excepción: c2, pero cortaría el flujo de ejecución
		if(k == null) throw new InvalidKeyException("clave nula");
		// indexar un arreglo y llamar a un método de tiempo n y asignar un valor a una variable = c3*n
		V t = A[h(k)].put(k, v);
		//tiempo de evaluar condicion: c4
		if (t == null) 
			n++; // asignar un valor a una variable: c5
		return t; //retornar: c6
		// c1 + c3*n + c4 + c5 + c6 = O(1)
	}

	public V remove(K k){
		// tiempo de evaluar la condición: c1
		// tiempo de arrojar excepción: c2, pero cortaría el flujo de ejecución
		if(k == null) throw new InvalidKeyException("clave nula");
		// indexar un arreglo y llamar a un método de tiempo n y asignar un valor a una variable = c3*n
		V t = A[h(k)].remove(k);
		//tiempo de evaluar condicion: c4
		if(t != null)
			n--; // asignar un valor a una variable: c5
		return t; // retornar: c6
		// c1 + c3*n + c4 + c5 + c6 = O(n)
	}

	public V get(K k){
		//tiempo de evaluar la condicón: c1
		//tiempo de arrojar la excepción: c2, pero corta el flujo de ejecución asi que no lo tengo en cuenta
		if(k == null) throw new InvalidKeyException("clave nula");
		// llamada a un método de tiempo lineal: c3*n
		//retornar: c4
		return A[h(k)].get(k);
		//c1 + c3*n + c4 = O(n)
	}
	public int size(){return n;} // O(1)
	public boolean isEmpty(){return n==0;} //O(1)


	public Iterable<K> keys(){
		//O(n)
		ListaDoblementeEnlazada<K> lista = new ListaDoblementeEnlazada<>();
		for(int i = 0; i < N; i++)
			for(K k : A[i].keys())
				lista.addLast(k);
		return lista;
	}

	public Iterable<V> values(){
		//O(n)
		ListaDoblementeEnlazada<V> lista = new ListaDoblementeEnlazada<>();
		for(int i = 0; i < N; i++)
			for(V v : A[i].values())
				lista.addLast(v);
		return lista;
	}
	public Iterable<Entry<K,V>> entries(){
		//O(n)
		ListaDoblementeEnlazada<Entry<K,V>> lista = new ListaDoblementeEnlazada<>();
		for(int i = 0; i < N; i++)
			for(Entry<K,V> e : A[i].entries())
				lista.addLast(e);
		return lista;
	}

}
	

















