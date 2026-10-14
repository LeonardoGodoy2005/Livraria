
import java.io.Serializable;
import java.time.LocalDate;

public class Autor implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private LocalDate dataNascimento;

    public Autor(int id, String nome, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Nome: " + nome
                + " | Nascimento: " + dataNascimento;
    }
}
