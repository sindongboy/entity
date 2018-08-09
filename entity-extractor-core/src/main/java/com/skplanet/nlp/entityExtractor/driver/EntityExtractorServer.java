package com.skplanet.nlp.entityExtractor.driver;

import com.skplanet.nlp.entityExtractor.analyzer.EntityExtractor;
import com.skplanet.nlp.entityExtractor.config.EConfiguration;
import com.skplanet.nlp.entityExtractor.type.Entity;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

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
public class EntityExtractorServer {

    private static Logger logger = Logger.getLogger(EntityExtractorServer.class.getName());


    public static void main(String[] args) {
        String category = args[0];
        int port = Integer.parseInt(args[1]);
        EntityExtractorServer server = new EntityExtractorServer(category, port);
        server.startServer();
    }

    EConfiguration config = null;
    EntityExtractor entityExtractor = null;

    ServerSocket echoServer = null;
    Socket clientSocket = null;
    int numConnections = 0;
    int port;
    String category = null;

    public EntityExtractorServer() {
    }

    public EntityExtractorServer(String category, int port) {
        this.category = category;
        this.port = port;

        this.config = new EConfiguration();
        this.entityExtractor = new EntityExtractor(category);
        this.entityExtractor.init(this.config);
        this.entityExtractor.loadNLP();
        this.entityExtractor.loadDictionary(this.config);
    }

    /**
     * Stop Server
     */
    public void stopServer() {
        logger.info("stop server");
        System.exit(1);
    }


    /**
     * Start Server
     */
    public void startServer() {
        // Try to open a server socket on the given port
        // Note that we can't choose a port less than 1024 if we are not
        // privileged users (root)

        try {
            echoServer = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info( "Sentiment API Server is now started and is waiting for connections." );
        //System.out.println( "Any client can send -1 to stop the server." );

        // waiting for connections
        while (true) {
            try {
                clientSocket = echoServer.accept();
                numConnections++;
                ServerConnection oneConnection = new ServerConnection(clientSocket, numConnections, this, this.entityExtractor);
                new Thread(oneConnection).start();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}

class ServerConnection implements Runnable {
    private static Logger logger = Logger.getLogger(ServerConnection.class.getName());

    BufferedReader is;
    PrintStream os;
    Socket clientSocket;
    int id;
    EntityExtractorServer server;
    EntityExtractor entityExtractor;

    public ServerConnection(Socket clientSocket, int id, EntityExtractorServer server, EntityExtractor entityExtractor) {
        this.clientSocket = clientSocket;
        this.id = id;
        this.server = server;
        this.entityExtractor = entityExtractor;
        logger.info("Connection " + id + " established with " + clientSocket);

        try {
            is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = new PrintStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p/>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        StringBuilder resultString = new StringBuilder();
        String line;
        try {

            boolean serverStop = false;
            while (true) {
                logger.debug("before readline()");
                line = is.readLine();
                logger.debug("after readline()");

                // stop server
                if (line.equals("-1")) {
                    logger.info("Server Stopped");
                    serverStop = true;
                    break;
                }

                // disconnect the client
                if (line.equals("0")) {
                    logger.info("Client disconnected");
                    break;
                }

                logger.info("Received " + line + " from connection " + id + ".");

                // TODO: business logic must be added here


                if (line.trim().length() == 0) {
                    os.println("NULL");
                } else {
                    List<Entity> results = this.entityExtractor.find(line, EntityExtractor.SEN_LEVEL_ANAL);
                    if(results != null && results.size() > 0) {
                        int count = 0;
                        for(Entity e : results) {
                            if (count == results.size() - 1) {
                                resultString.append(e.getName().replace(" ", ""));
                                continue;
                            }
                            resultString.append(e.getName().replace(" ", "")).append("||");
                            count++;
                        }
                    } else {
                        resultString.append("NONE");
                    }

                    os.println(resultString.toString());
                }
            }

            logger.info("Connection " + id + "closed");
            is.close();
            os.close();
            clientSocket.close();

            if (serverStop) {
                server.stopServer();
            }

        } catch (IOException e) {
            logger.error("IOException occurred");
            logger.error(e);
        }
    }
}
