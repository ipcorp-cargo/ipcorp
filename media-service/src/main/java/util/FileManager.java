package util;

import lombok.SneakyThrows;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.ServerException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class FileManager {
    public static final String UPLOAD_FOLDER_PATH = "/upload";

    public static String saveFile(InputStream inputStream, String path, String fileName) throws IOException {
        System.out.println("=============================");
        System.out.println("path: "+path);
        System.out.println("=============================");
        System.out.println("filename: " + fileName);
        System.out.println("=============================");
        System.out.println("=============================");
        File directory = new File(UPLOAD_FOLDER_PATH + path);
        System.out.println(directory.getAbsolutePath());
        Files.createDirectories(Path.of(directory.getAbsolutePath()));
//        if(directory.exists()){
//            if(directory.mkdirs()){
//                throw new ServerException("Unable to create folder:" + directory.getAbsolutePath());
//            }
//        }else {
//            System.out.println(directory.mkdirs());
//        }

        File file = new File(directory, fileName);
        System.out.println(file.getAbsolutePath());
        FileOutputStream fileOutputStream = new FileOutputStream(file, true);

        inputStream.transferTo(fileOutputStream);

        fileOutputStream.close();

        return file.getAbsolutePath();
    }

    public static boolean deleteFileIfExists(String path){
        if (path == null){
            return false;
        }
        File file = new File(path);
        return file.delete();
    }


    public static String hashFile(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        MessageDigest sha256Digest = null;

        try {
            sha256Digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] buffer = new byte[4096];
        int bytesRead = 0;

        while ((bytesRead = bufferedInputStream.read(buffer))!= -1){
            sha256Digest.update(buffer, 0,bytesRead);
        }

        byte[] hashBytes = sha256Digest.digest();
        StringBuilder stringBuilder = new StringBuilder();

        for (byte sha256Byte : hashBytes){
            stringBuilder.append(Integer.toString((sha256Byte & 0xff) + 0x100, 16).substring(1));
        }

        return stringBuilder.toString();
    }


}
