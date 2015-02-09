package huffmancoding;

import java.util.*; // for Stack class

class Node<T> implements Comparable<Node<T>> {
    public int iData; // data item (key)
    public T dData; // data item
    public Node<T> leftChild; // this node's left child
    public Node<T> rightChild; // this node's right child

    public Node(int iData, T dData) {
        this.iData = iData;
        this.dData = dData;
    }

    public Node(Node<T> childOne, Node<T> childTwo) {
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
    public int compareTo(Node<T> compareTo) {
        if (iData < (compareTo).iData)
            return -1;
        else
            return 1;
    }

    // public void printDataAndCodes(String code) {
    // if (dData != null)
    // System.out.println("dData: " + Node.fixWeirdChars(dData.toString()) +
    // "\tCode: " + code + "\tFrequency: "
    // + iData);
    // else {
    // leftChild.printDataAndCodes(code + "0");
    // rightChild.printDataAndCodes(code + "1");
    // }
    // }

    public static String fixWeirdChars(String dData) {
        switch (dData.charAt(0)) {
        case '\r':
        case '\n':
            return "\\";
        case ' ':
            return "]";
        default:
            return dData;
        }
    }

    public void fillEncodingTable(Map<T, String> encodingTable, String code) {
        if (dData != null)
            encodingTable.put(dData, code);
        else {
            leftChild.fillEncodingTable(encodingTable, code + "0");
            rightChild.fillEncodingTable(encodingTable, code + "1");
        }
    }

    public String readCode(String code) {
        Node<T> head = this;
        Node<T> current = this;
        String toReturn = "";
        for (char c : code.toCharArray()) {
            if (current.dData != null) {
                toReturn = toReturn.concat(current.dData.toString());
                current = head;
            }
            if ('1' == c)
                current = current.rightChild;
            else
                current = current.leftChild;
        }
        return toReturn + current.dData;
    }
}