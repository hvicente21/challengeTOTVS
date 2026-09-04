package br.com.fiap.dto;

public class PalavraChave {
    private int idPalavraChave;
    private String palavra;

    public PalavraChave() {
    }

    public PalavraChave(String palavra, int idPalavraChave) {
        setPalavra(palavra);
        this.idPalavraChave = idPalavraChave;
    }

    public int getIdPalavraChave() {
        return idPalavraChave;
    }

    public void setIdPalavraChave(int idPalavraChave) {
        this.idPalavraChave = idPalavraChave;
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        if (palavra == null || palavra.trim().isEmpty()) {
            throw new IllegalArgumentException("A palavra-chave não pode ser nula ou vazia.");
        }

        this.palavra = palavra.trim();
    }

    public String toString() {
        return "PalavraChave{" +
                "idPalavraChave=" + idPalavraChave +
                ", palavra='" + palavra + '\'' +
                '}';
    }
}
