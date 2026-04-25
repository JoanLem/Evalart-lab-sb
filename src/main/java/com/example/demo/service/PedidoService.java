package com.example.demo.service;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.example.demo.model.PedidoRequest;
import com.example.demo.model.PedidoResponse;

@Service
public class PedidoService {
	
    @Value("${Endpoint}") // Inyectar el endpoint desde properties
    private String ENDPOINT;

    @Autowired
    private RestTemplate restTemplate;

    // ─── PASO 1: JSON → XML 
    public String jsonToXml(PedidoRequest request) {
    	
    	PedidoRequest.EnviarPedido p = request.getEnviarPedido();
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <Pedido>
                <pedido>%s</pedido>
                <Cantidad>%d</Cantidad>
                <EAN>%s</EAN>
                <Producto>%s</Producto>
                <Cedula>%s</Cedula>
                <Direccion>%s</Direccion>
            </Pedido>
            """.formatted(
                    p.getNumPedido(),
                    p.getCantidadPedido(),
                    p.getCodigoEAN(),
                    p.getNombreProducto(),
                    p.getNumDocumento(),
                    p.getDireccion() 
                );
    }

    // ─── PASO 2: Llamar al endpoint con XML ──────────────────────────
    public String llamarEndpoint(String xml) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        headers.set("Accept", MediaType.APPLICATION_XML_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(xml, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            ENDPOINT,
            HttpMethod.POST, 
            entity,
            String.class
        );

        return response.getBody();
    }

    // ─── PASO 3: XML → JSON (mapeo de respuesta) ─────────────────────
    public PedidoResponse xmlToJson(String xmlResponse) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(
            new ByteArrayInputStream(xmlResponse.getBytes())
        );

        document.getDocumentElement().normalize();

        // Mapeo: Codigo → codigoEnvio
        String codigoEnvio = getTagValue("Codigo", document);

        // Mapeo: Mensaje → estado
        String estado = getTagValue("Mensaje", document);
        
        PedidoResponse.EnviarPedidoRespuesta respuesta =
                new PedidoResponse.EnviarPedidoRespuesta(codigoEnvio, estado);

            return new PedidoResponse(respuesta);
    }

    // ─── Utilidad: extraer valor de tag XML ──────────────────────────
    private String getTagValue(String tag, Document document) {
        NodeList nodeList = document.getElementsByTagName(tag);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    // ─── Flujo completo ──────────────────────────────────────────────
    public PedidoResponse procesarPedido(PedidoRequest request) throws Exception {
        // 1. JSON → XML
        String xml = jsonToXml(request);

        // 2. Llamar endpoint
        String xmlResponse = llamarEndpoint(xml);

        // 3. XML → JSON
        return xmlToJson(xmlResponse);
    }
}
