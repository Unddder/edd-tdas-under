package ar.edu.uns.cs.ed.tdas.Ejercicios.TP2.Persona;
import java.util.Stack;
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

    public void Invertir(Persona[] p){
        Stack<Persona> pilaPer = new Stack<>();
        for(int i = 0; i < p.length; i++)
            pilaPer.push(p[i]);
        for(int i = 0; i < p.length; i++)
            p[i] = pilaPer.pop();
    }

    public String toString(){
        String s = null;
        if(this != null)
            s = ("Nombre: " + nombre + " DNI: " + dni);
        return s;
    }



}