package akkaHW2019S;

import java.io.File;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import akka.actor.ActorRef;
import akka.actor.DeadLetter;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.sysmsg.Terminate;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * This is the main actor and the only actor that is created directly under the
 * {@code ActorSystem} This actor creates 4 child actors
 * {@code Searcher}
 * 
 * @author Yifan Zhang
 *
 */
public class Solver extends UntypedActor {
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	private int startCity;
	private int maxLength;
	private double[][] distanceArray;
	
	private int counter = 4;
	private boolean solutionSubmitted = false;
	
	public Solver(int startCity, int maxLength, double[][] distanceArray) {
        this.startCity = startCity;
        this.maxLength = maxLength;
        this.distanceArray = distanceArray;
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		
		//Code to implement
		if (msg instanceof String) {
			String mes = (String) msg;
			if (mes.equals("StartSimulation")) {
				Props searcherProps = Props.create(Searcher.class, this.startCity, 
						this.maxLength, this.distanceArray);
				List<ActorRef> actors = new ArrayList<>();
				for (int i = 1; i < 5; i++) {
					ActorRef searcherActor = getContext().actorOf(searcherProps, "Agent" + i);
					actors.add(searcherActor);
				}
				for (ActorRef a : getContext().getChildren())
					a.tell("StartSearching", getSelf());
			}
			
		} else if (msg instanceof SolutionMessage) {
			counter = counter - 1;
			if (!this.solutionSubmitted) {
				SolutionMessage solution = (SolutionMessage) msg;
		        //System.out.println();
		        System.out.println("---------------------------------");
		        System.out.println("All agents paths and messages:");
		        System.out.println("---------------------------------");
		        System.out.println();
		        
		        this.solutionSubmitted = true;
		        for (ActorRef a : getContext().getChildren()) {
		        	a.tell(solution, getSelf());
		        }
			}
	        if (counter == 0) {
	        	context().stop(getSelf());
	        	context().system().terminate();
	        }
		}

	}

}
