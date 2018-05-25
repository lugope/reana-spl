package transformation;

import java.util.HashMap;
import java.util.Map;

import fdtmc.FDTMC;
import fdtmc.State;
import parsing.activitydiagrams.Activity;
import parsing.activitydiagrams.ActivityType;
import parsing.activitydiagrams.Edge;

public class PathTransformer {
	
	private Map<String, State> stateByActID;
	private FDTMC fdtmc;
	private State errorState;
	private State sourceState;
	
	public PathTransformer(FDTMC fdtmc, State sourceState, State errorState) {
		stateByActID = new HashMap<String, State>();
		this.fdtmc = fdtmc;
		this.errorState = errorState;
		this.sourceState = sourceState;
	}
	
	public void transformPath(Edge adEdge) {
		Activity targetAct = adEdge.getTarget();
		Activity sourceAct = adEdge.getSource();
		State targetState;

		String sourceActivitySD = sourceAct.getSd() != null ? sourceAct.getSd().getName() : "";

		if (sourceAct.getType().equals(ActivityType.INITIAL_NODE)) {
			for (Edge e : targetAct.getOutgoing()) {
				transformPath(e);
			}
		} else if (sourceAct.getType().equals(ActivityType.CALL)) {
			stateByActID.put(sourceAct.getId(), sourceState); // insere source no hashmap
			targetState = stateByActID.get(targetAct.getId()); // verifica se target esta no hashmap

			if (targetState == null) { // atividade target nao foi criada
				if (targetAct.getType().equals(ActivityType.FINAL_NODE)) {
					targetState = fdtmc.createSuccessState();
					stateByActID.put(targetAct.getId(), targetState);
					fdtmc.createTransition(targetState, targetState, "", "1.0");
				}
				else targetState = fdtmc.createState();

                fdtmc.createInterface(sourceActivitySD, sourceState, targetState, errorState);
                
                this.sourceState = targetState;
                
				/* continue path */
				for (Edge e : targetAct.getOutgoing()) {
					transformPath(e);
				}
			} else { // atividade target ja foi criada
			    fdtmc.createInterface(sourceActivitySD, sourceState, targetState, errorState);
				/* end path */
			}
		} else if (sourceAct.getType().equals(ActivityType.DECISION)) {
			stateByActID.put(sourceAct.getId(), sourceState); // insere source no hashmap
			targetState = stateByActID.get(targetAct.getId()); // verifica se target esta no hashmap

			if (targetState == null) { // atividade target nao foi criada
				if (targetAct.getType().equals(ActivityType.FINAL_NODE)) {
					targetState = fdtmc.createSuccessState();
					stateByActID.put(targetAct.getId(), targetState);
					fdtmc.createTransition(targetState, targetState, "", "1.0");
				}
				else targetState = fdtmc.createState();

				fdtmc.createTransition(sourceState, targetState, "", Float.toString(adEdge.getProbability()));

				this.sourceState = targetState;
				
				/* continue path */
				for (Edge e : targetAct.getOutgoing()) {
					transformPath(e);
				}
			} else { // atividade target ja foi criada
				fdtmc.createTransition(sourceState, targetState, "", Float.toString(adEdge.getProbability()));
				/* end path */
			}
		} else if (sourceAct.getType().equals(ActivityType.MERGE)) {
			stateByActID.put(sourceAct.getId(), sourceState); // insere source no hashmap
			targetState = stateByActID.get(targetAct.getId()); // verifica se target esta no hashmap

			if (targetState == null) { // atividade target nao foi criada
				if (targetAct.getType().equals(ActivityType.FINAL_NODE)) {
					targetState = fdtmc.createSuccessState();
					stateByActID.put(targetAct.getId(), targetState);
					fdtmc.createTransition(targetState, targetState, "", "1.0");
				}
				else targetState = fdtmc.createState();

				fdtmc.createTransition(sourceState, targetState, sourceAct.getName(), "1.0");

				this.sourceState = targetState;
				
				/* continue path */
				for (Edge e : targetAct.getOutgoing()) {
					transformPath(e);
				}
			} else { // atividade target ja foi criada
				fdtmc.createTransition(sourceState, targetState, sourceAct.getName(), "1.0");
				/* end path */
			}
		}
	}
}
