package br.com.fiap.model.entity;
import jakarta.persistence.*;

@Entity(name ="PalavraChave")
@Table(name = "PALAVRA_CHAVE")
public class PalavraChave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PALAVRA_CHAVE")
    private int idPalavraChave;
    @Column(name = "PALAVRA", nullable = false, unique = true, length = 100)
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
