package Modeling;

import java.util.ArrayList;
import java.util.HashMap;

import Modeling.SequenceDiagrams.FragmentType;
import Modeling.SequenceDiagrams.Lifeline;

public class FragmentOld extends Node {
	private String name;
	private FragmentType type;
	private String operandName;
	private String guard;
	protected int coveredLifelines = 0;
	private ArrayList<Lifeline> lifelines;
	private ArrayList<Node> nodes;
	private HashMap<String, Node> nodeByID;

	public FragmentOld(String id, String name) {
		super(id);
		this.name = name;
		this.lifelines = new ArrayList<Lifeline>();
		this.nodes = new ArrayList<Node>();
		this.nodeByID = new HashMap<String, Node>();
	}

	public ArrayList<Lifeline> getLifelines() {
		return lifelines;
	}

	public void setLifelines(ArrayList<Lifeline> lifelines) {
		this.lifelines = lifelines;
	}
	
	public void addLifeline(Lifeline lifeline) {
		this.lifelines.add(lifeline);
		this.coveredLifelines += 1;
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
	
	public void addNode(Node node) {
		this.nodes.add(node);
		this.nodeByID.put(this.getId(), node);
	}

	public String getGuard() {
		return guard;
	}
	public String getName() {
		return name;
	}

	public FragmentType getType() {
		return type;
	}

	public void setType(String typeName) throws UnsupportedFragmentTypeException {
		if (typeName.equals("opt")) {
			this.setType(FragmentType.optional);
		} else if (typeName.equals("loop")) {
			this.setType(FragmentType.loop);
		} else {
			throw new UnsupportedFragmentTypeException("Fragment of type " + typeName + " is not supported!");
		}
	}
	
	public void setType(FragmentType type) {
		this.type = type;
	}

	public String getOperandName() {
		return operandName;
	}

	public void setOperandName(String operandName) {
		this.operandName = operandName;
	}
	
	public void setGuard(String guard) {
		this.guard = guard;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getCoveredLifelines() {
		return coveredLifelines;
	}

	public void setCoveredLifelines(int coveredLifelines) {
		this.coveredLifelines = coveredLifelines;
	}

	public void print() {
		super.print();
	}
}