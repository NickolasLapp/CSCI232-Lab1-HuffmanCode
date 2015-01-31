package huffmancoding;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanTree {
	
	private Map<Character, Integer> frequencyTable; 
	private Map<Character, String> encodingTable; 
	private Node<Character> encodingTree;
	
	public HuffmanTree(InputStream dataStream)
	{
		try {
			generateFrequencyTable(dataStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		buildTree();
	}
	
	private void generateFrequencyTable(InputStream dataStream) throws IOException
	{	
		frequencyTable = new HashMap<Character, Integer>();
		
		char read;
		while( (read = (char) dataStream.read()) != '$')
		{
			Integer previousFrequency = frequencyTable.get(read);
			frequencyTable.put(read, previousFrequency == null ? 1 : previousFrequency + 1);
		}
	}
	
	private void buildTree()
	{
		PriorityQueue<Node<Character>> nodes = new PriorityQueue<Node<Character>>(frequencyTable.size());
		
		for(char c : frequencyTable.keySet())
		{
			nodes.add(new Node<Character>(frequencyTable.get(c), c));
		}
		
		while(nodes.size() > 1)
		{
			nodes.add(new Node<Character>(nodes.poll(), nodes.poll()));
		}
		encodingTree = nodes.poll();	
		encodingTable = new HashMap<Character, String>();
		encodingTree.fillEncodingTable(encodingTable, "");
	}
	
	private String encodeOne(char data)
	{
		try{
			return encodingTable.get(data);
		}
		catch(NullPointerException e){
			System.out.println("Please create encoding Table Before encoding.");
			e.printStackTrace();
		}
		return null;
	}
	
	private String decode(InputStream dataStream) throws IOException
	{
		String message = new String();
		String code;
		while(!(code = Integer.toString(dataStream.read())).equals("$"))
			message.concat(encodingTree.readCode(code).toString());
		return message;
	}
	
	public static void main(String[] args) throws IOException
	{
		InputStream data = new FileInputStream("/home/nickolas/Desktop/testFile.txt");
		new HuffmanTree(data);
		
		
	}
}
