package jcprojects.registro.exception;

public class InternalServerErrorException extends RuntimeException {
    public InternalServerErrorException(String mensaje) {
        super(mensaje);
    }
}
