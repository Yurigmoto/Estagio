package com.fatec.projetoestagio.Aluno;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByRa(String ra);
    
    Optional<Aluno> findByRa(String ra); ///

    List<Aluno> findByCurso(Curso curso); ///
}