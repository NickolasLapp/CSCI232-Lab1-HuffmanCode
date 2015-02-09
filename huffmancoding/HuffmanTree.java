package huffmancoding;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanTree {

    private Map<Integer, Integer> frequencyTable;
    private Map<Integer, String> encodingTable;
    private Node encodingTree;

    public HuffmanTree(InputStream dataStream) throws IOException {
        generateFrequencyTable(dataStream);
        buildTree();
    }

    private void generateFrequencyTable(InputStream dataStream) throws IOException {
        frequencyTable = new HashMap<Integer, Integer>();
        int read;
        while ((read = dataStream.read()) != (int) '$') {
            Integer previousFrequency = frequencyTable.get(read);
            frequencyTable.put(read, previousFrequency == null ? 1 : previousFrequency + 1);
        }
    }

    private void buildTree() {
        PriorityQueue<Node> nodes = new PriorityQueue<Node>(frequencyTable.size());

        for (int c : frequencyTable.keySet()) {
            nodes.add(new Node(frequencyTable.get(c), c));
        }

        while (nodes.size() > 1) {
            nodes.add(new Node(nodes.poll(), nodes.poll()));
        }
        encodingTree = nodes.poll();
        encodingTable = new HashMap<Integer, String>();
        encodingTree.fillEncodingTable(encodingTable, "");
    }

    private String encodeAll(InputStream dataStream) throws IOException {
        String codedMessage = "";
        int read;
        while ((read = dataStream.read()) != (int) '$') {
            codedMessage = codedMessage.concat(encodingTable.get(read));
        }
        return codedMessage;
    }

    private byte[] decodeBytes(InputStream dataStream) throws IOException {
        byte codedBytes[] = new byte[4000000];
        int bytesRead = dataStream.read(codedBytes);

        return encodingTree.readCodeAsBinary(codedBytes, bytesRead);
    }

    public static void main(String[] args) throws IOException {
        InputStream data = new FileInputStream("/home/nickolas/Desktop/testFile.txt");
        HuffmanTree testTree = new HuffmanTree(data);

        data = new FileInputStream("/home/nickolas/Desktop/testFile.txt");
        String encodedString = testTree.encodeAll(data);

        testTree.encodingTree.printDataAndCodesAsChars("");

        FileOutputStream out = new FileOutputStream("/home/nickolas/Desktop/outputEncodedFile");
        for (byte b : encodedBytes)
            out.write(b);
        out.close();

        data = new ByteArrayInputStream(encodedBytes);

        byte[] dataToWrite = testTree.decodeBytes(data);
        out = new FileOutputStream("/home/nickolas/Desktop/outputtestFile.txt");
        for (byte b : dataToWrite)
            out.write(b);
        out.close();
    }
}
