
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Biblioteca biblioteca = Biblioteca.carregar();

    public static void main(String[] args) {
        biblioteca.cadastrarDadosIniciais();

        int opcao;

        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1 -> listarLivros(false);
                case 2 -> listarLivros(true);
                case 3 -> cadastrarAutor();
                case 4 -> cadastrarLivro();
                case 5 -> realizarEmprestimo();
                case 6 -> devolverLivro();
                case 7 -> listarEmprestimos();
                case 8 -> buscarLivro();
                case 9 -> consultarHistorico();
                case 0 -> {
                    biblioteca.salvar();
                    System.out.println("Dados salvos. Até a próxima!");
                }
                default -> System.out.println("Opção inválida.");
            }

            if (opcao != 0) {
                System.out.println("\nPressione ENTER para continuar...");
                scanner.nextLine();
            }

        } while (opcao != 0);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n======================================");
        System.out.println("       SISTEMA DE BIBLIOTECA");
        System.out.println("======================================");
        System.out.println("1  - Listar todos os livros");
        System.out.println("2  - Listar livros disponíveis");
        System.out.println("3  - Cadastrar autor");
        System.out.println("4  - Cadastrar livro");
        System.out.println("5  - Realizar empréstimo");
        System.out.println("6  - Devolver livro");
        System.out.println("7  - Listar empréstimos");
        System.out.println("8  - Buscar livro por título ou autor");
        System.out.println("9  - Consultar histórico de um cliente");
        System.out.println("0  - Sair");
        System.out.println("======================================");
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String texto = scanner.nextLine().trim();

            if (!texto.isBlank()) {
                return texto;
            }

            System.out.println("O campo não pode ficar vazio.");
        }
    }

    private static void listarLivros(boolean apenasDisponiveis) {
        List<Livro> livros = apenasDisponiveis
                ? biblioteca.listarDisponiveis()
                : biblioteca.getLivros();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }

        System.out.println("\n--- LIVROS ---");

        for (Livro livro : livros) {
            System.out.println(livro);
        }
    }

    private static void cadastrarAutor() {
        System.out.println("\n--- CADASTRAR AUTOR ---");

        String nome = lerTexto("Nome do autor: ");
        LocalDate nascimento;

        while (true) {
            System.out.print("Data de nascimento (AAAA-MM-DD): ");
            String data = scanner.nextLine().trim();

            try {
                nascimento = LocalDate.parse(data);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Exemplo: 1980-05-21");
            }
        }

        Autor autor = biblioteca.cadastrarAutor(nome, nascimento);
        System.out.println("Autor cadastrado com sucesso! ID: " + autor.getId());
    }

    private static void cadastrarLivro() {
        System.out.println("\n--- CADASTRAR LIVRO ---");

        List<Autor> autores = biblioteca.getAutores();

        if (autores.isEmpty()) {
            System.out.println("Cadastre um autor antes de cadastrar livros.");
            return;
        }

        System.out.println("Autores cadastrados:");

        for (Autor autor : autores) {
            System.out.println(autor);
        }

        String titulo = lerTexto("Título do livro: ");
        int idAutor = lerInteiro("ID do autor: ");

        Livro livro = biblioteca.cadastrarLivro(titulo, idAutor);

        if (livro == null) {
            System.out.println("Autor não encontrado. Livro não cadastrado.");
        } else {
            System.out.println("Livro cadastrado com sucesso! ID: " + livro.getId());
        }
    }

    private static void realizarEmprestimo() {
        System.out.println("\n--- REALIZAR EMPRÉSTIMO ---");

        listarLivros(true);

        if (biblioteca.listarDisponiveis().isEmpty()) {
            return;
        }

        int idLivro = lerInteiro("ID do livro: ");
        String nome = lerTexto("Nome do cliente: ");

        if (biblioteca.realizarEmprestimo(idLivro, nome)) {
            System.out.println("Empréstimo realizado com sucesso!");
        } else {
            System.out.println("Livro inexistente ou indisponível.");
        }
    }

    private static void devolverLivro() {
        System.out.println("\n--- DEVOLVER LIVRO ---");

        boolean existemAbertos = false;

        for (Emprestimo emprestimo : biblioteca.getEmprestimos()) {
            if (emprestimo.estaEmAberto()) {
                System.out.println(emprestimo);
                existemAbertos = true;
            }
        }

        if (!existemAbertos) {
            System.out.println("Não existem empréstimos pendentes.");
            return;
        }

        int id = lerInteiro("ID do empréstimo a devolver: ");

        if (biblioteca.devolverLivro(id)) {
            System.out.println("Devolução registrada com sucesso!");
        } else {
            System.out.println("Empréstimo inválido ou já devolvido.");
        }
    }

    private static void listarEmprestimos() {
        System.out.println("\n--- HISTÓRICO DE EMPRÉSTIMOS ---");

        List<Emprestimo> emprestimos = biblioteca.getEmprestimos();

        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo registrado.");
            return;
        }

        for (Emprestimo emprestimo : emprestimos) {
            System.out.println(emprestimo);
        }
    }

    private static void buscarLivro() {
        System.out.println("\n--- BUSCAR LIVRO ---");
        System.out.println("1 - Buscar por título");
        System.out.println("2 - Buscar por autor");

        int opcao = lerInteiro("Escolha: ");

        List<Livro> resultados;

        if (opcao == 1) {
            String titulo = lerTexto("Digite o título ou parte dele: ");
            resultados = biblioteca.buscarPorTitulo(titulo);
        } else if (opcao == 2) {
            String autor = lerTexto("Digite o nome ou parte dele: ");
            resultados = biblioteca.buscarPorAutor(autor);
        } else {
            System.out.println("Opção inválida.");
            return;
        }

        if (resultados.isEmpty()) {
            System.out.println("Nenhum resultado encontrado.");
            return;
        }

        for (Livro livro : resultados) {
            System.out.println(livro);
        }
    }

    private static void consultarHistorico() {
        System.out.println("\n--- HISTÓRICO DO CLIENTE ---");

        String nome = lerTexto("Nome ou parte do nome do cliente: ");
        List<Emprestimo> historico = biblioteca.historicoCliente(nome);

        if (historico.isEmpty()) {
            System.out.println("Nenhum empréstimo encontrado para esse cliente.");
            return;
        }

        for (Emprestimo emprestimo : historico) {
            System.out.println(emprestimo);
        }
    }
}
