package com.example.kanban.controller;

import com.example.kanban.model.Prioridade;
import com.example.kanban.model.Status;
import com.example.kanban.model.Tarefa;
import com.example.kanban.service.TarefaService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public Tarefa create(@RequestBody Tarefa tarefa) {
        return tarefaService.create(tarefa);
    }

    @GetMapping
    public List<Tarefa> read() {
        return tarefaService.read();
    }

    @PutMapping("/{id}")
    public Tarefa update(@PathVariable Long id, @RequestBody Tarefa tarefaAtualizada) {
        return tarefaService.update(id, tarefaAtualizada);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tarefaService.delete(id);
    }

    @PutMapping("/move/{id}")
    public Tarefa move(@PathVariable Long id) {
        return tarefaService.move(id);
    }

    @GetMapping("/por-coluna")
    public Map<Status, List<Tarefa>> listarPorColuna() {
        return tarefaService.listarPorColuna();
    }

    @GetMapping("/filtrar/prioridade/{prioridade}")
    public List<Tarefa> filtrarPorPrioridade(@PathVariable Prioridade prioridade) {
        return tarefaService.filtrarPorPrioridade(prioridade);
    }

    @GetMapping("/filtrar/data-limite/{data}")
    public List<Tarefa> filtrarPorDataLimite(@PathVariable("data") LocalDate dataLimite) {
        return tarefaService.filtrarPorDataLimite(dataLimite);
    }

    @GetMapping("/relatorio")
    public Map<Status, List<Tarefa>> gerarRelatorio() {
        return tarefaService.gerarRelatorio();
    }

    @GetMapping("/atrasadas")
    public List<Tarefa> listarTarefasAtrasadas() {
        return tarefaService.listarTarefasAtrasadas();
    }
}
