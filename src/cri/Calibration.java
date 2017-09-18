/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cri;

import dialog.ErrorArchivo;
import dialog.ErrorLectura;
import dialog.ErrorOpen;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.JDialog;

/**
 *
 * @author BALUM ETB3
 */
public class Calibration {
    
    private ListData calK1=new ListData("K1");
    private double  calK2;
    
    private ListData patron;
    private Vector dataPatron;

    private ListData medido;
    private Vector dataMedido;
    
    
    
    
    
    Calibration(){
        
        try
            {
        Scanner salida = new Scanner(new File(System.getProperty("user.dir")+"/Calibracion/CargarCalibracion/actual"));
        
            try // lee registros del archivo, usando el objeto Scanner
                {
                          dataMedido=cargarVector(salida.next());
                          dataPatron=cargarVector(salida.next());
                
                
                } // fin de try
                catch ( NoSuchElementException elementException )
                {
                    JDialog error=  new ErrorArchivo(Lux.getFrames()[0],true);
                   error.setLocationRelativeTo(Lux.getFrames()[0]);
                   error.setVisible(true);
               //     salida.close();
               //     System.exit( 1 );
                } // fin de catch
            catch ( IllegalStateException stateException )
            {
                   JDialog error=  new ErrorLectura(Lux.getFrames()[0],true);
                   error.setLocationRelativeTo(Lux.getFrames()[0]);
                   error.setVisible(true);
            } // f
        
          } // fin de try
        catch ( FileNotFoundException fileNotFoundException )
            {
                   JDialog error=  new ErrorOpen(Lux.getFrames()[0],true);
                   error.setLocationRelativeTo(Lux.getFrames()[0]);
                   error.setVisible(true);
            } 
        
        Calibrar(Double.parseDouble(dataPatron.elementAt(4).toString()));


    
    }
    
    Calibration(String patron, String medido){
    
        System.out.print("p1"+patron);
        System.out.print("e1"+medido);
        
        dataMedido=cargarVector(medido);
        dataPatron=cargarVector(patron);
        
        setMedido();
        setPatron();
        
        System.out.print(dataPatron);
        
        Calibrar(Double.parseDouble(dataPatron.elementAt(4).toString()));
    
    
    }
    
    public Vector cargarVector(String nombre){
        
        Vector vector=new Vector();
        
        try
            {
        Scanner salida = new Scanner(new File(System.getProperty("user.dir")+"/Calibracion/Tabla_"+nombre));
        
            try // lee registros del archivo, usando el objeto Scanner
                {
                
                                      int i=0;
                    while (salida.hasNext()){
                        salida.next();
                        vector.add(i, salida.next());
                        System.out.print("\nEnCalibracion"+vector);
                        System.out.print("\nEnCalibracion2"+vector);
                        
                    i++;
                }
                
                } // fin de try
                catch ( NoSuchElementException elementException )
                {
                    JDialog error=  new ErrorArchivo(Lux.getFrames()[0],true);
                   error.setLocationRelativeTo(Lux.getFrames()[0]);
                   error.setVisible(true);
               //     salida.close();
               //     System.exit( 1 );
                } // fin de catch
            catch ( IllegalStateException stateException )
            {
                   JDialog error=  new ErrorLectura(Lux.getFrames()[0],true);
                   error.setLocationRelativeTo(Lux.getFrames()[0]);
                   error.setVisible(true);
            } // f
        
          } // fin de try
        catch ( FileNotFoundException fileNotFoundException )
            {
                   JDialog error=  new ErrorOpen(Lux.getFrames()[0],true);
                   error.setLocationRelativeTo(Lux.getFrames()[0]);
                   error.setVisible(true);
            } 
        
        return vector;
        
    }
            
            
            
            
    
    public void setMedido(){
    
        this.medido=new ListData(new File(System.getProperty("user.dir")+"/Calibracion/"+dataMedido.elementAt(0).toString()));
    
    }
    
    public void setPatron(){
        
        this.patron=new ListData(new File(System.getProperty("user.dir")+"/Calibracion/"+dataPatron.elementAt(0).toString()));
    }
    
    
     
    public void Calibrar(double flujoReference ){
        
      
        ListData underTest=medido;
        ListData referenceTest=patron;
        double minor;
        double major;
        underTest.resetNode();
        referenceTest.resetNode();
         
        if(underTest.getFirstNode().getLengthWave()<referenceTest.getFirstNode().getLengthWave())
            minor=referenceTest.getFirstNode().getLengthWave();
        else
            minor=underTest.getFirstNode().getLengthWave();
         
        if(underTest.getFinalNode().getLengthWave()>referenceTest.getFinalNode().getLengthWave())
            major=referenceTest.getFinalNode().getLengthWave();
        else
            major=underTest.getFinalNode().getLengthWave();
        
        
        NodeData nodeUnderTest=underTest.getNodo();
        NodeData nodeReferenceTest=referenceTest.getNodo();
        
            while(nodeUnderTest.getLengthWave()<minor){
                nodeUnderTest=underTest.getNodo();                
            }
            while(nodeReferenceTest.getLengthWave()<minor){
                nodeReferenceTest=referenceTest.getNodo();
            }
        
        while(major!=referenceTest.getNodoActual().getLengthWave()){
            calK1.insertNode(Math.abs(nodeUnderTest.getAmplitude()-nodeReferenceTest.getAmplitude()), nodeUnderTest.getLengthWave());
            nodeUnderTest=underTest.getNodo();
            nodeReferenceTest=referenceTest.getNodo();
            
        }
        
        calK2=flujoReference/flujo(Multiplicacion(underTest,calK1));    

        
        
        
    }
        
        
        
   
        
        
        private double flujo(ListData spd){
         
        double minor;
        double major;
        double flux=0;
        NodeMetric.photopic.resetNode();
        spd.resetNode();
         
        if(NodeMetric.photopic.getFirstNode().getLengthWave()<spd.getFirstNode().getLengthWave())
            minor=spd.getFirstNode().getLengthWave();
        else
            minor=NodeMetric.photopic.getFirstNode().getLengthWave();
         
        if(NodeMetric.photopic.getFinalNode().getLengthWave()>spd.getFinalNode().getLengthWave())
            major=NodeMetric.photopic.getFinalNode().getLengthWave();
        else
            major=spd.getFinalNode().getLengthWave();

        NodeData metric=NodeMetric.photopic.getNodo();
        NodeData spdNode=spd.getNodo();
        
        while(metric.getLengthWave()<minor){
                metric=NodeMetric.photopic.getNodo();                
            }
        while(spdNode.getLengthWave()<minor){
                spdNode=spd.getNodo();
            }
        
        while(major!=spd.getNodoActual().getLengthWave() && major!=NodeMetric.photopic.getNodoActual().getLengthWave()){
            
            flux=flux+0.5*(metric.getAmplitude()*spdNode.getAmplitude()+
                    metric.nextNodo.getAmplitude()*spdNode.nextNodo.getAmplitude());
            
            metric=NodeMetric.photopic.getNodo(); 
            spdNode=spd.getNodo();
            
        }
                
        return flux;
     }
     
        
    
    
     private ListData Multiplicacion(ListData first, ListData second){
        
        ListData multiplication=new ListData("Multiplicacion");
    
    double minor;
    double major;
    first.resetNode();
    second.resetNode();
         
        if(first.getFirstNode().getLengthWave()<second.getFirstNode().getLengthWave())
            minor=second.getFirstNode().getLengthWave();
        else
            minor=first.getFirstNode().getLengthWave();
         
        if(first.getFinalNode().getLengthWave()>second.getFinalNode().getLengthWave())
            major=second.getFinalNode().getLengthWave();
        else
            major=first.getFinalNode().getLengthWave();

        NodeData nodeFirts=first.getNodo();
        NodeData nodeSecond=second.getNodo();
        
            while(nodeFirts.getLengthWave()<minor){
                nodeFirts=first.getNodo();                
            }
            while(nodeSecond.getLengthWave()<minor){
                nodeSecond=second.getNodo();
            }
            
        
        while(major!=second.getNodoActual().getLengthWave() && major!=first.getNodoActual().getLengthWave() ){
            
            multiplication.insertNode(nodeFirts.getAmplitude()*nodeSecond.getAmplitude(), nodeFirts.getLengthWave());
 
            nodeFirts=first.getNodo();
            nodeSecond=second.getNodo();
            
        }
        
        return multiplication;
    
    }
     
     
     public ListData getK1(){
     
     
         return calK1;
     
     }
     
     
     public double getK2(){
     
        return calK2; 
     
    }
     
     
     public Vector getDataMedido(){
     
     
         return dataMedido;
     
     
     }
     
     
     public Vector getDataPatron(){
     
     
         return dataPatron;
     
     
     }
     
     
}

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    