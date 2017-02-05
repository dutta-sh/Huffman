import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HuffmanMain {
	private static List<Character> eligibleChars = new ArrayList<>();
	static {
		// converts a string to character array
		char[] ch = "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(); 
		for(char c : ch)
			eligibleChars.add(c); // using List to access 'contains' method later which is not present in array
	}
	
	private static List<HuffmanNode> nodes = new ArrayList<>();
	private static Map<HuffmanNode, String> codeMap = new LinkedHashMap<>();
	
	public static void main(String[] args) throws Exception {
		StringBuilder strBld = new StringBuilder();
		
		byte[] byteArr = Files.readAllBytes(Paths.get("infile.dat")); // reading data from file
		int charCount = getCharFreq(byteArr);
		sortByFreq();
		
		strBld.append("The frequency table:\r\n");
		for(HuffmanNode node : nodes) {
			double ratio = (double)node.getFq()/charCount; // calculates the ratio
			//strBld.append(node.getVal() + ", " + node.getFq() + ", " + ratio*100 + "%\r\n");
			strBld.append(node.getVal() + ", " + ratio*100 + "%\r\n");
		}
		HuffmanNode root = generateTree();
		//System.out.println(root);
		
		getCode(root, new ArrayList<Character>());
		Integer totalBits = 0;
		strBld.append("\r\nThe huffman codes:\r\n");
		for(Map.Entry<HuffmanNode, String> entry : codeMap.entrySet()) {
			strBld.append(entry.getKey().getVal() + " --> " + entry.getValue() + "\r\n");
			totalBits += entry.getKey().getFq() * entry.getValue().length();
		}
		strBld.append("\r\nLength of coded message:" + totalBits + " bits");
		
		Files.write(Paths.get("outfile.dat"), strBld.toString().getBytes());
	}
	
	// we iterate through every eligible characters in the file and count the number of occurrence
	private static int getCharFreq(byte[] byteArr) {
		int charCount = 0;
		for(byte b : byteArr) {
			Character c = (char)b; // type casting and auto boxing 
			
			if(eligibleChars.contains(c)) {
				charCount++;
				boolean charInNodes = false;
				for(HuffmanNode node : nodes) {
					if(node.getVal().equals(c.toString())) { //checking the existence of the node and incrementing it
						charInNodes = true;
						node.incrementFq();
						break;
					}
				}
				if(!charInNodes) { 
					HuffmanNode node = new HuffmanNode(c.toString()); // creating a new node 
					nodes.add(node);
				}
			}
		}
		return charCount;
	}
	
	// bubble sorting in descending order by frequency
	private static void sortByFreq() {
		boolean swapped = true;
	    while(swapped) {
	    	swapped = false;
	        for(int i=0; i<nodes.size()-1; i++) {
	        	if(nodes.get(i).getFq() < nodes.get(i+1).getFq()) {
		            HuffmanNode tempNode = nodes.get(i);
		            nodes.set(i, nodes.get(i+1));
		            nodes.set(i+1, tempNode);
		            swapped = true; 
	        	}
	        } 
	    }
	}
	
	// generating tree structure from the list of nodes until a single element is left
	private static HuffmanNode generateTree() {
		while(nodes.size() > 1) {
			//assigning the smallest element of the list as a left child of the tree and removing it from the list
			HuffmanNode lChild = nodes.remove(nodes.size() - 1); 
			// assigning the smallest element of the list as a right child of the tree and removing it from the list
			HuffmanNode rChild = nodes.remove(nodes.size() - 1);
			// calling of the constructor that generates the tree structure
			HuffmanNode parent = new HuffmanNode(lChild, rChild);
			// now adding the newly created parent node to the end of the list
			nodes.add(parent);
			// again sorting the list by frequency
			sortByFreq();
		}
		// returns the first element which is now the root and holds the total tree structure
		return nodes.get(0); 
	}
		
	
	
	// method to generate the Huffman code
	private static void getCode(HuffmanNode node, List<Character> code) {
		if(node.getlChild() != null) {
			code.add('0');
			getCode(node.getlChild(), code); // calls recursively until left child is null
		}
		
		if(node.getrChild() != null) {
			code.add('1');
			getCode(node.getrChild(), code); // calls recursively until right child is null
		}
		if(node.getVal().length() == 1) { // assures it's the leaf node
			StringBuilder codeStr = new StringBuilder();
			for(Character c : code) {
				codeStr.append(c);
			}
			// assigns node as the key and the constructed code as the value of the map
			codeMap.put(node, codeStr.toString()); 
		}
		if(code.size() > 0)// check for the last element
			code.remove(code.size() - 1);
	}
}