package com.imobiliaria_api.service;

import com.imobiliaria_api.api.dto.input.RegistroRequest;
import com.imobiliaria_api.api.dto.input.UsuarioRequest;
import com.imobiliaria_api.api.dto.input.UsuarioUpdateRequest;
import com.imobiliaria_api.exception.EntidadeNaoEncontradaException;
import com.imobiliaria_api.entity.Cliente;
import com.imobiliaria_api.entity.Perfil;
import com.imobiliaria_api.entity.PerfilNome;
import com.imobiliaria_api.entity.Usuario;
import com.imobiliaria_api.exception.NegocioException;
import com.imobiliaria_api.repository.ClienteRepository;
import com.imobiliaria_api.repository.PerfilRepository;
import com.imobiliaria_api.repository.UsuarioRepository;
import com.imobiliaria_api.security.AutorizacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private static final int MAX_BYTES_SENHA = 72;

    private final UsuarioRepository repository;
    private final PerfilRepository perfilRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final AutorizacaoService autorizacaoService;

    public UsuarioService(UsuarioRepository repository,
                          PerfilRepository perfilRepository,
                          ClienteRepository clienteRepository,
                          PasswordEncoder passwordEncoder,
                          AutorizacaoService autorizacaoService) {
        this.repository = repository;
        this.perfilRepository = perfilRepository;
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.autorizacaoService = autorizacaoService;
    }

    @Transactional(readOnly = true)
    public Page<Usuario> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Usuario buscarOuFalhar(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario criar(UsuarioRequest request) {
        validarTamanhoSenha(request.senha());
        validarLoginUnico(request.login(), null);

        Set<Perfil> perfis = resolverPerfis(request.perfis());
        Cliente cliente = validarVinculoCliente(perfis, request.clienteId(), null);

        Usuario usuario = new Usuario();
        usuario.setLogin(request.login());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setAtivo(request.ativo() == null || request.ativo());
        usuario.setPerfis(perfis);
        usuario.setCliente(cliente);

        return repository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Integer id, UsuarioUpdateRequest request) {
        Usuario usuario = buscarOuFalhar(id);
        validarLoginUnico(request.login(), id);

        Set<Perfil> perfis = resolverPerfis(request.perfis());
        validarAlteracaoDosPropriosPerfis(id, usuario, request.perfis());

        Cliente cliente = validarVinculoCliente(perfis, request.clienteId(), id);

        usuario.setLogin(request.login());
        usuario.setAtivo(request.ativo());
        usuario.setPerfis(perfis);
        usuario.setCliente(cliente);

        if (StringUtils.hasText(request.senha())) {
            validarTamanhoSenha(request.senha());
            usuario.setSenha(passwordEncoder.encode(request.senha()));
        }

        return repository.save(usuario);
    }

    @Transactional
    public void remover(Integer id) {
        if (autorizacaoService.idUsuarioAtual().equals(id)) {
            throw new NegocioException("Não é possível remover o próprio usuário");
        }
        Usuario usuario = buscarOuFalhar(id);
        repository.delete(usuario);
    }

    @Transactional
    public Usuario registrar(RegistroRequest request) {
        validarTamanhoSenha(request.senha());
        validarLoginUnico(request.login(), null);

        Perfil perfilCliente = perfilRepository.findByNome(PerfilNome.CLIENTE)
                .orElseThrow(() -> new NegocioException("Perfil CLIENTE não configurado"));

        Usuario usuario = new Usuario();
        usuario.setLogin(request.login());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setAtivo(true);
        usuario.setCliente(null);
        usuario.setPerfis(new HashSet<>(Set.of(perfilCliente)));

        return repository.save(usuario);
    }

    private void validarTamanhoSenha(String senha) {
        if (senha != null && senha.getBytes(StandardCharsets.UTF_8).length > MAX_BYTES_SENHA) {
            throw new NegocioException("A senha deve ter no máximo 72 bytes");
        }
    }

    private void validarLoginUnico(String login, Integer idAtual) {
        boolean existe = idAtual == null
                ? repository.existsByLogin(login)
                : repository.existsByLoginAndIdNot(login, idAtual);

        if (existe) {
            throw new NegocioException("Já existe um usuário cadastrado com este login");
        }
    }

    private Set<Perfil> resolverPerfis(Set<PerfilNome> nomes) {
        Set<Perfil> perfis = new HashSet<>();
        for (PerfilNome nome : nomes) {
            Perfil perfil = perfilRepository.findByNome(nome)
                    .orElseThrow(() -> new NegocioException("Perfil não encontrado: " + nome));
            perfis.add(perfil);
        }
        return perfis;
    }

    private Cliente validarVinculoCliente(Set<Perfil> perfis, Integer clienteId, Integer idAtual) {
        boolean possuiPerfilCliente = perfis.stream()
                .anyMatch(perfil -> perfil.getNome() == PerfilNome.CLIENTE);
        boolean possuiPerfilStaff = perfis.stream()
                .anyMatch(perfil -> perfil.getNome() == PerfilNome.ADMIN || perfil.getNome() == PerfilNome.ATENDENTE);

        if (possuiPerfilCliente && possuiPerfilStaff) {
            throw new NegocioException("O perfil CLIENTE não pode ser combinado com perfis administrativos");
        }

        if (!possuiPerfilCliente) {
            if (clienteId != null) {
                throw new NegocioException("Somente usuários CLIENTE podem ser vinculados a um cliente");
            }
            return null;
        }

        if (clienteId == null) {
            throw new NegocioException("Usuário CLIENTE deve estar vinculado a um cliente");
        }

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));

        boolean jaVinculado = idAtual == null
                ? repository.existsByClienteId(clienteId)
                : repository.existsByClienteIdAndIdNot(clienteId, idAtual);

        if (jaVinculado) {
            throw new NegocioException("Este cliente já possui um usuário vinculado");
        }

        return cliente;
    }

    private void validarAlteracaoDosPropriosPerfis(Integer id, Usuario usuario, Set<PerfilNome> novosPerfis) {
        if (!autorizacaoService.idUsuarioAtual().equals(id)) {
            return;
        }

        Set<PerfilNome> perfisAtuais = usuario.getPerfis().stream()
                .map(Perfil::getNome)
                .collect(Collectors.toSet());

        if (!perfisAtuais.equals(novosPerfis)) {
            throw new AccessDeniedException("Um usuário não pode alterar os próprios perfis");
        }
    }
}
