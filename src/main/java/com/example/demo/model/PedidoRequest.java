package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Solicitud para enviar un pedido.")
public class PedidoRequest {

    @JsonProperty("enviarPedido")
    @Schema(description = "Datos del pedido a enviar.", requiredMode = Schema.RequiredMode.REQUIRED)
    private EnviarPedido enviarPedido;

    @Data
    @Schema(description = "Detalle del pedido.")
    public static class EnviarPedido {
        @Schema(description = "Numero unico del pedido.", example = "P-100")
        private String numPedido;
        @Schema(description = "Cantidad de unidades pedidas.", example = "2")
        private Integer cantidadPedido;
        @Schema(description = "Codigo EAN del producto.", example = "7701234567890")
        private String codigoEAN;
        @Schema(description = "Nombre del producto.", example = "Teclado")
        private String nombreProducto;
        @Schema(description = "Numero de documento del cliente.", example = "1234567890")
        private String numDocumento;
        @Schema(description = "Direccion de entrega.", example = "Calle 123 #45-67")
        private String direccion;
    }
}