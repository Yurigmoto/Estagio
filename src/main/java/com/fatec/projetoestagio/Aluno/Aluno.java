package com.fatec.projetoestagio.Aluno;

import com.fatec.projetoestagio.Aluno.Curso;
import com.fatec.projetoestagio.Aluno.Periodo;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "aluno")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String genero;
    private LocalDate dataNascimento;
    private String etnia;
    private String nivelEscolaridade;
    private String rendaFamiliar;
    private String localidadeCEP;
    private String cidade;
    private String estado; // sigla (UF), ex.: SP
    private String deficiencia; // "sim" ou "nao"
    @Column(unique = true, nullable = false)
    private String ra; // String: não perde zero à esquerda e não estoura Long
    @Enumerated(EnumType.STRING)
    private Curso curso;
    @Enumerated(EnumType.STRING)
    private Periodo periodo;
    private int semestre;
    private String notoriedadeCurso;
    private String descricaoOutros;

    public Aluno(DadosCadastroAluno d) {
        this.nome = d.nome();
        this.email = d.email();
        this.genero = d.genero();
        this.dataNascimento = d.nascimento();
        this.etnia = d.etnia();
        this.nivelEscolaridade = d.escolaridade();
        this.rendaFamiliar = d.renda();
        this.localidadeCEP = d.cep();
        this.cidade = d.cidade();
        this.estado = d.estado();
        this.deficiencia = d.deficiencia();
        this.ra = d.ra();
        this.curso = d.curso();
        this.periodo = d.periodo();
        this.semestre = d.semestre();
        this.notoriedadeCurso = d.comoSoube();
        this.descricaoOutros = d.outros();
    }
}
