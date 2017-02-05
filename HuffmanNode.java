public class HuffmanNode {

	private String val;
	private Integer fq;
	private HuffmanNode lChild;
	private HuffmanNode rChild;
	
	public HuffmanNode(String val) {
		this.val = val;
		this.fq = 1;
	}
	
	// constructor generating the tree structure
	public HuffmanNode(HuffmanNode lChild, HuffmanNode rChild) {
		this.val = lChild.getVal() + rChild.getVal();
		this.fq = lChild.getFq() + rChild.getFq();
		this.lChild = lChild;
		this.rChild = rChild;
	}

	public void incrementFq() {
		fq++;
	}

	public String getVal() {
		return val;
	}

	public Integer getFq() {
		return fq;
	}

	public HuffmanNode getlChild() {
		return lChild;
	}

	public HuffmanNode getrChild() {
		return rChild;
	}

	@Override
	public String toString() {
		return "HuffmanNode [val=" + val + ", fq=" + fq + ", lChild=" + lChild + ", rChild=" + rChild + "]";
	}
}