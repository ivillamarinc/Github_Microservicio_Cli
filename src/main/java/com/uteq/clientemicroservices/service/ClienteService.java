package com.uteq.clientemicroservices.service;

import com.uteq.clientemicroservices.entity.Cliente;
import com.uteq.clientemicroservices.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.uteq.clientemicroservices.exception.ApiException;
import com.uteq.clientemicroservices.exception.ApiErrorCode;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente guardarCliente(Cliente cliente) {

        if (cliente == null) {
            throw new ApiException(ApiErrorCode.VALIDATION_ERROR, "El cliente no puede ser null");
        }
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new ApiException(ApiErrorCode.VALIDATION_ERROR, "El nombre es obligatorio");
        }
        if (cliente.getEmail() == null || cliente.getEmail().trim().isEmpty()) {
            throw new ApiException(ApiErrorCode.VALIDATION_ERROR, "El email es obligatorio");
        }
        if (clienteRepository.existsByEmail(cliente.getEmail().trim())) {
            throw new ApiException(ApiErrorCode.EMAIL_DUPLICATE, "Ya existe un cliente con ese email");
        }

        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {

        // STATE-BASED
        if (clienteActualizado == null) {
            throw new IllegalArgumentException("Datos de actualización vacíos");
        }

        return clienteRepository.findById(id)
                .map(cliente -> {

                    // IF: solo actualiza si viene algo
                    if (clienteActualizado.getNombre() != null && !clienteActualizado.getNombre().trim().isEmpty()) {
                        cliente.setNombre(clienteActualizado.getNombre());
                    }

                    if (clienteActualizado.getEmail() != null && !clienteActualizado.getEmail().trim().isEmpty()) {
                        // si cambió email, validar repetido
                        String nuevoEmail = clienteActualizado.getEmail().trim();
                        if (!nuevoEmail.equalsIgnoreCase(cliente.getEmail())
                                && clienteRepository.existsByEmail(nuevoEmail)) {
                            throw new IllegalStateException("Ya existe un cliente con ese email");
                        }
                        cliente.setEmail(nuevoEmail);
                    }

                    if (clienteActualizado.getTelefono() != null) {
                        cliente.setTelefono(clienteActualizado.getTelefono());
                    }

                    return clienteRepository.save(cliente);
                })
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
    }

    public void eliminarCliente(Long id) {

        // STATE-BASED
        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        clienteRepository.deleteById(id);
    }
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

}
