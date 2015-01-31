package huffmancoding;

import java.io.*;
import java.util.*; // for Stack class

class Node implements Comparable
{
	public int iData; // data item (key)
	public Byte dData; // data item
	public Node leftChild; // this node's left child
	public Node rightChild; // this node's right child
	
	public Node(int iData, byte dData)
	{
		this.iData = iData;
		this.dData = dData;
	}
	
	public Node(Node childOne, Node childTwo)
	{
		this.iData = childOne.iData + childTwo.iData;
		this.dData = null;
		
		if(childOne.compareTo(childTwo) == 1)
		{
			leftChild = childOne;
			rightChild = childTwo;
		}
		else
		{
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
	public int compareTo(Object compareTo) {
		if( iData < ((Node)compareTo).iData)
			return -1;
		else
			return 1;
	}

	public void printDataAndCodes(String code) {
		if(dData != null)
			System.out.println("dData: " + dData + "\tCode: " + code + "\tFrequency: " + iData);
		else
		{
			leftChild.printDataAndCodes(code + "0");
			rightChild.printDataAndCodes(code + "1");
		}
	}

	public void fillEncodingTable(Map<Byte, byte[]> encodingTable, byte[] code) {
		if(dData != null)
			encodingTable.put(dData, code);
		else
		{
			byte nextCodeLeft [] = Arrays.copyOf(code, code.length+1);
			nextCodeLeft[nextCodeLeft.length-1] = (byte)0;
			leftChild.fillEncodingTable(encodingTable, nextCodeLeft);
			
			byte nextCodeRight [] = Arrays.copyOf(code, code.length+1); 
			nextCodeRight[nextCodeRight.length-1] = (byte)1;
			rightChild.fillEncodingTable(encodingTable, nextCodeRight);
		}
	}

	public String readCodeAsChar(String code) {
		Node head = this;
		Node current = this;
		String message = "";
		
		for(char c : code.toCharArray())
		{
			if(current.dData != null)
			{
				Character coded = (char)(current.dData & 0xFF);
				message = message.concat(coded.toString());
				current = head;
			}
			if('1' == c)
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
	
	public byte[] readCodeAsBinary(byte[] code, int numBytes) {
		Node head = this;
		Node current = this;
		ArrayList<Byte> message = new ArrayList<Byte>(1000);
		for(int i = 0; i < numBytes; i++)
		{
			if(current.dData != null)
			{
				message.add(current.dData);
				current = head;
			}
			if((byte)1 == code[i])
				current = current.rightChild;
			else
				current = current.leftChild;
		}
		return toByteArray(message);
	}
}