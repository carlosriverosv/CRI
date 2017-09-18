/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controlador;

import dialog.Cargando;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author andres
 */
public class Conexion {
    
    
    private Connection conn = null;
    Cargando cargar;
    
    public Conexion(){
    
    }
    
    public Conexion(Cargando cargar){
    
        this.cargar=cargar;
    
    }
    
     
    public  Connection crearBD(){
        
        boolean bandera = true;
        
       try{
         //obtenemos el driver de para mysql
         Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
         //obtenemos la conexión
         conn = DriverManager.getConnection("jdbc:derby:.\\DB\\Iluminacion.DB;create=true");
         if (conn!=null){
             actualizarMensaje("Bases de Datos ok");
          //  JOptionPane.showMessageDialog(null,"OK base de datos listo");
            String creartabla="CREATE TABLE referenciaL (referenciaID INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),observador INT NOT NULL, refNombre varchar (50) NOT NULL, refX DECIMAL(10,4) NOT NULL, refY DECIMAL(10,4) NOT NULL, refZ DECIMAL(10,4) NOT NULL, PRIMARY KEY (referenciaID) )";          
            
            String desc="disconnect";
            try {
            PreparedStatement pstm = conn.prepareStatement(creartabla);
            pstm.execute();
            pstm.close();
            
         //  JOptionPane.showMessageDialog(null,"BD Creada correctamente");
        } catch (SQLException ex) {
            System.out.print("Tabla ya existe");
            bandera=false;
         //   JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
         }
      }catch(SQLException e){
       //JOptionPane.showMessageDialog(null,e.getMessage(),"Error1" ,  JOptionPane.ERROR_MESSAGE);
      }catch(ClassNotFoundException e){
        // JOptionPane.showMessageDialog(null,e.getMessage(),"Error2" ,  JOptionPane.ERROR_MESSAGE);
      }
       if(bandera)
        valoresIniciales();
       return conn;
       
       
       
       
  }
    
    public void valoresIniciales(){
    
        
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "2,' A (Incandescente)',109.850,100,35.585",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "2,'C',98.074,100,118.232",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "2,'D50',96.422,100,82.521",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "2,'D55',95.682,100,92.149",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "2,'D65 (Daylight)',95.047,100,108.883",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "2,'D75',94.972,100,122.638",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "2,'F2 (Fluorescent)',99.187,100,67.395",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "2,'F7',95.044,100,108.755",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "2,'F11',100.966,100,64.370",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "10,'A (Incandescente)',111.144,100,35.200",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "10,'C',97.285,100,116.145",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "10,'D50',96.720,100,81.427",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "10,'D55',95.799,100,90.926",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "10,'D65 (Daylight)',94.811,100,107.304",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "10,'D75',94.416,100,120.641",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "10,'F2 (Fluorescente)',103.280,100,69.026",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "10,'F7',95.792,100,107.687",conn);
        Operaciones.insert("referenciaL", "observador,refNombre,refX,refY,refZ" , "10,'F11',103.866,100,65.627",conn);
        
            
    }
    
     public Connection accederBD(){
       try{
         //obtenemos el driver de para mysql
         Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
         //obtenemos la conexión
         conn = DriverManager.getConnection("jdbc:derby:.\\DB\\Iluminacion.DB");
         if (conn!=null){
          //  JOptionPane.showMessageDialog(null,"OK base de datos listo");
         }
      }catch(SQLException e){
     //  JOptionPane.showMessageDialog(null,e.getMessage(),"Error" ,  JOptionPane.ERROR_MESSAGE);
      }catch(ClassNotFoundException e){
       //  JOptionPane.showMessageDialog(null,e.getMessage(),"Error" ,  JOptionPane.ERROR_MESSAGE);
      }
       return conn;
  }
     
      public void cerracon (){
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
      
      
      public void actualizarMensaje(String Text){
      
       //  cargar.setTextLabel(Text);
      
      }
}
