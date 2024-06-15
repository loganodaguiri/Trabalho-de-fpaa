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
            
            // Ordena os pratos por lucro/custo em ordem decrescente para priorizar pratos mais lucrativos
            Arrays.sort(pratos, (a, b) -> Double.compare(b.lucro / (double)b.custo, a.lucro / (double)a.custo));
            
            double lucroTotal = 0; // Inicializa o lucro total
            int orcamentoRestante = m; // Inicializa o orçamento restante
            List<Integer> dias = new ArrayList<>(); // Lista para armazenar a sequência de pratos escolhidos
            Map<Integer, Integer> contadorPratos = new HashMap<>(); // Map para contar quantas vezes cada prato foi escolhido
            int ultimoPrato = -1; // Variável para controlar o prato escolhido no dia anterior
            
            // Loop para cada dia
            for (int dia = 0; dia < k; dia++) {
                boolean pratoEscolhido = false; // Flag para verificar se algum prato foi escolhido no dia
                
                // Loop para cada prato ordenado por lucro/custo
                for (Prato prato : pratos) {
                    int vezesCozinhado = contadorPratos.getOrDefault(prato.id, 0); // Quantas vezes o prato foi cozinhado
                    double lucroAtual = prato.lucro;
                    
                    // Reduz o lucro atual se o prato foi cozinhado antes
                    if (vezesCozinhado == 1) {
                        lucroAtual *= 0.5; // Metade do lucro se foi cozinhado uma vez antes
                    } else if (vezesCozinhado >= 2) {
                        lucroAtual = 0; // Sem lucro se foi cozinhado duas ou mais vezes
                    }
                    
                    // Verifica se o prato pode ser escolhido com o orçamento restante e se não é o mesmo do dia anterior
                    if (orcamentoRestante >= prato.custo && prato.id != ultimoPrato) {
                        orcamentoRestante -= prato.custo; // Atualiza o orçamento restante
                        lucroTotal += lucroAtual; // Atualiza o lucro total
                        dias.add(prato.id); // Adiciona o ID do prato à lista de dias
                        if(prato.id == ultimoPrato){
                            contadorPratos.put(prato.id, vezesCozinhado + 1); // Atualiza o contador de vezes que o prato foi cozinhado
                        }
                        ultimoPrato = prato.id; // Atualiza o último prato escolhido
                        pratoEscolhido = true; // Marca que um prato foi escolhido
                        break; // Sai do loop dos pratos
                    }
                }
                // Se nenhum prato foi escolhido, reinicia o lucro total para zero e sai do loop dos dias
                if (!pratoEscolhido) {
                    lucroTotal = 0;
                    break;
                }
            }
            
            // Imprime o lucro total formatado com uma casa decimal
            System.out.printf("%.1f\n", lucroTotal);
            if (lucroTotal > 0) {
                // Imprime a sequência de pratos escolhidos, se houver lucro
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
