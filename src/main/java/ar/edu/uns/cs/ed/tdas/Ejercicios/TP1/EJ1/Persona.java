package ar.edu.uns.cs.ed.tdas.Ejercicios.TP1.EJ1;

public class Persona{
    private String nombre;
    private int dni;

    public Persona(String n, int dni){
        nombre = n;
        this.dni = dni;
    }

    public void establecerDNI(int i){
        dni = i;
    }

    public void establecerNombre(String s){
        nombre = s;
    }
    
    public int obtenerDNI(){
        return dni;
    }

    public String toString(){
        String s = null;
        if(this != null)
            s = ("Nombre: " + nombre + " DNI: " + dni);
        return s;
    }



}