package com.sistemacompras.sistemacompras_api.dto;

public class MensajeResponseDto {
    private String mensaje;
    private String token;

    public MensajeResponseDto(String mensaje, String token) {
        this.mensaje = mensaje;
        this.token = token;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getToken() {
        return token;
    }
}


