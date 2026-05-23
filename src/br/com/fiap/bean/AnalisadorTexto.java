package br.com.fiap.bean;
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
        this.palavrasPositivas = Arrays.asList("satisfeito", "satisfeita", "excelente", "ótimo", "boa", "bom", "gostamos", "aprovado", "eficiente", "rápido", "funcionando", "estável", "sucesso", "parceria", "evolução", "crescimento", "produtivo", "melhoria", "facilitou", "organizado", "automatizado", "recomendamos", "confiável", "atendeu", "resultado", "qualidade", "positivo", "performance", "ganho", "benefício", "integração", "implantação bem sucedida", "superou expectativas", "resolveu", "prático", "eficaz", "seguro", "inovador", "intuitivo", "excelente suporte"
        );
        this.palavrasNegativas = Arrays.asList("problema", "erro", "falha", "lentidão", "travando", "instável", "ruim", "demorado", "dificuldade", "insatisfeito", "insatisfeita", "reclamação", "cancelamento", "prejuízo", "bug", "inconsistente", "complexo", "fraco", "péssimo", "atraso", "reprocesso", "suporte ruim", "desorganizado", "queda", "interrupção", "rejeitado", "ineficiente", "custo alto","caro", "frustração", "negativo", "instabilidade","não funciona", "não atende", "retrabalho", "crítico", "desconforto","descontentamento", "baixa performance", "perda"
        );
        this.palavrasRisco = Arrays.asList("senior", "senior sistemas", "sap", "oracle", "linx", "omie", "netsuite", "concorrente", "cancelar", "cancelamento", "churn", "trocar sistema", "insatisfação", "reclamação", "problema recorrente", "instabilidade", "não atende", "não funciona", "perda", "risco", "encerrar contrato", "migração", "desistir", "suporte ruim", "custos elevados", "queda de performance", "baixa utilização", "sem retorno", "falhas", "atraso", "retrabalho", "crítico", "urgente", "multa", "desconforto", "descontentamento", "avaliando concorrente", "trocar fornecedor", "problemas constantes", "cliente irritado", "não renovar", "perda de confiança"
        );
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

    //metodos da classe
    public boolean detectarRisco(String texto){
        texto = texto.toLowerCase();
        for (String palavra : palavrasRisco){
            if (texto.contains(palavra)){
                return true;
            }
        }
        return false;
    }

    public boolean detectarOportunidade(String texto){
        texto = texto.toLowerCase();
        for (String palavra : palavrasOportunidade){
            if (texto.contains(palavra)){
                return true;
            }
        }
        return false;
    }

}
