package ar.edu.uns.cs.ed.tdas.Ejercicios.TP1;
public class TesterConjunto {
    public static void main(String[] args) {

        // Crear conjuntos
        Conjunto<Integer> c1 = new ConjuntoArreglo<>(10);
        Conjunto<Integer> c2 = new ConjuntoArreglo<>(10);

        // put()
        c1.put(1);
        c1.put(2);
        c1.put(3);

        c2.put(2);
        c2.put(3);
        c2.put(4);

        // size() e isEmpty()
        System.out.println("c1 size: " + c1.size()); // 3
        System.out.println("c1 vacío?: " + c1.isEmpty()); // false

        // pertenece()
        System.out.println("c1 contiene 2?: " + c1.pertenece(2)); // true
        System.out.println("c1 contiene 5?: " + c1.pertenece(5)); // false

        // get()
        System.out.println("Elemento en pos 0 de c1: " + c1.get(0));

        // interseccion()
        Conjunto<Integer> inter = c1.interseccion(c2);

        System.out.println("Intersección c1 ∩ c2:");
        for(int i = 0; i < inter.capacity(); i++){
            if(inter.get(i) != null){
                System.out.println(inter.get(i));
            }
        }

        // capacidad
        System.out.println("Capacidad de c1: " + c1.capacity());
            }
    }




