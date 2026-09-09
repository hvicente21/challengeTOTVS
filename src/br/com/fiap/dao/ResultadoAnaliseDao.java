package br.com.fiap.dao;

import br.com.fiap.dto.ResultadoAnalise;

import java.util.List;

public interface ResultadoAnaliseDao {

    String inserir(ResultadoAnalise resultadoAnalise);

    ResultadoAnalise buscar(int id);

    String atualizar(ResultadoAnalise resultadoAnalise);

    String remover(int id);

    List<ResultadoAnalise> listarTodos();
}