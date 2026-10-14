
import java.io.Serializable;
import java.time.LocalDate;

public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private Autor autor;
    private boolean disponivel;
    private LocalDate dataCadastro;
    private LocalDate dataAtualizacao;

    public Livro(int id, String titulo, Autor autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.disponivel = true;
        this.dataCadastro = LocalDate.now();
        this.dataAtualizacao = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public LocalDate getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void emprestar() {
        disponivel = false;
        dataAtualizacao = LocalDate.now();
    }

    public void devolver() {
        disponivel = true;
        dataAtualizacao = LocalDate.now();
    }

    @Override
    public String toString() {
        return "ID: " + id
                + " | Título: " + titulo
                + " | Autor: " + autor.getNome()
                + " | Disponível: " + (disponivel ? "Sim" : "Não")
                + " | Cadastro: " + dataCadastro
                + " | Atualização: " + dataAtualizacao;
    }
}
