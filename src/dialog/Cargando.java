/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dialog;

import Controlador.Conexion;

/**
 *
 * @author andres
 */
public class Cargando extends javax.swing.JDialog {

    Conexion conexion;
    public int estado=0;

    /**
     * Creates new form Cargando
     */
    public Cargando(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        


        
    }
    
    public void conexionBD(){
    
        conexion=new Conexion(this);
        conexion.crearBD();
        estado=1;
    
     
    }
    
    public void setTextLabel(String text){
        
        
        jLabel2.setText(text);
    
    
    
    
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(0, 0));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(0, 0));
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 32767));
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 100), new java.awt.Dimension(0, 20), new java.awt.Dimension(0, 1000));
        jLabel1 = new javax.swing.JLabel();
        filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 30), new java.awt.Dimension(0, 10));
        jLabel2 = new javax.swing.JLabel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 32767));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(java.awt.Color.white);
        setMaximumSize(new java.awt.Dimension(620, 500));
        setMinimumSize(new java.awt.Dimension(620, 100));
        setPreferredSize(new java.awt.Dimension(500, 400));

        jPanel1.setBackground(java.awt.Color.white);
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel1.setRequestFocusEnabled(false);
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jProgressBar1, java.awt.BorderLayout.CENTER);
        jPanel1.add(filler1, java.awt.BorderLayout.LINE_END);
        jPanel1.add(filler2, java.awt.BorderLayout.LINE_START);
        jPanel1.add(filler3, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel3.setBackground(java.awt.Color.white);
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Mapa.png"))); // NOI18N
        jLabel3.setMaximumSize(new java.awt.Dimension(150, 150));
        jLabel3.setMinimumSize(new java.awt.Dimension(150, 150));
        jLabel3.setPreferredSize(new java.awt.Dimension(150, 150));
        jPanel3.add(jLabel3, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(java.awt.Color.white);
        jPanel2.setMaximumSize(new java.awt.Dimension(300, 300));
        jPanel2.setMinimumSize(new java.awt.Dimension(300, 300));
        jPanel2.setName(""); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 300));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));
        jPanel2.add(filler5);

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Configurando Alux");
        jLabel1.setMaximumSize(new java.awt.Dimension(300, 18));
        jLabel1.setMinimumSize(new java.awt.Dimension(300, 18));
        jLabel1.setPreferredSize(new java.awt.Dimension(300, 18));
        jPanel2.add(jLabel1);
        jPanel2.add(filler6);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(" ");
        jLabel2.setMaximumSize(new java.awt.Dimension(300, 18));
        jLabel2.setMinimumSize(new java.awt.Dimension(300, 18));
        jLabel2.setPreferredSize(new java.awt.Dimension(300, 18));
        jPanel2.add(jLabel2);
        jPanel2.add(filler4);

        jPanel3.add(jPanel2, java.awt.BorderLayout.LINE_END);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Cargando.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cargando.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cargando.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cargando.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Cargando dialog = new Cargando(new javax.swing.JFrame(), true);
    
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}
