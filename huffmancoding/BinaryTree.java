package huffmancoding;

// tree.java
// demonstrates binary tree
// to run this program: C>java TreeApp
import java.io.*;
import java.util.*; // for Stack class
////////////////////////////////////////////////////////////////
class Node<T> implements Comparable
{
	public int iData; // data item (key)
	public T dData; // data item
	public Node<T> leftChild; // this node's left child
	public Node<T> rightChild; // this node's right child
	
	public Node(int iData, T dData)
	{
		this.iData = iData;
		this.dData = dData;
	}
	
	public Node(Node<T> childOne, Node<T> childTwo)
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
		if( iData < ((Node<Byte>)compareTo).iData)
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
} // end class Node
//////////////////////////////////////////////////////////////////
//class BinaryTree<T>
//{
//	private Node<T> root; // first node of tree
//	// -------------------------------------------------------------
//	public BinaryTree() // constructor
//	{ root = null; } // no nodes in tree yet
//	
//	public BinaryTree() 
//	{
//
//	}
//	
	//-------------------------------------------------------------
//	public Node<T> find(int key) // find node with given key
//	{ // (assumes non-empty tree)
//		Node<T> current = root; // start at root
//		while(current.iData != key) // while no match,
//		{
//			if(key < current.iData) // go left?
//				current = current.leftChild;
//			else // or go right?
//				current = current.rightChild;
//			if(current == null) // if no child,
//				return null; // didn't find it
//		}
//		return current; // found it
//	} // end find()
	// -------------------------------------------------------------
//	public void insert(int id, T dd)
//	{
//		Node<T> newNode = new Node<T>(id, dd); // make new node
//		if(root==null) // no node in root
//			root = newNode;
//		else // root occupied
//		{
//			Node<T> current = root; // start at root
//			Node<T> parent;
//			while(true) // (exits internally)
//			{
//				parent = current;
//				if(id < current.iData) // go left?
//				{
//					current = current.leftChild;
//					if(current == null) // if end of the line,
//					{ // insert on left
//						parent.leftChild = newNode;
//						return;
//					}
//				} // end if go left
//				else // or go right?
//				{
//					current = current.rightChild;
//					if(current == null) // if end of the line
//					{ // insert on right
//						parent.rightChild = newNode;
//						return;
//					}
//				} // end else go right
//			} // end while
//		} // end else not root
//	} // end insert()
//	// -------------------------------------------------------------
//	public boolean delete(int key) // delete node with given key
//	{ // (assumes non-empty list)
//		Node<T> current = root;
//		Node<T> parent = root;
//		boolean isLeftChild = true;
//		while(current.iData != key) // search for node
//		{
//			parent = current;
//			if(key < current.iData) // go left?
//			{
//				isLeftChild = true;
//				current = current.leftChild;
//			}
//			else // or go right?
//			{
//				isLeftChild = false;
//				current = current.rightChild;
//			}
//			if(current == null) // end of the line,
//				return false; // didn't find it
//		} // end while
//		// found node to delete
//		// if no children, simply delete it
//		if(current.leftChild==null &&
//				current.rightChild==null)
//		{
//			if(current == root) // if root,
//				root = null; // tree is empty
//			else if(isLeftChild)
//				parent.leftChild = null; // disconnect
//			else // from parent
//				parent.rightChild = null;
//		}
//		// if no right child, replace with left subtree
//		else if(current.rightChild==null)
//			if(current == root)
//				root = current.leftChild;
//			else if(isLeftChild)
//				parent.leftChild = current.leftChild;
//			else
//				parent.rightChild = current.leftChild;
//		// if no left child, replace with right subtree
//		else if(current.leftChild==null)
//			if(current == root)
//				root = current.rightChild;
//			else if(isLeftChild)
//				parent.leftChild = current.rightChild;
//			else
//				parent.rightChild = current.rightChild;
//		else // two children, so replace with inorder successor
//		{
//			// get successor of node to delete (current)
//			Node<T> successor = getSuccessor(current);
//			// connect parent of current to successor instead
//			if(current == root)
//				root = successor;
//			else if(isLeftChild)
//				parent.leftChild = successor;
//			else
//				parent.rightChild = successor;
//			// connect successor to current's left child
//			successor.leftChild = current.leftChild;
//		} // end else two children
//		// (successor cannot have a left child)
//		return true; // success
//	} // end delete()
//	// -------------------------------------------------------------
//	// returns node with next-highest value after delNode
//	// goes to right child, then right child's left descendents
//	private Node<T> getSuccessor(Node<T> delNode)
//	{
//		Node<T> successorParent = delNode;
//		Node<T> successor = delNode;
//		Node<T> current = delNode.rightChild; // go to right child
//		while(current != null) // until no more
//		{ // left children,
//			successorParent = successor;
//			successor = current;
//			current = current.leftChild; // go to left child
//		}
//		// if successor not
//		if(successor != delNode.rightChild) // right child,
//		{ // make connections
//			successorParent.leftChild = successor.rightChild;
//			successor.rightChild = delNode.rightChild;
//		}
//		return successor;
//	}
//	// -------------------------------------------------------------
//	public void traverse(int traverseType)
//	{
//		switch(traverseType)
//		{
//		case 1: System.out.print("\nPreorder traversal: ");
//		preOrder(root);
//		break;
//		case 2: System.out.print("\nInorder traversal: ");
//		inOrder(root);
//		break;
//		case 3: System.out.print("\nPostorder traversal: ");
//		postOrder(root);
//		break;
//		}
//		System.out.println();
//	}
//	// -------------------------------------------------------------
//	private void preOrder(Node<T> localRoot)
//	{
//		if(localRoot != null)
//		{
//			System.out.print(localRoot.iData + " ");
//			preOrder(localRoot.leftChild);
//			preOrder(localRoot.rightChild);
//		}
//	}
//	// -------------------------------------------------------------
//	private void inOrder(Node<T> localRoot)
//	{
//		if(localRoot != null)
//		{
//			inOrder(localRoot.leftChild);
//			System.out.print(localRoot.iData + " ");
//			inOrder(localRoot.rightChild);
//		}
//	}
//	// -------------------------------------------------------------
//	private void postOrder(Node<T> localRoot)
//	{
//		if(localRoot != null)
//		{
//			postOrder(localRoot.leftChild);
//			postOrder(localRoot.rightChild);
//			System.out.print(localRoot.iData + " ");
//		}
//	}
//	// -------------------------------------------------------------
//	public void displayTree()
//	{
//		Stack globalStack = new Stack();
//		globalStack.push(root);
//		int nBlanks = 32;
//		boolean isRowEmpty = false;
//		System.out.println(
//				"......................................................");
//		while(isRowEmpty==false)
//		{
//			Stack localStack = new Stack();
//			isRowEmpty = true;
//			for(int j=0; j<nBlanks; j++)
//				System.out.print(' ');
//			while(globalStack.isEmpty()==false)
//			{
//				Node<T> temp = (Node<T>)globalStack.pop();
//				if(temp != null)
//				{
//					System.out.print(temp.iData+"|"+temp.dData);
//					localStack.push(temp.leftChild);
//					localStack.push(temp.rightChild);
//					if(temp.leftChild != null ||
//							temp.rightChild != null)
//						isRowEmpty = false;
//				}
//				else
//				{
//					System.out.print("--");
//					localStack.push(null);
//					localStack.push(null);
//				}
//				for(int j=0; j<nBlanks*2-2; j++)
//					System.out.print(' ');
//			} // end while globalStack not empty
//			System.out.println();
//			nBlanks /= 2;
//			while(localStack.isEmpty()==false)
//				globalStack.push( localStack.pop() );
//		} // end while isRowEmpty is false
//		System.out.println(
//				"......................................................");
//	} // end displayTree()
//	// -------------------------------------------------------------
//} // end class Tree