
import agentes.Imovel;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Matheus
 */
public class IniciadorDeAgentes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //String[] parametro;
        String parametroAgente = "";
        String nomeAgente;
        String tipoAgente;
        
        Imovel imovel = new Imovel();
        int opcao;
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Digite 1 para criar um agente ou 2 para executar:");
        opcao = sc.nextInt();
        
        while(opcao != 2){
            sc.nextLine();
            System.out.print("Digite o nome do agente: ");
            nomeAgente = sc.nextLine();
            System.out.print("Digite o tipo do agente: ");
            tipoAgente = sc.nextLine();
            
            System.out.print("Digite o tipo do imovel: ");
            imovel.setTipoImovel(sc.nextLine());
            System.out.print("Digite o tamanho do imovel: ");
            imovel.setTamanho(Double.parseDouble(sc.nextLine()));
            System.out.print("Digite a localização do imovel: ");
            imovel.setLocalizacao(sc.nextLine());
            System.out.print("Digite o valor do imovel: ");
            imovel.setValor(Double.parseDouble(sc.nextLine()));
            
            if(tipoAgente.equals("cliente"))
                parametroAgente += nomeAgente + ":agentes.AgenteCliente(" + imovel.getTipoImovel() + ", " + imovel.getTamanho() +
                        ", " + imovel.getLocalizacao() + ", " + imovel.getValor() + ");";
            if(tipoAgente.equals("proprietario"))
                parametroAgente += nomeAgente + ":agentes.AgenteProprietario(" + imovel.getTipoImovel() + ", " + imovel.getTamanho() +
                        ", " + imovel.getLocalizacao() + ", " + imovel.getValor() + ");";
            
            
            System.out.println("Digite 1 para criar um agente ou 2 para executar:");
            opcao = sc.nextInt();
        }
        System.out.println(parametroAgente);
        String[] parametro = {"-gui", parametroAgente};
        
        jade.Boot.main(parametro);
    }
    
}
