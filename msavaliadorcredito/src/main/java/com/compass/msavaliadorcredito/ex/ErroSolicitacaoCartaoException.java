package com.compass.msavaliadorcredito.ex;

public class ErroSolicitacaoCartaoException extends RuntimeException {
    public ErroSolicitacaoCartaoException (String message) {
        super(message);
    }
}