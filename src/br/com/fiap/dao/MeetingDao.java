package br.com.fiap.dao;

import br.com.fiap.dto.Meeting;

import java.util.List;

public interface MeetingDao {

    String inserir(Meeting meeting);

    Meeting buscar(int id);

    String atualizar(Meeting meeting);

    String remover(int id);

    List<Meeting> listarTodos();
}