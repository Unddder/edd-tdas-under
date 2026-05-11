package ar.edu.uns.cs.ed.tdas.Ejercicios.TP1.EJ1;

public class EJ1 {
    public static void cambiarDNI1(int dni){
    dni=000;}
    public static void cambiarDNI2(Persona p){
    p.establecerDNI(000);}
    public static void cambiarPersona1(Persona p){
    p=new Persona("Favio",55555555);}
    public static void cambiarPersona2(Persona p){
    p.establecerNombre("Favio");
    p.establecerDNI(55555555); }
    public static void main (String [] args){
    Persona p= new Persona("Alejandra",11111111);
    System.out.println(p.toString());
    cambiarDNI1(p.obtenerDNI());
    System.out.println(p.toString());
    cambiarDNI2(p);
    System.out.println(p.toString());
    cambiarPersona1(p);
    System.out.println(p.toString());
    cambiarPersona2(p);
    System.out.println(p.toString());
    }
}


/*RTA: LA SALIDA EN CONSOLA ES LA SIGUIENTE: 
Nombre: Alejandra DNI: 11111111
Nombre: Alejandra DNI: 11111111
Nombre: Alejandra DNI: 0
Nombre: Alejandra DNI: 0
Nombre: Favio DNI: 55555555*/