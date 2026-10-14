
import java.io.Serializable;
import java.time.LocalDate;

public class Emprestimo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Livro livro;
    private String nomeCliente;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;

    public Emprestimo(int id, Livro livro, String nomeCliente) {
        this.id = id;
        this.livro = livro;
        this.nomeCliente = nomeCliente;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucao = null;
    }

    public int getId() {
        return id;
    }

    public Livro getLivro() {
        return livro;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public boolean estaEmAberto() {
        return dataDevolucao == null;
    }

    public void registrarDevolucao() {
        dataDevolucao = LocalDate.now();
        livro.devolver();
    }

    @Override
    public String toString() {
        return "Empréstimo #" + id
                + " | Livro: " + livro.getTitulo()
                + " | Cliente: " + nomeCliente
                + " | Retirada: " + dataEmprestimo
                + " | Devolução: "
                + (dataDevolucao == null ? "Pendente" : dataDevolucao);
    }
}
