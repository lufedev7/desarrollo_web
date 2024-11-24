package com.proyect.web.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "Excepción general de la aplicación web")
public class WebException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Estado HTTP", example = "BAD_REQUEST")
    private HttpStatus state;

    @Schema(description = "Mensaje de error", example = "Datos inválidos")
    private String message;

    public WebException(HttpStatus state, String message){
        super();
        this.state = state;
        this.message = message;
    }
    public WebException(HttpStatus state, String message,String message1){
        super();
        this.state = state;
        this.message = message;
        this.message = message1;
    }
    public HttpStatus getState() {
        return state;
    }
    public void setState(HttpStatus state) {
        this.state = state;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
