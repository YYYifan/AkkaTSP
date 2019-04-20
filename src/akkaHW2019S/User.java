package akkaHW2019S;

import akka.actor.ActorSystem;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Scanner;

/**
 * Main class for your estimation actor system.
 *
 * @author Yifan Zhang
 *
 */
public class User {

	private int startCity;
	private int maxLength;
	private double[][] distanceArray;
	
	public static void main(String[] args) throws Exception {
		User user = new User();
		//ActorSystem system = ActorSystem.create("EstimationSystem");
		
		/*
		 * Create the Solver Actor and send it the StartProcessing
		 * message. Once you get back the response, use it to print the result.
		 * Remember, there is only one actor directly under the ActorSystem.
		 * Also, do not forget to shutdown the actorsystem
		 */
		
		user.userInput();
		ActorSystem system = ActorSystem.create("EstimationSystem");
        Props solverProps = Props.create(Solver.class, user.startCity, user.maxLength, user.distanceArray);
        // Props propsSearcher = Props.create(Solver.class, 0citiesArray);
        ActorRef searcherActor = system.actorOf(solverProps, "searcherActor");
        searcherActor.tell("StartSimulation", null);

	}
	
	public void userInput() {
		Scanner myScan = new Scanner(System.in);
		System.out.println("Please enter file Name as input: Ex: \"cities\"");
		String fileName = myScan.nextLine();
		
		System.out.println("Please enter the start city node:");
		this.startCity = myScan.nextInt();
		
		System.out.println("Please enter the max path length:");
		this.maxLength = myScan.nextInt();
		
		this.distanceArray = getDistanceArray(fileName);
	}
	
	public double[][] getDistanceArray(String fileName) {
		String file = fileName + ".txt";
		File myFile = new File(file);
//		if (!myFile.exists()) {
//			System.out.println("File does not exist. Please enter valid file name");
//			return null;
//		}
		Scanner arrayScan = null;
        try {
        	arrayScan = new Scanner(new BufferedReader(new FileReader(myFile)));
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist. Please enter valid file name");
        }
        
        int index = 0;
        while (arrayScan.hasNextLine()) {
        	arrayScan.nextLine();
        	index++;
        }
        
        double[][] myArray = new double[index][index];
        try {
        	arrayScan = new Scanner(new BufferedReader(new FileReader(myFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < myArray.length; i++) {
        	String[] ss = arrayScan.nextLine().split(" ");
        	if (ss.length != index) {
                System.out.println("Not a square matrix, Please provide valid file!");
                return null;
        	}
        	for (int j=0; j < ss.length; j++)
        		myArray[i][j] = Integer.parseInt(ss[j]);
        }
        if (this.startCity > index - 1) {
            System.out.println("Please enter valid city index");
            return null;
        }
        
        System.out.println();
        System.out.println("---------------------------------");
        System.out.println("The input distance metrix is:");
        System.out.println("---------------------------------");
        System.out.println();
        for (int i = 0; i < myArray.length; i++) {
            for (int j = 0; j < myArray[i].length; j++) {
                System.out.print(myArray[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("---------------------------------");
        System.out.println();
        return myArray;
	}

}
