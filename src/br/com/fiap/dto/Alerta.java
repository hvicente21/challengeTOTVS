package br.com.fiap.dto;


public class Alerta {
    private int idAlerta;
    private ResultadoAnalise resultadoAnalise;
    private String tipoAlerta;
    private String nivelRisco;
    private String descricao;

    public Alerta() {
    }
    public Alerta(int idAlerta, ResultadoAnalise resultadoAnalise, String tipoAlerta, String nivelRisco, String descricao) {
        this.idAlerta = idAlerta;
        this.resultadoAnalise = resultadoAnalise;
        setTipoAlerta(tipoAlerta);
        setNivelRisco(nivelRisco);
        setDescricao(descricao);
    }

    public int getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(int idAlerta) {
        this.idAlerta = idAlerta;
    }

    public ResultadoAnalise getResultadoAnalise() {
        return resultadoAnalise;
    }

    public void setResultadoAnalise(ResultadoAnalise resultadoAnalise) {
        this.resultadoAnalise = resultadoAnalise;
    }

    public String getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(String tipoAlerta) {
        if (tipoAlerta == null ||
                (!tipoAlerta.equals("Risco") && !tipoAlerta.equals("Oportunidade"))) {
            throw new IllegalArgumentException(
                    "O tipo do alerta deve ser Risco ou Oportunidade."
            );
        }

        this.tipoAlerta = tipoAlerta;
    }

    public String getNivelRisco() {
        return nivelRisco;
    }

    public void setNivelRisco(String nivelRisco) {
        if (tipoAlerta == null) {
            throw new IllegalStateException(
                    "Defina o tipo do alerta antes do nível de risco."
            );
        }
        if (tipoAlerta.equals("Oportunidade")) {
            this.nivelRisco = null;
            return;
        }
        if (nivelRisco == null) {
            throw new IllegalArgumentException(
                    "Alertas de risco devem possuir nível de risco."
            );
        }
        if (!nivelRisco.equals("Baixo") &&
                !nivelRisco.equals("Médio") &&
                !nivelRisco.equals("Alto") &&
                !nivelRisco.equals("Crítico")) {

            throw new IllegalArgumentException(
                    "O nível de risco deve ser Baixo, Médio, Alto ou Crítico."
            );
        }
        this.nivelRisco = nivelRisco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "A descrição não pode ser nula ou vazia."
            );
        }
        descricao = descricao.trim();
        if (descricao.length() > 500) {
            throw new IllegalArgumentException(
                    "A descrição deve possuir no máximo 500 caracteres."
            );
        }
        this.descricao = descricao;
    }

    public String exibirAlerta() {
        if (tipoAlerta.equals("Risco")) {
            return "[" + tipoAlerta + " - " + nivelRisco + "] " + descricao;
        }

        return "[" + tipoAlerta + "] " + descricao;
    }

    public String toString() {
        return "Alerta{" +
                "idAlerta=" + idAlerta +
                ", idResultadoAnalise=" +
                (resultadoAnalise != null ?
                        resultadoAnalise.getIdResultadoAnalise() : 0) +
                ", tipoAlerta='" + tipoAlerta + '\'' +
                ", nivelRisco='" + nivelRisco + '\'' +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}