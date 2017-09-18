/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cri;

import dialog.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.JDialog;

/**
 *
 * @author BALUM ETB3
 */

public class  ListMunsell {
    
    
    private String nameData;
    public ListData fMunsell[]=new ListData[15];
    


    
    
    ListMunsell(String ubicationLamp){
        
        double ampl[]= new double[15];
        double ampl2[]= new double[15];
        
        for(int i=0;i<15;i++)
            fMunsell[i]=new ListData("Musell");
        
        
    
         try
            {
                Scanner salida = new Scanner(new File(ubicationLamp));
                
                try // lee registros del archivo, usando el objeto Scanner
                {
                    double lamb1=0;
                    double lambR;
                    double lamb2;

                    if(salida.hasNext()){// read the firsts values
                        lamb1=salida.nextDouble();
                        for(int i=0;i<15;i++){
                            ampl[i]=salida.nextDouble();
                        }
                    }
                    

                    if(Math.ceil(lamb1)==Math.floor(lamb1)){
                        for(int i=0;i<15;i++){
                            fMunsell[i].insertNode(ampl[i], lamb1);
                        }
                        lambR=lamb1+1;       //aumenta la sigueinte requerida                 
                    }
                    else
                        lambR=Math.ceil(lamb1);// Si no es entero calcula el techo del valor actual
                   
                    lamb2=salida.nextDouble();// the neext Lamb
                      for(int i=0;i<15;i++){
                            ampl2[i]=salida.nextDouble();
                        }
                   
                    
                   
                    while ( salida.hasNext() )
                {

                    
                    if(lambR>lamb2){
                        lamb1=lamb2;
                        lamb2=salida.nextDouble();
                        for(int i=0;i<15;i++){
                            ampl[i]=ampl2[i];
                            ampl2[i]=salida.nextDouble();
                        }

                    }
                    if(lamb2==lambR){// if the next value is equal to value requerided then save
                       for(int i=0;i<15;i++){
                            fMunsell[i].insertNode(ampl2[i], lambR);

                        }
                        lambR=lambR+1;
                    }                      
                     else if(lamb2>lambR){// if the next value is major to value requerided tehn calculate and save
                         for(int i=0;i<15;i++){
                                fMunsell[i].insertNode((ampl2[i]-ampl[i])/(lamb2-lamb1)*(lambR-lamb2)+ampl2[i], lambR);
                               //  if(i==0)
                              //  System.out.println("Ampl[1]="+ampl[i]+"\tAmpl2="+ampl2[i]+"\tlmp="+lambR+"\n");
                         }
                            lambR=lambR+1;
                        }
   

                          
                } // fin de while
                
                } // fin de try
                catch ( NoSuchElementException elementException )
                {
                   JDialog error=  new ErrorArchivo(Lux.getFrames()[0],true);
                   error.setLocationRelativeTo(Lux.getFrames()[0]);
                   error.setVisible(true);
                } // fin de catch
            catch ( IllegalStateException stateException )
            {
                JDialog error=  new ErrorLectura(Lux.getFrames()[0],true);
                error.setLocationRelativeTo(Lux.getFrames()[0]);
                error.setVisible(true);
            } // fin de catch
        } // fin de try
        catch ( FileNotFoundException fileNotFoundException )
            {

            JDialog error=  new ErrorOpen(Lux.getFrames()[0],true);
            error.setLocationRelativeTo(Lux.getFrames()[0]);
            error.setVisible(true);
            
          //  System.exit( 1 );
            } // fin de catch
    
          
             
    }
    
    public void printMunsell(int n){
        fMunsell[n].resetNode();
        NodeData node=fMunsell[n].getNodo();
        while(node!=null){
            System.out.println("Ampl="+node.getAmplitude()+"\tlmp="+node.getLengthWave()+"\n");
            node=fMunsell[n].getNodo();
        
        
        
        }
            
        
    
    
    
    }
    
}
