package com.fatec.projetoestagio.Aluno;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> listar(Curso curso) {
        List<Aluno> alunos = (curso == null)
                ? alunoRepository.findAll()
                : alunoRepository.findByCurso(curso);
        return alunos.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public Page<AlunoResponseDTO> listarPaginado(Pageable pageable) {
        return alunoRepository.findAll(pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public AlunoResponseDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public AlunoResponseDTO buscarPorRa(String ra) {
        return alunoRepository.findByRa(ra)
                .map(this::toDTO)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado com o RA: " + ra));
    }

    @Transactional
    public AlunoResponseDTO criar(DadosCadastroAluno dados) {
        if (alunoRepository.existsByRa(dados.ra())) {
            throw raDuplicado();
        }
        return toDTO(alunoRepository.save(new Aluno(dados)));
    }

    @Transactional
    public AlunoResponseDTO atualizar(Long id, DadosCadastroAluno dados) {
        Aluno aluno = buscarEntidade(id);
        //só confere duplicidade se o RA mudou
        if (!aluno.getRa().equals(dados.ra()) && alunoRepository.existsByRa(dados.ra())) {
            throw raDuplicado();
        }
        copiar(dados, aluno);
        return toDTO(alunoRepository.save(aluno));
    }

    @Transactional
    public void deletar(Long id) {
        alunoRepository.delete(buscarEntidade(id));
    }

    // auxiliares 

    private Aluno buscarEntidade(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Aluno não encontrado com o ID: " + id));
    }

    private ResponseStatusException raDuplicado() {
        return new ResponseStatusException(
                HttpStatus.CONFLICT, "Já existe um aluno cadastrado com este RA.");
    }

    private void copiar(DadosCadastroAluno d, Aluno a) {
        a.setNome(d.nome());
        a.setEmail(d.email());
        a.setGenero(d.genero());
        a.setDataNascimento(d.nascimento());
        a.setEtnia(d.etnia());
        a.setNivelEscolaridade(d.escolaridade());
        a.setRendaFamiliar(d.renda());
        a.setLocalidadeCEP(d.cep());
        a.setCidade(d.cidade());
        a.setEstado(d.estado());
        a.setDeficiencia(d.deficiencia());
        a.setRa(d.ra());
        a.setCurso(d.curso());
        a.setPeriodo(d.periodo());
        a.setSemestre(d.semestre());
        a.setNotoriedadeCurso(d.comoSoube());
        a.setDescricaoOutros(d.outros());
    }

    private AlunoResponseDTO toDTO(Aluno a) {
        return AlunoResponseDTO.builder()
                .id(a.getId())
                .nome(a.getNome())
                .email(a.getEmail())
                .ra(a.getRa())
                .genero(a.getGenero())
                .curso(a.getCurso())
                .dataNascimento(a.getDataNascimento())
                .semestre(a.getSemestre())
                .periodo(a.getPeriodo())
                .etnia(a.getEtnia())
                .nivelEscolaridade(a.getNivelEscolaridade())
                .rendaFamiliar(a.getRendaFamiliar())
                .localidadeCEP(a.getLocalidadeCEP())
                .cidade(a.getCidade())
                .estado(a.getEstado())
                .deficiencia(a.getDeficiencia())
                .notoriedadeCurso(a.getNotoriedadeCurso())
                .descricaoOutros(a.getDescricaoOutros())
                .build();
    }
}
