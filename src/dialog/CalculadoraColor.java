/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dialog;


import Controlador.Conexion;
import Controlador.Operaciones;
import cri.Calculos;
import static cri.Calculos.*;
import java.awt.Color;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author andres
 */
public class CalculadoraColor extends javax.swing.JDialog {
    
    
    double []XYZ=new double[3];
    double []ref_XYZ =new double[3]  ;
    static final String CONTROLADOR = "com.mysql.jdbc.Driver";
    static final String URL_BASEDATOS = "jdbc:mysql://localhost/Iluminacion";
    Conexion cn;
    
    public CalculadoraColor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        Vector< String > lista = new Vector< String >();
        cn=new Conexion();
        Object respuesta[][]=Operaciones.select("referenciaL", "observador", null, cn.accederBD(),true);
        
        for(int i=0;i<respuesta.length;i++)
            if((String)respuesta[i][0]!=null)
                lista.add((String)respuesta[i][0]);
        
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(lista));
        
        consultarIluminante(2);
        
       

        
        
    }
    
   public void consultarIluminante(int observador){
   
        Vector< String > lista = new Vector< String >();
        
        Object respuesta[][]=Operaciones.select("referenciaL", "refNombre", "observador = 2", cn.accederBD(), true);
        
        for(int i=0;i<respuesta.length;i++)
            if((String)respuesta[i][0]!=null)
                lista.add((String)respuesta[i][0]);
        
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(lista));

       
       
       
       
       
   }
           
           
           
           

    public void calcularCoordenadas(){
        
        
        
    
    
    
    
    
    
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox1 = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jTextField21 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Coordenada origen", "XYZ", "Yxy", "CIE-L*ab", "CIE-L*uv","RGB 0 - 255", "RGB 0 - 1", "RGB 0 - FF" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jPanel2.setLayout(new java.awt.GridLayout(3, 4, 20, 10));

        jPanel1.setBackground(null);
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jLabel3.setText("XYZ");
        jPanel1.add(jLabel3);

        jTextField4.setText("X:");
        jPanel1.add(jTextField4);

        jTextField5.setText("Y:");
        jPanel1.add(jTextField5);

        jTextField6.setText("Z:");
        jPanel1.add(jTextField6);

        jPanel2.add(jPanel1);

        jPanel3.setBackground(null);
        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jLabel4.setText("Yxy");
        jPanel3.add(jLabel4);

        jTextField7.setText("Y:");
        jPanel3.add(jTextField7);

        jTextField8.setText("x:");
        jPanel3.add(jTextField8);

        jTextField9.setText("y:");
        jPanel3.add(jTextField9);

        jPanel2.add(jPanel3);

        jPanel4.setBackground(null);
        jPanel4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jLabel5.setText("CIE-L*ab");
        jPanel4.add(jLabel5);

        jTextField10.setText("L*:");
        jPanel4.add(jTextField10);

        jTextField11.setText("a*:");
        jPanel4.add(jTextField11);

        jTextField12.setText("b*:");
        jPanel4.add(jTextField12);

        jPanel2.add(jPanel4);

        jPanel5.setBackground(null);
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));

        jLabel2.setText("    CIE-L*uv");
        jPanel5.add(jLabel2);

        jTextField13.setText("L*:");
        jTextField13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField13ActionPerformed(evt);
            }
        });
        jPanel5.add(jTextField13);

        jTextField14.setText("u*:");
        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });
        jPanel5.add(jTextField14);

        jTextField15.setText("v*:");
        jPanel5.add(jTextField15);

        jPanel2.add(jPanel5);

        jPanel6.setBackground(null);
        jPanel6.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("RGB 0 - 255");
        jPanel6.add(jLabel1);

        jTextField16.setText("R:");
        jPanel6.add(jTextField16);

        jTextField17.setText("G:");
        jPanel6.add(jTextField17);

        jTextField18.setText("B:");
        jPanel6.add(jTextField18);

        jPanel2.add(jPanel6);

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.Y_AXIS));

        jLabel6.setText("RGB 0 - 1");
        jPanel9.add(jLabel6);

        jTextField19.setText("R:");
        jPanel9.add(jTextField19);

        jTextField20.setText("G:");
        jPanel9.add(jTextField20);

        jTextField21.setText("B:");
        jPanel9.add(jTextField21);

        jPanel2.add(jPanel9);

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.Y_AXIS));

        jLabel7.setText("RGB 00 - FF");
        jPanel7.add(jLabel7);

        jTextField22.setText("R:");
        jPanel7.add(jTextField22);

        jTextField23.setText("G:");
        jTextField23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField23ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField23);

        jTextField24.setText("B:");
        jPanel7.add(jTextField24);

        jPanel2.add(jPanel7);

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.X_AXIS));

        jTextField1.setForeground(java.awt.Color.lightGray);
        jTextField1.setText("Coordenada #1");
        jTextField1.setToolTipText("a");
        jTextField1.setDisabledTextColor(java.awt.Color.red);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jPanel8.add(jTextField1);

        jTextField2.setForeground(java.awt.Color.lightGray);
        jTextField2.setText("Coordenada #2");
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });
        jPanel8.add(jTextField2);

        jTextField3.setForeground(java.awt.Color.lightGray);
        jTextField3.setText("Coordenada #3");
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });
        jPanel8.add(jTextField3);

        jButton2.setText("Calcular");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setText("cerrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel10.setLayout(new java.awt.GridLayout(2, 2));

        jLabel8.setText("            Observador:");
        jPanel10.add(jLabel8);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel10.add(jComboBox2);

        jLabel9.setText("            Iluminante:");
        jPanel10.add(jLabel9);

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel10.add(jComboBox3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(74, 74, 74))))
            .addGroup(layout.createSequentialGroup()
                .addGap(155, 155, 155)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void leerVectorReferencia(String observador, String nombre ){
   
        Vector< String > lista = new Vector< String >();
        Object respuesta[][]=Operaciones.select("referenciaL", "refX, refY, refZ", "observador = "+observador +" AND refNombre = "+"'"+nombre+"'", cn.accederBD(), true);
        
        
             ref_XYZ[0]=Double.parseDouble((String)respuesta[0][0]);
             ref_XYZ[1]=Double.parseDouble((String)respuesta[0][1]);
             ref_XYZ[2]=Double.parseDouble((String)respuesta[0][2]);
             
        
    
    }
    
    
    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        leerVectorReferencia((String) jComboBox2.getSelectedItem(),(String)jComboBox3.getSelectedItem() );
        
        if(jComboBox1.getSelectedIndex()==1){
            
            XYZ[0]=Double.parseDouble(jTextField1.getText());
            XYZ[1]=Double.parseDouble(jTextField2.getText());        
            XYZ[2]=Double.parseDouble(jTextField3.getText());
            TransformacionTotal();   
        }
        else if(jComboBox1.getSelectedIndex()==2){
                
            
            double []entrada=new double[3];
        
            entrada[0]=Double.parseDouble(jTextField1.getText());
            entrada[1]=Double.parseDouble(jTextField2.getText());
            entrada[2]=Double.parseDouble(jTextField3.getText());


            XYZ=Calculos.xyY2XYZ(XYZ);
            TransformacionTotal();

        }
        else if(jComboBox1.getSelectedIndex()==3){
            
            
            double []entrada=new double[3];
        
            entrada[0]=Double.parseDouble(jTextField1.getText());
            entrada[1]=Double.parseDouble(jTextField2.getText());
            entrada[2]=Double.parseDouble(jTextField3.getText());

            XYZ=Calculos.luv2XYZ(entrada,ref_XYZ);
            TransformacionTotal();        
    
                    }
        else if(jComboBox1.getSelectedIndex()==4){
            
            
            double []entrada=new double[3];
        
            entrada[0]=Double.parseDouble(jTextField1.getText());
            entrada[1]=Double.parseDouble(jTextField2.getText());
            entrada[2]=Double.parseDouble(jTextField3.getText());

            XYZ=Calculos.lab2XYZ(entrada,ref_XYZ);
            TransformacionTotal();        
    
                    }
        
        else if(jComboBox1.getSelectedIndex()==5){
            
            double []entrada=new double[3];
        
            entrada[0]=Double.parseDouble(jTextField1.getText());
            entrada[1]=Double.parseDouble(jTextField2.getText());
            entrada[2]=Double.parseDouble(jTextField3.getText());

            XYZ=Calculos.rgbXYZ(entrada);
            TransformacionTotal();        
    
        }
        else if(jComboBox1.getSelectedIndex()==6){
            
            double []entrada=new double[3];
        
            entrada[0]=Double.parseDouble(jTextField1.getText())*255;
            entrada[1]=Double.parseDouble(jTextField2.getText())*255;
            entrada[2]=Double.parseDouble(jTextField3.getText())*255;

            XYZ=Calculos.rgbXYZ(entrada);
            TransformacionTotal();
        }
        else if(jComboBox1.getSelectedIndex()==7){
            
            double []entrada=new double[3];
        
            entrada[0]=Integer.parseInt(jTextField1.getText(), 16);
            entrada[1]=Integer.parseInt(jTextField2.getText(), 16);
            entrada[2]=Integer.parseInt(jTextField3.getText(), 16);
            XYZ=Calculos.rgbXYZ(entrada);
            TransformacionTotal();
        }
              
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    
    public void rgb2XYZ(){
        
        double []entrada=new double[3];
        
        entrada[0]=Double.parseDouble(jTextField1.getText());
        entrada[1]=Double.parseDouble(jTextField2.getText());
        entrada[2]=Double.parseDouble(jTextField3.getText());
        
        XYZ=Calculos.rgbXYZ(entrada);
        TransformacionTotal();        
    
    }
    
    public void uvY2XYZ(){
        
        double []entrada=new double[3];
        
        entrada[0]=Double.parseDouble(jTextField1.getText());
        entrada[1]=Double.parseDouble(jTextField2.getText());
        entrada[2]=Double.parseDouble(jTextField3.getText());
    
        XYZ=Calculos.uvY2XYZ(XYZ);
        TransformacionTotal();
    
    }
    
    public void xyz2XYZ(){
        
        double []entrada=new double[3];
        
        entrada[0]=Double.parseDouble(jTextField1.getText());
        entrada[1]=Double.parseDouble(jTextField2.getText());
        entrada[2]=Double.parseDouble(jTextField3.getText());
    
    
        XYZ=Calculos.xyY2XYZ(XYZ);
        TransformacionTotal();
    
    }
    
    
    private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField13ActionPerformed

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        // TODO add your handling code here:
        jTextField2.setText("");
        jTextField2.setForeground(Color.BLACK);
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        // TODO add your handling code here:
        jTextField1.setText("");
        jTextField1.setForeground(Color.BLACK);
        

    }//GEN-LAST:event_jTextField1FocusGained

    private void jTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusGained
        // TODO add your handling code here:
       jTextField3.setText("");
        jTextField3.setForeground(Color.BLACK);
    }//GEN-LAST:event_jTextField3FocusGained

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        // TODO add your handling code here:
        
        if(jTextField1.getText().isEmpty()){
            jTextField1.setForeground(Color.lightGray);
            jTextField1.setText("Coordenada #1");
            
        }
    }//GEN-LAST:event_jTextField1FocusLost

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
        // TODO add your handling code here:
         if(jTextField2.getText().isEmpty()){
            jTextField2.setForeground(Color.lightGray);
            jTextField2.setText("Coordenada #2");
            
        }
    }//GEN-LAST:event_jTextField2FocusLost

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        // TODO add your handling code here:
         if(jTextField3.getText().isEmpty()){
            jTextField3.setForeground(Color.lightGray);
            jTextField3.setText("Coordenada #3");
         }     
    }//GEN-LAST:event_jTextField3FocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField23ActionPerformed

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CalculadoraColor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CalculadoraColor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CalculadoraColor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CalculadoraColor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CalculadoraColor dialog = new CalculadoraColor(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    
    public void TransformacionTotal(){
              
        float rgb[]=XYZ2rgb(XYZ);
        double Lab[]=XYZ2Lab(XYZ,ref_XYZ);
        double xyY[]=XYZ2xyY(XYZ);
        double xyz[]=XYZ2xyz(XYZ);
        double Luv[]=XYZ2Luv(XYZ,ref_XYZ);
        DecimalFormat decimales = new DecimalFormat("0.0000");  
        jTextField4.setText("X: "+String.valueOf(XYZ[0]));
        jTextField5.setText("Y: "+String.valueOf(XYZ[1]));
        jTextField6.setText("Z: "+String.valueOf(XYZ[2]));
        jTextField7.setText("x: "+decimales.format(xyY[0]));
        jTextField8.setText("y: "+decimales.format(xyY[1]));
        jTextField9.setText("Y: "+decimales.format(xyY[2]));
        jTextField16.setText("R: "+String.valueOf((int)(rgb[0]*255)));
        jTextField17.setText("G: "+String.valueOf((int)(rgb[1]*255)));
        jTextField18.setText("B: "+String.valueOf((int)(rgb[2]*255)));
        jTextField19.setText("R: "+decimales.format(rgb[0]));
        jTextField20.setText("G: "+decimales.format(rgb[1]));
        jTextField21.setText("B: "+decimales.format(rgb[2]));
        jTextField22.setText("R: "+Integer.toHexString((int)(rgb[0]*255)));
        jTextField23.setText("G: "+Integer.toHexString((int)(rgb[1]*255)));
        jTextField24.setText("B: "+Integer.toHexString((int)(rgb[2]*255)));
        jTextField10.setText("L*: "+decimales.format(Lab[0]));
        jTextField11.setText("a*: "+decimales.format(Lab[1]));
        jTextField12.setText("b*: "+decimales.format(Lab[2]));
        jTextField13.setText("L*: "+decimales.format(Luv[0]));
        jTextField14.setText("u*: "+decimales.format(Luv[1]));
        jTextField15.setText("v*: "+decimales.format(Luv[2]));
            
    }

    
   
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
