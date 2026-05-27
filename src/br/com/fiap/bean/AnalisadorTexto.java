package br.com.fiap.bean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnalisadorTexto {
    //atributos
    private List<String> palavrasPositivas;
    private List<String> palavrasNegativas;
    private List<String> palavrasRisco;
    private List<String> palavrasOportunidade;

    //construtores
    public AnalisadorTexto() {
        this.palavrasPositivas = Arrays.asList("satisfeito", "satisfeita", "excelente", "ótimo", "boa", "bom", "gostamos", "aprovado", "eficiente", "rápido", "funcionando", "estável", "sucesso", "parceria", "evolução", "crescimento", "produtivo", "melhoria", "facilitou", "organizado", "automatizado", "recomendamos", "confiável", "atendeu", "resultado", "qualidade", "positivo", "performance", "ganho", "benefício", "integração", "implantação bem sucedida", "superou expectativas", "resolveu", "prático", "eficaz", "seguro", "inovador", "intuitivo", "excelente suporte");

        this.palavrasNegativas = Arrays.asList("problema", "erro", "falha", "lentidão", "travando", "instável", "ruim", "demorado", "dificuldade", "insatisfeito", "insatisfeita", "reclamação", "cancelamento", "prejuízo", "bug", "inconsistente", "complexo", "fraco", "péssimo", "atraso", "reprocesso", "suporte ruim", "desorganizado", "queda", "interrupção", "rejeitado", "ineficiente", "custo alto", "caro", "frustração", "negativo", "instabilidade", "não funciona", "não atende", "retrabalho", "crítico", "desconforto", "descontentamento", "baixa performance", "perda");

        this.palavrasRisco = Arrays.asList("senior", "senior sistemas", "sap", "oracle", "linx", "omie", "netsuite", "concorrente", "cancelar", "cancelamento", "churn", "trocar sistema", "insatisfação", "reclamação", "problema recorrente", "instabilidade", "não atende", "não funciona", "perda", "risco", "encerrar contrato", "migração", "desistir", "suporte ruim", "custos elevados", "queda de performance", "baixa utilização", "sem retorno", "falhas", "atraso", "retrabalho", "crítico", "urgente", "multa", "desconforto", "descontentamento", "avaliando concorrente", "trocar fornecedor", "problemas constantes", "cliente irritado", "não renovar", "perda de confiança");

        this.palavrasOportunidade = Arrays.asList("upgrade", "expansão", "upsell", "cross-sell", "novo módulo", "automação", "integração", "contratar", "crescimento", "ampliação", "nova licença", "novo contrato", "renovação", "parceria", "implantação", "melhoria", "otimização", "dashboard", "analytics", "inteligência artificial", "nova funcionalidade", "expansão da operação", "multiempresa", "filiais", "mais usuários", "interesse", "oportunidade", "expansão comercial", "modernização", "transformação digital", "rm", "protheus", "fluig", "datasul", "crm", "bi", "rh", "folha de pagamento", "gestão", "automatizar", "escala", "integração total", "centralizar", "consolidar", "expansão do contrato", "nova implantação"
        );
    }

    public AnalisadorTexto(List<String> palavrasPositivas, List<String> palavrasNegativas, List<String> palavrasRisco, List<String> palavrasOportunidade) {
        this.palavrasPositivas = palavrasPositivas;
        this.palavrasNegativas = palavrasNegativas;
        this.palavrasRisco = palavrasRisco;
        this.palavrasOportunidade = palavrasOportunidade;
    }

    // getters e setters
    public List<String> getPalavrasPositivas() {
        return palavrasPositivas;
    }
    public void setPalavrasPositivas(List<String> palavrasPositivas) {
        this.palavrasPositivas = palavrasPositivas;
    }

    public List<String> getPalavrasNegativas() {
        return palavrasNegativas;
    }
    public void setPalavrasNegativas(List<String> palavrasNegativas) {
        this.palavrasNegativas = palavrasNegativas;
    }

    public List<String> getPalavrasRisco() {
        return palavrasRisco;
    }
    public void setPalavrasRisco(List<String> palavrasRisco) {
        this.palavrasRisco = palavrasRisco;
    }

    public List<String> getPalavrasOportunidade() {
        return palavrasOportunidade;
    }
    public void setPalavrasOportunidade(List<String> palavrasOportunidade) {
        this.palavrasOportunidade = palavrasOportunidade;
    }

    // metodos da classe
    public boolean detectarRisco(String texto) {
        texto = texto.toLowerCase();
        for (String palavra : palavrasRisco) {
            if (texto.contains(palavra)) {
                return true;
            }
        }
        return false;
    }

    public boolean detectarOportunidade(String texto) {
        texto = texto.toLowerCase();
        for (String palavra : palavrasOportunidade) {
            if (texto.contains(palavra)) {
                return true;
            }
        }
        return false;
    }

    public String analisarSentimento(String texto) {
        int positivas = 0, negativas = 0;
        texto = texto.toLowerCase();
        List<String> palavras = Arrays.asList(texto.split(" "));

        for (String palavra : palavras) {
            if (palavrasPositivas.contains(palavra)){
                positivas++;
            }
            if (palavrasNegativas.contains(palavra)){
                negativas++;
            }
        }

        if (positivas > negativas) {
            return "Positivo";
        }
        else if (positivas == negativas) {
            return "Neutro";
        }
        else{
            return "Negativo";
        }
    }

    public List<String> extrairPalavrasChave(String texto) {
        texto = texto.toLowerCase();
        List<String> palavras = Arrays.asList(texto.split(" "));
        List<String> palavrasChave = new ArrayList<>();

        for (String palavra : palavras) {
            if (palavrasRisco.contains(palavra) || palavrasOportunidade.contains(palavra)) {
                if (!palavrasChave.contains(palavra)) {
                    palavrasChave.add(palavra);
                }
            }
        }
        return palavrasChave;
    }

    public String gerarResumo(String texto) {
        if (texto.length() <= 100) return texto;
        return texto.substring(0, 100) + "...";
    }

    public double calcularNps(String texto) {
        int positivas = 0, negativas = 0;
        texto = texto.toLowerCase();
        List<String> palavras = Arrays.asList(texto.split(" "));

        for (String palavra : palavras) {
            if (palavrasPositivas.contains(palavra)){
                positivas++;
            }
            if (palavrasNegativas.contains(palavra))
                negativas++;
        }

        int total = positivas + negativas;
        if (total == 0) {
            return 5.0;
        }
        return (double)(positivas * 10) / total;
    }

    // CORRIGIDO: agora retorna ResultadoAnalise em vez de void
    public ResultadoAnalise analisar(Meeting meeting) {
        String texto = meeting.getTranscricao().toLowerCase();
        String sentimento = analisarSentimento(texto);
        List<String> chave = extrairPalavrasChave(texto);
        String resumo = gerarResumo(texto);
        boolean risco = detectarRisco(texto);
        boolean oportunidade = detectarOportunidade(texto);
        double nota = calcularNps(texto);

        meeting.setNotaNps(nota);

        String classificacao;
        if (risco && oportunidade){
            classificacao = "Risco";
        }
        else if (risco){
            classificacao = "Risco";
        }
        else if (oportunidade){
            classificacao = "Oportunidade";
        }
        else{
            classificacao = "Neutro";
        }

        ResultadoAnalise resultado = new ResultadoAnalise();
        resultado.setResumo(resumo);
        resultado.setSentimento(sentimento);
        resultado.setClassificacao(classificacao);

        // palavrasChave pode estar vazia — só seta se tiver algo
        if (!chave.isEmpty()) {
            resultado.setPalavrasChave(chave);
        }

        return resultado;
    }
}