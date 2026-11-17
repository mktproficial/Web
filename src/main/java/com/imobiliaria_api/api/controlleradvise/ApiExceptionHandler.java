package com.imobiliaria_api.api.controlleradvise;

import com.imobiliaria_api.exception.EntidadeNaoEncontradaException;
import com.imobiliaria_api.exception.NegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private final MessageSource messageSource;

    public ApiExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrosDeValidacao handle(MethodArgumentNotValidException exception) {

        ErrosDeValidacao erros = new ErrosDeValidacao(LocalDateTime.now(),
                "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente");

        for (FieldError field : exception.getBindingResult().getFieldErrors()) {
            String mensagem = messageSource.getMessage(field, LocaleContextHolder.getLocale());
            erros.adiciona(new Erro(field.getField(), mensagem));
        }

        return erros;
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<ErrosDeValidacao> handle(NegocioException exception) {
        ErrosDeValidacao erros = new ErrosDeValidacao(LocalDateTime.now(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(erros);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<ErrosDeValidacao> handle(EntidadeNaoEncontradaException exception) {
        ErrosDeValidacao erros = new ErrosDeValidacao(LocalDateTime.now(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrosDeValidacao> handle(AccessDeniedException exception) {
        ErrosDeValidacao erros = new ErrosDeValidacao(LocalDateTime.now(),
                "Você não tem permissão para acessar este recurso");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erros);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrosDeValidacao> handle(DataIntegrityViolationException exception) {
        ErrosDeValidacao erros = new ErrosDeValidacao(LocalDateTime.now(),
                "Operação não permitida: existem dados relacionados a este registro");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erros);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class})
    public ResponseEntity<ErrosDeValidacao> handleRequisicaoInvalida(Exception exception) {
        ErrosDeValidacao erros = new ErrosDeValidacao(LocalDateTime.now(),
                "A requisição contém dados inválidos");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrosDeValidacao> handleRecursoNaoEncontrado(NoResourceFoundException exception) {
        ErrosDeValidacao erros = new ErrosDeValidacao(LocalDateTime.now(), "Recurso não encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erros);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrosDeValidacao> handleMetodoNaoSuportado(HttpRequestMethodNotSupportedException exception) {
        ErrosDeValidacao erros = new ErrosDeValidacao(LocalDateTime.now(), "Método HTTP não suportado");

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED);
        if (exception.getSupportedHttpMethods() != null) {
            builder.allow(exception.getSupportedHttpMethods().toArray(HttpMethod[]::new));
        }
        return builder.body(erros);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrosDeValidacao> handleTipoConteudoNaoSuportado(HttpMediaTypeNotSupportedException exception) {
        ErrosDeValidacao erros = new ErrosDeValidacao(LocalDateTime.now(), "Tipo de conteúdo não suportado");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(erros);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrosDeValidacao> handleTipoRespostaNaoAceito(HttpMediaTypeNotAcceptableException exception) {
        ErrosDeValidacao erros = new ErrosDeValidacao(LocalDateTime.now(), "Tipo de resposta não suportado");
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(erros);
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class, PropertyReferenceException.class})
    public ResponseEntity<ErrosDeValidacao> handleParametroConsultaInvalido(Exception exception) {
        ErrosDeValidacao erros = new ErrosDeValidacao(LocalDateTime.now(), "Parâmetro de consulta inválido");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrosDeValidacao> handleErroNaoTratado(Exception exception) {
        LOGGER.error("Erro não tratado", exception);
        ErrosDeValidacao erros = new ErrosDeValidacao(LocalDateTime.now(),
                "Ocorreu um erro inesperado. Entre em contato com a administração.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erros);
    }
}