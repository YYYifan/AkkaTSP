package akkaHW2019S;

import akka.actor.UntypedActor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 * this actor implements the search for a path that satisfies the project requirements 
 *
 * @author Yifan Zhang
 *
 */
public class Searcher extends UntypedActor {

	private final int startCity;
	private final int maxLength;
	private final double[][] distanceArray;
	private final int rows;
	
    private double minTourCost;
    private List<Integer> tour = new ArrayList<>();
    
    private SolutionMessage solution;
    private final int FINISHED_STATE;
    
	public Searcher(int startCity, int maxLength, double[][] distanceArray) {
        this.startCity = startCity;
        this.maxLength = maxLength;
        this.distanceArray = distanceArray;
        
        this.rows = distanceArray.length;
        this.FINISHED_STATE = (1 << rows) - 1;
        this.validate();
	}
	public void validate() {
		if (this.rows <= 2) throw new IllegalStateException("TSP on 0, 1 or 2 nodes doesn't make sense.");
        if (this.rows != this.distanceArray[0].length) throw new IllegalArgumentException("Matrix must be square (N x N)");
        if (this.startCity < 0 || this.startCity >= this.rows)
            throw new IllegalArgumentException("Starting node must be: 0 <= startNode < N");
        if (this.rows > 32)
            throw new IllegalArgumentException("Matrix too large! A matrix that size for the DP TSP problem with a time complexity of" +
                    "O(n^2*2^n) requires way too much computation for any modern home computer to handle");
		
	}
	@Override
	public void onReceive(Object msg) throws Throwable {
		
		//Code to implement
		if (msg instanceof String) {
			String message = (String) msg;
			if (message.equals("StartSearching")) {
				SolutionMessage sol = search();
				//local solution:
				this.solution = sol;
				sol.setAgentName(getSelf().path().name());
				getSender().tell(sol, getSelf());
				
			} 
		} else if (msg instanceof SolutionMessage) {
				//global solution:
				SolutionMessage sol = (SolutionMessage) msg;
	            String name = sol.getAgentName();
	            //double minTourCost = sol.getMinTourCost();
	            
	            if (sol.getMinTourCost() > this.maxLength) {
	                System.out.println(getSelf().path().name() + " Message: " + "Not able to find Path with length less than desired Length");
	                context().stop(getSelf());
	                return;
	            }
	            if (name.equals(getSelf().path().name())) {
	                System.out.println(getSelf().path().name() + " Path: " + this.solution.getTour() + " Length: " + (int) this.solution.getMinTourCost() + " Message: " + "I Won");
	            } else {
	                System.out.println(getSelf().path().name() + " Path: " + this.solution.getTour() + " Length: " + (int) this.solution.getMinTourCost() + " Message: " + name + " won");
	            }
	            context().stop(getSelf());
	      }
	}
	
	public SolutionMessage search() {
		int state = 1 << this.startCity;
        Double[][] memo = new Double[rows][1 << rows];
        Integer[][] prev = new Integer[rows][1 << rows];
        minTourCost = tsp(startCity, state, memo, prev);
        // Regenerate path
        int index = startCity;
        while (true) {
            //tour.add((int) distance[0][index]);
            tour.add(index);
            Integer nextIndex = prev[index][state];
            if (nextIndex == null) break;
            int nextState = state | (1 << nextIndex);
            state = nextState;
            index = nextIndex;
        }
        //tour.add((int) distance[0][START_NODE]);
        tour.add(startCity);
        //ranSolver = true;
        return new SolutionMessage(minTourCost, tour);
	}
    private double tsp(int i, int state, Double[][] memo, Integer[][] prev) {
        // Done this tour. Return cost of going back to start node.
        if (state == FINISHED_STATE) return distanceArray[i][startCity];
        // Return cached answer if already computed.
        if (memo[i][state] != null) return memo[i][state];
        double minCost = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int next = 0; next < rows; next++) {
            // Skip if the next node has already been visited.
            if ((state & (1 << next)) != 0) continue;
            int nextState = state | (1 << next);
            double newCost = distanceArray[i][next] + tsp(next, nextState, memo, prev);
            if (newCost < minCost) {
                minCost = newCost;
                index = next;
            }
        }
        prev[i][state] = index;
        return memo[i][state] = minCost;
    }
}
