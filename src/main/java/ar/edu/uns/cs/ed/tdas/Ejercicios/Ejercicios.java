package ar.edu.uns.cs.ed.tdas.Ejercicios;
import java.util.Iterator;

import ar.edu.uns.cs.ed.tdas.Entry;
import ar.edu.uns.cs.ed.tdas.tdalista.ListaDoblementeEnlazada;
import ar.edu.uns.cs.ed.tdas.tdalista.PositionList;
import ar.edu.uns.cs.ed.tdas.tdamapeo.*;
import ar.edu.uns.cs.ed.tdas.tdadiccionario.*;
public class Ejercicios <K,V> {
    protected static class Entrada<K,V> implements Entry<K,V>{ //implementar iteradores
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

    public PositionList<Entry<Integer,Integer>> ejA(Map<Integer,Integer> m1, Map<Integer,Integer> m2){
        PositionList<Entry<Integer,Integer>> nueva = new ListaDoblementeEnlazada<>();
        for(Integer k1 : m1.keys())
            if(!m1.get(k1).equals(m2.get(k1)) && m2.get(k1) != null){
                Entry<Integer,Integer> p1 = new Entrada<>(k1, m1.get(k1));
                Entry<Integer,Integer> p2 = new Entrada<>(k1, m2.get(k1));
                nueva.addLast(p1); nueva.addLast(p2);
            }
        return nueva;
    }

    public boolean ej2(Map<K,V> m1, Map<K,V> m2){
        for(K k1 : m1.keys())
            if(m2.get(k1) == null)
                return false;
        return true;
    }

    public Dictionary<K,V> acomodar(Dictionary<K,V> d){
        Map<K,V> aux = new MapeoConLista<K,V>();
        for(Entry<K,V> e : d.entries())
            aux.put(e.getKey(),e.getValue());
        Dictionary<K,V> dRes = new DiccionarioConLista<K,V>();
        for(K k1 : aux.keys())
            dRes.insert(k1, aux.get(k1));
        return dRes;
    }
    public Map<Character,Integer> ejercicioD(PositionList<Character> pl){
        Map<Character, Integer> nuevo = new MapeoConLista<Character,Integer>();
        for(Character c : pl)
            if (nuevo.get(c) == null)
                nuevo.put(c,1);
            else nuevo.put(c, nuevo.get(c) + 1);
        return nuevo;
    }
}
