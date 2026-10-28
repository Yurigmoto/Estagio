package com.fatec.projetoestagio.Aluno;

import com.fatec.projetoestagio.Aluno.Curso;
import com.fatec.projetoestagio.Aluno.Periodo;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

// Os nomes dos componentes são os atributos name="" do HTML (comoSoube = name="comoSoube").
// Serializable: o controller devolve este objeto ao formulário quando há erro (flash attribute).
public record DadosCadastroAluno(
        @NotBlank(message = "Informe o nome completo") String nome,
        @NotBlank(message = "Informe o e-mail")
        @Email(regexp = ".+@.+\\..+", message = "E-mail inválido") String email,
        @NotBlank(message = "Informe o RA")
        @Pattern(regexp = "\\d*", message = "O RA deve ter apenas números") String ra,
        @NotBlank(message = "Selecione o gênero") String genero,
        @NotNull(message = "Selecione o curso") Curso curso,
        @NotNull(message = "Informe a data de nascimento")
        @Past(message = "A data de nascimento deve estar no passado")
        @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate nascimento,
        @NotNull(message = "Selecione o semestre")
        @Min(value = 1, message = "Semestre inválido")
        @Max(value = 10, message = "Semestre inválido") Integer semestre,
        @NotNull(message = "Selecione o período") Periodo periodo,
        @NotBlank(message = "Selecione a etnia") String etnia,
        @NotBlank(message = "Selecione o nível de escolaridade") String escolaridade,
        @NotBlank(message = "Informe como ficou sabendo do curso") String comoSoube,
        String outros,
        @NotBlank(message = "Selecione a renda familiar") String renda,
        @NotBlank(message = "Informe o CEP")
        @Pattern(regexp = "\\d*", message = "O CEP deve ter apenas números")
        @Size(min = 8, max = 8, message = "O CEP deve ter 8 dígitos") String cep,
        @NotBlank(message = "Cidade não preenchida: confira o CEP") String cidade,
        @NotBlank(message = "Estado não preenchido: confira o CEP")
        @Size(min = 2, max = 2, message = "Use a sigla do estado com 2 letras (ex.: SP)") String estado,
        String deficiencia
) implements Serializable {

    // Roda antes da validação: "03.828-000" vira "03828000" e espaços sobrando somem.
    public DadosCadastroAluno {
        nome = nome == null ? null : nome.trim();
        email = email == null ? null : email.trim().toLowerCase();
        ra = ra == null ? null : ra.trim();
        cep = cep == null ? null : cep.replaceAll("\\D", "");
        cidade = cidade == null ? null : cidade.trim();
        estado = estado == null ? null : estado.trim().toUpperCase();
    }

    @AssertTrue(message = "Descreva como ficou sabendo do curso")
    public boolean isOutrosDescrito() {
        return !"outros".equals(comoSoube) || (outros != null && !outros.isBlank());
    }

    // Barra períodos que o curso não oferece (mesmo se alguém burlar o formulário).
    // Se curso, semestre ou período estiverem vazios, os @NotNull acima já avisam.
    @AssertTrue(message = "Este curso não é oferecido no período/semestre escolhido")
    public boolean isPeriodoValidoParaCurso() {
        if (curso == null || periodo == null || semestre == null) return true;
        return curso.aceita(periodo, semestre);
    }
}
