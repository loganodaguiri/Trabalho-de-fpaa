import java.util.*;

public class AlfredCozinha {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Laço principal para processar múltiplos conjuntos de entrada
        while (true) {
            // Leitura dos parâmetros de entrada: k (dias), n (tipos de pratos), m (orçamento)
            int k = scanner.nextInt();
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            
            // Condição de parada: quando k, n e m são todos zero, o programa termina
            if (k == 0 && n == 0 && m == 0) break;

            // Cria um array para armazenar os pratos
            Prato[] pratos = new Prato[n];
            for (int i = 0; i < n; i++) {
                int custo = scanner.nextInt();
                int lucro = scanner.nextInt();
                pratos[i] = new Prato(custo, lucro, i + 1); // Cria cada prato com custo, lucro e ID
            }

            // dp[dia][orcamento] = lucro máximo que podemos obter até o dia "dia" com "orcamento" restante
            double[][] dp = new double[k + 1][m + 1];
            // prev[dia][orcamento] = ID do prato escolhido no dia "dia" com "orcamento" restante
            int[][] prev = new int[k + 1][m + 1];
            // usado[dia][orcamento][id] = número de vezes que o prato "id" foi usado até o dia "dia" com "orcamento" restante
            int[][][] usado = new int[k + 1][m + 1][n + 1];

            // Inicializa o array prev com -1 (nenhum prato escolhido)
            for (int i = 0; i <= k; i++) {
                Arrays.fill(prev[i], -1);
            }

            // Loop para cada dia
            for (int dia = 1; dia <= k; dia++) {
                // Loop para cada orçamento possível
                for (int orc = 0; orc <= m; orc++) {
                    // Inicialmente consideramos o lucro do dia anterior para o mesmo orçamento
                    dp[dia][orc] = dp[dia - 1][orc];
                    prev[dia][orc] = -1;
                    for (int id = 1; id <= n; id++) {
                        usado[dia][orc][id] = usado[dia - 1][orc][id];
                    }

                    // Verifica todas as possibilidades para os pratos no dia atual e orçamento atual
                    for (int j = 0; j < n; j++) {
                        Prato prato = pratos[j];
                        if (orc >= prato.custo) {
                            // Verifica se o prato foi escolhido no dia anterior
                            boolean mesmoPratoEscolhido = (prev[dia - 1][orc - prato.custo] == prato.id);
                            
                            // Atualiza o número de vezes que o prato foi cozinhado
                            int vezesCozinhado = mesmoPratoEscolhido ? usado[dia - 1][orc - prato.custo][prato.id] : 0;

                            double lucroAtual = prato.lucro;
                            if (vezesCozinhado == 1) {
                                lucroAtual *= 0.5;
                            } else if (vezesCozinhado >= 2) {
                                lucroAtual = 0;
                            }

                            // Verifica se esse prato dá um lucro maior para o dia atual e orçamento atual
                            if (dp[dia][orc] < dp[dia - 1][orc - prato.custo] + lucroAtual) {
                                dp[dia][orc] = dp[dia - 1][orc - prato.custo] + lucroAtual;
                                prev[dia][orc] = prato.id;
                                // Atualiza o número de vezes que cada prato foi usado
                                for (int id = 1; id <= n; id++) {
                                    usado[dia][orc][id] = mesmoPratoEscolhido ? usado[dia - 1][orc - prato.custo][id] : 0;
                                }
                                // Incrementa o número de vezes que o prato atual foi usado
                                usado[dia][orc][prato.id]++;
                            }
                        }
                    }
                }
            }

            // Encontra o lucro máximo possível para todos os dias e verifica se é possível cozinhar para todos os dias
            double lucroTotal = 0;
            boolean possible = false;
            for (int orc = 0; orc <= m; orc++) {
                if (dp[k][orc] > lucroTotal) {
                    lucroTotal = dp[k][orc];
                }
                // Verifica se é possível cobrir todos os dias com os pratos escolhidos
                boolean todosDiasCobertos = true;
                for (int dia = 1; dia <= k; dia++) {
                    if (prev[dia][orc] == -1) {
                        todosDiasCobertos = false;
                        break;
                    }
                }
                if (dp[k][orc] > 0 && todosDiasCobertos) {
                    possible = true;
                }
            }

            // Se não for possível cozinhar pratos para todos os dias, define lucroTotal como 0
            if (!possible) {
                lucroTotal = 0;
            }

            // Imprimir o lucro total formatado com uma casa decimal
            System.out.printf("%.1f\n", lucroTotal);

            // Imprimir a sequência de pratos escolhidos apenas se houver lucro
            if (lucroTotal > 0) {
                List<Integer> dias = new ArrayList<>();
                int dia = k;
                int orc = 0;
                // Encontrar o orçamento final escolhido
                for (int o = 0; o <= m; o++) {
                    if (dp[k][o] == lucroTotal) {
                        orc = o;
                        break;
                    }
                }
                // Reconstruir a sequência de pratos escolhidos
                while (dia > 0) {
                    if (prev[dia][orc] != -1) {
                        dias.add(prev[dia][orc]);
                        orc -= pratos[prev[dia][orc] - 1].custo;
                    }
                    dia--;
                }
                Collections.reverse(dias);
                for (int id : dias) {
                    System.out.print(id + " ");
                }
                System.out.println();
            }
        }

        scanner.close(); // Fecha o scanner
    }

    // Classe Prato para armazenar informações de cada prato
    static class Prato {
        int custo;
        int lucro;
        int id;

        Prato(int custo, int lucro, int id) {
            this.custo = custo;
            this.lucro = lucro;
            this.id = id;
        }
    }
}
