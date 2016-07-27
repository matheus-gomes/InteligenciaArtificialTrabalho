/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agentes;

import jade.core.AID;

/**
 *
 * @author Matheus
 */
public class MelhoresProprietarios {
    private AID proprietario;
    private int gradeProprietario;
    private double valorPropriedade;

    /**
     * @return the proprietario
     */
    public AID getProprietario() {
        return proprietario;
    }

    /**
     * @param proprietario the proprietario to set
     */
    public void setProprietario(AID proprietario) {
        this.proprietario = proprietario;
    }

    /**
     * @return the gradeProprietario
     */
    public int getGradeProprietario() {
        return gradeProprietario;
    }

    /**
     * @param gradeProprietario the gradeProprietario to set
     */
    public void setGradeProprietario(int gradeProprietario) {
        this.gradeProprietario = gradeProprietario;
    }

    /**
     * @return the valorPropriedade
     */
    public double getValorPropriedade() {
        return valorPropriedade;
    }

    /**
     * @param valorPropriedade the valorPropriedade to set
     */
    public void setValorPropriedade(double valorPropriedade) {
        this.valorPropriedade = valorPropriedade;
    }
    
}
