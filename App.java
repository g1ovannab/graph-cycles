import java.io.*;
import java.util.*;


public class App {

    //private static int cyclesThroughPermutation = 0;
    //private static int cyclesThroughWal = 0;

    public static void main(String[] args) throws IOException {
        Scanner read = new Scanner(System.in);
        
        /* Creating a Object 'File' that stores a file. */
        File file = new File("graph.txt");

        /* FileReader is a readable method. */
        FileReader fr = new FileReader(file);
        /* BufferedReader is a method that allows you to read line by line until a \n is found. */
        BufferedReader br = new BufferedReader(fr);

        /* Line control. */
        String line = " ";

        /* Reading the first line that represents the amount of vertices that this graph has. */
        line = br.readLine();
        int vertices = Integer.parseInt(line);

        /* Reading the first line that represents the amount of edges that this graph has. */
        line = br.readLine();
        //int edges = Integer.parseInt(line);
        

        /* The 'matrix' variable is created with the vertices dimensions. */
        byte[][] matrix = new byte[vertices][vertices];
        /* Calls the method that'll do the matrix representation in the form of matrix. */
        adjacencyMatrix(matrix, vertices, line, br);
        // Só pra visualizar
        printGraphs(read, matrix, vertices);


        /* Counting the cycles through Permutation. */
        getCyclesThroughPermutation(); //(matrix, vertices)

        /* Counting the cycles through Walks. */
        getCyclesThroughWalks();


        read.close();
    }

    public static void getCyclesThroughPermutation(){}

    
    public static void getCyclesThroughWalks(){}



    public static void adjacencyMatrix(byte[][] adjMatrix, int vertices, String lineRead, BufferedReader br) throws IOException  {

        /* Initializing the matrix with 0's. */
        for (int row = 0; row < vertices; row++){
            for (int column = 0; column < vertices; column++){
                adjMatrix[row][column] = 0;
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

            adjMatrix[row - 1][column - 1] = 1;
            adjMatrix[column - 1][row - 1] = 1;

            /* We need to read the next line, so it can go back to the while validation;  */
            lineRead = br.readLine();
        }
    }

    /* 
    ! Not important (view only). 
    */
    public static void printGraphs(Scanner read, byte[][] matrix, int vertices){
        
        /* This variable stores the answer that the user will give (if we'll show the graph 
        representation or not). */
        String answer = "";

        System.out.println("Do you want to print the representation of the graph? Type y or n.");
        answer = read.nextLine().toLowerCase();

        /* If the answer is yes, it'll print one, all of some graphs. */
        if (answer.equals("yes") || answer.equals("y")){

            for (int i = 0; i < vertices; i++){
                for (int j = 0; j < vertices; j++){
                    System.out.print(matrix[i][j] + "  ");
                }
                System.out.print("\n");
            }

        } 
        
        /* If the answer is 'no', it will greet the user. */
        else{
            System.out.println("Okay, thanks for visiting. Check out the code!");
        }
    }

}