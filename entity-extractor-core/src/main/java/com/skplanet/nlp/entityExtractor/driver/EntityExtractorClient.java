package com.skplanet.nlp.entityExtractor.driver;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * <br>
 * <br>
 * Created by Donghun Shin
 * <br>
 * Contact: donghun.shin@sk.com, sindongboy@gmail.com
 * <br>
 * Date: 4/7/14
 * <br>
 */
public class EntityExtractorClient {
    private static Logger logger = Logger.getLogger(EntityExtractorClient.class.getName());

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        new EntityExtractorClient().startClient(port);
    }

    public void startClient(int port) {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        InetAddress host = null;
        BufferedReader stdIn = null;

        try {
            host = InetAddress.getLocalHost();
            logger.info("Host Name: " + host.getHostName());
            socket = new Socket("localhost", port);

            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            // read from socket and write back the response to server
            //
            System.out.print("SENTENCE: ");
            while ((fromUser = stdIn.readLine()) != null) {
                if (fromUser.trim().length() == 0) {
                    System.out.print("SENTENCE: ");
                    continue;
                }

                if (fromUser.equals("0")) {
                    out.println("0");
                    logger.info("Client now disconnected");
                    break;
                }

                if (fromUser.equals("-1")) {
                    out.println("-1");
                    logger.info("stop the server, then disconnect the client");
                    break;
                }

                out.println(fromUser + "\n");
                logger.info("client sending : " + fromUser);

                fromServer = in.readLine();

                // server may be down at this time
                if (fromServer == null) {
                    logger.info("Server is not responding, now disconnects the client");
                    break;
                }

                logger.info("Server Response: " + fromServer);
                System.out.println("Server Responded: " + fromServer);


                System.out.print("SENTENCE: ");

            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
