package akkaHW2019S;

import akka.actor.ActorRef;

import java.util.Date;
import java.util.List;
/**
 * Messages that are passed around the actors are usually immutable classes.
 * Think how you go about creating immutable classes:) Make them all static
 * classes inside the Messages class.
 * 
 * This class should have all the immutable messages that you need to pass
 * around actors. You are free to add more classes(Messages) that you think is
 * necessary
 * 
 * @author Yifan Zhang
 *
 */
public class SolutionMessage {
	
	//Messages defined here
    private double minTourCost;

    private List<Integer> tour;

    private String agentName;
    

	public SolutionMessage(double minTourCost, List<Integer> tour) {

		this.minTourCost = minTourCost;
		this.tour = tour;

	}

	public double getMinTourCost() {
		return minTourCost;
	}

	public void setMinTourCost(double minTourCost) {
		this.minTourCost = minTourCost;
	}

	public List<Integer> getTour() {
		return tour;
	}

	public void setTour(List<Integer> tour) {
		this.tour = tour;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
    
}