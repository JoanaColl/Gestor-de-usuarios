package jcprojects.registro.exception;

public class UsrNotFoundException extends RuntimeException {
    public UsrNotFoundException(String mensaje) {
        super(mensaje);
    }
}
