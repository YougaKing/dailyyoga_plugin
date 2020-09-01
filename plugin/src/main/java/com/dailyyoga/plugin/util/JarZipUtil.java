package com.dailyyoga.plugin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * @author: YougaKingWu@gmail.com
 * @created on: 2018/04/26 12:13
 * @description:
 */
public class JarZipUtil {

    private static String mDestJarName;

    public static void unzipJar(String jarPath, String destDirPath) throws IOException {
        if (!jarPath.endsWith(".jar")) return;
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> jarEntrys = jarFile.entries();
        while (jarEntrys.hasMoreElements()) {
            JarEntry jarEntry = jarEntrys.nextElement();
            if (jarEntry.isDirectory()) {
                continue;
            }
            String entryName = jarEntry.getName();
            String outFileName = destDirPath + "/" + entryName;
            File outFile = new File(outFileName);
            outFile.getParentFile().mkdirs();
            InputStream inputStream = jarFile.getInputStream(jarEntry);
            FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            int count;
            byte[] buffer = new byte[2156];
            while ((count = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.close();
            inputStream.close();
        }
        jarFile.close();
    }

    public static void zipJar(File jarFile, File sourceDir) {
        JarOutputStream jarStream = null;
        FileOutputStream outputStream = null;
        try {
            mDestJarName = jarFile.getCanonicalPath();
            outputStream = new FileOutputStream(jarFile);
            jarStream = new JarOutputStream(outputStream);
            zipJar(sourceDir, jarStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (jarStream != null) jarStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void zipJar(File sourceDir, JarOutputStream jarStream, String path) throws IOException {
        if (sourceDir.isDirectory()) {
            String[] dirList = sourceDir.list();
            String subPath = (path == null) ? "" : (path + sourceDir.getName() + '/');
            if (path != null) {
                JarEntry je = new JarEntry(subPath);
                je.setTime(sourceDir.lastModified());
                jarStream.putNextEntry(je);
                jarStream.flush();
                jarStream.closeEntry();
            }
            for (int i = 0; dirList != null && i < dirList.length; i++) {
                File f = new File(sourceDir, dirList[i]);
                zipJar(f, jarStream, subPath);
            }
        } else {
            if (sourceDir.getCanonicalPath().equals(mDestJarName)) {
                return;
            }
            FileInputStream fis = new FileInputStream(sourceDir);
            JarEntry entry = new JarEntry(path + sourceDir.getName());
            entry.setTime(sourceDir.lastModified());
            jarStream.putNextEntry(entry);
            int count;
            byte[] buffer = new byte[2156];
            while ((count = fis.read(buffer)) != -1) {
                jarStream.write(buffer, 0, count);
            }
            jarStream.flush();
            jarStream.closeEntry();
        }
    }
}