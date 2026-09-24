import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class GerenciadorContasApp {

    // O DAO fica guardado aqui, para todos os métodos usarem (o "-dao 0..1" do slide 18)
    private static IContaDao dao;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        String url = System.getenv("URL");
        Connection conexao = ConnectionFactory.getConnection(url);
        dao = new ContaDao(conexao);

        lacoMenuPrincipal();

        conexao.close();
        System.out.println("Até mais!");
    }

    private static void lacoMenuPrincipal() {
        int opcao;
        do {
            opcao = solicitarOperacao();
            switch (opcao) {
                case 1 -> criarConta();
                case 2 -> mostrarContas();
                case 3 -> alterarConta();
                case 4 -> apagarConta();
                case 5 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida. Escolha de 1 a 5.");
            }
        } while (opcao != 5);
    }

    private static int solicitarOperacao() {
        System.out.println();
        System.out.println("===== GERENCIADOR DE CONTAS =====");
        System.out.println("1 - Criar nova conta");
        System.out.println("2 - Consultar contas");
        System.out.println("3 - Alterar saldo de uma conta");
        System.out.println("4 - Apagar uma conta");
        System.out.println("5 - Sair");
        System.out.print("Escolha uma opção: ");
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return 0;   // 0 cai no "default" e o menu repete
        }
    }

    private static void criarConta() {
        System.out.print("Número da nova conta: ");
        long nro = Long.parseLong(sc.nextLine());
        System.out.print("Saldo inicial: ");
        double saldo = Double.parseDouble(sc.nextLine());

        Conta nova = new Conta(nro, saldo);     // monta a "ficha"
        if (dao.criar(nova)) {                  // entrega a ficha ao DAO
            System.out.println("Conta criada com sucesso!");
        } else {
            System.out.println("Não foi possível criar a conta (o número já existe?).");
        }
    }

    private static void mostrarContas() {
        List<Conta> contas = dao.lerTodas();
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
        }
        for (Conta c : contas) {
            System.out.println("Conta " + c.nroConta() + " | saldo R$ " + c.saldo());
        }
    }

    private static void alterarConta() {
        System.out.print("Número da conta: ");
        long nro = Long.parseLong(sc.nextLine());

        Conta atual = dao.buscarPeloNumero(nro);   // confere se existe
        if (atual == null) {
            System.out.println("Conta não encontrada.");
            return;
        }
        System.out.println("Saldo atual: R$ " + atual.saldo());

        System.out.print("Novo saldo: ");
        double saldo = Double.parseDouble(sc.nextLine());

        Conta alterada = new Conta(nro, saldo);    // record é imutável: cria uma nova
        if (dao.atualizar(alterada)) {
            System.out.println("Saldo alterado com sucesso!");
        } else {
            System.out.println("Não foi possível alterar o saldo.");
        }
    }

    private static void apagarConta() {
        System.out.print("Número da conta: ");
        long nro = Long.parseLong(sc.nextLine());

        Conta conta = dao.buscarPeloNumero(nro);
        if (conta == null) {
            System.out.println("Conta não encontrada.");
            return;
        }
        if (dao.apagar(conta)) {
            System.out.println("Conta apagada com sucesso!");
        } else {
            System.out.println("Não foi possível apagar a conta.");
        }
    }
}