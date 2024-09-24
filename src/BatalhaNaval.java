import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {
    static int TAMANHO_TABULEIRO = 10;
    static char SIMBOLO_AGUA = '~';
    static String COLUNAS = "ABCDEFGHIJK";
    static int[] NAVIOS_ORDEM = {4,3,3,2,2,2,1,1,1,1};
    static char HORIZONTAL = 'H';
    static char VERTICAL = 'V';

    public static boolean tentarPosicionarNavio(char [][] tabuleiro, int linha, int coluna, int tamanhoNavio, char orientacao) {


        if(orientacao == HORIZONTAL) {
            if(coluna + tamanhoNavio > TAMANHO_TABULEIRO) return false;

            for(int i = 0; i < tamanhoNavio; i++) {
                char celulaTabuleiro = tabuleiro[linha][coluna + i];
                boolean temNavio = Character.isDigit(celulaTabuleiro);
                if(temNavio) return false;
            }

            for(int i=0; i< tamanhoNavio; i++) {
                tabuleiro[linha][coluna+i] = Character.forDigit(tamanhoNavio,10);
            }

        } else {
            if(linha + tamanhoNavio > TAMANHO_TABULEIRO) return false;

            for(int i=0; i < tamanhoNavio; i++) {
                char celulaTabuleiro = tabuleiro[linha + i][coluna];
                boolean temNavio = Character.isDigit(celulaTabuleiro);
                if(temNavio) return false;
            }

            for(int i=0; i < tamanhoNavio; i++) {
                tabuleiro[linha+i][coluna] = Character.forDigit(tamanhoNavio,10);
            }
        }

        return true;
    }

    public static void posicionarNaviosAutomaticamente(char [][] tabuleiro) {
        Random random = new Random();
        for(int navio: NAVIOS_ORDEM) {
            boolean posicionado = false;
            while (!posicionado) {
                int linha = random.nextInt(TAMANHO_TABULEIRO);
                int coluna = random.nextInt(TAMANHO_TABULEIRO);
                char orientacao = random.nextBoolean() ? HORIZONTAL : VERTICAL;
                posicionado = tentarPosicionarNavio(tabuleiro, linha, coluna, navio,orientacao);
            }
        }
    }

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
        System.out.printf("%3s","");//espaço para alinhar letras nas colunas
        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            System.out.printf("%3s",COLUNAS.charAt(i)+" ");
        }
        System.out.println();

        for (int linha = 0; linha < TAMANHO_TABULEIRO ; linha++) {
            System.out.printf("%2d",linha + 1);
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

      posicionarNaviosAutomaticamente(tabuleiro1);

      exibirTabuleiro(tabuleiro1,false);





   }
}

