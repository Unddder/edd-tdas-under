package ar.edu.uns.cs.ed.tdas.excepciones;

public class BoundaryViolationException extends RuntimeException {
	public BoundaryViolationException(String msg) {
		super(msg);
	}
}
