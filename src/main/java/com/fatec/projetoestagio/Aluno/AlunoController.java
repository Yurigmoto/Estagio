package com.fatec.projetoestagio.Aluno;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired
    private AlunoRepository alunoRepository;

    @GetMapping("/formulario")
    public String formulario() {
        return "aluno/formulario";
    }

    @PostMapping
    public String cadastrar(@Valid DadosCadastroAluno dados, BindingResult result, RedirectAttributes flash) {
        if (result.hasErrors()) {
            return voltar(mensagens(result), dados, flash);
        }
        if (alunoRepository.existsByRa(dados.ra())) {
            return voltar("Já existe um aluno cadastrado com este RA.", dados, flash);
        }
        try {
            alunoRepository.save(new Aluno(dados));
        } catch (DataIntegrityViolationException e) {
            return voltar("Já existe um aluno cadastrado com este RA.", dados, flash);
        }
        flash.addFlashAttribute("mensagem", "Aluno cadastrado com sucesso.");
        return "redirect:/aluno/formulario";
    }

    // Volta ao formulário com o aviso e com o que a pessoa já digitou (dados pode ser null).
    private String voltar(String erro, DadosCadastroAluno dados, RedirectAttributes flash) {
        flash.addFlashAttribute("erro", erro);
        if (dados != null) {
            flash.addFlashAttribute("dados", dados);
        }
        return "redirect:/aluno/formulario";
    }

    // Junta as mensagens de validação numa frase só.
    private String mensagens(BindingResult result) {
        // campos que nem conseguiram ser convertidos (ex.: data fora do formato)
        Set<String> falhas = new HashSet<>();
        for (FieldError e : result.getFieldErrors()) {
            if (e.isBindingFailure()) falhas.add(e.getField());
        }
        return result.getAllErrors().stream()
                .map(e -> texto(e, falhas))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.joining("; "));
    }

    private String texto(ObjectError erro, Set<String> falhas) {
        if (erro instanceof FieldError campo) {
            if (campo.isBindingFailure()) {
                return "nascimento".equals(campo.getField())
                        ? "Data de nascimento inválida. Use o formato dd/mm/aaaa."
                        : "Valor inválido no campo '" + campo.getField() + "'.";
            }
            if (falhas.contains(campo.getField())) return null; // já avisamos que o valor é inválido
        }
        return erro.getDefaultMessage();
    }
}
