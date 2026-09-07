package br.com.fiap.service;

import br.com.fiap.dto.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalisadorTexto {

    private List<String> palavrasPositivas;
    private List<String> palavrasNegativas;
    private List<String> palavrasRisco;
    private List<String> palavrasOportunidade;

    public AnalisadorTexto() {
        palavrasPositivas = Arrays.asList("bom", "boa", "bons", "boas", "ótimo", "ótima", "ótimos", "ótimas", "gostou", "funcionando", "satisfeito", "satisfeita", "satisfeitos", "satisfeitas", "atendendo", "melhor", "feliz", "automatizar", "reduziu"
        );
        palavrasNegativas = Arrays.asList("problema", "problemas", "manual", "sofrendo", "dificuldade", "dificuldades", "erro", "erros", "frustrado", "frustrada", "frustrados", "frustradas", "insatisfeito", "insatisfeita", "insatisfeitos", "insatisfeitas", "falha", "falhas", "ruído", "ruídos", "retrabalho", "insustentável"
        );
        palavrasRisco = Arrays.asList("senior", "sap", "oracle", "concorrente", "concorrência","trocar", "migração", "cancelar", "cancelamento", "reclamação", "insatisfeito", "falha", "problema"
        );
        palavrasOportunidade = Arrays.asList("rh", "folha", "rm", "módulo", "expansão", "expandir", "usuários", "backoffice", "cfo", "roi", "comprar", "contratar", "integrar", "integração", "upgrade", "interesse", "interessado", "novo", "novos", "conhecer", "demonstração", "demonstrar"
        );
    }

    public ResultadoAnalise analisar(Transcricao transcricao) {

        if (transcricao == null ||
                transcricao.getTexto() == null ||
                transcricao.getTexto().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "A transcrição deve possuir um texto válido para análise."
            );
        }

        ProcessadorTexto processador = new ProcessadorTexto();

        String texto = transcricao.getTexto();

        List<String> palavras = processador.tokenizar(texto);
        List<String> palavrasFiltradas =
                processador.removerStopwords(palavras);

        String sentimento =
                analisarSentimento(palavrasFiltradas);

        Alerta alertaRisco =
                detectarRisco(palavrasFiltradas);

        Alerta alertaOportunidade =
                detectarOportunidade(palavrasFiltradas);

        List<PalavraChave> palavrasChave =
                extrairPalavrasChave(palavrasFiltradas);

        String resumo =
                gerarResumo(texto);

        boolean upsell =
                detectarUpsell(palavrasFiltradas);

        String concorrente =
                detectarConcorrente(palavrasFiltradas);

        if (alertaOportunidade != null && upsell) {
            alertaOportunidade.setDescricao(
                    alertaOportunidade.getDescricao()
                            + " Também foram identificados indícios de upsell."
            );
        }

        double riscoChurn = 0;

        if (alertaRisco != null) {

            if (alertaRisco.getNivelRisco().equals("Baixo")) {
                riscoChurn += 10;

            } else if (alertaRisco.getNivelRisco().equals("Médio")) {
                riscoChurn += 25;

            } else if (alertaRisco.getNivelRisco().equals("Alto")) {
                riscoChurn += 45;

            } else if (alertaRisco.getNivelRisco().equals("Crítico")) {
                riscoChurn += 65;
            }
        }

        if (sentimento.equals("Negativo")) {
            riscoChurn += 15;

        } else if (sentimento.equals("Misto")) {
            riscoChurn += 8;
        }

        String classificacao;

        if (alertaRisco != null &&
                (alertaRisco.getNivelRisco().equals("Alto") ||
                        alertaRisco.getNivelRisco().equals("Crítico"))) {

            classificacao = "Risco";

        } else if (alertaOportunidade != null) {

            classificacao = "Oportunidade";

        } else if (alertaRisco != null) {

            classificacao = "Risco";

        } else {

            classificacao = "Estável";
        }

        ResultadoAnalise resultado = new ResultadoAnalise(
                0,
                transcricao,
                resumo,
                sentimento,
                new ArrayList<>(),
                riscoChurn,
                classificacao
        );

        if (alertaRisco != null) {
            resultado.adicionarAlerta(alertaRisco);
        }

        if (alertaOportunidade != null) {
            resultado.adicionarAlerta(alertaOportunidade);
        }

        transcricao.setResultadoAnalise(resultado);

        if (transcricao.getMeeting() != null) {

            for (PalavraChave palavraChave : palavrasChave) {
                transcricao.getMeeting().adicionarPalavraChave(palavraChave);
            }
        }

        return resultado;
    }

    public String analisarSentimento(List<String> palavras) {
        if (palavras == null || palavras.isEmpty()) {
            return "Neutro";
        }
        int positivas = 0;
        int negativas = 0;

        for (String palavra : palavras) {
            if (palavrasPositivas.contains(palavra)) {
                positivas++;
            }
            if (palavrasNegativas.contains(palavra)) {
                negativas++;
            }
        }
        if (positivas == 0 && negativas == 0) {return "Neutro";}
        if (positivas > negativas) {return "Positivo";}
        if (negativas > positivas) {return "Negativo";}

        return "Misto";
    }

    public Alerta detectarRisco(List<String> palavras) {

        if (palavras == null || palavras.isEmpty()) {
            return null;
        }

        boolean temCancelamento = palavras.contains("cancelar") || palavras.contains("cancelamento");

        boolean temTroca = palavras.contains("trocar") || palavras.contains("troca") || palavras.contains("migração") || palavras.contains("migrar");

        boolean temConcorrencia = palavras.contains("concorrente") || palavras.contains("concorrência") || palavras.contains("senior") || palavras.contains("sap") || palavras.contains("oracle");

        boolean temInsatisfacao = palavras.contains("insatisfeito") || palavras.contains("insatisfeita") || palavras.contains("insatisfeitos") || palavras.contains("insatisfeitas") || palavras.contains("reclamação") || palavras.contains("reclamações");

        boolean temProblema = palavras.contains("problema") || palavras.contains("problemas") || palavras.contains("falha") || palavras.contains("falhas") || palavras.contains("erro") || palavras.contains("erros") || palavras.contains("dificuldade") || palavras.contains("dificuldades");

        int quantidadeSinaisNegativos = 0;

        for (String palavra : palavras) {
            if (palavrasNegativas.contains(palavra)) {
                quantidadeSinaisNegativos++;
            }
        }

        String nivelRisco;
        StringBuilder descricao = new StringBuilder();

        if (temCancelamento) {
            nivelRisco = "Crítico";
            descricao.append("Foi identificada intenção de cancelamento");
        } else if (temTroca && temConcorrencia) {
            nivelRisco = "Crítico";
            descricao.append("Foram identificados sinais de troca de solução e avaliação de concorrentes");
        } else if (temTroca) {
            nivelRisco = "Médio";
            descricao.append("Foi identificada possível intenção de troca ou migração de solução");
        } else if (temConcorrencia) {
            nivelRisco = "Alto";
            descricao.append("Foi identificada avaliação ou menção a solução concorrente");
        } else if (temInsatisfacao && temProblema) {
            nivelRisco = "Alto";
            descricao.append("Foram identificados sinais de insatisfação acompanhados de problemas no serviço");
        } else if (temInsatisfacao) {
            nivelRisco = "Médio";
            descricao.append("Foram identificados sinais de insatisfação do cliente");
        } else if (temProblema || quantidadeSinaisNegativos >= 2) {
            nivelRisco = "Médio";
            descricao.append("Foram identificados problemas ou dificuldades relevantes durante a conversa");
        } else if (quantidadeSinaisNegativos == 1) {
            nivelRisco = "Baixo";
            descricao.append("Foi identificado um sinal isolado de dificuldade ou insatisfação");
        } else {
            return null;
        }

        List<String> concorrentesEncontrados = new ArrayList<>();

        if (palavras.contains("senior")) {
            concorrentesEncontrados.add("Senior");
        }
        if (palavras.contains("sap")) {
            concorrentesEncontrados.add("SAP");
        }
        if (palavras.contains("oracle")) {
            concorrentesEncontrados.add("Oracle");
        }
        if (!concorrentesEncontrados.isEmpty()) {
            descricao.append(". Concorrente(s) mencionado(s): ");

            for (int i = 0; i < concorrentesEncontrados.size(); i++) {
                descricao.append(concorrentesEncontrados.get(i));

                if (i < concorrentesEncontrados.size() - 1) {
                    descricao.append(", ");
                }
            }
        }
        descricao.append(".");
        return new Alerta(
                0,
                null,
                "Risco",
                nivelRisco,
                descricao.toString()
        );
    }

    public Alerta detectarOportunidade(List<String> palavras) {

        if (palavras == null || palavras.isEmpty()) {
            return null;
        }

        boolean temCompra = palavras.contains("comprar") || palavras.contains("contratar");

        boolean temExpansao = palavras.contains("expansão") || palavras.contains("expandir") || palavras.contains("aumentar");

        boolean temIntegracao = palavras.contains("integrar") || palavras.contains("integração");

        boolean temNovoModulo = palavras.contains("módulo") && (palavras.contains("novo") || palavras.contains("novos"));

        boolean temUpgrade = palavras.contains("upgrade");

        boolean temInteresse = palavras.contains("interesse") || palavras.contains("interessado") || palavras.contains("conhecer");

        boolean temDemonstracao = palavras.contains("demonstração") || palavras.contains("demonstrar");

        boolean mencionaProduto = palavras.contains("rm") || palavras.contains("protheus") || palavras.contains("fluig") || palavras.contains("clockin");

        if (!temCompra && !temExpansao && !temIntegracao && !temNovoModulo && !temUpgrade && !temInteresse && !temDemonstracao) {
            return null;
        }

        StringBuilder descricao = new StringBuilder("Foi identificada uma oportunidade comercial");

        List<String> sinaisEncontrados = new ArrayList<>();

        if (temCompra) {
            sinaisEncontrados.add("intenção de contratação");
        }
        if (temExpansao) {
            sinaisEncontrados.add("expansão");
        }
        if (temIntegracao) {
            sinaisEncontrados.add("integração");
        }
        if (temNovoModulo) {
            sinaisEncontrados.add("interesse em novo módulo");
        }
        if (temUpgrade) {
            sinaisEncontrados.add("upgrade");
        }
        if (temInteresse) {
            sinaisEncontrados.add("interesse em nova solução");
        }
        if (temDemonstracao) {
            sinaisEncontrados.add("pedido ou interesse em demonstração");
        }

        if (!sinaisEncontrados.isEmpty()) {

            descricao.append(" relacionada a ");

            for (int i = 0; i < sinaisEncontrados.size(); i++) {

                descricao.append(sinaisEncontrados.get(i));

                if (i < sinaisEncontrados.size() - 1) {
                    descricao.append(", ");
                }
            }
        }
        if (mencionaProduto) {

            String produto = detectarProdutoRelacionado(palavras);

            descricao.append(". Produto ou área relacionada: ")
                    .append(produto);
        }

        descricao.append(".");
        return new Alerta(
                0,
                null,
                "Oportunidade",
                null,
                descricao.toString()
        );
    }

    public List<PalavraChave> extrairPalavrasChave(List<String> palavras) {
        List<PalavraChave> palavrasChave = new ArrayList<>();

        if (palavras == null || palavras.isEmpty()) {
            return palavrasChave;
        }

        ProcessadorTexto processador = new ProcessadorTexto();
        List<String> palavrasFiltradas =
                processador.removerStopwords(palavras);

        List<String> termosEspecificos = Arrays.asList("totvs", "protheus", "rm", "fluig", "clockin", "erp", "rh", "folha", "churn"
        );

        for (String palavra : palavrasFiltradas) {
            boolean palavraRelevante =
                    palavrasRisco.contains(palavra) ||
                            palavrasOportunidade.contains(palavra) ||
                            palavrasNegativas.contains(palavra) ||
                            termosEspecificos.contains(palavra);

            if (palavraRelevante) {
                boolean duplicada = false;
                for (PalavraChave palavraChave : palavrasChave) {

                    if (palavraChave.getPalavra()
                            .equalsIgnoreCase(palavra)) {

                        duplicada = true;
                        break;
                    }
                }
                if (!duplicada) {
                    palavrasChave.add(new PalavraChave(palavra, 0));
                }
            }
        }
        return palavrasChave;
    }

    public String gerarResumo(String texto) {

        if (texto == null || texto.trim().isEmpty()) {
            return "Sem resumo disponível.";
        }

        String[] trechos = texto.split("(?<=[.!?])\\s+|\\R+");

        int[] pontuacoes = new int[trechos.length];

        ProcessadorTexto processador = new ProcessadorTexto();

        for (int i = 0; i < trechos.length; i++) {
            List<String> palavras = processador.tokenizar(trechos[i]);
            int pontuacao = 0;

            for (String palavra : palavras) {
                if (palavrasRisco.contains(palavra)) {
                    pontuacao += 3;
                }
                if (palavrasOportunidade.contains(palavra)) {
                    pontuacao += 2;
                }
                if (palavrasNegativas.contains(palavra)) {
                    pontuacao += 2;
                }
                if (palavrasPositivas.contains(palavra)) {
                    pontuacao++;
                }
                if (palavra.equals("protheus") || palavra.equals("rm") || palavra.equals("fluig") || palavra.equals("clockin")) {
                    pontuacao += 2;
                }
            }
            pontuacoes[i] = pontuacao;
        }

        int[] selecionados = {-1, -1, -1};

        for (int posicao = 0; posicao < selecionados.length; posicao++) {
            int maiorPontuacao = 0;
            int melhorIndice = -1;

            for (int i = 0; i < pontuacoes.length; i++) {
                boolean jaSelecionado = false;
                for (int indice : selecionados) {
                    if (indice == i) {
                        jaSelecionado = true;
                        break;
                    }
                }
                if (!jaSelecionado && pontuacoes[i] > maiorPontuacao) {
                    maiorPontuacao = pontuacoes[i];
                    melhorIndice = i;
                }
            }
            selecionados[posicao] = melhorIndice;
        }
        for (int i = 0; i < selecionados.length - 1; i++) {
            for (int j = i + 1; j < selecionados.length; j++) {
                if (selecionados[i] == -1 || (selecionados[j] != -1 && selecionados[j] < selecionados[i])) {
                    int auxiliar = selecionados[i];
                    selecionados[i] = selecionados[j];
                    selecionados[j] = auxiliar;
                }
            }
        }

        StringBuilder resumo = new StringBuilder();

        for (int indice : selecionados) {
            if (indice != -1) {
                String trecho = trechos[indice].trim();
                if (!trecho.isEmpty()) {
                    if (resumo.length() > 0) {
                        resumo.append(" ");
                    }
                    resumo.append(trecho);
                }
            }
        }
        if (resumo.length() == 0) {
            String textoLimpo = texto.trim();
            if (textoLimpo.length() <= 200) {
                return textoLimpo;
            }
            return textoLimpo.substring(0, 200) + "...";
        }
        return resumo.toString();
    }

    public String detectarConcorrente(List<String> palavras) {
        if (palavras == null || palavras.isEmpty()) {
            return "Nenhum concorrente detectado";
        }
        List<String> concorrentesEncontrados = new ArrayList<>();

        if (palavras.contains("senior")) {
            concorrentesEncontrados.add("Senior");
        }
        if (palavras.contains("sap")) {
            concorrentesEncontrados.add("SAP");
        }
        if (palavras.contains("oracle")) {
            concorrentesEncontrados.add("Oracle");
        }
        if (concorrentesEncontrados.isEmpty()) {
            return "Nenhum concorrente detectado";
        }
        return String.join(", ", concorrentesEncontrados);
    }

    public String detectarProdutoRelacionado(List<String> palavras) {
        if (palavras == null || palavras.isEmpty()) {
            return "Nenhum produto TOTVS detectado";
        }
        List<String> produtosEncontrados = new ArrayList<>();

        if (palavras.contains("protheus")) {
            produtosEncontrados.add("TOTVS Protheus");
        }
        if (palavras.contains("rm")) {
            produtosEncontrados.add("TOTVS RM");
        }
        if (palavras.contains("fluig")) {
            produtosEncontrados.add("TOTVS Fluig");
        }
        if (palavras.contains("clockin")) {
            produtosEncontrados.add("TOTVS ClockIn");
        }
        if (produtosEncontrados.isEmpty()) {
            return "Nenhum produto TOTVS detectado";
        }
        return String.join(", ", produtosEncontrados);
    }

    public boolean detectarUpsell(List<String> palavras) {

        if (palavras == null || palavras.isEmpty()) {
            return false;
        }

        boolean mencionaProdutoAtual = palavras.contains("totvs") || palavras.contains("protheus") || palavras.contains("rm") || palavras.contains("fluig") || palavras.contains("clockin");

        boolean temNovoModulo = palavras.contains("módulo") && (palavras.contains("novo") || palavras.contains("novos"));

        boolean temExpansao = palavras.contains("expansão") || palavras.contains("expandir") || palavras.contains("aumentar");

        boolean temUpgrade = palavras.contains("upgrade");

        boolean temIntegracao = palavras.contains("integrar") || palavras.contains("integração");

        boolean temInteresseComercial = palavras.contains("interesse") || palavras.contains("interessado") || palavras.contains("conhecer") || palavras.contains("demonstração") || palavras.contains("demonstrar") || palavras.contains("contratar") || palavras.contains("comprar");

        if (temNovoModulo || temUpgrade) {
            return true;
        }
        if (mencionaProdutoAtual &&
                (temExpansao || temIntegracao || temInteresseComercial)) {
            return true;
        }
        return false;
    }
}