package kz.ipcorp.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.ServerException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@Slf4j
public class FileManager {
    public static final String UPLOAD_FOLDER_PATH = "/upload";

    public static String saveFile(InputStream inputStream, String path, String fileName) throws IOException {
        File directory = new File(UPLOAD_FOLDER_PATH, path);
        if(!directory.exists()){
            log.info("path: " + directory.getAbsolutePath());
            boolean status = directory.mkdirs();
            if(!status){
                throw new ServerException("Unable to create folder:" + directory.getAbsolutePath());
            }
        }

        File file = new File(directory, fileName);
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

    public static Resource loadFile(String filePath) throws MalformedURLException {
        Path file = Path.of(filePath);
        return new UrlResource(filePath);
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


    public static byte[] getFile(String iconPath) throws IOException {
        Path filePath = Path.of(iconPath);
        if (!Files.exists(filePath)){
            throw new FileNotFoundException("File not found: " + iconPath);
        }
        return Files.readAllBytes(filePath);
    }
}
