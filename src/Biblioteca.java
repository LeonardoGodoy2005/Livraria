
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Biblioteca implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String ARQUIVO = "biblioteca.dat";

    private List<Livro> livros = new ArrayList<>();
    private List<Autor> autores = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();

    public static Biblioteca carregar() {
        File arquivo = new File(ARQUIVO);

        if (!arquivo.exists()) {
            return new Biblioteca();
        }

        try (ObjectInputStream entrada = new ObjectInputStream(
                new FileInputStream(arquivo))) {

            return (Biblioteca) entrada.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro ao carregar os dados: " + e.getMessage());
            System.out.println("O sistema iniciará com uma biblioteca vazia.");
            return new Biblioteca();
        }
    }

    public void salvar() {
        try (ObjectOutputStream saida = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO))) {

            saida.writeObject(this);

        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    private int proximoIdAutor() {
        return autores.stream()
                .mapToInt(Autor::getId)
                .max()
                .orElse(0) + 1;
    }

    private int proximoIdLivro() {
        return livros.stream()
                .mapToInt(Livro::getId)
                .max()
                .orElse(0) + 1;
    }

    private int proximoIdEmprestimo() {
        return emprestimos.stream()
                .mapToInt(Emprestimo::getId)
                .max()
                .orElse(0) + 1;
    }

    public Autor cadastrarAutor(String nome, LocalDate nascimento) {
        Autor autor = new Autor(proximoIdAutor(), nome, nascimento);
        autores.add(autor);
        salvar();
        return autor;
    }

    public Livro cadastrarLivro(String titulo, int idAutor) {
        Autor autor = buscarAutorPorId(idAutor);

        if (autor == null) {
            return null;
        }

        Livro livro = new Livro(proximoIdLivro(), titulo, autor);
        livros.add(livro);
        salvar();
        return livro;
    }

    public Autor buscarAutorPorId(int id) {
        for (Autor autor : autores) {
            if (autor.getId() == id) {
                return autor;
            }
        }
        return null;
    }

    public Livro buscarLivroPorId(int id) {
        for (Livro livro : livros) {
            if (livro.getId() == id) {
                return livro;
            }
        }
        return null;
    }

    public List<Autor> getAutores() {
        return new ArrayList<>(autores);
    }

    public List<Livro> getLivros() {
        return new ArrayList<>(livros);
    }

    public List<Emprestimo> getEmprestimos() {
        return new ArrayList<>(emprestimos);
    }

    public List<Livro> listarDisponiveis() {
        List<Livro> resultado = new ArrayList<>();

        for (Livro livro : livros) {
            if (livro.isDisponivel()) {
                resultado.add(livro);
            }
        }

        return resultado;
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        List<Livro> resultado = new ArrayList<>();

        for (Livro livro : livros) {
            if (livro.getTitulo().toLowerCase()
                    .contains(titulo.toLowerCase())) {
                resultado.add(livro);
            }
        }

        return resultado;
    }

    public List<Livro> buscarPorAutor(String nomeAutor) {
        List<Livro> resultado = new ArrayList<>();

        for (Livro livro : livros) {
            if (livro.getAutor().getNome().toLowerCase()
                    .contains(nomeAutor.toLowerCase())) {
                resultado.add(livro);
            }
        }

        return resultado;
    }

    public boolean realizarEmprestimo(int idLivro, String nomeCliente) {
        Livro livro = buscarLivroPorId(idLivro);

        if (livro == null || !livro.isDisponivel()) {
            return false;
        }

        livro.emprestar();

        Emprestimo emprestimo = new Emprestimo(
                proximoIdEmprestimo(),
                livro,
                nomeCliente
        );

        emprestimos.add(emprestimo);
        salvar();
        return true;
    }

    public boolean devolverLivro(int idEmprestimo) {
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getId() == idEmprestimo
                    && emprestimo.estaEmAberto()) {

                emprestimo.registrarDevolucao();
                salvar();
                return true;
            }
        }

        return false;
    }

    public List<Emprestimo> historicoCliente(String nome) {
        List<Emprestimo> resultado = new ArrayList<>();

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getNomeCliente().toLowerCase()
                    .contains(nome.toLowerCase())) {
                resultado.add(emprestimo);
            }
        }

        return resultado;
    }

    public void cadastrarDadosIniciais() {
        if (!autores.isEmpty() || !livros.isEmpty()) {
            return;
        }

        Autor machado = new Autor(
                proximoIdAutor(),
                "Machado de Assis",
                LocalDate.of(1839, 6, 21)
        );

        autores.add(machado);

        Autor orwell = new Autor(
                proximoIdAutor(),
                "George Orwell",
                LocalDate.of(1903, 6, 25)
        );

        autores.add(orwell);

        livros.add(new Livro(proximoIdLivro(), "Dom Casmurro", machado));
        livros.add(new Livro(proximoIdLivro(), "1984", orwell));

        salvar();
    }
}
