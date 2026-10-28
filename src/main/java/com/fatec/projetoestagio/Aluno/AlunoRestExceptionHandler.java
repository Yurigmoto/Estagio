package com.fatec.projetoestagio.Aluno;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Transforma os erros da API em JSON legível.
 * assignableTypes garante que vale SÓ para o AlunoRestController
 */
@RestControllerAdvice(assignableTypes = AlunoRestController.class)
public class AlunoRestExceptionHandler {

    // @Valid falhou: devolve campo -> mensagem
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacao(MethodArgumentNotValidException ex) {
        Map<String, String> erros = new LinkedHashMap<>();
        ex.getBindingResult().getAllErrors().forEach(e -> {
            String campo = (e instanceof FieldError f) ? f.getField() : e.getObjectName();
            erros.putIfAbsent(campo, e.getDefaultMessage());
        });
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("status", HttpStatus.BAD_REQUEST.value());
        corpo.put("mensagem", "Dados inválidos");
        corpo.put("erros", erros);
        return ResponseEntity.badRequest().body(corpo);
    }

    // JSON malformado, enum inexistente ou data fora do formato yyyy-MM-dd
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> jsonInvalido(HttpMessageNotReadableException ex) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("status", HttpStatus.BAD_REQUEST.value());
        corpo.put("mensagem", "JSON inválido. Confira o formato dos campos (data: yyyy-MM-dd; curso e período: valores do enum).");
        return ResponseEntity.badRequest().body(corpo);
    }

    // 404 (não encontrado) e 409 (RA duplicado) vindos do service
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> status(ResponseStatusException ex) {
        Map<String, Object> corpo = new LinkedHashMap<>();
        corpo.put("status", ex.getStatusCode().value());
        corpo.put("mensagem", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(corpo);
    }
}
