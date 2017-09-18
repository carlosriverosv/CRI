/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cri;

/**
 *
 * @author andres
 */
public class Calculos {
    
    
    
    public static ListData suma(ListData spd1,ListData spd2){
    
        
        ListData multiplication=new ListData("Suma");
        
        spd1.resetNode();
        spd2.resetNode();
    
        double menor;
        double mayor;
         
        if(spd1.getFirstNode().getLengthWave()<spd2.getFirstNode().getLengthWave())
            menor=spd2.getFirstNode().getLengthWave();
        else
            menor=spd1.getFirstNode().getLengthWave();
         
        if(spd1.getFinalNode().getLengthWave()>spd2.getFinalNode().getLengthWave())
            mayor=spd2.getFinalNode().getLengthWave();
        else
            mayor=spd1.getFinalNode().getLengthWave();
        
        while(spd1.getNodo().getLengthWave()<menor){}
            
        while(spd2.getNodo().getLengthWave()<menor){}
            
            
        do{
            multiplication.insertNode(spd1.getNodoActual().getAmplitude()+
                    spd2.getNodoActual().getAmplitude(), spd2.getNodoActual().getLengthWave());

        }while(mayor!=spd1.getNodo().getLengthWave() && mayor!=spd2.getNodo().getLengthWave());
        

        return multiplication;
    
    
    }
    
    public static ListData resta(ListData spd1,ListData spd2){
    
        
        ListData multiplication=new ListData("Resta");
        
        spd1.resetNode();
        spd2.resetNode();
    
        double menor;
        double mayor;
         
        if(spd1.getFirstNode().getLengthWave()<spd2.getFirstNode().getLengthWave())
            menor=spd2.getFirstNode().getLengthWave();
        else
            menor=spd1.getFirstNode().getLengthWave();
         
        if(spd1.getFinalNode().getLengthWave()>spd2.getFinalNode().getLengthWave())
            mayor=spd2.getFinalNode().getLengthWave();
        else
            mayor=spd1.getFinalNode().getLengthWave();
        
        while(spd1.getNodo().getLengthWave()<menor){}
            
        while(spd2.getNodo().getLengthWave()<menor){}
            
            
        do{
            multiplication.insertNode(spd1.getNodoActual().getAmplitude()-
                    spd2.getNodoActual().getAmplitude(), spd2.getNodoActual().getLengthWave());

        }while(mayor!=spd1.getNodo().getLengthWave() && mayor!=spd2.getNodo().getLengthWave());
        

        return multiplication;
    
    
    }
    
    
        
    public static ListData Multiplicacion(ListData spd1,ListData spd2){
        

        ListData multiplication=new ListData("Multiplicacion");
        
        spd1.resetNode();
        spd2.resetNode();
    
        double menor;
        double mayor;
         
        if(spd1.getFirstNode().getLengthWave()<spd2.getFirstNode().getLengthWave())
            menor=spd2.getFirstNode().getLengthWave();
        else
            menor=spd1.getFirstNode().getLengthWave();
         
        if(spd1.getFinalNode().getLengthWave()>spd2.getFinalNode().getLengthWave())
            mayor=spd2.getFinalNode().getLengthWave();
        else
            mayor=spd1.getFinalNode().getLengthWave();
        
        while(spd1.getNodo().getLengthWave()<menor){}
            
        while(spd2.getNodo().getLengthWave()<menor){}
            
            
        do{
            multiplication.insertNode(spd1.getNodoActual().getAmplitude()*
                    spd2.getNodoActual().getAmplitude(), spd2.getNodoActual().getLengthWave());

        }while(mayor!=spd1.getNodo().getLengthWave() && mayor!=spd2.getNodo().getLengthWave());
        

        return multiplication;
    
    }
    
    
    public static double integral(ListData spd1,ListData spd2){
        
        
       double resultado=0.0;
        
        spd1.resetNode();
        spd2.resetNode();
    
        double menor;
        double mayor;
         
        if(spd1.getFirstNode().getLengthWave()<spd2.getFirstNode().getLengthWave())
            menor=spd2.getFirstNode().getLengthWave();
        else
            menor=spd1.getFirstNode().getLengthWave();
         
        if(spd1.getFinalNode().getLengthWave()>spd2.getFinalNode().getLengthWave())
            mayor=spd2.getFinalNode().getLengthWave();
        else
            mayor=spd1.getFinalNode().getLengthWave();
        
        while(spd1.getNodo().getLengthWave()<menor){}
            
        while(spd2.getNodo().getLengthWave()<menor){}
        
        
        do{
            resultado=0.5*(spd1.getNodoActual().getAmplitude()*spd2.getNodoActual().getAmplitude()+
                    spd1.getNextNodeA().getAmplitude()*spd2.getNextNodeA().getAmplitude());

        }while(mayor!=spd1.getNodo().getLengthWave() && mayor!=spd2.getNodo().getLengthWave());
        
        return resultado;
        
    }
    
    
    
///////////////////////////////////Calculos Colorimetricos///////////////////////////////////////////////////////////////////
    
    
     public static double[] XYZ2xyz(double XYZ[]){
    
        double []xyz=new double[3];
         
        xyz[0]=XYZ[0]/(XYZ[0]+XYZ[1]+XYZ[2]);
        xyz[1]=XYZ[1]/(XYZ[0]+XYZ[1]+XYZ[2]);
        xyz[2]=XYZ[2]/(XYZ[0]+XYZ[1]+XYZ[2]);
        
        return xyz;
    
    }
     
     public static double[] XYZ2xyY(double XYZ[]){
    
        double []xyY=new double[3];
         
        xyY[0]=XYZ[0]/(XYZ[0]+XYZ[1]+XYZ[2]);
        xyY[1]=XYZ[1]/(XYZ[0]+XYZ[1]+XYZ[2]);
        xyY[2]=XYZ[1];
        
        return xyY;
    
    }
     
     public static double[] xyY2XYZ(double xyY[]){
    
        double []XYZ=new double[3];
         
            XYZ[0]= xyY[0]*(xyY[2]/xyY[1]);
            XYZ[1]= xyY[2];
            XYZ[2]= (1-xyY[0]-xyY[1])*(xyY[2]/xyY[1]);
        
        return XYZ;
    
    }
     
    public static double[] XYZ2Lab(double XYZ[], double ref_XYZ[]){
    
        
        double var_X = XYZ[0] / ref_XYZ[0];          //ref_X =  95.047   Observer= 2°, Illuminant= D65
        double var_Y = XYZ[1] / ref_XYZ[1];          //ref_Y = 100.000
        double var_Z = XYZ[2] / ref_XYZ[2];          //ref_Z = 108.883

        if ( var_X > 0.008856 ) var_X = Math.pow(var_X , ( 1.0/3.0 ));
        else                    var_X = ( 7.787 * var_X ) + ( 16.0 / 116.0 );
        if ( var_Y > 0.008856 ) var_Y = Math.pow(var_Y , ( 1.0/3.0 ));
        else                    var_Y = ( 7.787 * var_Y ) + ( 16.0 / 116.0 );
        if ( var_Z > 0.008856 ) var_Z = Math.pow(var_Z , ( 1.0/3.0 ));
        else                    var_Z = ( 7.787 * var_Z ) + ( 16.0 / 116.0 );

       
        double Lab[]=new double[3];
        Lab[0]=( 116.0 * var_Y ) - 16.0;
        Lab[1]=500.0 * ( var_X - var_Y );
        Lab[2]=200.0 * ( var_Y - var_Z );
        
        return Lab;   
          
    } 
     
     
    public static double[] XYZ2Luv(double XYZ[], double ref_XYZ[]){
        
        
        
        double var_U = (4.0 * XYZ[0] ) / ( XYZ[0] + ( 15.0 * XYZ[1] ) + ( 3.0 * XYZ[2] ) );
        double var_V = (9.0 * XYZ[1] ) / ( XYZ[0] + ( 15.0 * XYZ[1] ) + ( 3.0 * XYZ[2] ) );

        double var_Y = XYZ[1] / 100.0;
        if ( var_Y > 0.008856 ) var_Y = Math.pow(var_Y, ( 1.0/3.0 ));
        else var_Y = ( 7.787 * var_Y ) + ( 16.0 / 116.0 );

        double ref_X = ref_XYZ[0];        //Observer= 2°, Illuminant= D65
        double ref_Y = ref_XYZ[1];
        double ref_Z = ref_XYZ[2];

        double ref_U = ( 4.0 * ref_X ) / ( ref_X + ( 15.0 * ref_Y ) + ( 3.0 * ref_Z ) );
        double ref_V = ( 9.0 * ref_Y ) / ( ref_X + ( 15.0 * ref_Y ) + ( 3.0 * ref_Z ) );

        double CIE_L = ( 116.0 * var_Y ) - 16.0;
        double CIE_u = 13.0 * CIE_L * ( var_U - ref_U );
        double CIE_v = 13.0 * CIE_L * ( var_V - ref_V );
        

        double Luv[]=new double[3];
        Luv[0]=( 116.0 * var_Y ) - 16.0;
        Luv[1]=13.0 * CIE_L * ( var_U - ref_U );
        Luv[2]=13.0 * CIE_L * ( var_V - ref_V );
        
        return Luv;     
    }
    
    public static double[] uvY2XYZ(double XYZ[]){
        
        double []uvY=new double[3];
        
        uvY[0]=(4*XYZ[0])/(XYZ[0]+(15*XYZ[1])+(3*XYZ[2]));
        uvY[1]=(6*XYZ[1])/(XYZ[0]+(15*XYZ[1])+(3*XYZ[2]));
        uvY[2]=100/XYZ[1];
        
        return uvY;
     
    }
    
    public static double[] luv2XYZ(double luv[], double ref_XYZ[]){
    

        double  var_Y = ( luv[0]* + 16 ) / 116;
        if ( Math.pow(var_Y,3) > 0.008856 ) var_Y = Math.pow(var_Y,3);
        else                     var_Y = ( var_Y - 16 / 116 ) / 7.787;

        double ref_X =  ref_XYZ[0];   //Observer= 2°, Illuminant= D65
        double ref_Y =  ref_XYZ[1];
        double ref_Z =  ref_XYZ[2];

        double ref_U = ( 4 * ref_X ) / ( ref_X + ( 15 * ref_Y ) + ( 3 * ref_Z ) );
        double ref_V = ( 9 * ref_Y ) / ( ref_X + ( 15 * ref_Y ) + ( 3 * ref_Z ) );

        double var_U = luv[1] / ( 13 * luv[0] ) + ref_U;
        double var_V = luv[2] / ( 13 * luv[0] ) + ref_V;
        
        double XYZ[]= new double[3];

        XYZ[1] = var_Y * 100;
        XYZ[0] =  - ( 9 * XYZ[1] * var_U ) / ( ( var_U - 4 ) * var_V  - var_U * var_V );
        XYZ[2] = ( 9 * XYZ[1] - ( 15 * var_V * XYZ[1] ) - ( var_V * XYZ[0] ) ) / ( 3 * var_V );
    
    
    return XYZ;
        
        
    }
    
    
    public static double[] lab2XYZ(double lab[], double ref_XYZ[]){
    
        double var_Y = ( lab[0] + 16 ) / 116;
        double var_X = lab[1] / 500 + var_Y;
        double var_Z = var_Y - lab[2] / 200;

        if ( Math.pow(var_Y, 3) > 0.008856 ) var_Y = Math.pow(var_Y, 3);
        else                      var_Y = ( var_Y - 16 / 116 ) / 7.787;
        if ( Math.pow(var_X,3) > 0.008856 ) var_X = Math.pow(var_X, 3);
        else                      var_X = ( var_X - 16 / 116 ) / 7.787;
        if ( Math.pow(var_Y, 3) > 0.008856 ) var_Z = Math.pow(var_Z, 3);
        else                      var_Z = ( var_Z - 16 / 116 ) / 7.787;

      
        
        
        double XYZ[]=new double[3];
        
        XYZ[0] = ref_XYZ[0] * var_X;     //ref_X =  95.047     Observer= 2°, Illuminant= D65
        XYZ[1] = ref_XYZ[1] * var_Y;     //ref_Y = 100.000
        XYZ[2] = ref_XYZ[2] * var_Z;     //ref_Z = 108.883
           
        return XYZ;
    }
     
    
    
    public static float[] XYZ2rgb(double XYZ[]){

        float []rgb=new float[3];
        
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
        
        
        
        return rgb;

    }
    
    
     public static double[] rgbXYZ(double RGB[]){
         
         
        double []XYZ=new double [3];
    
        double var_R = ( RGB[0] / 255 );        //R from 0 to 255
        double var_G = ( RGB[1] / 255 );        //G from 0 to 255
        double var_B = ( RGB[2] / 255 );        //B from 0 to 255

        if ( var_R > 0.04045 ) var_R = Math.pow((var_R+0.055)/1.055,2.4);
        else                   var_R = var_R/12.92;
        if ( var_G > 0.04045 ) var_G =Math.pow(( var_G + 0.055 )/1.055,2.4);
        else                   var_G = var_G / 12.92;
        if ( var_B > 0.04045 ) var_B = Math.pow( ( var_B + 0.055 ) / 1.055 ,2.4);
        else                   var_B = var_B / 12.92;

        var_R = var_R * 100;
        var_G = var_G * 100;
        var_B = var_B * 100;

        //Observer. = 2°, Illuminant = D65
        XYZ[0] = var_R * 0.4124 + var_G * 0.3576 + var_B * 0.1805;
        XYZ[1] = var_R * 0.2126 + var_G * 0.7152 + var_B * 0.0722;
        XYZ[2] = var_R * 0.0193 + var_G * 0.1192 + var_B * 0.9505;

        return XYZ;
        
     }
     
    
    
}
