package com.example.Curso.View;

import com.example.Curso.Controller.CursoController;
import com.example.Curso.model.Aluno;
import com.example.Curso.model.Curso;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/curso")
public class CursoView {

    CursoController cursoController = new CursoController();

    @GetMapping
    public ResponseEntity<List<Curso>> getCurso(
            @RequestParam(required = false) String nomeProfessor,
            @RequestParam(required = false) Integer sala)
    {
        if (nomeProfessor != null) {
            return cursoController.getByProfessor(nomeProfessor);
        }
        else if (sala != null) {
            return cursoController.getBySala(sala);
        }
        else {
            return cursoController.getAll();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> getById(@PathVariable int id){
        return cursoController.getById(id);
    }

    @PostMapping
    public ResponseEntity<Curso> insert(@RequestBody Curso curso){
        return cursoController.insertBanco(curso);
    }

    @PostMapping("/{idCurso}/aluno")
    public ResponseEntity<ArrayList<Aluno>> insertAluno(@RequestBody Aluno aluno, @PathVariable int idCurso){
        return cursoController.insertAluno(idCurso, aluno);
    }

    // esta função fatao mesmo insert que a função a cima, porém com melhores práticas de programação
    @PostMapping("/{idCurso}/alunoMelhorado")
    public ResponseEntity<String> insertAlunoMelhorado(@RequestBody Aluno aluno, @PathVariable int idCurso){
        return cursoController.insertAlunoMelhorado(idCurso, aluno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> update(@RequestBody Curso curso, @PathVariable int id){
        return cursoController.update(id, curso);
    }

    @PutMapping("/{idCurso}/aluno/{idAluno}")
    public ResponseEntity<String> updateAluno(@PathVariable int idCurso, @PathVariable int idAluno, @RequestBody Aluno aluno){
        return cursoController.updateAluno(idCurso, idAluno, aluno);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id){
        return cursoController.deletar(id);
    }
    }

