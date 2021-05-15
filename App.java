import java.io.*;
import java.util.*;



public class App {

                                                /* Global variables. */
    
    /* ArrayList with all permutations (without repetition). */
    static ArrayList<int[]> allPermutations = new ArrayList<>();

    /* The permutation itself. */
    static int[] permutation;

    /* How many cycles we got through permutation. */
    static int cyclesPermutation = 0;

    /* How many cycles we got through walking. */
    static int cyclesWalk = 0;


    public static void main(String[] args) throws IOException {
        Scanner read = new Scanner(System.in);
        
        File file = new File("graph.txt");

        /* FileReader is a readable method. */
        FileReader fr = new FileReader(file);
        /* BufferedReader is a method that allows you to read line by line until a \n is found. */
        BufferedReader br = new BufferedReader(fr);


        String line = " ";


        line = br.readLine();
        int vertices = Integer.parseInt(line);

        line = br.readLine();        

        
        byte[][] graph = new byte[vertices][vertices];

        adjacencyMatrix(graph, vertices, line, br);

        printGraphs(read, graph, vertices);


        getCyclesThroughPermutation(graph, vertices);
        System.out.println("\nCycles through permutation: " + cyclesPermutation);


        getCyclesThroughWalks(graph, vertices);
        System.out.println("\nCycles through walks: " + cyclesWalk/2);

        read.close();
    }


    //* PERMUTATION
    public static void getCyclesThroughPermutation(byte[][] graph, int vertices){
    
        int allVertices[] = new int[vertices];

        /* Assigning values for the vertices array (counting that the vertices are 
        in ascending order). */
        int value = 0;
        for (int i = 0; i < vertices; i++){
            value++;
            allVertices[i] = value;
        } 

        /* Here we control the permutations length. Now it's 3, so the permutations
        will have length 3 (123, 124, 125...). After, we'll increase to 4, 5 (1234, 12345...). 
        
        We start the index with value 3, bc a cycle needs AT LEAST 3 vertices/points.
        If the graph has 4 vertices, we'll do permutations with 3 values (xxx, xxy) AND 
        4 values (xxxx, xxxy). But if the graph has 5 vertices, we'll do the permutations 
        with 3 (xxx), 4 (xxxx) and 5 (xxxxx) values. */
        for (int i = 3; i <= vertices; i++){
            permutation = new int[i];
            permute(allVertices, 0, i);
        }

        for (int[] p : allPermutations) {
            if (checkIfPermutationIsCycle(p, graph) == true){
                cyclesPermutation++;
            }
        }
    }

	public static void permute(int[] vertices, int n, int index) {

        /* If the given number is equal to the permutation array length: */
		if (n == permutation.length) {

            int[] sortedPermutation = permutation.clone();
            Arrays.sort(sortedPermutation);


            /* If the arraylist of all permutations DOES NOT contains the sorted permutation, 
            we'll add the clone of this permutation. */
            boolean contains = false;

            /* BUT, when we're talking about a permutation with the max number of vertices, 
            the things are a little bit different. We'll check if the first vertice of 
            a cicle of a existing permutation on the list if equal to the last vertice of 
            the cicle of the current permutation.        
            The variable close will check if the permutations are inverted, and if it isn't, we 
            can use the permutation. */
            boolean mirrored = false;


            for (int p = 0; p < allPermutations.size(); p++){

                int[] perm = allPermutations.get(p);


                if (Arrays.equals(perm, sortedPermutation)) contains = true;
                mirrored = isMirrored(perm, permutation);
                if (mirrored == true) break;

            }

            if (mirrored == false && contains == false){
                int[] clone = permutation.clone();
                allPermutations.add(clone);
            }

		} else {
			for (int i=0; i < vertices.length; i++) {

                /* Boolean that keeps track if will be repetitions. */
				boolean found = false;

                /* For each index of the permutation array: */
				for (int j = 0; j < n; j++) {
					if (permutation[j]==vertices[i]) found = true;
				}

                /* If there's no equal values: */
				if (!found) {
                    /* The permutation array in the n index will be equal to the vertice. */
					permutation[n] = vertices[i];
                    /* And now, we permute again, but the next index will change. */
					permute(vertices, n+1, index);
				}
            }
		}
	}

    public static boolean isMirrored(int[] perm1, int[] perm2){
        
        int[] mirror = new int [perm1.length];

        /* If we gave 7 vertices, we'll check this:
            
            permutation A                           permutation B
        (permutation on the list)              (the current permutation) 
            [0 1 2 3 4 5 6]                         [0 1 2 3 4 5 6]         [indexes]
            1 2 3 4 5 6 7                           7 6 5 4 3 2 1           [permuation]

        if the A[0] is equal to the B[7 (length) - 1 - 0 (current index)], they'll possibly
        be inverted, and this will apply to the following values of the index loop (for). */
        for (int i = 0; i < perm1.length; i++){
            mirror[i] = perm1[perm1.length-1-i];
        }   

        if (Arrays.equals(mirror, perm2)) return true;
        else return false; 

    }

    public static boolean checkIfPermutationIsCycle(int[] p, byte[][] graph){
        int edgeExists = 0;

        /* For each vertice: */
        for (int i = 0; i < p.length; i++){

            int row = p[i] - 1;
            int column;

            /* If we're analyzating the last vertice, the edge is different. */            
            if (i == p.length - 1){
                column = p[0] - 1;
            } else {
                column = p[i+1] - 1;
            }

            /* If the value in the graph is equal to 1, it means that the edge exists. */
            if (graph[row][column] == 1){
                edgeExists++;
            }
        }

        /* If the counting of the edges is equal to the length of permutation, is a cycle. */
        if (edgeExists == p.length) return true;
        else return false;

    }


    //* WALKING
    public static void getCyclesThroughWalks(byte[][] graph, int vertices){
        
        /* Here we control the path's length. Now it's 3, so the walks
        will have length 3 (123, 124, 125...). After, we'll increase to 4, 5 (1234, 12345...). 
        
        We start the index with value 3, bc a cycle needs AT LEAST 3 vertices/points.
        If the graph has 4 vertices, we'll do walks with 3 values (xxx, xxy) AND 
        4 values (xxxx, xxxy). But if the graph has 5 vertices, we'll do the walks 
        with 3 (xxx), 4 (xxxx) and 5 (xxxxx) values. */
        for (int n = 3; n <= vertices; n++){

            /* All vertices are marked un-visited. */
            boolean marked[] = new boolean[vertices];
                
            for (int i = 0; i < vertices - (n - 1); i++) {
                depthFirstSearch(graph, marked, n - 1, i, i, vertices);
                
                /* Ith vertice is marked as visited and it'll not be visited again. */
                marked[i] = true;
            }  
        }
    }

    public static void depthFirstSearch(byte graph[][], boolean marked[], int n, int vert, int start, int vertices) {
          
        marked[vert] = true;
          
        if (n == 0) {
              
            /* Mark as un-visited to make it usable again. */
            marked[vert] = false;
              
            if (graph[vert][start] == 1) {
                cyclesWalk++;
                return;
            } else return;
        }
          

        for (int i = 0; i < vertices; i++){
            if (!marked[i] && graph[vert][i] == 1){
                depthFirstSerach(graph, marked, n-1, i, start, vertices);
            }
        }
            
        /* Vertice is unvisited to make it usable again.*/ 
        marked[vert] = false;
    }

    
    //* GRAPH REPRESENTATION
    public static void adjacencyMatrix(byte[][] graph, int vertices, String lineRead, BufferedReader br) throws IOException  {

        /* Initializing the matrix with 0's. */
        for (int row = 0; row < vertices; row++){
            for (int column = 0; column < vertices; column++){
                graph[row][column] = 0;
            }
        }

        /* Start reading the edges set. */
        lineRead = br.readLine();
        
        /* If this line isn't null, it means that has a edge value. */
        while(lineRead != null){
            /* This is the edge. */
            String regexEdges = " ";

            /* We'll slice this line in a array of Strings. */
            String[] slicedEdges = lineRead.split(regexEdges);

            /* The first 2 values of this array is equal to some vertice and according to 
            the concept of adjacency matrix, we'll represent the edge from the rows and columns. */
            int row = Integer.parseInt(slicedEdges[0]); 
            int column = Integer.parseInt(slicedEdges[1]);

            graph[row - 1][column - 1] = 1;
            graph[column - 1][row - 1] = 1;

            /* We need to read the next line, so it can go back to the while validation;  */
            lineRead = br.readLine();
        }
    }

    //!View only. 
    public static void printGraphs(Scanner read, byte[][] graph, int vertices){
        String answer = "";

        System.out.println("Do you want to print the representation of the graph? Type y or n.");
        answer = read.nextLine().toLowerCase();

        if (answer.equals("yes") || answer.equals("y")){
            for (int i = 0; i < vertices; i++){
                for (int j = 0; j < vertices; j++){
                    System.out.print(graph[i][j] + "  ");
                }
                System.out.print("\n");
            }
        } else System.out.println("Okay, thanks for visiting. Check out the code!");
    }

}