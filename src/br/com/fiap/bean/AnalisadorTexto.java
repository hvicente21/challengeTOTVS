package br.com.fiap.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalisadorTexto {

    private List<String> palavrasPositivas;
    private List<String> palavrasNegativas;
    private List<String> palavrasRisco;
    private List<String> palavrasOportunidade;

    public AnalisadorTexto() {
        // Banco de palavras ajustado SEM ACENTOS para bater 100% com o ProcessadorTexto
        palavrasPositivas = Arrays.asList("bom", "otimo", "gostou", "funcionando", "satisfeito", "atendendo", "integrado", "consolidar", "melhor", "feliz", "automatizar", "reduziu", "retorno", "aumentar");
        palavrasNegativas = Arrays.asList("problema", "manual", "sofrendo", "dificuldade", "erro", "frustrado", "insatisfeito", "falha", "ruido", "retrabalho", "suporte", "insustentavel");
        palavrasRisco = Arrays.asList("senior", "sap", "oracle", "concorrente", "concorrencia", "trocar", "migracao");
        palavrasOportunidade = Arrays.asList("rh", "folha", "rm", "modulo", "expansao", "expandir", "usuarios", "backoffice", "cfo", "roi");
    }

    public ResultadoAnalise analisar(Meeting meeting) {
        ProcessadorTexto processador = new ProcessadorTexto();
        String textoLimpo = processador.limparTexto(meeting.getTranscricao());
        List<String> palavras = processador.tokenizar(textoLimpo);

        // 1. Calcula o sentimento da conversa
        String sentimentoCalculado = analisarSentimento(palavras);

        // 2. Vincula a Nota NPS da UML dinamicamente ao sentimento do texto
        if (sentimentoCalculado.equals("Positivo")) {
            meeting.setNotaNps(9.5); // Promotor
        } else if (sentimentoCalculado.contains("Misto")) {
            meeting.setNotaNps(7.0); // Neutro
        } else {
            meeting.setNotaNps(4.0); // Detrator
        }

        ResultadoAnalise resultado = new ResultadoAnalise();
        // CORRIGIDO: Agora chamando o nome correto do método 'gerarResumo'
        resultado.setResumo(gerarResumo(meeting.getTranscricao()));
        resultado.setSentimento(sentimentoCalculado);
        resultado.setPalavrasChave(extrairPalavrasChave(palavras));

        // 3. Regra de Negócio: Trata Alertas de Risco (Concorrência)
        if (detectarRisco(palavras)) {
            resultado.adicionarAlerta(new AlertaRisco(
                    "Ameaça de Concorrência",
                    "Alto",
                    "Cliente avaliando solução da " + detectarConcorrente(palavras) + " para RH.",
                    detectarConcorrente(palavras),
                    true
            ));
        }

        // 4. Regra de Negócio: Trata Alertas de Oportunidade (Upsell/Expansão)
        if (detectarOportunidade(palavras)) {
            String descricaoOportunidade = sentimentoCalculado.equals("Positivo")
                    ? "Alta probabilidade de expansão comercial e aumento de licenças."
                    : "Alta probabilidade de venda do módulo de Folha de Pagamento.";

            resultado.adicionarAlerta(new AlertaOportunidade(
                    "Sinal de Venda (Upsell)",
                    "Médio/Alto",
                    descricaoOportunidade,
                    detectarProdutoRelacionado(palavras),
                    detectarUpsell(palavras)
            ));
        }

        // 5. Classificação de Contexto Corporativo
        if (detectarRisco(palavras) && detectarOportunidade(palavras)) {
            resultado.setClassificacao("Misto (Oportunidade Comercial & Risco de Churn)");
        } else if (sentimentoCalculado.equals("Positivo") && detectarOportunidade(palavras)) {
            resultado.setClassificacao("Oportunidade Comercial Expansionista (Sucesso do Cliente)");
        } else if (detectarOportunidade(palavras)) {
            resultado.setClassificacao("Oportunidade Comercial (Módulos Não Contratados)");
        } else if (detectarRisco(palavras)) {
            resultado.setClassificacao("Risco Crítico de Churn (Concorrência Ativa)");
        } else {
            resultado.setClassificacao("Relacionamento / Cliente Estabilizado");
        }

        return resultado;
    }

    public String analisarSentimento(List<String> palavras) {
        int positivas = 0;
        int negativas = 0;

        for (String palavra : palavras) {
            if (palavrasPositivas.contains(palavra)) positivas++;
            if (palavrasNegativas.contains(palavra)) negativas++;
        }

        // Tratamento contextualizado para a frase "reduziu retrabalho"
        if (palavras.contains("reduziu") && palavras.contains("retrabalho")) {
            negativas--;
        }

        if (positivas > 0 && negativas > 0) {
            return "Misto (Satisfeito com um sector / Frustrado com outro)";
        }
        if (positivas > negativas) return "Positivo";
        if (negativas > positivas) return "Negativo";

        return "Neutro";
    }

    public boolean detectarRisco(List<String> palavras) {
        for (String palavra : palavras) {
            if (palavrasRisco.contains(palavra)) return true;
        }
        return false;
    }

    public boolean detectarOportunidade(List<String> palavras) {
        for (String palavra : palavras) {
            if (palavrasOportunidade.contains(palavra)) return true;
        }
        return false;
    }

    public List<String> extrairPalavrasChave(List<String> palavras) {
        List<String> palavrasChave = new ArrayList<>();
        for (String palavra : palavras) {
            if (palavrasOportunidade.contains(palavra) ||
                    palavrasRisco.contains(palavra) ||
                    palavrasNegativas.contains(palavra) ||
                    palavra.equals("protheus") ||
                    palavra.equals("rm")) {

                if (!palavrasChave.contains(palavra)) {
                    palavrasChave.add(palavra);
                }
            }
        }
        return palavrasChave;
    }

    // CORRIGIDO: Nome do método alterado de 'generarResumo' para 'gerarResumo'
    public String gerarResumo(String texto) {
        if (texto == null || texto.isEmpty()) return "Sem resumo disponível.";
        if (texto.length() <= 150) return texto;
        return texto.substring(0, 150) + "...";
    }

    public String detectarConcorrente(List<String> palavras) {
        for (String palavra : palavras) {
            if (palavrasRisco.contains(palavra) && !palavra.equals("trocar") && !palavra.equals("migracao")) {
                return palavra.substring(0, 1).toUpperCase() + palavra.substring(1);
            }
        }
        return "Não explicitado";
    }

    // CORRIGIDO: Removido o 'Lazarus' intruso e utilizado a lista correta 'palavras'
    public String detectarProdutoRelacionado(List<String> palavras) {
        boolean temProtheus = palavras.contains("protheus");
        boolean temRm = palavras.contains("rm");

        if (temProtheus && temRm) {
            if (palavras.contains("expandir") || palavras.contains("aumentar")) {
                return "Expansão do ecossistema TOTVS (Novos Usuários/Módulos)";
            }
            return "TOTVS Protheus (Atual) e TOTVS RM (Oportunidade)";
        }
        if (temRm || palavras.contains("folha") || palavras.contains("rh")) {
            return "TOTVS RM (Módulo RH/Folha)";
        }
        if (temProtheus) {
            return "TOTVS Protheus (Módulo ERP/Backoffice)";
        }

        return "Ecossistema TOTVS";
    }

    public boolean detectarUpsell(List<String> palavras) {
        return palavras.contains("folha") || palavras.contains("rh") || palavras.contains("expandir") || palavras.contains("aumentar");
    }
}