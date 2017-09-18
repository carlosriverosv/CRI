/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cri;


import java.io.File;


/**
 *
 * @author andres
 */
public class Lamp {
    
    
    public ListData spd;
    public double XYZ[]=new double[3];
    public double xyz[]=new double[3];
    public double uvY[]=new double[3];
    protected double Lab[]=new double[3];
    public float rgb[]=new float[3];
    
    protected double CCT;
    public double max;
    
    public Lamp(){
        this.spd=new ListData("Lampara"); 
    }

    
    Lamp(String ubicationLamp){
        
  
            spd=new ListData(new File(ubicationLamp));
            max=spd.max;
       
          Spd2XYZ();
          XYZ2xyz();
          xyz2uvY();
          XYZ2rgb();
          Cct();
          
                  
    }   
    
      public Lamp(ListData listd){
        
        this.spd=listd;  
                max=1;
        Spd2XYZ();
        XYZ2xyz();
        xyz2uvY();
        XYZ2rgb(); 
    }

      
       public void Cct(){
   
        Lamp BlackBody=new Lamp(500);
        double min=100000;
        for (int T=500;T<24000;T++){
            if(min>=Math.sqrt(Math.pow(BlackBody.uvY[0]-this.uvY[0],2)+
                    Math.pow(BlackBody.uvY[1]-this.uvY[1],2))){
                min=Math.sqrt(Math.pow(BlackBody.uvY[0]-this.uvY[0],2)+
                        Math.pow(BlackBody.uvY[1]-this.uvY[1],2));
                this.CCT=T;
                
        }
            BlackBody=new Lamp(T);
           
        }
        System.out.print("hol");
       }
        
       
    Lamp(double Cct){
        
        spd=new ListData("Blackbody");
        
        double h=6.6260693e-34;
        double k=1.3806505e-23;
        double c=299792458;
        double c1=(2*Math.PI*h*Math.pow(c,2));
        double c2=h*c/k;

        for (int i=380;i<=780;i++){
           // System.out.println((c1/(Math.pow((i*Math.pow(10,-9)),5)))/(Math.exp(c2/(Cct*(i*Math.pow(10,-9))))-1));
           setSpd(i,(c1/(Math.pow((i*Math.pow(10,-9)),5)))/(Math.exp(c2/(Cct*(i*Math.pow(10,-9))))-1));
        }
        
        Spd2XYZ();
        XYZ2xyz();
        xyz2uvY();
        XYZ2rgb();
    }

    public void setSpd(double lamb ,double ampl){
        
        
        //System.out.println(ampl);
       spd.insertNode(ampl, lamb);
       if(ampl>max)
           max=ampl;
       
       
    }
   
    public void Spd2XYZ(){
        
        
        ListTries.resetNode();
        spd.resetNode();
        
        double minor;
        double major;
         
        if(ListTries.getFirstNode().getLengthWave()<spd.getFirstNode().getLengthWave())
            minor=spd.getFirstNode().getLengthWave();
        else
            minor=ListTries.getFirstNode().getLengthWave();
         
        if(ListTries.getFinalNode().getLengthWave()>spd.getFinalNode().getLengthWave())
            major=spd.getFinalNode().getLengthWave();
        else
            major=ListTries.getFinalNode().getLengthWave();

        NodeTries tries=ListTries.getNodo();
        NodeData spdNode=spd.getNodo();
                while(tries.getLengthWave()<minor){
                tries=ListTries.getNodo();                
            }
            while(spdNode.getLengthWave()<minor){
                spdNode=spd.getNodo();
            }
        
        while(major!=spd.getNodoActual().getLengthWave()){
            
    
            
            XYZ[0]=0.5*(spdNode.getAmplitude()/max*tries.amplX+
                    spdNode.nextNodo.getAmplitude()/max*tries.nextNodo.amplX)+
                    XYZ[0];
            XYZ[1]=0.5*(spdNode.getAmplitude()/max*tries.amplY+
                    spdNode.nextNodo.getAmplitude()/max*tries.nextNodo.amplY)+XYZ[1];
            XYZ[2]=0.5*(spdNode.getAmplitude()/max*tries.amplZ+
                    spdNode.nextNodo.getAmplitude()/max*tries.nextNodo.amplZ)+XYZ[2];
            
            tries=ListTries.getNodo();
            spdNode=spd.getNodo();

            
        }
        
    }
    
    public void XYZ2xyz(){
    
        xyz[0]=XYZ[0]/(XYZ[0]+XYZ[1]+XYZ[2]);
        xyz[1]=XYZ[1]/(XYZ[0]+XYZ[1]+XYZ[2]);
        xyz[2]=XYZ[2]/(XYZ[0]+XYZ[1]+XYZ[2]);
    
    }
    

    public void xyz2uvY(){
        
        uvY[0]=(4*XYZ[0])/(XYZ[0]+(15*XYZ[1])+(3*XYZ[2]));
        uvY[1]=(6*XYZ[1])/(XYZ[0]+(15*XYZ[1])+(3*XYZ[2]));
        uvY[2]=100/XYZ[1];
     
    }
    
     
    
    
    public void XYZ2rgb(){

    rgb[0] = (float)(XYZ[0]* 3.2406/100+XYZ[1]*-1.5372/100+ XYZ[2]*-0.4986/100);
    rgb[1] = (float)(XYZ[0]*-0.9689/100+XYZ[1]* 1.8758/100+ XYZ[2]* 0.0415/100);
    rgb[2] = (float)(XYZ[0]* 0.0557/100+XYZ[1]*-0.2040/100+ XYZ[2]* 1.0570/100);
    float min;

    if(rgb[1]<rgb[0]){
        if(rgb[1]<rgb[2]){
            if(rgb[1]<0)
                min=rgb[1];
            else
                min=0;
        }
        else {
            if(rgb[2]<0)
                min=rgb[2];
            else
                min=0;
        }
    }
    else {
        if(rgb[0]<rgb[2]){
            if(rgb[0]<0)
                min=rgb[0];
            else
                min=0;
        }
        else {
            if(rgb[2]<0)
                min=rgb[2];
            else
                min=0;
        }
    }

    rgb[0]=rgb[0]+(-1)*min;
    rgb[1]=rgb[1]+(-1)*min;
    rgb[2]=rgb[2]+(-1)*min;

    if (rgb[0]>0.0031306684425005883) rgb[0] = (float)(1.055*(Math.pow(rgb[0],1/2.4))-0.055);
    else             rgb[0] = (float)12.92*rgb[0];
    if (rgb[1]>0.0031306684425005883) rgb[1] = (float)(1.055*(Math.pow(rgb[1],1/2.4))-0.055);
    else             rgb[1] = (float)12.9*rgb[1];
    if (rgb[2]>0.0031306684425005883) rgb[2] = (float)(1.055*(Math.pow(rgb[2],1/2.4))-0.055);
    else             rgb[2] = (float)12.92*rgb[2];

    if(rgb[0]<0)rgb[0]=0;
    if(rgb[0]>1)rgb[0]=1;
    if(rgb[1]<0)rgb[1]=0;
    if(rgb[2]<0)rgb[2]=0;
    if(rgb[1]>1)rgb[1]=1;
    if(rgb[2]>1)rgb[2]=1;

    }



 
    
    
    
    
    public void imprimir(){
    
    
        System.out.print("\nX="+XYZ[0]);
        System.out.print("\nY="+XYZ[1]);
        System.out.print("\nZ="+XYZ[2]);
        System.out.print("\nu="+uvY[0]);
        System.out.print("\nv="+uvY[1]);
        System.out.print("\nY="+uvY[2]);
        System.out.print("\nR="+rgb[0]);
        System.out.print("\nG="+rgb[1]);
        System.out.print("\nB="+rgb[2]);
        System.out.print("\nx="+xyz[0]);
        System.out.print("\ny="+xyz[1]);
        System.out.print("\nz="+xyz[2]);
        System.out.print("\nL="+Lab[0]);
        System.out.print("\na="+Lab[1]);
        System.out.print("\nb="+Lab[2]);
        System.out.println("\nCCT="+CCT);
    
    
    
    }
    
    public void printSpd(){
    
    
        spd.resetNode();
        NodeData node=spd.getNodo();
        
        while(node!=null){
        
           System.out.println("Ampl="+node.getAmplitude()/max+"\tlmp="+node.getLengthWave()+"\n");
            node=spd.getNodo();
        
        }
        
        
    System.out.println("Final="+max+"\tlmp="+spd.getFinalNode().getLengthWave()+"\n");
    
    
    
    
    }
    
    
    
    
    public void setLab(double XYZ[]){
    
       Lab[0]=116.0*Math.pow(this.XYZ[1]/XYZ[1],1.0/3.0)-16.0;
       Lab[1]=500.0*(Math.pow(this.XYZ[0]/XYZ[0],1.0/3.0)-Math.pow(this.XYZ[1]/XYZ[1],1.0/3.0));
       Lab[2]=200.0*(Math.pow(this.XYZ[1]/XYZ[1],1.0/3.0)-Math.pow(this.XYZ[2]/XYZ[2],1.0/3.0));
    
    
    
    
    
    }
    
    public void setLabP(double Lab[]){
    
       this.Lab[0]=Lab[0];
       this.Lab[1]=Lab[1];
       this.Lab[2]=Lab[2];
        
    
    }
    
    
    
}
