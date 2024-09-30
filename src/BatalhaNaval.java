//import java.util.Arrays;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {
    static int TAMANHO_TABULEIRO = 10;
    static char SIMBOLO_AGUA = '~';
    static char SIMBOLO_NAVIO = 'X';
    static char SIMBOLO_OCULTO = '?';
    static String COLUNAS = "ABCDEFGHIJK";
    //static int[] NAVIOS_ORDEM = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    static int[] NAVIOS_ORDEM = {2};
    static char HORIZONTAL = 'H';
    static char VERTICAL = 'V';


    public static void mostraMensagemTemporaria(String mensagem, boolean limparTela) {
        if (limparTela) limpaTela();
        System.out.print(mensagem);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        limpaTela();
    }


    public static void limpaTela() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static boolean haColisaoNaVertical(char[][] tabuleiro, int linha, int coluna, int tamanhoNavio) {
        for (int i = 0; i < tamanhoNavio; i++) {
            char celulaTabuleiro = tabuleiro[linha + i][coluna];
            boolean temNavio = Character.isDigit(celulaTabuleiro);
            if (temNavio) return true;
        }

        return false;
    }


    public static  boolean haColisaoNaHorizontal(char[][] tabuleiro, int linha, int coluna, int tamanhoNavio) {
        for (int i = 0; i < tamanhoNavio; i++) {
            char celulaTabuleiro = tabuleiro[linha][coluna + i];
            boolean temNavio = Character.isDigit(celulaTabuleiro);
            if (temNavio) return true;
        }

        return false;
    }


    public static void inserirNavioHorizontal(char[][] tabuleiro, int linha, int coluna, int tamanhoNavio) {
        for (int i = 0; i < tamanhoNavio; i++) {
            tabuleiro[linha][coluna + i] = Character.forDigit(tamanhoNavio, 10);
        }
    }


    public static void inserirNavioVertical(char[][] tabuleiro, int linha, int coluna, int tamanhoNavio) {
        for (int i = 0; i < tamanhoNavio; i++) {
            tabuleiro[linha + i][coluna] = Character.forDigit(tamanhoNavio, 10);
        }
    }


    public static boolean tentarPosicionarNavio(char [][] tabuleiro, int linha, int coluna, int tamanhoNavio, char orientacao) {
        if (orientacao == HORIZONTAL) {
            if (coluna + tamanhoNavio > TAMANHO_TABULEIRO) return false;
            if (haColisaoNaHorizontal(tabuleiro, linha, coluna, tamanhoNavio)) return false;
            inserirNavioHorizontal(tabuleiro, linha,coluna, tamanhoNavio);
            return true;
        }

        if (orientacao == VERTICAL) {
            if (linha + tamanhoNavio > TAMANHO_TABULEIRO) return false;
            if (haColisaoNaVertical(tabuleiro, linha, coluna, tamanhoNavio)) return false;
            inserirNavioVertical(tabuleiro, linha, coluna, tamanhoNavio);
            return true;
        }

        return false;
    }

    public static void posicionarNaviosAutomaticamente(char [][] tabuleiro) {
        Random random = new Random();
        for (int navio: NAVIOS_ORDEM) {
            boolean posicionado = false;
            while (!posicionado) {
                int linha = random.nextInt(TAMANHO_TABULEIRO);
                int coluna = random.nextInt(TAMANHO_TABULEIRO);
                char orientacao = random.nextBoolean() ? HORIZONTAL : VERTICAL;
                posicionado = tentarPosicionarNavio(tabuleiro, linha, coluna, navio,orientacao);
            }
        }
    }


    public static void posicionarNaviosManualmente(char [][] tabuleiro, Scanner scan, String jogador) {
        for (int navio: NAVIOS_ORDEM) {
            boolean posicionado = false;

            while (!posicionado) {
                System.out.println("Vez de " + jogador);
                System.out.println("Posicione um navio de " + navio + " espaço(s) no tabuleiro");

                exibirTabuleiro(tabuleiro, false);

                System.out.println();
                System.out.print("Digite a linha inicial (1-10): ");
                int ajusteIndice = 1;
                int linha = scan.nextInt() - ajusteIndice;

                System.out.println();
                System.out.print("Digite a coluna inicial (A-K): ");
                String colunaLetra = scan.next().toUpperCase();
                int colunaIndice = COLUNAS.indexOf(colunaLetra);

                System.out.println();
                System.out.print("Digite a orientação (H para horizontal, V para vertical): ");
                int letraInicial = 0;
                char orientacao = scan.next().toUpperCase().charAt(letraInicial);

                posicionado = tentarPosicionarNavio(tabuleiro,linha,colunaIndice,navio,orientacao);

                if (!posicionado) {
                    mostraMensagemTemporaria("Posição inválida ou navio já existe nessa área", true);
                }
            }

            limpaTela();
        }

        mostraMensagemTemporaria(jogador + " acabou de posicionar todos seus navios no tabuleiro", true);
    }


    public static char[][] criaTabuleiro() {
        char[][] tabuleiro = new char[TAMANHO_TABULEIRO][TAMANHO_TABULEIRO];

        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            for (int j = 0; j < TAMANHO_TABULEIRO; j++) {
                tabuleiro[i][j] = SIMBOLO_OCULTO;
            }
        }

        return  tabuleiro;
    }


    public static void exibirTabuleiro(char[][] tabuleiro, boolean ocultarNavios) {
        System.out.println();
        System.out.printf("%3s ", ""); //espaço para alinhar letras nas colunas

        for (int i = 0; i < TAMANHO_TABULEIRO; i++) {
            System.out.printf("%3s", COLUNAS.charAt(i) + " ");
        }

        System.out.println();

        for (int linha = 0; linha < TAMANHO_TABULEIRO; linha++) {
            System.out.printf("%2d ", linha + 1);

            for (int coluna = 0; coluna < TAMANHO_TABULEIRO; coluna++) {
                if (ocultarNavios) {
                    if (Character.isDigit(tabuleiro[linha][coluna])) {
                        System.out.printf("%3s", SIMBOLO_OCULTO);
                    } else {
                        System.out.printf("%3s", tabuleiro[linha][coluna]);
                    }
                } else {
                    if (tabuleiro[linha][coluna] == SIMBOLO_OCULTO) {
                        System.out.printf("%3s", SIMBOLO_AGUA);
                    } else {
                        System.out.printf("%3s", tabuleiro[linha][coluna]);
                    }
                }
            }

            System.out.println();
        }
    }


    public static void fecharJogo() {
        System.out.println("Saindo...");
    }


    public static int exibirMenu(Scanner scan) {
        int opcao = 0;

        limpaTela();

        while (opcao != 1 && opcao != 2 && opcao != 3) {
            System.out.printf("%2s\n", "===== BATALHA NAVAL =====");
            System.out.printf("%2s\n", "1. Jogador vs Computador");
            System.out.printf("%2s\n", "2. Jogador vs Jogador");
            System.out.printf("%2s\n", "3. Sair");
            System.out.println();
            System.out.printf("%2s", "Escolha uma opção: ");
            opcao = scan.nextInt();
            scan.nextLine();

            switch (opcao) {
                case 1 -> mostraMensagemTemporaria("Modo de jogo: Jogador vs Computador", true);
                case 2 -> mostraMensagemTemporaria("Modo de jogo: Jogador vs Jogador", true);
                case 3 -> fecharJogo();
                default -> mostraMensagemTemporaria("Opção inválida! Tente novamente", true);
            }
        }

        return opcao;
    }


    public static String pegaNomeJogador(Scanner scan, int numeroJogador) {
        System.out.printf("Nome do jogador %d: ", numeroJogador);
        String jogador = scan.nextLine();

        return jogador;
    }


    public static boolean jogadaValida(char[][] tabuleiro, int linha, int coluna) {
        boolean valida = true;

        if (linha < 0 || linha >= 10) {
            valida = false;
        } else if (coluna < 0 || coluna >= 10) {
            valida = false;
        } else if (tabuleiro[linha][coluna] == SIMBOLO_AGUA || tabuleiro[linha][coluna] == SIMBOLO_NAVIO) {
            valida = false;
        }

        return valida;
    }


    public static boolean realizaJogadaManualmente(Scanner scan, char[][] tabuleiro, String jogador) {
        boolean valida = false;

        while (!valida) {
            int ajusteIndice = 1;
            exibirTabuleiro(tabuleiro, true);

            System.out.println();
            System.out.println("Jogada de " + jogador);
    
            System.out.println();
            System.out.print("Digite a linha do lance (1-10): ");
            int linha = scan.nextInt() - ajusteIndice;

            System.out.print("Digite a coluna do lance (A-K): ");
            String colunaLetra = scan.next().toUpperCase();
            int colunaIndice = COLUNAS.indexOf(colunaLetra);
    
            valida = jogadaValida(tabuleiro, linha, colunaIndice);

            if (valida) {
                boolean acertou = false;

                if (tabuleiro[linha][colunaIndice] == SIMBOLO_OCULTO) {
                    tabuleiro[linha][colunaIndice] = SIMBOLO_AGUA;
                } else if (Character.isDigit(tabuleiro[linha][colunaIndice])) {
                    tabuleiro[linha][colunaIndice] = SIMBOLO_NAVIO;
                    acertou = true;
                }

                limpaTela();
                exibirTabuleiro(tabuleiro, true);

                System.out.println();
                if (acertou) {
                    mostraMensagemTemporaria(jogador + " acertou!", false);
                } else {
                    mostraMensagemTemporaria(jogador + " errou!", false);
                }
            } else {
                mostraMensagemTemporaria("Jogada inválida! Tente novamente", true);
            }
        }

        return verificaVencedor(tabuleiro);
    }


    public static boolean verificaVencedor(char[][] tabuleiro) {
        boolean temNavio = false;

        for (int linha = 0; linha < TAMANHO_TABULEIRO; linha++) {
            for (int coluna = 0; coluna < TAMANHO_TABULEIRO; coluna++) {
                if (Character.isDigit(tabuleiro[linha][coluna])) {
                    temNavio = true;
                }
            }
        }

        return !temNavio;
    }

    public static void mostraVencedor(String jogador) {
        mostraMensagemTemporaria(jogador + " venceu o jogo! Parabéns!", true);
    }


    public static void main(String[] args) {
      char[][] tabuleiro1 = criaTabuleiro();
      char[][] tabuleiro2 = criaTabuleiro();
      Scanner scanner = new Scanner(System.in);

      int opcao = exibirMenu(scanner);
      int jogada = 1;
      boolean temVencedor = false;

      if (opcao == 1) {
        
      } else {
        String jogador1 = pegaNomeJogador(scanner, 1);
        String jogador2 = pegaNomeJogador(scanner, 2);
        limpaTela();

        posicionarNaviosManualmente(tabuleiro1, scanner, jogador1);
        posicionarNaviosManualmente(tabuleiro2, scanner, jogador2);

        while (!temVencedor) {
            if (jogada == 1) {
                temVencedor = realizaJogadaManualmente(scanner, tabuleiro2, jogador1);
                if (temVencedor) {
                    mostraVencedor(jogador1);
                } else {
                    jogada = 2;
                }
            } else {
                temVencedor = realizaJogadaManualmente(scanner, tabuleiro1, jogador2);
                if (temVencedor) {
                    mostraVencedor(jogador2);
                } else {
                    jogada = 1;
                }
            }
        }
      }
   }
}

