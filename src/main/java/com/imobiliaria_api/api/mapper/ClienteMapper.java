package com.imobiliaria_api.api.mapper;

import com.imobiliaria_api.api.dto.input.ClienteRequest;
import com.imobiliaria_api.api.dto.output.ClienteResponse;
import com.imobiliaria_api.api.dto.output.ClienteResumoResponse;
import com.imobiliaria_api.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente paraEntidade(ClienteRequest request) {
        Cliente cliente = new Cliente();
        atualizarEntidade(cliente, request);
        return cliente;
    }

    public void atualizarEntidade(Cliente cliente, ClienteRequest request) {
        cliente.setNome(request.nomeCliente());
        cliente.setCpf(request.cpf());
        cliente.setTelefone(request.telefone());
        cliente.setEmail(request.email());
        cliente.setDtNascimento(request.dtNascimento());
    }

    public ClienteResponse paraResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getTelefone(),
                cliente.getEmail(),
                cliente.getDtNascimento()
        );
    }

    public ClienteResumoResponse paraResumo(Cliente cliente) {
        return new ClienteResumoResponse(cliente.getId(), cliente.getNome());
    }
}