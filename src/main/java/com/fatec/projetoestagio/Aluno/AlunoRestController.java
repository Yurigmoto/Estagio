package com.fatec.projetoestagio.Aluno;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alunos")
@RequiredArgsConstructor
public class AlunoRestController {

    private final AlunoService alunoService;

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listar(@RequestParam(required = false) Curso curso) {
        return ResponseEntity.ok(alunoService.listar(curso));
    }

    @GetMapping("/paginado")
    public ResponseEntity<Page<AlunoResponseDTO>> listarPaginado(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(alunoService.listarPaginado(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.buscarPorId(id));
    }

    @GetMapping("/ra/{ra}")
    public ResponseEntity<AlunoResponseDTO> buscarPorRa(@PathVariable String ra) {
        return ResponseEntity.ok(alunoService.buscarPorRa(ra));
    }

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> criar(@Valid @RequestBody DadosCadastroAluno dados) {
        AlunoResponseDTO criado = alunoService.criar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> atualizar(@PathVariable Long id,
                                                      @Valid @RequestBody DadosCadastroAluno dados) {
        return ResponseEntity.ok(alunoService.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        alunoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
