package com.example.kanban.service;

import com.example.kanban.model.Prioridade;
import com.example.kanban.model.Status;
import com.example.kanban.model.Tarefa;
import com.example.kanban.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public Tarefa create(Tarefa tarefa) {
        tarefa.setStatus(Status.A_FAZER);
        return tarefaRepository.save(tarefa);
    }

    public List<Tarefa> read() {
        return tarefaRepository.findAll();
    }

    public Tarefa update(Long id, Tarefa tarefaAtualizada) {
        Tarefa tarefa = tarefaRepository.findById(id).orElse(null);
        if (tarefa != null) {
            tarefa.setTitulo(tarefaAtualizada.getTitulo());
            tarefa.setDescricao(tarefaAtualizada.getDescricao());
            tarefa.setPrioridade(tarefaAtualizada.getPrioridade());
            tarefa.setDataLimite(tarefaAtualizada.getDataLimite());
            return tarefaRepository.save(tarefa);
        }
        return null;
    }

    public void delete(Long id) {
        tarefaRepository.deleteById(id);
    }

    public Tarefa move(Long id) {
        Tarefa tarefa = tarefaRepository.findById(id).orElse(null);
        if (tarefa != null) {
            if (tarefa.getStatus() == Status.A_FAZER) {
                tarefa.setStatus(Status.EM_PROGRESSO);
            } else if (tarefa.getStatus() == Status.EM_PROGRESSO) {
                tarefa.setStatus(Status.CONCLUIDO);
            }
            return tarefaRepository.save(tarefa);
        }
        return null;
    }

    public Map<Status, List<Tarefa>> listarPorColuna() {
        List<Tarefa> todasTarefas = tarefaRepository.findAll();
        return todasTarefas.stream()
                .sorted((t1, t2) -> t1.getPrioridade().compareTo(t2.getPrioridade()))
                .collect(Collectors.groupingBy(Tarefa::getStatus, () -> new EnumMap<>(Status.class), Collectors.toList()));
    }

    public List<Tarefa> filtrarPorPrioridade(Prioridade prioridade) {
        return tarefaRepository.findAll().stream()
                .filter(tarefa -> tarefa.getPrioridade() == prioridade)
                .collect(Collectors.toList());
    }

    public List<Tarefa> filtrarPorDataLimite(LocalDate dataLimite) {
        return tarefaRepository.findAll().stream()
                .filter(tarefa -> tarefa.getDataLimite() != null && tarefa.getDataLimite().equals(dataLimite))
                .collect(Collectors.toList());
    }

    public Map<Status, List<Tarefa>> gerarRelatorio() {
        return tarefaRepository.findAll().stream()
                .collect(Collectors.groupingBy(Tarefa::getStatus, Collectors.toList()));
    }

    public List<Tarefa> listarTarefasAtrasadas() {
        return tarefaRepository.findAll().stream()
                .filter(tarefa -> tarefa.getDataLimite() != null &&
                        tarefa.getDataLimite().isBefore(LocalDate.now()) &&
                        tarefa.getStatus() != Status.CONCLUIDO)
                .collect(Collectors.toList());
    }
}
