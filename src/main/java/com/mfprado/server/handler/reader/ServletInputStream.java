package com.mfprado.server.handler.reader;

import java.io.*;

public class ServletInputStream {

    private static final int CR = 0x0d;
    private static final int LF = 0x0a;

    private final BufferedInputStream input;
    private BufferedReader bufferedReader;

    public ServletInputStream(InputStream input) {
        this.input = new BufferedInputStream(input);
    }

    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        int c = input.read();
        while (c >= 0 && c != LF) {
            if (c != CR) {
                sb.append((char) c);
            }
            c = input.read();
        }
        return sb.toString();
    }

    public int read() throws IOException {
        if (bufferedReader == null) {
            this.bufferedReader = new BufferedReader(new InputStreamReader(input));
        }
        return bufferedReader.read();
    }
}
