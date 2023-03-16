package com.mycompany.seniorproject;

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonathan Espinal
 */
public class PeerToPeer {

    private ServerSocket hostSocket;
    private Socket clientSocket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;

    /**
     * Starts a connection for a client to connect to.
     *
     * @param port The port to listen on for incoming connections.
     */
    public void startHost(int port) {
        try {
            hostSocket = new ServerSocket(port);
            clientSocket = hostSocket.accept(); // process will block here waiting for client request
            inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputStream = new PrintWriter(clientSocket.getOutputStream(),true);
        } catch (IOException ex) {
            Logger.getLogger(PeerToPeer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Connect to designated IP address and port as client.
     *
     * @param ip The host IP to connect to.
     * @param port The port to connect to on the host.
     */
    public void connectToHost(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputStream = new PrintWriter(clientSocket.getOutputStream(),true);
        } catch (IOException ex) {
            Logger.getLogger(PeerToPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Read incoming data.
     *
     * @return
     */
    public String readPacket() {
        String data;
        String packet = "";
        try {
            // process will block here waiting for input
            while (!packet.contains("END MESSAGE")&& (data = inputStream.readLine()) != null) {
                packet += data;
            }
            return packet;
        } catch (IOException ex) {
            Logger.getLogger(PeerToPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return packet;
    }

    /**
     * Send a message across the connection
     *
     * @param message The message to send.
     */
    public void sendPacket(String message) {
        outputStream.println(message);
    }

    /**
     * Close all open streams and client/host connections.
     */
    public void closeConnection() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (hostSocket != null) {
                hostSocket.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(PeerToPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public String toString(){
        String output = "";
        if (hostSocket != null){
            output += "Host socket info: " + hostSocket.toString() + "\n";
        }
        if (clientSocket != null){
            output += "Client Socket info: " + clientSocket.toString();
        }
        return output.compareTo("") == 0 ? null : output;
    }
}
