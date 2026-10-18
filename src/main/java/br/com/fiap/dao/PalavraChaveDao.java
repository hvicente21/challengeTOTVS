package br.com.fiap.dao;

import br.com.fiap.model.entity.PalavraChave;

import java.util.List;

public interface PalavraChaveDao {

    String inserir(PalavraChave palavraChave);

    PalavraChave buscar(int id);

    String atualizar(PalavraChave palavraChave);

    String remover(int id);

    List<PalavraChave> listarTodos();
}