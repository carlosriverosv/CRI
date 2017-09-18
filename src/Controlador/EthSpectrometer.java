/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

/**
 *
 * @author Carlos
 */
public class EthSpectrometer extends Spectrometer {
    
    private String ipAddress;
    
    public EthSpectrometer(int index, String name, String serialNumber, String firmware, int channels, String ipAddress) {
        super(index, name, serialNumber, firmware, channels);
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }
    
    
    
}
