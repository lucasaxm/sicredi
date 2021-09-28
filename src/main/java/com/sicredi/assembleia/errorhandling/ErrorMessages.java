package com.sicredi.assembleia.errorhandling;

public final class ErrorMessages {
    public static final String INVALID_JSON = "JSON Inválido";
    public static final String VALIDATION_ERROR = "Erro de validação";
    public static final String NO_SEARCH_PARAMS = "Nenhum parâmetro de busca fornecido.";
    public static final String DATA_NOT_FOUND = "O registro com id %s não foi encontrado.";
    public static final String SESSION_CLOSED = "Sessão %s fechada.";
    public static final String VOTING_AGAIN = "Associado %s já votou.";
    public static final String INVALID_CPF = "CPF \"%s\" inválido.";
    public static final String CPF_UNABLE_TO_VOTE = "CPF \"%s\" não permitido votar.";

    private ErrorMessages(){}
}
