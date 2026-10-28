package com.fatec.projetoestagio.Aluno;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * O que a API devolve. A entrada (POST/PUT) reaproveita o DadosCadastroAluno,
 * que já tem todas as validações, inclusive a regra curso x período.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String ra;
    private String genero;
    private Curso curso;
    private LocalDate dataNascimento;
    private Integer semestre;
    private Periodo periodo;
    private String etnia;
    private String nivelEscolaridade;
    private String rendaFamiliar;
    private String localidadeCEP;
    private String cidade;
    private String estado;
    private String deficiencia;
    private String notoriedadeCurso;
    private String descricaoOutros;
}
