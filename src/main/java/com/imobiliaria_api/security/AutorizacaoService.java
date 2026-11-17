package com.imobiliaria_api.security;

import com.imobiliaria_api.entity.PerfilNome;
import com.imobiliaria_api.repository.AluguelRepository;
import com.imobiliaria_api.repository.LocacaoRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("autorizacao")
public class AutorizacaoService {

    private final LocacaoRepository locacaoRepository;
    private final AluguelRepository aluguelRepository;

    public AutorizacaoService(LocacaoRepository locacaoRepository, AluguelRepository aluguelRepository) {
        this.locacaoRepository = locacaoRepository;
        this.aluguelRepository = aluguelRepository;
    }

    public UsuarioDetails usuarioAtual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UsuarioDetails usuario)) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        return usuario;
    }

    public Integer idUsuarioAtual() {
        return usuarioAtual().getId();
    }

    public Integer clienteIdAtual() {
        return usuarioAtual().getClienteId();
    }

    public boolean isAdmin() {
        return usuarioAtual().possuiPerfil(PerfilNome.ADMIN);
    }

    public boolean isAtendente() {
        return usuarioAtual().possuiPerfil(PerfilNome.ATENDENTE);
    }

    public boolean isCliente() {
        return usuarioAtual().possuiPerfil(PerfilNome.CLIENTE);
    }

    public Integer clienteIdRestricao() {
        if (isAdmin() || isAtendente()) {
            return null;
        }

        if (!isCliente()) {
            throw new AccessDeniedException("Usuário sem permissão para consultar locações");
        }

        Integer clienteId = clienteIdAtual();
        if (clienteId == null) {
            throw new AccessDeniedException("Usuário CLIENTE sem vínculo com um cliente");
        }
        return clienteId;
    }

    public boolean podeAcessarLocacao(Integer locacaoId) {
        if (isAdmin() || isAtendente()) {
            return true;
        }

        if (!isCliente()) {
            return false;
        }

        Integer clienteId = clienteIdAtual();
        if (clienteId == null) {
            return false;
        }

        return locacaoRepository.findById(locacaoId)
                .map(locacao -> locacao.getCliente() != null
                        && clienteId.equals(locacao.getCliente().getId()))
                .orElse(false);
    }

    public boolean podeAcessarAluguel(Integer aluguelId) {
        if (isAdmin() || isAtendente()) {
            return true;
        }

        if (!isCliente()) {
            return false;
        }

        Integer clienteId = clienteIdAtual();
        if (clienteId == null) {
            return false;
        }

        return aluguelRepository.findById(aluguelId)
                .map(aluguel -> aluguel.getLocacao() != null
                        && aluguel.getLocacao().getCliente() != null
                        && clienteId.equals(aluguel.getLocacao().getCliente().getId()))
                .orElse(false);
    }

    public boolean podeCadastrarLocacao(Integer inquilinoId) {
        if (isAdmin() || isAtendente()) {
            return true;
        }

        if (!isCliente()) {
            return false;
        }

        Integer clienteId = clienteIdAtual();
        return clienteId != null && clienteId.equals(inquilinoId);
    }
}