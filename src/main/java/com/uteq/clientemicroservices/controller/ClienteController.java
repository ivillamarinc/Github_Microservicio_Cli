package com.uteq.clientemicroservices.controller;

import com.uteq.clientemicroservices.entity.Cliente;
import com.uteq.clientemicroservices.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // CREATE -> 201 Created
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        Cliente creado = clienteService.guardarCliente(cliente);

        // STATE-BASED: si se guardó bien -> 201
        return ResponseEntity.status(201).body(creado);
    }

    // READ ALL -> 200
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    // READ BY ID -> IF/ELSE (200 o 404)
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable Long id) {
        var opt = clienteService.obtenerClientePorId(id);

        // EJEMPLO pedido: IF código 200 muestra el cliente
        if (opt.isPresent()) {
            return ResponseEntity.ok(opt.get()); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // UPDATE -> 200
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(
            @PathVariable Long id,
            @RequestBody Cliente cliente) {

        Cliente actualizado = clienteService.actualizarCliente(id, cliente);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE -> 204
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }




}


