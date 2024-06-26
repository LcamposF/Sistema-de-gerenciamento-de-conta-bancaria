import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static contaCorrente contaCorrente;
    private static contaPoupanca contaPoupanca;
    private static Map<String, Usuario> usuarios = new HashMap<>();

    public static void main(String[] args) {

        boolean exit = false;

        while (!exit) {
            System.out.println("\n---------------------------------------------------------");
            System.out.println("\n--------------- Bem vindo a nossa Agência ---------------");
            System.out.println("\n---------------------------------------------------------");
            System.out.println("\n---------------Que operação deseja realizar? ------------");
            System.out.println("\n          1. Cadastrar Conta Corrente          ");
            System.out.println("\n          2. Cadastrar Conta Poupança");
            System.out.println("\n          3. Realizar Saque (Conta Corrente)");
            System.out.println("\n          4. Realizar Depósito (Conta Corrente)");
            System.out.println("\n          5. Realizar Saque (Conta Poupança)");
            System.out.println("\n          6. Realizar Depósito (Conta Poupança)");
            System.out.println("\n          7. Calcular Rendimento Mensal (Conta Poupança)");
            System.out.println("\n          8. Realizar Transferência");
            System.out.println("\n          9. Mostrar Saldos");
            System.out.println("\n          10. Sair");
            System.out.print("\n Escolha uma opção: ");
            int opcao = sc.nextInt();

            switch (opcao) {
                case 1:
                    cadastrarContaCorrente();
                    break;
                case 2:
                    cadastrarContaPoupanca();
                    break;
                case 3:
                    realizarSaque(contaCorrente);
                    break;
                case 4:
                    realizarDeposito(contaCorrente);
                    break;
                case 5:
                    realizarSaque(contaPoupanca);
                    break;
                case 6:
                    realizarDeposito(contaPoupanca);
                    break;
                case 7:
                    calcularRendimentoMensal();
                    break;
                case 8:
                    realizarTransferencia();
                    break;
                case 9:
                    mostrarSaldos();
                    break;
                case 10:
                    exit = true;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void cadastrarContaCorrente() {
        sc.nextLine();
        System.out.print("Titular da conta: ");
        String titularCC = sc.nextLine();
        System.out.print("Saldo inicial: ");
        double saldoInicialCC = sc.nextDouble();
        String id = "Usuario" + (usuarios.size() + 1);
        String senha = gerarSenhaAleatoria();
        usuarios.put(id, new Usuario(titularCC, senha, true));
        contaCorrente = new contaCorrente(id, titularCC, saldoInicialCC);
        System.out.println("Conta Corrente cadastrada com sucesso.");
        System.out.println("ID do usuário: " + id);
        System.out.println("Senha: " + senha);
    }

    private static void cadastrarContaPoupanca() {
        sc.nextLine();
        System.out.print("Titular da conta: ");
        String titularCP = sc.nextLine();
        System.out.print("Saldo inicial: ");
        double saldoInicialCP = sc.nextDouble();
        String id = "Usuario" + (usuarios.size() + 1);
        String senha = gerarSenhaAleatoria();
        usuarios.put(id, new Usuario(titularCP, senha, false));
        contaPoupanca = new contaPoupanca(id, titularCP, saldoInicialCP);
        System.out.println("Conta Poupança cadastrada com sucesso.");
        System.out.println("ID do usuário: " + id);
        System.out.println("Senha: " + senha);
    }

    private static String gerarSenhaAleatoria() {
        Random random = new Random();
        int senha = random.nextInt(800000) + 200000;
        return String.valueOf(senha);
    }

    private static Usuario autenticarUsuario(contaCorrente contaCorrente) {
        sc.nextLine();
        System.out.print("ID do usuário: ");
        String id = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        Usuario usuario = usuarios.get(id);
        if (usuario != null && usuario.autenticar(senha)) {
            return usuario;
        } else {
            System.out.println("Autenticação falhou.");
            return null;
        }
    }

    private static void realizarSaque(contaBancaria conta) {
        Usuario usuario = autenticarUsuario((contaCorrente) conta);
        if (conta != null && usuario != null) {
            System.out.print("Digite o valor para sacar: ");
            double valorSaque = sc.nextDouble();
            conta.sacar(valorSaque, usuario);
        } else {
            System.out.println("Conta não cadastrada ou usuário não autenticado.");
        }
    }

    private static void realizarDeposito(contaBancaria conta) {
        Usuario usuario = autenticarUsuario((contaCorrente) conta);
        if (conta != null && usuario != null) {
            System.out.print("Digite o valor para depositar: ");
            double valorDeposito = sc.nextDouble();
            conta.depositar(valorDeposito, usuario);
        } else {
            System.out.println("Conta não cadastrada ou usuário não autenticado.");
        }
    }

    private static void calcularRendimentoMensal() {
        if (contaPoupanca != null) {
            contaPoupanca.calculoDeRendimentoMensal();
        } else {
            System.out.println("Conta Poupança não cadastrada.");
        }
    }

    private static void realizarTransferencia() {
        Usuario usuario = autenticarUsuario(contaCorrente);
        if (contaCorrente != null && contaPoupanca != null && usuario != null) {
            System.out.print("Digite o valor para transferir: ");
            double valorTransferencia = sc.nextDouble();

            System.out.println("Escolha a conta de origem:");
            System.out.println("1. Conta Corrente");
            System.out.println("2. Conta Poupança");
            System.out.print("Opção: ");
            int opcaoOrigem = sc.nextInt();

            System.out.println("Escolha a conta de destino:");
            System.out.println("1. Conta Corrente");
            System.out.println("2. Conta Poupança");
            System.out.print("Opção: ");
            int opcaoDestino = sc.nextInt();

            if (opcaoOrigem == 1 && opcaoDestino == 2) {
                contaCorrente.transferir(valorTransferencia, contaPoupanca, usuario);
            } else if (opcaoOrigem == 2 && opcaoDestino == 1) {
                contaPoupanca.transferir(valorTransferencia, contaCorrente, usuario);
            } else {
                System.out.println("Opção inválida.");
            }
        } else {
            System.out.println("Ambas as contas devem estar cadastradas e usuário autenticado para realizar a transferência.");
        }
    }

    private static void mostrarSaldos() {
        if (contaCorrente != null) {
            System.out.println("Saldo Conta Corrente " + contaCorrente.getNumeroDaConta() + ": " + contaCorrente.getSaldo());
        } else {
            System.out.println("Conta Corrente não cadastrada.");
        }
        if (contaPoupanca != null) {
            System.out.println("Saldo Conta Poupança " + contaPoupanca.getNumeroDaConta() + ": " + contaPoupanca.getSaldo());
        } else {
            System.out.println("Conta Poupança não cadastrada.");
        }
    }
}

