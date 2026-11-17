package com.imobiliaria_api.security;

import com.imobiliaria_api.entity.Perfil;
import com.imobiliaria_api.entity.PerfilNome;
import com.imobiliaria_api.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UsuarioDetails implements UserDetails {

    private final Integer id;
    private final String login;
    private final String senha;
    private final boolean ativo;
    private final Set<PerfilNome> perfis;
    private final Integer clienteId;

    public UsuarioDetails(Usuario usuario) {
        this.id = usuario.getId();
        this.login = usuario.getLogin();
        this.senha = usuario.getSenha();
        this.ativo = usuario.isAtivo();
        this.perfis = usuario.getPerfis().stream()
                .map(Perfil::getNome)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(PerfilNome.class)));
        this.clienteId = usuario.getCliente() == null ? null : usuario.getCliente().getId();
    }

    public Integer getId() {
        return id;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public boolean possuiPerfil(PerfilNome perfil) {
        return perfis.contains(perfil);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis.stream()
                .map(perfil -> new SimpleGrantedAuthority("ROLE_" + perfil.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}