import java.util.Scanner;

public class BatalhaNaval {
    static int TAMANHO_TABULEIRO = 10;
    static char SIMBOLO_AGUA = '~';
    static String COLUNAS = "ABCDEFGHIJK";

    public static char[][] criaTabuleiro() {
        char[][] tabuleiro = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];

        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                tabuleiro[i][j] = SIMBOLO_AGUA;
            }
        }

        return  tabuleiro;
    }

    public static void exibirTabuleiro(char[][] tabuleiro, boolean ocultarNavios) {
        System.out.printf("%4s","");//espaço para alinhar letras nas colunas
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            System.out.printf("%3s",COLUNAS.charAt(i)+" ");
        }
        System.out.println();

        for (int linha = 0; linha < TAMANHO_TABULEIRO ; linha++) {
            System.out.printf("%3d",linha + 1);
            for(int coluna = 0; coluna < TAMANHO_TABULEIRO; coluna++) {
                char celulaTabuleiro = tabuleiro[linha][coluna];

                if(ocultarNavios && Character.isDigit(celulaTabuleiro)) {
                    System.out.printf("%3s", SIMBOLO_AGUA);
                }else {
                    System.out.printf("%3s",tabuleiro[linha][coluna]);
                }

            }
            System.out.println();
        }

    }

    public static boolean fecharJogo() {
            System.out.println("Saindo...");
            return true;
    }

    public static void exibirMenu(Scanner scan) {
        int opcao;
        boolean sair = false;
        while (!sair) {
            System.out.printf("%2s\n","===== BATALHA NAVAL =====");
            System.out.printf("%2s\n","1. Jogador vs Computador");
            System.out.printf("%2s\n","2. Jogador vs Jogador");
            System.out.printf("%2s\n","3. Sair");
            System.out.printf("%2s","Escolha uma opção:");
            opcao = scan.nextInt();

            switch (opcao) {
                case 1 -> System.out.println("Jogador vs Computador");
                case 2 -> System.out.println("Jogador vs Jogador");
                case 3 -> sair = fecharJogo();
                default -> System.out.println("Opção inválida!! Tente novamente.");
            }
        }

    }


    public static void main(String[] args) {
      char[][] tabuleiro1 = criaTabuleiro();
      char[][] tabuleiro2 = criaTabuleiro();
      Scanner scanner = new Scanner(System.in);

      exibirMenu(scanner);

   }
}

