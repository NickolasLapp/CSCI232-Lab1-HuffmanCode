package huffmancoding;

import java.io.*;
import java.util.*; // for Stack class

class Node implements Comparable<Node> {
    public int iData; // data item (key)
    public Integer dData; // data item
    public Node leftChild; // this node's left child
    public Node rightChild; // this node's right child

    public Node(int iData, int dData) {
        this.iData = iData;
        this.dData = dData;
    }

    public Node(Node childOne, Node childTwo) {
        this.iData = childOne.iData + childTwo.iData;
        this.dData = null;

        if (childOne.compareTo(childTwo) == 1) {
            leftChild = childOne;
            rightChild = childTwo;
        } else {
            leftChild = childTwo;
            rightChild = childOne;
        }

    }

    public void displayNode() // display ourself
    {
        System.out.print('{');
        System.out.print(iData);
        System.out.print(", ");
        System.out.print(dData);
        System.out.print("} ");
    }

    @Override
    public int compareTo(Node compareTo) {
        if (iData < compareTo.iData)
            return -1;
        else
            return 1;
    }

    public void fillEncodingTable(Map<Integer, String> encodingTable, String code) {
        if (dData != null)
            encodingTable.put(dData, code);
        else {
            leftChild.fillEncodingTable(encodingTable, code + "0");
            rightChild.fillEncodingTable(encodingTable, code + "1");
        }
    }

    public String readCodeAsChar(String code) {
        Node head = this;
        Node current = this;
        String message = "";

        for (char c : code.toCharArray()) {
            if (current.dData != null) {
                Character coded = (char) (current.dData & 0xFF);
                message = message.concat(coded.toString());
                current = head;
            }
            if ('1' == c)
                current = current.rightChild;
            else
                current = current.leftChild;
        }
        return message;
    }

    public byte[] toByteArray(List<Byte> in) {
        final int n = in.size();
        byte ret[] = new byte[n];
        for (int i = 0; i < n; i++) {
            ret[i] = in.get(i);
        }
        return ret;
    }

    public byte[] codeToBytes(byte[] code, int numBytes) {
        Node head = this;
        Node current = this;
        ArrayList<Byte> message = new ArrayList<Byte>(1000);
        for (int i = 0; i < numBytes; i++) {
            if (current.dData != null) {
                message.add(current.dData);
                current = head;
            }
            if ((byte) 1 == code[i])
                current = current.rightChild;
            else
                current = current.leftChild;
        }
        return toByteArray(message);
    }

    public void printDataAndCodesAsChars(String code) {
        if (dData != null)
            System.out.println("dData: " + (char) (dData & 0xff) + "\tCode: " + code + "\tFrequency: " + iData);
        else {
            leftChild.printDataAndCodesAsChars(code + "0");
            rightChild.printDataAndCodesAsChars(code + "1");
        }
    }
}