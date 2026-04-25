package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponse {


	@JsonProperty("enviarPedidoRespuesta")
    private EnviarPedidoRespuesta enviarPedidoRespuesta;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnviarPedidoRespuesta {
        private String codigoEnvio;
        private String estado;
    }
}