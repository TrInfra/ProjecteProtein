
package com.mycompany.testedesimilaridade;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TesteDeSimilaridade {

    public static void main(String[] args) {

        // Tentativa de ler o arquivo e armazenar as sequências em uma lista
        try {
            BufferedReader reader = new BufferedReader(new FileReader(".fasta"));// colocar o local do arquivo aqui
                                                                                 // dentro
            String line;
            ArrayList<String> sequencias = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith(">")) {
                    sequencias.add(line);
                }
            }

            reader.close();

            // Inicialização da matriz para armazenar os resultados
            double[][] melhoresResultados = new double[sequencias.size()][2];

            // Comparação de cada sequência com todas as outras
            for (int i = 0; i < sequencias.size(); i++) {
                for (int j = 0; j < sequencias.size(); j++) {
                    if (i != j) {
                        double similaridade = compareStrings(sequencias.get(i), sequencias.get(j));
                        if (Double.isNaN(similaridade)) {
                            System.out.println("Uma das sequências está vazia");
                        } else {
                            System.out.println("Semelhança entre a sequência " + i + " e a sequencia " + j + ": "
                                    + similaridade * 100 + "%");
                            if (similaridade > melhoresResultados[i][1]) {
                                melhoresResultados[i][0] = j;
                                melhoresResultados[i][1] = similaridade;
                            }
                        }
                    }
                }
            }

            // Impressão dos resultados
            for (int i = 0; i < melhoresResultados.length; i++) {
                System.out.println("Sequencia " + i + " teve melhores resultados com a sequencia "
                        + melhoresResultados[i][0] + " com uma semelhança de " + melhoresResultados[i][1] * 100 + "%");
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(TesteDeSimilaridade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TesteDeSimilaridade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para calcular a semelhança entre duas strings
    public static double compareStrings(String str1, String str2) {
        ArrayList pairs1 = wordLetterPairs(str1.toUpperCase());
        ArrayList pairs2 = wordLetterPairs(str2.toUpperCase());

        if (pairs1.isEmpty() || pairs2.isEmpty()) {
            return Double.NaN;
        }
        int intersection = 0;
        int union = pairs1.size() + pairs2.size();

        for (int i = 0; i < pairs1.size(); i++) {
            Object pair1 = pairs1.get(i);
            for (int j = 0; j < pairs2.size(); j++) {
                Object pair2 = pairs2.get(j);
                if (pair1.equals(pair2)) {
                    intersection++;
                    pairs2.remove(j);
                    break;
                }
            }
        }

        return (2.0 * intersection) / union;
    }

    // Método para converter uma string em pares de letras
    private static ArrayList wordLetterPairs(String str) {
        ArrayList allPairs = new ArrayList();

        String[] pairsInWord = letterPairs(str);

        for (int p = 0; p < pairsInWord.length; p++) {
            allPairs.add(pairsInWord[p]);
        }

        return allPairs;
    }

    // Método para converter uma string em pares de letras
    private static String[] letterPairs(String str) {
        int numPairs = str.length() - 1;
        String[] pairs = new String[numPairs];

        for (int i = 0; i < numPairs; i++) {
            pairs[i] = str.substring(i, i + 2);
        }

        return pairs;
    }
}
