//import java.util.Arrays;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class BatalhaNaval {
    static int TAMANHO_TABULEIRO = 10;
    static char SIMBOLO_AGUA = '~';
    static char SIMBOLO_NAVIO = 'X';
    static char SIMBOLO_OCULTO = '?';
    static String COLUNAS = "ABCDEFGHIJ";
    static int[] NAVIOS_ORDEM = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
    static char HORIZONTAL = 'H';
    static char VERTICAL = 'V';
    static int totalNaviosJogador1 = 20;
    static int totalNaviosJogador2 = 20;


    public static void mostraMensagemTemporaria(String mensagem, boolean limparTela) {
        if (limparTela) limpaTela();
        System.out.print(mensagem);

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


    public static boolean posicaoValida(int linha, int coluna) {
        if(linha  < 0 || linha >= TAMANHO_TABULEIRO) return false;
        if(coluna < 0 || coluna >= TAMANHO_TABULEIRO) return false;
        return true;
    }


    public static boolean tentarPosicionarNavio(char [][] tabuleiro, int linha, int coluna, int tamanhoNavio, char orientacao) {

        if(!posicaoValida(linha,coluna)) return false;

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

                System.out.print("Digite a coluna inicial (A-J): ");
                String colunaLetra = scan.next().toUpperCase();
                int colunaIndice = COLUNAS.indexOf(colunaLetra);

                System.out.print("Digite a orientação (H para horizontal, V para vertical): ");
                int letraInicial = 0;
                char orientacao = scan.next().toUpperCase().charAt(letraInicial);

                posicionado = tentarPosicionarNavio(tabuleiro, linha, colunaIndice, navio, orientacao);

                if (!posicionado) {
                    mostraMensagemTemporaria("Posição inválida ou navio já existe nessa área\n", true);
                }
            }

            limpaTela();
        }

        mostraMensagemTemporaria(jogador + " acabou de posicionar todos seus navios no tabuleiro\n", true);
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
        System.out.printf("%3s ", ""); // Espaço para alinhar letras nas colunas

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


    public static int escolherPosicionamentoNavios(String jogador, Scanner scan) {
        System.out.println(jogador + ", escolha como deseja posicionar seus navios (1-Manual, 2-Automático)!");

        System.out.print("Digite a sua escolha: ");
        int escolha = scan.nextInt();

        while (escolha != 1 && escolha != 2) {
            System.out.println("Opção inválida!");
            System.out.print("Digite a sua escolha:");
            escolha = scan.nextInt();
        }

        limpaTela();

        return escolha;
    }


    public static void posicionarNavios(char [][]tabuleiro, String jogador, int escolha, Scanner scan) {
        switch (escolha) {
            case 1 -> posicionarNaviosManualmente(tabuleiro,scan,jogador);
            case 2 -> posicionarNaviosAutomaticamente(tabuleiro);
            default -> mostraMensagemTemporaria("Opção inválida!", true);
        }
    }

    public static void jogadorVsJogador(Scanner scanner) {
        mostraMensagemTemporaria("Modo de jogo: Jogador vs Jogador", true);
        char[][] tabuleiro1 = criaTabuleiro();
        char[][] tabuleiro2 = criaTabuleiro();

        String jogador1 = pegaNomeJogador(scanner, 1);
        int escolhaPosicionarNaviosJogador1 = escolherPosicionamentoNavios(jogador1, scanner);
        posicionarNavios(tabuleiro1, jogador1, escolhaPosicionarNaviosJogador1, scanner);
        scanner.nextLine();
        String jogador2 = pegaNomeJogador(scanner, 2);
        int escolhaPosicionarNaviosJogador2 = escolherPosicionamentoNavios(jogador2, scanner);
        posicionarNavios(tabuleiro2, jogador2, escolhaPosicionarNaviosJogador2, scanner);

        String jogadorDaRodada = new Random().nextBoolean() ? jogador1 : jogador2;
        System.out.println(jogadorDaRodada + " começa!");

        boolean temVencedor = false;

        totalNaviosJogador1 = 20;
        totalNaviosJogador2 = 20;

        while (!temVencedor) {
            if (jogadorDaRodada.equals(jogador1)) {
                System.out.println("Vez de " + jogador1);

                boolean acertou = realizaJogadaManualmente(scanner, tabuleiro2, jogador1);
                if (acertou) --totalNaviosJogador2;
                temVencedor = verificaVencedor(totalNaviosJogador2);
                exibirTotalDeNavios(jogador2, totalNaviosJogador2);

                if (!temVencedor && !acertou) jogadorDaRodada = jogador2;
            }else {
                System.out.println("Vez de " + jogador2);

                boolean acertou = realizaJogadaManualmente(scanner, tabuleiro1, jogador2);
                if (acertou) --totalNaviosJogador1;
                temVencedor = verificaVencedor(totalNaviosJogador1);
                exibirTotalDeNavios(jogador1, totalNaviosJogador1);

                if (!temVencedor && !acertou) jogadorDaRodada = jogador1;
            }
        }

        mostraMensagemTemporaria("O jogador " + jogadorDaRodada + " venceu!", true);
    }


    public static void jogadorVsComputador(Scanner scanner) {
        mostraMensagemTemporaria("Modo de jogo: Jogador vs Computador", true);
        char[][] jogadorTabuleiro = criaTabuleiro();
        char[][] computadorTabuleiro = criaTabuleiro();

        String jogadorNome = pegaNomeJogador(scanner, 1);
        int escolhaPosicionarNaviosJogador1 = escolherPosicionamentoNavios(jogadorNome, scanner);
        posicionarNavios(jogadorTabuleiro, jogadorNome, escolhaPosicionarNaviosJogador1, scanner);

        posicionarNaviosAutomaticamente(computadorTabuleiro);

        String jogador2 = "Computador";
        String jogadorDaRodada = new Random().nextBoolean() ? jogadorNome : jogador2;
        System.out.println(jogadorDaRodada + " começa!");

        boolean temVencedor = false;

        totalNaviosJogador1 = 20;
        totalNaviosJogador2 = 20;

        while (!temVencedor) {
            if (jogadorDaRodada.equals(jogadorNome)) {
                System.out.println("Vez de " + jogadorNome);

                boolean acertou = realizaJogadaManualmente(scanner, computadorTabuleiro, jogadorNome);
                if (acertou) --totalNaviosJogador2;
                temVencedor = verificaVencedor(totalNaviosJogador2);
                exibirTotalDeNavios(jogador2, totalNaviosJogador2);

                if (!temVencedor && !acertou) jogadorDaRodada = jogador2;
            } else {
                System.out.println("Vez de " + jogador2);

                boolean acertou = realizaJogadaAutomaticamente(jogadorTabuleiro, jogador2);
                if (acertou) --totalNaviosJogador1;
                temVencedor = verificaVencedor(totalNaviosJogador1);
                exibirTotalDeNavios(jogadorNome,totalNaviosJogador1);

                if (!temVencedor && !acertou) jogadorDaRodada = jogadorNome;
            }
        }

        mostraMensagemTemporaria("O jogador " + jogadorDaRodada + " venceu!", true);
    }


    public static void exibirTotalDeNavios(String nomeJogador, int naviosRestantes) {
        System.out.println("Jogador " + nomeJogador + " ainda tem " + naviosRestantes +" blocos de navios");
    }


    public static void exibirMenu(Scanner scan) {
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
                case 1 -> jogadorVsComputador(scan);
                case 2 -> jogadorVsJogador(scan);
                case 3 -> fecharJogo();
                default -> mostraMensagemTemporaria("Opção inválida! Tente novamente", true);
            }
        }
    }


    public static String pegaNomeJogador(Scanner scan, int numeroJogador) {
        System.out.printf("\nNome do jogador %d: ", numeroJogador);
        String jogador = scan.nextLine().trim();

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


    public static boolean realizaJogadaAutomaticamente(char [][] tabuleiro, String jogador) {
        Random random = new Random();
        boolean valida = false;

        while (!valida) {
            int linha = random.nextInt(TAMANHO_TABULEIRO);
            int coluna = random.nextInt(TAMANHO_TABULEIRO);
            boolean acertou = false;
            valida = jogadaValida(tabuleiro, linha, coluna);

            if (valida) {


                if (tabuleiro[linha][coluna] == SIMBOLO_OCULTO) {
                    tabuleiro[linha][coluna] = SIMBOLO_AGUA;
                } else if (Character.isDigit(tabuleiro[linha][coluna])) {
                    tabuleiro[linha][coluna] = SIMBOLO_NAVIO;
                    acertou = true;
                }

                limpaTela();
                exibirTabuleiro(tabuleiro, true);

                System.out.println();
                System.out.println("Jogou " + (linha + 1) + "" + COLUNAS.charAt(coluna));

                if (acertou) {
                    mostraMensagemTemporaria(jogador + " acertou!\n", false);
                } else {
                    mostraMensagemTemporaria(jogador + " errou!\n", false);
                }

                return acertou;
            }
        }

        return false;
    }


    public static boolean realizaJogadaManualmente(Scanner scan, char[][] tabuleiro, String jogador) {
        boolean valida = false;

        while (!valida) {
            int ajusteIndice = 1;
            exibirTabuleiro(tabuleiro, true);

            boolean acertou = false;

            System.out.println();
            System.out.println("Jogada de " + jogador);

            System.out.println();
            System.out.print("Digite a linha do lance (1-10): ");
            int linha = scan.nextInt() - ajusteIndice;

            System.out.print("Digite a coluna do lance (A-J): ");
            String colunaLetra = scan.next().toUpperCase();
            int colunaIndice = COLUNAS.indexOf(colunaLetra);

            valida = jogadaValida(tabuleiro, linha, colunaIndice);

            if (valida) {


                if (tabuleiro[linha][colunaIndice] == SIMBOLO_OCULTO) {
                    tabuleiro[linha][colunaIndice] = SIMBOLO_AGUA;
                } else if (Character.isDigit(tabuleiro[linha][colunaIndice])) {
                    tabuleiro[linha][colunaIndice] = SIMBOLO_NAVIO;
                    acertou = true;
                }

                limpaTela();
                exibirTabuleiro(tabuleiro, true);

                System.out.println();
                System.out.println("Jogou " + (linha + 1) + "" + colunaLetra);

                if (acertou) {
                    mostraMensagemTemporaria(jogador + " acertou!\n", false);
                } else {
                    mostraMensagemTemporaria(jogador + " errou!\n", false);
                }

                return acertou;
            } else {
                mostraMensagemTemporaria("Jogada inválida! Tente novamente", true);
            }
        }

        return false;
    }


    public static boolean verificaVencedor(int totalNaviosRestantes) {
        return totalNaviosRestantes == 0;
    }


    public static void mostraVencedor(String jogador) {
        mostraMensagemTemporaria(jogador + " venceu o jogo!", true);
    }


    public static void main(String[] args) {

      Scanner scanner = new Scanner(System.in);
      exibirMenu(scanner);

      scanner.close();
   }
}

