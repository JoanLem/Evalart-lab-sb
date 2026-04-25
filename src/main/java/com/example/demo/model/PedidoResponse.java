package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Respuesta del proceso de pedido.")
public class PedidoResponse {


	@JsonProperty("enviarPedidoRespuesta")
	@Schema(description = "Resultado del pedido procesado.")
    private EnviarPedidoRespuesta enviarPedidoRespuesta;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Detalle de la respuesta del pedido.")
    public static class EnviarPedidoRespuesta {
        @Schema(description = "Codigo de envio retornado por el endpoint externo.", example = "COD-77")
        private String codigoEnvio;
        @Schema(description = "Estado del procesamiento.", example = "RECIBIDO")
        private String estado;
    }
}