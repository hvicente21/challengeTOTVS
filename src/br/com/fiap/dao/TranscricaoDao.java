package br.com.fiap.dao;

import br.com.fiap.dto.Transcricao;

import java.util.List;

public interface TranscricaoDao {

    String inserir(Transcricao transcricao);

    Transcricao buscar(int id);

    String atualizar(Transcricao transcricao);

    String remover(int id);

    List<Transcricao> listarTodos();
}