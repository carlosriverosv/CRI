/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cri;

/**
 *
 * @author BALUM ETB3
 */
public class NodeMunsell {
    
    public double Munsell1;
    public double Munsell2;
    public double Munsell3;
    public double Munsell4;
    public double Munsell5;
    public double Munsell6;
    public double Munsell7;
    public double Munsell8;
    public double Munsell9;
    public double Munsell10;
    public double Munsell11;
    public double Munsell12;
    public double Munsell13;
    public double Munsell14;
    public double Munsell15;
    
    private double lengthWave;
    NodeMunsell nextNodo;
    
    
    NodeMunsell(double Munsell1,double Munsell2,double Munsell3,double Munsell4,
        double Munsell5,
        double Munsell6,
        double Munsell7,
        double Munsell8,
        double Munsell9,
        double Munsell10,
        double Munsell11,
        double Munsell12,
        double Munsell13,
        double Munsell14,double lengthWave){
    
        this(Munsell1,Munsell2,Munsell3,Munsell4,Munsell5,Munsell6,Munsell7,Munsell8,
        Munsell9,Munsell10,Munsell11,Munsell12,Munsell13,Munsell14,lengthWave,null);
            
    }
    
    NodeMunsell(double Munsell1,double Munsell2,double Munsell3,double Munsell4,
        double Munsell5,
        double Munsell6,
        double Munsell7,
        double Munsell8,
        double Munsell9,
        double Munsell10,
        double Munsell11,
        double Munsell12,
        double Munsell13,
        double Munsell14,double lengthWave, NodeMunsell nextNodo){
    
    
        this.Munsell1=Munsell1;
        this.Munsell2=Munsell2;
        this.Munsell3=Munsell3;
        this.Munsell4=Munsell4;
        this.Munsell5=Munsell5;
        this.Munsell6=Munsell6;
        this.Munsell7=Munsell7;
        this.Munsell8=Munsell8;
        this.Munsell9=Munsell9;
        this.Munsell10=Munsell10;
        this.Munsell11=Munsell11;
        this.Munsell12=Munsell12;
        this.Munsell13=Munsell13;
        this.Munsell14=Munsell14;
        this.lengthWave=lengthWave;
        this.nextNodo=nextNodo;
    
    }
    
    public double getAmplitude(){
    
    
        return Munsell1;
    
    }
    
     public double getLengthWave(){
    
    
        return lengthWave;
    
    }
     
     
}


    

