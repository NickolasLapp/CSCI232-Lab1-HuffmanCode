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

    private Map<Character, Integer> frequencyTable;
    private Map<Character, String> encodingTable;
    private Node<Character> encodingTree;

    public HuffmanTree(InputStream dataStream) throws IOException {
        generateFrequencyTable(dataStream);
        buildTree();
    }

    private void generateFrequencyTable(InputStream dataStream) throws IOException {
        frequencyTable = new HashMap<Character, Integer>();

        char read;
        while ((read = (char) dataStream.read()) != '$') {
            Integer previousFrequency = frequencyTable.get(read);
            frequencyTable.put(read, previousFrequency == null ? 1 : previousFrequency + 1);
        }
    }

    private void buildTree() {
        PriorityQueue<Node<Character>> nodes = new PriorityQueue<Node<Character>>(frequencyTable.size());

        for (char c : frequencyTable.keySet()) {
            nodes.add(new Node<Character>(frequencyTable.get(c), c));
        }

        while (nodes.size() > 1) {
            nodes.add(new Node<Character>(nodes.poll(), nodes.poll()));
        }
        encodingTree = nodes.poll();
        encodingTable = new HashMap<Character, String>();
        encodingTree.fillEncodingTable(encodingTable, "");
        encodingTree.printDataAndCodes("");
    }

    private String encodeAll(InputStream dataStream) throws IOException {
        String codedMessage = "";
        char toEncode;
        while ((toEncode = (char) dataStream.read()) != '$')
            codedMessage = codedMessage.concat(encodingTable.get(toEncode).toString());
        return codedMessage;
    }

    private String decode(String codedMessage) {
        return encodingTree.readCode(codedMessage);
    }

    public static void main(String[] args) throws IOException {
        InputStream data = new FileInputStream("/home/nickolas/Desktop/testFile.txt");
        HuffmanTree testTree = new HuffmanTree(data);
        data.close();
        data = new FileInputStream("/home/nickolas/Desktop/testFile.txt");

        String codedMessage = testTree.encodeAll(data);
        data.close();

        System.out.println(testTree.decode(codedMessage));
    }
}
