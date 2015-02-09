package huffmancoding;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanTree {

    private Map<Character, Integer> frequencyTable;
    private Map<Character, String> encodingTable;
    private Node<Character> encodingTree;

    public HuffmanTree(String inputString) throws IOException {
        generateFrequencyTable(inputString);
        buildTree();
    }

    private void generateFrequencyTable(String inputString) throws IOException {
        frequencyTable = new HashMap<Character, Integer>();

        for (char read : inputString.toCharArray()) {
            if (read == '$')
                break;
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

        printCodes();
    }

    private void printCodes() {
        Character keys[] = encodingTable.keySet().toArray(new Character[encodingTable.size()]);
        Arrays.sort(keys);
        for (char c : keys) {
            System.out.println("Character: " + Node.fixWeirdChars(Character.toString(c)) + "\tCode "
                    + encodingTable.get(c));
        }
    }

    private String encodeAll(String inputString) throws IOException {
        String codedMessage = "";
        for (char toEncode : inputString.toCharArray()) {
            if (toEncode == '$')
                break;
            codedMessage = codedMessage.concat(encodingTable.get(toEncode).toString());
        }
        return codedMessage;
    }

    private String decode(String codedMessage) {
        return encodingTree.readCode(codedMessage);
    }

    public static String getText() throws IOException {
        String outStr = "", str = "";
        while (true) {
            str = getString();
            if (str.equals("$"))
                return outStr;
            outStr = outStr + str + "\n";
        }
    }

    public static String getString() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        return s;
    }

    public static char getChar() throws IOException {
        String s = getString();
        return s.charAt(0);
    }

    public static void main(String[] args) throws IOException {
        // InputStream data = new
        // FileInputStream("/home/nickolas/Desktop/testFile.txt");
        // HuffmanTree testTree = new HuffmanTree(data);
        // data.close();
        // data = new FileInputStream("/home/nickolas/Desktop/testFile.txt");
        //
        // String codedMessage = testTree.encodeAll(data);
        // data.close();
        //
        // System.out.println(testTree.decode(codedMessage));

        HuffmanTree huffman = null;
        int value;
        String inputString = null, codedString = null;

        while (true) {
            System.out.print("Enter first letter of ");
            System.out.print("enter, show, code, or decode: ");
            int choice = getChar();
            switch (choice) {
            case 'e':
                System.out.println("Enter text lines, terminate with $");
                inputString = getText();
                printAdjustedInputString(inputString);
                huffman = new HuffmanTree(inputString);
                printFrequencyTable(huffman.frequencyTable);
                break;
            case 's':
                huffman.encodingTree.displayTree();
                break;
            case 'c':
                if (inputString == null)
                    System.out.println("Please enter string first");
                else {
                    codedString = huffman.encodeAll(inputString);
                    System.out.println("Code:\t" + codedString);
                }
                break;
            case 'd':
                if (inputString == null)
                    System.out.println("Please enter string first");
                else if (codedString == null)
                    System.out.println("Please enter 'c' for code first");
                else {
                    System.out.println("Decoded:\t" + huffman.decode(codedString));
                }
                break;
            default:
                System.out.print("Invalid entry\n");
            }
        }
    }

    private static void printAdjustedInputString(String inputString) {
        for (char c : inputString.toCharArray())
            System.out.print(Node.fixWeirdChars(Character.toString(c)));
        System.out.println();
    }

    private static void printFrequencyTable(Map<Character, Integer> frequencyTable) {
        Character keys[] = frequencyTable.keySet().toArray(new Character[frequencyTable.size()]);
        Arrays.sort(keys);
        System.out.print("Character:\t");
        for (char c : keys) {
            System.out.print(Node.fixWeirdChars(Character.toString(c)) + "\t");
        }
        System.out.print("\nFrequency:\t");
        for (char c : keys) {
            System.out.print(frequencyTable.get(c) + "\t");
        }
        System.out.println();
    }
}
