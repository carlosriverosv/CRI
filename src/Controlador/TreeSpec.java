/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author Carlos
 */
public class TreeSpec extends JTree implements TreeSelectionListener, Observable {

    public static String USB_SPEC = "Espectrómetros USB";
    public static String ETH_SPEC = "Espectrómetros Ethernet";
    public static String ROOT = "Recursos";
    public static String FILES = "Archivos de lámparas";
    public String path = System.getProperty("user.dir");
    private static ArrayList<Observer> observers = new ArrayList<>();
    private DefaultMutableTreeNode selectedNode;
    private Spectrometer selectedSpec = null;
    private static DefaultTreeModel treeModel;
    private static DefaultMutableTreeNode rootFiles, rootNode, USBSpec, EthSpec;
    private static int i;

    public TreeSpec() {
        rootNode = new DefaultMutableTreeNode(ROOT);
        USBSpec = new DefaultMutableTreeNode(USB_SPEC);
        EthSpec = new DefaultMutableTreeNode(ETH_SPEC);
        treeModel = new DefaultTreeModel(rootNode);
        setModel(treeModel);
        //loadSpectrometers();
        Resources.getResources();
        rootFiles = new DefaultMutableTreeNode(FILES);
        treeModel.insertNodeInto(USBSpec, rootNode, 0);
        treeModel.insertNodeInto(EthSpec, rootNode, 1);
        treeModel.insertNodeInto(rootFiles, rootNode, 2);
        loadFiles(rootFiles, path + "/Lamparas/");
        scrollPathToVisible(new TreePath(USBSpec.getPath()));
        scrollPathToVisible(new TreePath(EthSpec.getPath()));
        scrollPathToVisible(new TreePath(rootFiles.getPath()));
        this.setEditable(false);
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.setShowsRootHandles(true);
        this.addTreeSelectionListener(this);
        this.addMouseListener(ma);
    }

    public static void loadSpectrometers() {
        DefaultMutableTreeNode hijo = null;
        ArrayList<Spectrometer> spectrometers = Resources.getArraySpec();
        for (; i < spectrometers.size(); i++) {
            Spectrometer spectrometer = spectrometers.get(i);
            boolean allowChildren = spectrometer.getChannels() > 1;
            DefaultMutableTreeNode padre = spectrometer instanceof USBSpectrometer ? USBSpec : EthSpec;
            hijo = new DefaultMutableTreeNode(spectrometer);
            hijo.setAllowsChildren(allowChildren);
            treeModel.insertNodeInto(hijo, padre, i);
            if(allowChildren) {
                int j = 1;
                while(j <= spectrometer.getChannels()) { 
                    spectrometer = spectrometers.get(i);
                    DefaultMutableTreeNode nieto = new DefaultMutableTreeNode(spectrometer);
                    nieto.setAllowsChildren(false);
                    treeModel.insertNodeInto(nieto, hijo, j - 1);
                    j++;   //incrementa el número de canal a agregar
                    i++;   //incrementa el número de espectrómetro
                }
                i--; //se decrementa porque el último incremento no sirvió para agregar más canales
            } else {
                i++; // se incrementa porque cuando hay solo un canal el espectrómetro está 
                //repetido dos veces 
            }  
        }
    }

    private void loadFiles(DefaultMutableTreeNode padre, String ruta) {
        DefaultMutableTreeNode aux = null;
        File file = new File(ruta);
        File[] archivos = file.listFiles();
        if (archivos != null) {
            for (int i = 0; i < archivos.length; i++) {
                aux = new DefaultMutableTreeNode(archivos[i].getName());
                treeModel.insertNodeInto(aux, padre, i);
                if (archivos[i].isDirectory()) {
                    try {
                        loadFiles(aux, archivos[i].getAbsolutePath() + "/");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    aux.setAllowsChildren(false);
                }

            }

        }
    }

    public void addFile(String fileName) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(fileName);
        treeModel.insertNodeInto(childNode, rootFiles, rootFiles.getChildCount());
        childNode.setAllowsChildren(false);
        scrollPathToVisible(new TreePath(childNode.getPath()));
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {

        selectedNode = (DefaultMutableTreeNode) this.getLastSelectedPathComponent();
        if (!selectedNode.getAllowsChildren()) {
            if (!selectedNode.getParent().toString().equals(TreeSpec.FILES)) {
                selectedSpec = (Spectrometer) selectedNode.getUserObject();
            }
            notifyObservers();
        }
    }

    public DefaultMutableTreeNode getSelectedNode() {
        return selectedNode;
    }

    public Spectrometer getSelectedSpec() {
        return selectedSpec;
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public void register(Observer obs) {
        observers.add(obs);
    }

    @Override
    public void unRegister(Observer obs) {
        observers.remove(obs);
    }

    MouseAdapter ma = new MouseAdapter() {
        private void myPopupEvent(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            JTree tree = (JTree) e.getSource();
            TreePath path = tree.getPathForLocation(x, y);
            if (path == null) {
                return;
            }
            tree.setSelectionPath(path);
            DefaultMutableTreeNode obj = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (obj.toString().equals(TreeSpec.ETH_SPEC)) {
                String label = "Agregar espectrómetro Ethernet";
                JMenuItem mni = new JMenuItem(label);
                JPopupMenu popup = new JPopupMenu();
                popup.add(mni);
                popup.show(tree, x, y);
                mni.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String ip = JOptionPane.showInputDialog(null,"Introduzca la dirección IP del espectrómetro");
                        if(ip != null) {
                            if(!ip.isEmpty()) {
                            Resources.getResources().openEthSpectrometer(ip);
                            }
                        } 
                    }
                });
            }
        }

        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) {
                myPopupEvent(e);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                myPopupEvent(e);
            }
        }
    };

}
