/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author Admin
 */
public class ClienteInterfaceImpl extends JFrame{
    public ClienteInterfaceImpl(){
        
        super("Agente Cliente");
        
        Container c = getContentPane();
        c.setLayout(new GridLayout(6, 2));
        
        c.add(new JLabel("Tipo de imóvel"));
        c.add(new JTextField());
        c.add(new JLabel("Tamanho do imovel"));
        c.add(new JTextField());
        c.add(new JLabel("Quantidade de quartos"));
        c.add(new JTextField());
        c.add(new JLabel("Localização"));
        c.add(new JTextField());
        c.add(new JLabel("Preço"));
        c.add(new JTextField());
        c.add(new JButton("OK"));
        //JFrame frame = new JFrame();
        //JButton botao = new JButton();
        //getContentPane().add(botao);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setVisible(true);
    }
    
    public static void main(String []args){
        new ClienteInterfaceImpl();
    }
}
