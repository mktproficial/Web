package com.imobiliaria_api.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(String mensagem) {
        super(mensagem);
    }
}