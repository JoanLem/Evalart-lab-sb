package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PedidoRequest {

    @JsonProperty("enviarPedido")
    private EnviarPedido enviarPedido;

    @Data
    public static class EnviarPedido {
        private String numPedido;
        private Integer cantidadPedido; // cambiamos a Integer para manejar mejor las cantidades
        private String codigoEAN;
        private String nombreProducto;
        private String numDocumento;
        private String direccion;
    }
}