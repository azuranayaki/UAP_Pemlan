package Util;
import Model.Barang;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    public static void ensureFiles() throws IOException {
        Files.createDirectories(Paths.get(AppPaths.DATA_DIR));
        if (!Files.exists(Paths.get(AppPaths.BARANG_CSV))) Files.createFile(Paths.get(AppPaths.BARANG_CSV));
        if (!Files.exists(Paths.get(AppPaths.HISTORY_TXT))) Files.createFile(Paths.get(AppPaths.HISTORY_TXT));
    }
    public static List<Barang> loadBarang() throws IOException {
        ensureFiles();
        List<String> lines = Files.readAllLines(Paths.get(AppPaths.BARANG_CSV), StandardCharsets.UTF_8);
        List<Barang> out = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            out.add(Barang.fromCsv(line));
        }
        return out;
    }
    public static void saveBarang(List<Barang> data) throws IOException {
        ensureFiles();
        List<String> lines = new ArrayList<>();
        for (Barang b : data) lines.add(b.toCsv());
        Files.write(Paths.get(AppPaths.BARANG_CSV), lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
    }
    public static void appendHistory(String msg) throws IOException {
        ensureFiles();
        String line = LocalDate.now() + " " + msg + System.lineSeparator();
        Files.write(Paths.get(AppPaths.HISTORY_TXT), line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }
    public static String readHistory() throws IOException {
        ensureFiles();

        byte[] bytes = Files.readAllBytes(Paths.get(AppPaths.HISTORY_TXT));
        return new String(bytes, StandardCharsets.UTF_8);
    }
}