package com.company;
import java.io.*;
import java.net.*;

public class Main {

    private final static String serverIP = "127.0.0.1"; // IP сервер
    private final static int serverPort = 8000; // порт сервера
    private final static String fileOutput = "/home/random/11";// Название файла и путь куда скачивать

    public static void main(String args[]) {
        byte[] aByte = new byte[1024];
        int bytesRead;
        Socket clientSocket = null;
        InputStream is = null;
        try {
            clientSocket = new Socket( serverIP , serverPort );
            is = clientSocket.getInputStream();
        } catch (IOException ex) {
            System.out.println("Сервер не найден");
        }
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (is != null) {
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                fos = new FileOutputStream(fileOutput);
                bos = new BufferedOutputStream(fos);
                bytesRead = is.read(aByte, 0, aByte.length);
                do {
                    bos.write(aByte);
                    bos.flush();
                    bytesRead = is.read(aByte);
                } while (bytesRead != -1);
                bos.close();
                clientSocket.close();
            } catch (IOException ex) {
            }
        }
    }
}