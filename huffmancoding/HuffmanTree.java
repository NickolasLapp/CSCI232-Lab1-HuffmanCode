package huffmancoding;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanTree {
	
	private Map<Byte, Integer> frequencyTable; 
	private Map<Byte, String> encodingTable; 
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
		byte buffer[] = new byte[1000];
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
		byte buffer[] = new byte[1000000];
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
		encodingTable = new HashMap<Byte, String>();
		encodingTree.fillEncodingTable(encodingTable, "");
	}

	private String encodeAll(InputStream dataStream) throws IOException
	{
		byte buffer []= new byte[1000000];
		
		String codedMessage = new String();	
		dataStream.read(buffer);
				
		
		for(int i = 0; i < buffer.length; i++)
		{
//			if((char)(b & 0xFF) == '$')
//				break;
			String code = encodingTable.get(buffer[i]);
			if(code != null)
				codedMessage = codedMessage.concat(code.toString());
			else
				break;
		}
//		
//		while((byteToEncode = (byte)dataStream.read()) != '$')
//			codedMessage = codedMessage.concat(encodingTable.get(byteToEncode).toString());
		return codedMessage;
	}
	
	private byte[] decodeBytes(InputStream dataStream) throws IOException
	{		
	    java.util.Scanner s = new java.util.Scanner(dataStream).useDelimiter("\\A");
	    String codedString = s.hasNext() ? s.next() : "";
	    s.close();
	    
	    return encodingTree.readCodeAsBinary(codedString);
	}
	
	private String decodeChars(InputStream data) {
	    java.util.Scanner s = new java.util.Scanner(data).useDelimiter("\\A");
	    String codedString = s.hasNext() ? s.next() : "";
	    s.close();
	    
	    return encodingTree.readCodeAsChar(codedString);
	}
	
	public static void main(String[] args) throws IOException
	{
		byte buffer[] = new byte[1000000];
		InputStream data = new FileInputStream("/home/nickolas/Desktop/testJPG.jpg");
		data.read(buffer);	
		
		data = new FileInputStream("/home/nickolas/Desktop/testJPG.jpg");
		HuffmanTree testTree = new HuffmanTree(data, true);
		data =  new FileInputStream("/home/nickolas/Desktop/testJPG.jpg");
		
		String testString = testTree.encodeAll(data);
//		testTree.encodingTree.printDataAndCodes("");
		
		data = new ByteArrayInputStream(testString.getBytes());

		byte[] dataToWrite = testTree.decodeBytes(data);
		FileOutputStream out = new FileOutputStream("/home/nickolas/Desktop/outputJPGTest.jpg");
		
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
