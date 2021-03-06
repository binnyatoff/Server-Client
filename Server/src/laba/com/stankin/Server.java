package laba.com.stankin;

import java.io.*;
import java.net.*;


public class Server {
    public static void main(String args[]) throws IOException {
        ServerSocket welcomeSocket = new ServerSocket(8000);
        while (true) {
            Socket connectionSocket = null;
            try {
                connectionSocket = welcomeSocket.accept();
                BufferedOutputStream outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
                String fileToSend = "/home/random/10";
                System.out.println("Клиент подключился");
                Thread t = new ClientHandler(connectionSocket, outToClient, new File(fileToSend));
                t.start();
            } catch (Exception e) {
                connectionSocket.close();
                e.printStackTrace();
            }
        }
    }

    static class ClientHandler extends Thread {
        final BufferedOutputStream outToClient;
        final Socket connectionSocket;
        final File myFile;

        public ClientHandler(Socket connectionSocket, BufferedOutputStream outToClient, File fileToSend) {
            this.myFile = fileToSend;
            this.outToClient = outToClient;
            this.connectionSocket = connectionSocket;
        }

        public void run() {
            byte[] aByte = new byte[1024];//Задаем возможный буфер
            int speed = 1;//Кол-во байтов в секунду
            while (true) {
                try {
                    FileInputStream fis = new FileInputStream(myFile);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    try {
                        int bytesRead;
                        do {
                            long start = System.currentTimeMillis();
                            outToClient.write(aByte, 0, speed);
                            bytesRead = bis.read(aByte, 0, speed);
                            long delta = 1000 - (System.currentTimeMillis() - start);
                            if (delta > 0) {
                                Thread.sleep(delta); // Задержка
                            }
                        } while (bytesRead != -1);
                        {
                            System.out.println("Отправлено");
                        }

                        outToClient.flush();
                        outToClient.close();
                        return;
                    } catch (IOException | InterruptedException ex) {
                    } finally {
                        try {
                            connectionSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (
                        FileNotFoundException ex) {
                    System.out.println("Файл не найден");
                }
            }
        }
    }
}