package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.PedidoRequest;
import com.example.demo.model.PedidoResponse;
import com.example.demo.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/procesar")
    public ResponseEntity<PedidoResponse> procesarPedido(
            @RequestBody PedidoRequest request) {
        try {
            PedidoResponse response = pedidoService.procesarPedido(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Endpoint auxiliar: ver solo el XML generado
    @PostMapping("/preview-xml")
    public ResponseEntity<String> previewXml(
            @RequestBody PedidoRequest request) {
        String xml = pedidoService.jsonToXml(request);
        return ResponseEntity.ok()
            .header("Content-Type", "application/xml")
            .body(xml);
    }
}