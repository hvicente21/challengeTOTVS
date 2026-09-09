package br.com.fiap.dao;

import br.com.fiap.dto.Alerta;

import java.util.List;

public interface AlertaDao {

    String inserir(Alerta alerta);

    Alerta buscar(int id);

    String atualizar(Alerta alerta);

    String remover(int id);

    List<Alerta> listarTodos();
}