package br.com.fiap.service;

import br.com.fiap.model.entity.Alerta;
import br.com.fiap.model.entity.Meeting;
import br.com.fiap.model.entity.PalavraChave;
import br.com.fiap.model.entity.ResultadoAnalise;
import br.com.fiap.model.entity.Transcricao;
import br.com.fiap.model.repository.AlertaRepository;
import br.com.fiap.model.repository.MeetingRepository;
import br.com.fiap.model.repository.PalavraChaveRepository;
import br.com.fiap.model.repository.ResultadoAnaliseRepository;
import br.com.fiap.model.repository.TranscricaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnaliseAutomaticaService {

    @Autowired
    private AnalisadorTexto analisadorTexto;

    @Autowired
    private TranscricaoRepository transcricaoRepository;

    @Autowired
    private ResultadoAnaliseRepository resultadoAnaliseRepository;

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private PalavraChaveRepository palavraChaveRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Transactional
    public ResultadoAnalise analisar(int idTranscricao) {

        if (idTranscricao <= 0) {
            return null;
        }

        Transcricao transcricao =
                transcricaoRepository
                        .findById(idTranscricao)
                        .orElse(null);

        if (transcricao == null) {
            return null;
        }

        ResultadoAnalise resultadoExistente =
                resultadoAnaliseRepository
                        .findByTranscricao_IdTranscricao(
                                idTranscricao
                        );

        if (resultadoExistente != null) {
            return null;
        }

        ResultadoAnalise resultadoAnalise =
                analisadorTexto.analisar(
                        transcricao
                );

        resultadoAnalise =
                resultadoAnaliseRepository.save(
                        resultadoAnalise
                );

        for (Alerta alerta :
                resultadoAnalise.getAlertas()) {

            alerta.setResultadoAnalise(
                    resultadoAnalise
            );

            alertaRepository.save(
                    alerta
            );
        }

        Meeting meeting =
                transcricao.getMeeting();

        if (meeting != null) {

            List<PalavraChave> palavrasEncontradas =
                    new ArrayList<>(
                            meeting.getPalavrasChave()
                    );

            meeting.getPalavrasChave().clear();

            for (PalavraChave palavraChave :
                    palavrasEncontradas) {

                PalavraChave palavraExistente =
                        palavraChaveRepository
                                .findByPalavraIgnoreCase(
                                        palavraChave.getPalavra()
                                );

                if (palavraExistente == null) {

                    palavraExistente =
                            palavraChaveRepository.save(
                                    palavraChave
                            );
                }

                meeting.adicionarPalavraChave(
                        palavraExistente
                );
            }

            meetingRepository.save(
                    meeting
            );
        }

        return resultadoAnalise;
    }
}