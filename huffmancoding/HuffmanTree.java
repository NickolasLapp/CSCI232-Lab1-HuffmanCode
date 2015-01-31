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
	
	private Map<Byte, Integer> frequencyTable; 
	private Map<Byte, byte[]> encodingTable; 
	private Node encodingTree;
	
	public HuffmanTree(InputStream dataStream, boolean isByteStream) throws IOException
	{
		if(!isByteStream)
			generateFrequencyTable(dataStream);
		else
			generateFrequencyTableFromBytes(dataStream);
		buildTree();
	}
	
	private void generateFrequencyTable(InputStream dataStream) throws IOException
	{	
		byte buffer[] = new byte[4000000];
		frequencyTable = new HashMap<Byte, Integer>();
		
		dataStream.read(buffer);
				
		for(byte b : buffer)
		{
			if((char)(b & 0xFF) == '$')
				break;
//			System.out.println("Reading: " + (char)(b & 0xFF));
			Integer previousFrequency = frequencyTable.get(b);
			frequencyTable.put(b, previousFrequency == null ? 1 : previousFrequency + 1);
		}
	}
	
	private void generateFrequencyTableFromBytes(InputStream dataStream) throws IOException
	{	
		byte buffer[] = new byte[4000000];
		frequencyTable = new HashMap<Byte, Integer>();
		
		int bytesRead = dataStream.read(buffer);
				
		for(int i = 0; i < bytesRead; i++)
		{
//			System.out.println("Reading: " + (char)(b & 0xFF));
			Integer previousFrequency = frequencyTable.get(buffer[i]);
			frequencyTable.put(buffer[i], previousFrequency == null ? 1 : previousFrequency + 1);
		}
	}
	
	private void buildTree()
	{
		PriorityQueue<Node> nodes = new PriorityQueue<Node>(frequencyTable.size());
		
		for(byte b : frequencyTable.keySet())
		{
			nodes.add(new Node(frequencyTable.get(b), b));
		}
		
		while(nodes.size() > 1)
		{
			nodes.add(new Node(nodes.poll(), nodes.poll()));
		}
		encodingTree = nodes.poll();	
		encodingTable = new HashMap<Byte, byte[]>();
		encodingTree.fillEncodingTable(encodingTable, new byte[0]);
	}

	private byte[] encodeAll(InputStream dataStream) throws IOException
	{
		byte buffer []= new byte[4000000];
		
		byte codedMessage []= new byte[40000000];
		int bytesRead = dataStream.read(buffer);
		int codeIndex = 0;
		
		
		
		for(int i = 0; i < bytesRead; i++)
		{
//			if((char)(b & 0xFF) == '$')
//				break;	
			byte[] code = encodingTable.get(buffer[i]);
			if(code != null)
			{
				for(byte b : code)
					codedMessage[codeIndex++] = b;
			}
			else
				break;
		}
//		
//		while((byteToEncode = (byte)dataStream.read()) != '$')
//			codedMessage = codedMessage.concat(encodingTable.get(byteToEncode).toString());
		return Arrays.copyOf(codedMessage, codeIndex);
	}
	
	private byte[] decodeBytes(InputStream dataStream) throws IOException
	{		
		byte codedBytes[] = new byte [4000000];
		int bytesRead = dataStream.read(codedBytes);
	    
	    return encodingTree.readCodeAsBinary(codedBytes, bytesRead);
	}
	
	private String decodeChars(InputStream data) {
	    java.util.Scanner s = new java.util.Scanner(data).useDelimiter("\\A");
	    String codedString = s.hasNext() ? s.next() : "";
	    s.close();
	    
	    return encodingTree.readCodeAsChar(codedString);
	}
	
	public static void main(String[] args) throws IOException
	{
		byte buffer[] = new byte[4000000];
		InputStream data = new FileInputStream("/home/nickolas/Desktop/testFile.txt");
		int numBytesInFile = data.read(buffer);	
		
		data = new FileInputStream("/home/nickolas/Desktop/testFile.txt");
		HuffmanTree testTree = new HuffmanTree(data, true);
		data =  new FileInputStream("/home/nickolas/Desktop/testFile.txt");
		
		byte[] encodedBytes = testTree.encodeAll(data);
		testTree.encodingTree.printDataAndCodes("");
		
		data = new ByteArrayInputStream(encodedBytes);

		byte[] dataToWrite = testTree.decodeBytes(data);
		FileOutputStream out = new FileOutputStream("/home/nickolas/Desktop/outputtestFile.txt");
		
//		for(int i = 0; i < buffer.length; i++)
//		{
//			if(dataToWrite[i] != buffer[i])
//				System.out.println("DataToWrite: " + dataToWrite[i] + "\tBuffer: " + buffer[i] + " @ " + i);
//		}
		
		for(byte b:dataToWrite)
			out.write(b);
		out.close();
//		 
		
//		InputStream data = new FileInputStream("/home/nickolas/Desktop/testFile.txt");
//		HuffmanTree testTree = new HuffmanTree(data, false);
//		data =  new FileInputStream("/home/nickolas/Desktop/testFile.txt");
//		
//		String testString = testTree.encodeAll(data);
//		testTree.encodingTree.printDataAndCodes("");
//		System.out.println("Encoded String: " + testString);
//		
//		
//		data = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
//		System.out.println(testTree.decodeChars(data));
	}
}
