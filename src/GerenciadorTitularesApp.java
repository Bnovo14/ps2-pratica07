import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class GerenciadorTitularesApp {

    private static ITitularDao dao;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        String url = System.getenv("URL");
        Connection conexao = ConnectionFactory.getConnection(url);
        dao = new TitularDao(conexao);

        lacoMenuPrincipal();

        conexao.close();
        System.out.println("Até mais!");
    }

    private static void lacoMenuPrincipal() {
        int opcao;
        do {
            opcao = solicitarOperacao();
            switch (opcao) {
                case 1 -> cadastrarTitular();
                case 2 -> mostrarTitulares();
                case 3 -> editarTitular();
                case 4 -> apagarTitular();
                case 5 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida. Escolha de 1 a 5.");
            }
        } while (opcao != 5);
    }

    private static int solicitarOperacao() {
        System.out.println();
        System.out.println("===== GERENCIADOR DE TITULARES =====");
        System.out.println("1 - Cadastrar um novo titular");
        System.out.println("2 - Consultar titulares");
        System.out.println("3 - Editar dados de um titular");
        System.out.println("4 - Apagar o cadastro de um titular");
        System.out.println("5 - Sair");
        System.out.print("Escolha uma opção: ");
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void cadastrarTitular() {
        System.out.print("Número do novo titular: ");
        long nro = Long.parseLong(sc.nextLine());
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("RG: ");
        String rg = sc.nextLine();
        System.out.print("CPF: ");
        String cpf = sc.nextLine();

        Titular novo = new Titular(nro, nome, rg, cpf);
        if (dao.criar(novo)) {
            System.out.println("Titular cadastrado com sucesso!");
        } else {
            System.out.println("Não foi possível cadastrar (o número já existe?).");
        }
    }

    private static void mostrarTitulares() {
        List<Titular> titulares = dao.lerTodas();
        if (titulares.isEmpty()) {
            System.out.println("Nenhum titular cadastrado.");
        }
        for (Titular t : titulares) {
            System.out.println("Titular " + t.nroTitular() + " | " + t.nome()
                    + " | RG: " + t.rg() + " | CPF: " + t.cpf());
        }
    }

    private static void editarTitular() {
        System.out.print("Número do titular: ");
        long nro = Long.parseLong(sc.nextLine());

        Titular atual = dao.buscarPeloNumero(nro);
        if (atual == null) {
            System.out.println("Titular não encontrado.");
            return;
        }
        System.out.println("Dados atuais: " + atual.nome() + " | RG: " + atual.rg()
                + " | CPF: " + atual.cpf());

        System.out.print("Novo nome: ");
        String nome = sc.nextLine();
        System.out.print("Novo RG: ");
        String rg = sc.nextLine();
        System.out.print("Novo CPF: ");
        String cpf = sc.nextLine();

        Titular editado = new Titular(nro, nome, rg, cpf);
        if (dao.atualizar(editado)) {
            System.out.println("Dados alterados com sucesso!");
        } else {
            System.out.println("Não foi possível alterar os dados.");
        }
    }

    private static void apagarTitular() {
        System.out.print("Número do titular: ");
        long nro = Long.parseLong(sc.nextLine());

        Titular t = dao.buscarPeloNumero(nro);
        if (t == null) {
            System.out.println("Titular não encontrado.");
            return;
        }
        if (dao.apagar(t)) {
            System.out.println("Cadastro apagado com sucesso!");
        } else {
            System.out.println("Não foi possível apagar o cadastro.");
        }
    }
}