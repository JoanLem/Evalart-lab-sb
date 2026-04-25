package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.PedidoRequest;
import com.example.demo.model.PedidoResponse;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PedidoService pedidoService;

    @Captor
    private ArgumentCaptor<HttpEntity<String>> entityCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(pedidoService, "ENDPOINT", "https://example.test/pedidos");
    }

    @Test
    void jsonToXml_debeConstruirXmlConValoresDelRequest() {
        PedidoRequest request = crearRequest();

        String xml = pedidoService.jsonToXml(request);

        assertTrue(xml.contains("<pedido>P-100</pedido>"));
        assertTrue(xml.contains("<Cantidad>2</Cantidad>"));
        assertTrue(xml.contains("<EAN>7701234567890</EAN>"));
        assertTrue(xml.contains("<Producto>Teclado</Producto>"));
        assertTrue(xml.contains("<Cedula>1234567890</Cedula>"));
        assertTrue(xml.contains("<Direccion>Calle 123</Direccion>"));
    }

    @Test
    void llamarEndpoint_debeEnviarXmlConHeadersYRetornarBody() {
        String xml = "<Pedido><pedido>P-100</pedido></Pedido>";
        when(restTemplate.exchange(
                eq("https://example.test/pedidos"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)))
                .thenReturn(ResponseEntity.ok("<Respuesta><Codigo>00</Codigo></Respuesta>"));

        String response = pedidoService.llamarEndpoint(xml);

        assertEquals("<Respuesta><Codigo>00</Codigo></Respuesta>", response);
        verify(restTemplate).exchange(
                eq("https://example.test/pedidos"),
                eq(HttpMethod.POST),
                entityCaptor.capture(),
                eq(String.class));

        HttpEntity<String> sentEntity = entityCaptor.getValue();
        assertEquals(xml, sentEntity.getBody());
        assertEquals(MediaType.APPLICATION_XML, sentEntity.getHeaders().getContentType());
        assertEquals(MediaType.APPLICATION_XML_VALUE, sentEntity.getHeaders().getFirst("Accept"));
    }

    @Test
    void xmlToJson_debeMapearCodigoYMensaje() throws Exception {
        String xmlResponse = """
                <EnviarPedidoRespuesta>
                  <Codigo>ABC123</Codigo>
                  <Mensaje>OK</Mensaje>
                </EnviarPedidoRespuesta>
                """;

        PedidoResponse response = pedidoService.xmlToJson(xmlResponse);

        assertEquals("ABC123", response.getEnviarPedidoRespuesta().getCodigoEnvio());
        assertEquals("OK", response.getEnviarPedidoRespuesta().getEstado());
    }

    @Test
    void xmlToJson_conXmlInvalido_debeLanzarExcepcion() {
        String xmlInvalido = "<EnviarPedidoRespuesta><Codigo>ABC123</Codigo>";

        assertThrows(Exception.class, () -> pedidoService.xmlToJson(xmlInvalido));
    }

    @Test
    void procesarPedido_debeEjecutarFlujoCompleto() throws Exception {
        PedidoRequest request = crearRequest();
        when(restTemplate.exchange(
                eq("https://example.test/pedidos"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class)))
                .thenReturn(ResponseEntity.ok("""
                        <EnviarPedidoRespuesta>
                          <Codigo>COD-77</Codigo>
                          <Mensaje>RECIBIDO</Mensaje>
                        </EnviarPedidoRespuesta>
                        """));

        PedidoResponse response = pedidoService.procesarPedido(request);

        assertEquals("COD-77", response.getEnviarPedidoRespuesta().getCodigoEnvio());
        assertEquals("RECIBIDO", response.getEnviarPedidoRespuesta().getEstado());
        verify(restTemplate).exchange(
                eq("https://example.test/pedidos"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class));
    }

    private PedidoRequest crearRequest() {
        PedidoRequest request = new PedidoRequest();
        PedidoRequest.EnviarPedido enviarPedido = new PedidoRequest.EnviarPedido();
        enviarPedido.setNumPedido("P-100");
        enviarPedido.setCantidadPedido(2);
        enviarPedido.setCodigoEAN("7701234567890");
        enviarPedido.setNombreProducto("Teclado");
        enviarPedido.setNumDocumento("1234567890");
        enviarPedido.setDireccion("Calle 123");
        request.setEnviarPedido(enviarPedido);
        return request;
    }
}
