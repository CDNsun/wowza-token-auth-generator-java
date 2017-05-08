package net.cdnsun;

import junit.framework.Assert;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TokenGeneratorTest {

    @org.junit.Test
    public void testMain() throws Exception {
        List<String> input = readFile("/input.txt");
        List<String> expected = readFile("/output.txt");

        List<String> result = new ArrayList<String>();
        for (String inputLine : input) {
            String[] split = inputLine.split("\\s");
            try {
                result.add(TokenGenerator.run(split));
            } catch (Exception ignored) {}
        }
        writeFile(result, "output-java.txt");
        Assert.assertTrue("Expected output.txt file should match with result of processing input.txt file",
                expected.equals(result));
    }

    private List<String> readFile(String fileName) {
        List<String> lines = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            File file = new File(this.getClass().getResource(fileName).toURI());
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception ignore) {}
        }

        return lines;
    }

    private void writeFile(List<String> result, String fileName) {
        BufferedWriter writer = null;
        try {
            File file = new File(fileName);
            writer = new BufferedWriter(new FileWriter(file));
            for (String line : result) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception ignore) {}
        }
    }
}