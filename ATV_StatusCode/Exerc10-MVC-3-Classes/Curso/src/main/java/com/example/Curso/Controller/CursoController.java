package com.example.Curso.Controller;

import com.example.Curso.banco.CursoDb;
import com.example.Curso.model.Aluno;
import com.example.Curso.model.Curso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoDb repository;

    @GetMapping
    public ResponseEntity<List<Curso>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/professor")
    public ResponseEntity<List<Curso>> getByProfessor(@RequestParam String nomeProfessor) {
        List<Curso> cursos = repository.findByProfessor(nomeProfessor);
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/sala")
    public ResponseEntity<List<Curso>> getBySala(@RequestParam int sala) {
        List<Curso> cursos = repository.findBySala(sala);
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> getById(@PathVariable int id) {
        Curso curso = repository.getById(id);
        if (curso != null) {
            return ResponseEntity.ok(curso);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public  ResponseEntity<Curso> insertBanco(@RequestBody Curso curso) {
         Curso sucesso = repository.insert(curso);
        if (sucesso == null ) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(curso);

        }
    }

    @PostMapping("/{idCurso}/alunos")
    public ResponseEntity<ArrayList<Aluno>> insertAluno(@PathVariable int idCurso, @RequestBody Aluno aluno) {
        Curso curso = repository.getById(idCurso);
        if (curso == null){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        repository.insertAluno(idCurso, aluno);
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    // Método com tratamento e boas práticas
    @PostMapping("/{idCurso}/alunos/melhorado")
    public ResponseEntity<String> insertAlunoMelhorado(@PathVariable int idCurso, @RequestBody Aluno aluno) {
        Curso curso = repository.getById(idCurso);
        if (curso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Curso não encontrado para inserção do aluno.");
        }

        boolean result = repository.insertAlunoMelhorado(curso, aluno);
        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Aluno inserido com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Não foi possível inserir o aluno.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> update(@PathVariable int id, @RequestBody Curso curso) {
        Curso cursoExistente = repository.getById(id);
        if (cursoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        repository.update(id, curso);
        return ResponseEntity.ok(curso); // retorna o curso atualizado
    }

    @PutMapping("/{idCurso}/alunos/{idAluno}")
    public ResponseEntity<String> updateAluno(
            @PathVariable int idCurso,
            @PathVariable int idAluno,
            @RequestBody Aluno aluno
    ) {


       Curso curso = repository.getById(idCurso);
       if(curso == null){
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .build();
       }
       Curso  aluno1 =  repository.getById(idAluno);
        if (aluno1 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            boolean sucesso =  repository.updateAluno(idCurso,idAluno,aluno);
            if(sucesso){
                return ResponseEntity.status(HttpStatus.OK).body("Sucesso!");
            }else {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            }
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable int id) {
        Curso curso = repository.getById(id);
        if (curso == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Curso com ID " + id + " não encontrado.");
        }

        repository.delete(id);
        return ResponseEntity.ok("Curso '" + curso.getNome() + "' deletado com sucesso.");
    }
}
