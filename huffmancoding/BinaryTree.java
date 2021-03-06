package huffmancoding;

import java.lang.reflect.Array;
import java.util.*; // for Stack class

class Node<T extends Comparable<?>> implements Comparable<Node<T>> {
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

    public void displayNode(Node<T> node) // display ourself
    {
        // System.out.print('{');
        System.out.print(node == null ? "---" : String.format("%03d", node.iData));
        System.out.print(",");
        System.out.print(node == null ? "-" : node.dData == null ? "-" : Node.fixWeirdChars(node.dData.toString()));
        // System.out.print("}");
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
                toReturn = toReturn.concat(Node.fixWeirdChars(current.dData.toString()));
                current = head;
            }
            if ('1' == c)
                current = current.rightChild;
            else
                current = current.leftChild;
        }
        return toReturn + current.dData;
    }

    public void displayTree() {

        int depth = getTreeDepth(this);

        Node<T> treeList[][] = new Node[depth][];
        for (int i = 0; i < depth; i++)
            treeList[i] = getNodesAtLevel(i, this, depth);

        for (int level = 0; level < treeList.length; level++) {
            for (int nodeNum = 0; nodeNum < treeList[level].length; nodeNum++) {
                displayNodeCentered(treeList[level][nodeNum], level, depth, nodeNum == treeList[level].length - 1);
            }
            if (level != depth - 1)
                printConnectorLines(level, depth, treeList[level].length);
        }
        System.out.println();
    }

    private void displayNodeCentered(Node<T> node, int level, int depth, boolean lastNode) {
        int floor = depth - level + 2;
        int firstSpaces = (int) Math.pow(2, (floor)) - 5 + level / 2;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 5 + level / 2;

        printSpaces(firstSpaces);
        displayNode(node);
        printSpaces(betweenSpaces);
    }

    private void printConnectorLines(int level, int depth, int numNodes) {
        int floor = depth - level + 2;
        int firstSpaces = (int) Math.pow(2, (floor)) - 5 + level / 2;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 5 + level / 2;

        System.out.println();
        for (int i = 0; i < numNodes; i++) {
            printSpaces(firstSpaces / 2);
            printChar(firstSpaces / 2, '_');
            printSpaces(5);
            printChar(betweenSpaces / 2 + 1, '_');
            printSpaces(betweenSpaces / 2);
        }
        System.out.println();
    }

    private void printChar(int numChars, char toPrint) {
        for (int i = 0; i < numChars; i++) {
            System.out.print(toPrint);
        }
    }

    private void printSpaces(int numSpaces) {
        for (int i = 0; i < numSpaces; i++) {
            System.out.print(" ");
        }
    }

    private Node<T>[] getNodesAtLevel(int i, Node<T> node, int depth) {
        if (i == 0)
            return new Node[] { node };
        else
            return concatenate(node == null ? new Node[i] : getNodesAtLevel(i - 1, node.leftChild, depth),
                    node == null ? new Node[i] : getNodesAtLevel(i - 1, node.rightChild, depth));
    }

    public Node<T>[] concatenate(Node<T>[] a, Node<T>[] b) {
        int aLen = a.length;
        int bLen = b.length;

        @SuppressWarnings("unchecked")
        Node<T>[] c = (Node<T>[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);

        return c;
    }

    private int getTreeDepth(Node<T> node) {
        if (node == null)
            return 0;
        else
            return 1 + Math.max(getTreeDepth(node.leftChild), getTreeDepth(node.rightChild));
    }
}