package com.mycompany.java1;
import java.util.Random;
import java.util.Scanner;

public class BatalhaJaval {

    private char[][] mapaJogador1;
    private char[][] mapaJogador2;
    private boolean jogadorVsComputador;
    private boolean alocacaoManual;
    private boolean ocultarNavios;
    private Scanner ler;
    
    public BatalhaJaval(){
        mapaJogador1 = new char[10][10];
        mapaJogador2 = new char[10][10];
        jogadorVsComputador = true;
        alocacaoManual = true;
        ocultarNavios = false;
        ler = new Scanner(System.in);
    }

    public void jogar(){
        int opcaoAlocacao = 0;
        int opcaoJogo = 0;
        
        System.out.print("Bem-vindo ao Batalha Javal!\n\n");
        System.out.print("1 - Jogador VS Computador\n");
        System.out.print("2 - Jogador VS Jogador\n\n");
        
        do{
            System.out.print("Escolha uma opção: ");
            opcaoJogo = ler.nextInt();
            System.out.print("\n");
            if(opcaoJogo != 1 && opcaoJogo != 2){
                System.out.print("Opcao inválida. Digite novamente.\n\n");
            }
        }while(opcaoJogo != 1 && opcaoJogo != 2);

        if(opcaoJogo == 2){
            jogadorVsComputador = false;
        }
        if(opcaoJogo == 1){
            jogadorVsComputador = true;
        }
        
        do{
            System.out.print("1 - Alocação manual de navios\n");
            System.out.print("2 - Alocação automática de navios\n\n");
            System.out.print("Escolha uma opção para a alocação dos navios: ");
            opcaoAlocacao = ler.nextInt();
            System.out.print("\n");
            if(opcaoAlocacao != 1 && opcaoAlocacao != 2){
                System.out.print("Opcao inválida. Digite novamente.\n");
            }
        }while(opcaoAlocacao != 1 && opcaoAlocacao != 2);

        if(opcaoAlocacao == 1){
            alocarNavios(mapaJogador1);
            if(jogadorVsComputador == true){
                ocultarNavios = true;
                alocarNaviosAleatorios(mapaJogador2);
            }else{
                ocultarNavios = true;
                alocarNavios(mapaJogador2);
            }
        }
        if(opcaoAlocacao == 2){
            if(jogadorVsComputador == true){
                ocultarNavios = false;
                alocarNaviosAleatorios(mapaJogador1);
                
                ocultarNavios = true;
                alocarNaviosAleatorios(mapaJogador2);
            }else{
                ocultarNavios = true;
                alocarNaviosAleatorios(mapaJogador1);
                
                ocultarNavios = true;
                alocarNaviosAleatorios(mapaJogador2);
            }
        }

    boolean fimJogo = false;
    while(!fimJogo){
        System.out.println("\nJogador 1, sua vez!");
        ataque(mapaJogador2);

        if(verificarFimDeJogo(mapaJogador2)){
            System.out.println("\n\nVitória do jogador 1!");
            break;
        }

        if(jogadorVsComputador == true){
            System.out.println("\nO computador está atacando...\n");
            ocultarNavios = false;
            ataqueComputador(mapaJogador1);

            if(verificarFimDeJogo(mapaJogador1)){
                System.out.println("\n\nVitória do computador!");
                fimJogo = true;
                break;
            }
        }else{
            System.out.println("Jogador 2, sua vez!");
            ataque(mapaJogador1);

            if(verificarFimDeJogo(mapaJogador1)){
                System.out.println("Vitória do jogador 2");
                break;
            }
        }
    }
}
    
    private void alocarNavios(char[][] mapa){
        int[] tamanhosNavios = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

        for (int i = 0; i<tamanhosNavios.length; i++) {
            int tamanho = tamanhosNavios[i];
            boolean alocado = false;

            while(alocado == false){
                boolean repetirPosicao = true;
                mostrarMapa(mapa);

                System.out.printf("Aloque um navio de tamanho %d\n\n", tamanho);

                do{
                    System.out.print("Informe a linha: ");
                    int linha = ler.nextInt();
                    System.out.print("Informe a coluna: ");
                    String colunaLetra = ler.next().toUpperCase();
                    int coluna = colunaLetra.charAt(0) - 'A';
                    System.out.print("Informe a orientação (H para horizontal, V para vertical): ");
                    String orientacao = ler.next().toUpperCase();
                    System.out.print("\n");

                    if(verificarPosicaoLivre(mapa, linha, coluna, tamanho, orientacao) == true){
                        alocarNavio(mapa, linha, coluna, tamanho, orientacao);
                        alocado = true;
                        repetirPosicao = false;
                    }else{
                        System.out.print("Posição inválida. Digite novamente.\n\n");
                        repetirPosicao = true;
                    }
                }while(repetirPosicao == true); 
            }
        }
    }

    private void alocarNaviosAleatorios(char[][] mapa){
        int[] tamanhosNavios = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

        for(int i = 0; i<tamanhosNavios.length; i++){
            int tamanho = tamanhosNavios[i];
            boolean alocado = false;

            while(!alocado){
                int linha = (int) (Math.random() * 10);
                int coluna = (int) (Math.random() * 10);
                String orientacao;

                if(Math.random() < 0.5){
                    orientacao = "H";
                }else{
                    orientacao = "V";
                }

                if(verificarPosicaoLivre(mapa, linha, coluna, tamanho, orientacao)){
                    alocarNavio(mapa, linha, coluna, tamanho, orientacao);
                    alocado = true;
                }
            }
        }
        mostrarMapa(mapa);
    }

    private boolean verificarPosicaoLivre(char[][] mapa, int linha, int coluna, int tamanho, String orientacao){
        boolean repetirPosicao = true;

        if(orientacao.equals("H") || orientacao.equals("h")){
                if(coluna+tamanho > 10){
                    return false;
                }
                for(int i=0; i<tamanho; i++){
                    if(mapa[linha][coluna+i] != 0){
                        return false;
                    }
                }
            }else if(orientacao.equals("V") || orientacao.equals("v")){
                if(linha+tamanho > 10){
                    return false;
                }
                for(int i=0; i<tamanho; i++){
                    if(mapa[linha+i][coluna] != 0){
                        return false;
                    }
                }
            }else{
                System.out.print("Posição inválida. Digite novamente.");
                return false;
            }
        return true;
    }

    private void alocarNavio(char[][] mapa, int linha, int coluna, int tamanho, String orientacao){
        if(orientacao.equals("H")){
            for(int i = 0; i<tamanho; i++){
                mapa[linha][coluna+i] = 'N';
            }
        }else{
            for (int i = 0; i<tamanho; i++){
                mapa[linha+i][coluna] = 'N';
            }
        }
    }

    private void ataque(char[][] mapa){
        int linha, coluna;
        boolean repetir = true, repetirCoordenadas = true;

        do{
            do{
                System.out.print("\nInforme a linha para atacar: ");
                linha = ler.nextInt();
                System.out.print("Informe a coluna para atacar: ");
                String colunaLetra = ler.next().toUpperCase();
                coluna = colunaLetra.charAt(0) - 'A';

                if(linha<0 || linha>9 || coluna<0 || coluna>9){
                    System.out.print("\nCoordenadas inválidas. Digite novamente.\n");
                    repetirCoordenadas = true;
                }
                else {
                    repetirCoordenadas = false;
                }
            }while(repetirCoordenadas == true);

            if(mapa[linha][coluna] == ' '){
                System.out.println("\nVocê acertou um navio!\n");
                mapa[linha][coluna] = 'X';
                repetir = true;
            }else{
                System.out.println("\nVocê errou!\n");
                mapa[linha][coluna] = 'O';
                repetir = false;
            }
            mostrarMapa(mapa);
        }while(repetir == true);
    }

    private void ataqueComputador(char[][] mapa){
        Random random = new Random();
        int linha, coluna;
        boolean posicaoValida = false, repetir = true;

        do{
            do{
                linha = random.nextInt(mapa.length);
                coluna = random.nextInt(mapa[0].length);

                if(mapa[linha][coluna] != 'O' && mapa[linha][coluna] != 'X'){
                    posicaoValida = true;

                    if(mapa[linha][coluna] == 'N'){
                        System.out.println("O computador acertou um navio!\n");
                        mapa[linha][coluna] = 'X';
                        repetir = true;
                    }else{
                        System.out.println("O computador errou!\n");
                        mapa[linha][coluna] = 'O';
                        repetir = false;
                    }
                }
            }while(!posicaoValida);

            mostrarMapa(mapa);
        }while(repetir == true);
    }

    private boolean verificarFimDeJogo(char[][] mapa){
        int contadorNaviosAtingidos = 0;
        for(int linha = 0; linha<mapa.length; linha++){
            for(int coluna = 0; coluna<mapa[0].length; coluna++){
                if(mapa[linha][coluna] == 'X'){
                    contadorNaviosAtingidos++;
                    if(contadorNaviosAtingidos == 20){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private void mostrarMapa(char[][] mapa){
        System.out.println("  A B C D E F G H I J");
        for(int linha = 0; linha<mapa.length; linha++){
            System.out.print(linha);
            for(int coluna = 0; coluna < mapa[0].length; coluna++){
                if(mapa[linha][coluna] != 'N' && mapa[linha][coluna] != 'X' && mapa[linha][coluna] != 'O'){
                    System.out.print(" ");
                }

                if(ocultarNavios == true){
                    if(mapa[linha][coluna] == 'N'){
                        mapa[linha][coluna] = ' '; 
                    }
                }
                if(ocultarNavios == false){
                    if(mapa[linha][coluna] == 'N'){
                        mapa[linha][coluna] = 'N'; 
                    }
                }
                System.out.print(" " + mapa[linha][coluna]);
            }
                System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args){
        BatalhaJaval jogo = new BatalhaJaval();
        jogo.jogar();
    }
}