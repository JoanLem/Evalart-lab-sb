package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.PedidoRequest;
import com.example.demo.model.PedidoResponse;
import com.example.demo.service.PedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Endpoints para transformar JSON/XML y procesar pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/procesar")
    @Operation(
            summary = "Procesar pedido",
            description = "Recibe un pedido en JSON, lo transforma a XML, llama al endpoint externo y retorna la respuesta mapeada a JSON.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido procesado correctamente",
                    content = @Content(schema = @Schema(implementation = PedidoResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error procesando el pedido")
    })
    public ResponseEntity<PedidoResponse> procesarPedido(
            @RequestBody PedidoRequest request) {
        try {
            PedidoResponse response = pedidoService.procesarPedido(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint auxiliar: ver solo el XML generado
    @PostMapping("/preview-xml")
    @Operation(
            summary = "Previsualizar XML",
            description = "Transforma el JSON de entrada a XML sin invocar el endpoint externo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "XML generado correctamente",
                    content = @Content(mediaType = "application/xml"))
    })
    public ResponseEntity<String> previewXml(
            @RequestBody PedidoRequest request) {
        String xml = pedidoService.jsonToXml(request);
        return ResponseEntity.ok()
            .header("Content-Type", "application/xml")
            .body(xml);
    }
}