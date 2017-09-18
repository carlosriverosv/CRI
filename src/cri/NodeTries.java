/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cri;

/**
 *
 * @author Andrés Corredor
 */
public class NodeTries {
    
    public double amplX;
    public double amplY;
    public double amplZ;
    private double lengthWave;
    NodeTries nextNodo;
    
    
    NodeTries(double amplX, double amplY,double amplZ, double lengthWave){
    
        this(amplX, amplY, amplZ,lengthWave,null);
            
    }
    
    NodeTries(double amplX, double amplY,double amplZ,double lengthWave, NodeTries nextNodo){
    
    
        this.amplX=amplX;
        this.amplY=amplY;
        this.amplZ=amplZ;
        this.lengthWave=lengthWave;
        this.nextNodo=nextNodo;
    
    }
    
    public double getAmplitude(){
    
    
        return amplX;
    
    }
    
     public double getLengthWave(){
    
    
        return lengthWave;
    
    }
    
    
}
