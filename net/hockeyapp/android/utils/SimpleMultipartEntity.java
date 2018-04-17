package net.hockeyapp.android.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public class SimpleMultipartEntity {
    private static final char[] BOUNDARY_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private String mBoundary;
    private boolean mIsSetFirst = false;
    private boolean mIsSetLast = false;
    private OutputStream mOut;
    private File mTempFile;

    public SimpleMultipartEntity(File tempFile) {
        int i = 0;
        this.mTempFile = tempFile;
        try {
            this.mOut = new FileOutputStream(this.mTempFile);
        } catch (Throwable e) {
            HockeyLog.error("Failed to open temp file", e);
        }
        StringBuilder buffer = new StringBuilder();
        Random rand = new Random();
        while (i < 30) {
            buffer.append(BOUNDARY_CHARS[rand.nextInt(BOUNDARY_CHARS.length)]);
            i++;
        }
        this.mBoundary = buffer.toString();
    }

    public String getBoundary() {
        return this.mBoundary;
    }

    public void writeFirstBoundaryIfNeeds() throws IOException {
        if (!this.mIsSetFirst) {
            OutputStream outputStream = this.mOut;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("--");
            stringBuilder.append(this.mBoundary);
            stringBuilder.append("\r\n");
            outputStream.write(stringBuilder.toString().getBytes());
        }
        this.mIsSetFirst = true;
    }

    public void writeLastBoundaryIfNeeds() {
        if (!this.mIsSetLast) {
            try {
                OutputStream outputStream = this.mOut;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\r\n--");
                stringBuilder.append(this.mBoundary);
                stringBuilder.append("--\r\n");
                outputStream.write(stringBuilder.toString().getBytes());
                this.mOut.flush();
                this.mOut.close();
                this.mOut = null;
            } catch (Throwable e) {
                HockeyLog.error("Failed to close temp file", e);
            }
            this.mIsSetLast = true;
        }
    }

    public void addPart(String key, String value) throws IOException {
        writeFirstBoundaryIfNeeds();
        OutputStream outputStream = this.mOut;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Content-Disposition: form-data; name=\"");
        stringBuilder.append(key);
        stringBuilder.append("\"\r\n");
        outputStream.write(stringBuilder.toString().getBytes());
        this.mOut.write("Content-Type: text/plain; charset=UTF-8\r\n".getBytes());
        this.mOut.write("Content-Transfer-Encoding: 8bit\r\n\r\n".getBytes());
        this.mOut.write(value.getBytes());
        outputStream = this.mOut;
        stringBuilder = new StringBuilder();
        stringBuilder.append("\r\n--");
        stringBuilder.append(this.mBoundary);
        stringBuilder.append("\r\n");
        outputStream.write(stringBuilder.toString().getBytes());
    }

    public void addPart(String key, String fileName, InputStream fin, boolean lastFile) throws IOException {
        addPart(key, fileName, fin, "application/octet-stream", lastFile);
    }

    public void addPart(String key, String fileName, InputStream fin, String type, boolean lastFile) throws IOException {
        writeFirstBoundaryIfNeeds();
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Content-Type: ");
            stringBuilder.append(type);
            stringBuilder.append("\r\n");
            type = stringBuilder.toString();
            OutputStream outputStream = this.mOut;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Content-Disposition: form-data; name=\"");
            stringBuilder2.append(key);
            stringBuilder2.append("\"; filename=\"");
            stringBuilder2.append(fileName);
            stringBuilder2.append("\"\r\n");
            outputStream.write(stringBuilder2.toString().getBytes());
            this.mOut.write(type.getBytes());
            this.mOut.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());
            byte[] tmp = new byte[4096];
            while (true) {
                int read = fin.read(tmp);
                int l = read;
                if (read == -1) {
                    break;
                }
                this.mOut.write(tmp, 0, l);
            }
            this.mOut.flush();
            if (lastFile) {
                writeLastBoundaryIfNeeds();
            } else {
                OutputStream outputStream2 = this.mOut;
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("\r\n--");
                stringBuilder3.append(this.mBoundary);
                stringBuilder3.append("\r\n");
                outputStream2.write(stringBuilder3.toString().getBytes());
            }
        } finally {
            try {
                fin.close();
            } catch (IOException e) {
            }
        }
    }

    public long getContentLength() {
        writeLastBoundaryIfNeeds();
        return this.mTempFile.length();
    }

    public void writeTo(OutputStream out) throws IOException {
        writeLastBoundaryIfNeeds();
        FileInputStream fileInputStream = new FileInputStream(this.mTempFile);
        BufferedOutputStream outputStream = new BufferedOutputStream(out);
        byte[] tmp = new byte[4096];
        while (true) {
            int read = fileInputStream.read(tmp);
            int l = read;
            if (read != -1) {
                outputStream.write(tmp, 0, l);
            } else {
                fileInputStream.close();
                outputStream.flush();
                outputStream.close();
                this.mTempFile.delete();
                this.mTempFile = null;
                return;
            }
        }
    }
}
