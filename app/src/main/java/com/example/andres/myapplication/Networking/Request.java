package com.example.andres.myapplication.Networking;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase abstracta que no puede ser instanciada y que representa un web request.
 * Hasta ahora tiene de subclases a {@link PostRequest} y a {@link GetRequest}.
 */
public abstract class Request {
    public static final String API_URL = "https://guasapuc.herokuapp.com/api/students";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_HEADER = "Token token=";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";
    public static final String ACCEPT = "Accept";

    /**
     * Factory que crea un {@link GetRequest} o un {@link PostRequest} dependiendo del modo que
     * es pasado de parámetro.
     * @param mode Puede ser {@link #GET} o {@link #POST}.
     * @return Nuevo request.
     */
    public static Request createRequest(String mode) {
        if (mode.equals(GET)) {
            return new GetRequest();
        }
        else if (mode.equals(POST)) {
            return new PostRequest();
        }
        return null;
    }

    /**
     * Metodo que debe ser 'overrided'. Realiza el request.
     * @param token Authorization token.
     * @return String responses.
     * @throws IOException
     */
    public ArrayList<String> perform(String token) throws IOException {
        return null;
    }

}
