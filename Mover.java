import java.util.LinkedList;
import java.util.Iterator;
import java.nio.file.Files;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.lang.Math;
import java.util.ArrayList;

/**
 * A class for moving examples of the "11 piece puzzle" from one state to another.
 * This class will also find solutions to the "11 piece puzzle" tasks
 * and save said solutions in txt files.
 * 
 * @author Luke Carroll 
 * @version 26/02/2017
 */
public class Mover
{
    /**
     * Constructor for Mover clas.
     */
    public Mover()
    {
    }

    public static void main(String args[])
    {
        saveRoute(aStar("+dd+b_cb+dda+abd","+_a+bddd+dba+dbc"));
        saveRoute(aStar("+dd+bdab+ddc+ab_","+ad+_bda+dbd+dcb"));
        saveRoute(aStar("+ba+bdad+bdd+_dc","+bd+badd+a_b+cdd"));
        saveRoute(aStar("+dd+bbda+bcd+a_d","+bd+bbdd+aac+d_d"));
        saveRoute(aStar("+bd+bcd_+dda+dba","+bd+_bcd+dba+dad"));
        saveRoute(aStar("+da+bdad+d_d+cbb","+da+bdad+dbd+cb_"));
        saveRoute(aStar("+da+bdba+d_d+bdc","+bd+ba_a+ddc+bdd"));
        saveRoute(aStar("+bd+bbdd+aac+dd_","+da+bbdd+d_a+cbd"));
        saveRoute(aStar("+ab+bdcd+b_a+ddd","+bd+bacd+da_+bdd"));
        saveRoute(aStar("+ad+bd_d+bba+dcd","+ad+bdba+dbd+d_c"));
        saveRoute(aStar("+_b+bdad+dad+cdb","+dd+bdba+bda+c_d"));
        saveRoute(aStar("+_d+bbcd+baa+ddd","+_a+bdbd+cab+ddd"));
        saveRoute(aStar("+_d+bbdd+daa+cdb","+ba+bd_d+bcd+add"));
        saveRoute(aStar("+dc+b_dd+bad+bad","+dd+bd_a+cda+dbb"));
        saveRoute(aStar("+ad+bddd+da_+bbc","+ad+b_dd+dab+dbc"));
        saveRoute(aStar("+ad+bd_c+bdd+bad","+ad+b_dc+bdd+bda"));
    }

    /**
     * Method for moving empty space (represented by '_') to the left.
     * @param start The starting state of the puzzle.
     * @return The state of the puzzle after _ has moved left, or null if this is not possible.
     */
    private static String moveLeft(String start)
    {
        String begin = start;
        int pos = begin.indexOf('_');
        if(pos%4>0){
            String dest = begin.substring(0, pos-1)+"_"+begin.charAt(pos-1)+begin.substring(pos+1);
            return dest;
        }
        else{
            return null;
        }
    }

    /**
     * Method for moving empty space (represented by '_') to the right.
     * @param start The starting state of the puzzle.
     * @return The state of the puzzle after _ has moved right, or null if this is not possible.
     */    
    private static String moveRight(String start)
    {
        String begin = start;
        int pos = begin.indexOf('_');
        if(pos%4<3){
            String dest = begin.substring(0, pos)+begin.charAt(pos+1)+"_"+begin.substring(pos+2);
            return dest;
        }
        else{
            return null;
        }
    }

    /**
     * Method for moving empty space (represented by '_') down.
     * @param start The starting state of the puzzle.
     * @return The state of the puzzle after _ has moved down, or null if this is not possible.
     */
    private static String moveDown(String start)
    {
        String begin = start;
        int pos = begin.indexOf('_');
        if(pos<begin.length()-4){      
            String dest = begin.substring(0, pos)+begin.charAt(pos+4)+begin.substring(pos+1, pos+4) + "_" 
                +begin.substring(pos+5);
            return dest;
        }
        else{
            return null;
        }
    }

    /**
     * Method for moving empty space (represented by '_') up.
     * @param start The starting state of the puzzle.
     * @return The state of the puzzle after _ has moved up, or null if this is not possible.
     */    
    private static String moveUp(String start)
    {
        String begin = start;
        int pos = begin.indexOf('_');
        if(pos>3){
            String dest = begin.substring(0, pos-4)+"_"+begin.substring(pos-3, pos)+begin.charAt(pos-4)
                +begin.substring(pos+1);
            return dest;
        }
        else{
            return null;
        }
    }

    /**
     * Tests if a configuration has a valid arrangment of '+' symbols.
     * @param sample The configuration to be tested.
     * @return true if the configuration is valid, false otherwise.
     */
    private static boolean isValid(String sample)
    {
        if(sample==null || sample.length()<16){
            return false;
        }
        else if(sample.charAt(0)=='+' && sample.charAt(3)=='+'
        && sample.charAt(8)=='+' && sample.charAt(12)=='+')
        {
            return true;
        }
        else return false;
    }

    /**
     * Generates all the configurations adjacent to the entered starting configuration.
     * @param sample The starting configuration who's neighbours the method will find.
     * @return All of the configurations adjacent to the starting configuration.
     */
    private static LinkedList<String> nextConfig(String start)
    {
        LinkedList<String> result = new LinkedList<String>();
        String left = moveLeft(start);
        String right = moveRight(start);
        String down = moveDown(start);
        String up = moveUp(start);
        if(isValid(left)) result.add(left);
        if(isValid(right)) result.add(right);
        if(isValid(down)) result.add(down);
        if(isValid(up)) result.add(up);
        return result;
    }

    /**
     * If it exists will find a complete route, up to a given depth, to a destination,
     * with the complete route beginning with an entered initial route. This search
     * only finds routes without repetition.
     * @param route The starting route which a completed route would be built on.
     * @param dest The destination configuration string.
     * @param depth The depth the completed route must be.
     * @return The completed route, or null if an acceptable route does not exist.
     */
    private static LinkedList<String> depthFirstDevaVu(LinkedList<String> route,
    String dest, int depth)
    {
        if (depth == 0) return null;
        String last = route.getLast();
        if (last.equals(dest)) return route;
        else
        {
            LinkedList<String> nextStrings = nextConfig(last);
            for (String next:nextStrings)
            {
                if (!route.contains(next))
                {
                    LinkedList<String> nextRoute = (LinkedList<String>) route.clone();
                    nextRoute.add(next);
                    LinkedList<String> wholeRoute =
                        depthFirstDevaVu(nextRoute, dest, depth - 1);
                    if (wholeRoute != null) return wholeRoute;
                }
            }
        }
        return null;
    }   

    /**
     * If a route exists this method will return an optimal route between 2 configurations. 
     * If no route exists the method will run indefinitely.
     * @param start The starting configuration
     * @param dest The destination configuration.
     * @return An optimal route between 
     */
    private static LinkedList<String> iterativeDeepening(String start, String dest)
    {
        for (int depth = 1; true; depth++)
        {
            LinkedList<String> startroute = new LinkedList<String>();
            startroute.add(start);            
            System.out.println(depth);
            LinkedList<String> route = depthFirstDevaVu(startroute, dest, depth);
            if (route != null) return route;
        }
    }

    /**
     * Takes a route represented as a LinkedList of strings and converts it to an ArrayList
     * which helps printing the route in matrix format.
     * @param route The input route
     * @return The ArrayList which can be used to print the route in matrix format.
     */
    private static ArrayList routeArray(LinkedList<String> route)
    {
        ArrayList<String> matrixroute = new ArrayList<String>();

        Iterator<String> it = route.iterator();
        String first = "";
        while(it.hasNext()){
            String state = it.next();
            if(state.length()==16){
                first+=state.substring(0,4)+" ";
            }
        }  
        String firstline = first.substring(0,first.length()-1);

        Iterator<String> it2 = route.iterator();
        String second = "";
        while(it2.hasNext()){
            String state = it2.next();
            if(state.length()==16){
                second+=state.substring(4,8)+" ";
            }
        }  
        String secondline = second.substring(0,second.length()-1);  

        Iterator<String> it3 = route.iterator();
        String third = "";
        while(it3.hasNext()){
            String state = it3.next();
            if(state.length()==16){
                third+=state.substring(8,12)+" ";
            }
        }  
        String thirdline = third.substring(0,third.length()-1);  

        Iterator<String> it4 = route.iterator();
        String fourth = "";
        while(it4.hasNext()){
            String state = it4.next();
            if(state.length()==16){
                fourth+=state.substring(12,16)+" ";
            }
        }  
        String fourthline = fourth.substring(0,fourth.length()-1);   

        matrixroute.add(firstline);
        matrixroute.add(secondline);
        matrixroute.add(thirdline);
        matrixroute.add(fourthline);

        return matrixroute;
    }

    /**
     * Finds a lower bound for the distance between the entered 'start' configuration
     * and the entered 'end' destination. If the 2 configurations have 'n' characters
     * that are different then this method will return n-1.
     * 
     * To prove that this is an admissible heuristic I shall refer to a particular
     * 'slot' in a configuration, e.g. the 3rd character slot of the string, as a
     * cell.
     * 
     * Every cell that has a 'wrong' character in it needs '_' to be in that cell
     * and move out of it. Therefore, every wrong cell requires at least 1 move
     * to make it 'right'. The one exception is the final destination of '_'. 
     * At the end of its path '_' should end up in this terminal cell which 
     * will be achieved by '_' moving out of a different cell. Therefore, given
     * that the start and end have 'n' cells which are different a minimum of
     * n-1 moves will be required to move from the start to the end.
     * 
     * If '_' starts in the position it is supposed to end in then this case
     * can be reduced to the case above. Given that '_' starts in its final 
     * destination and there are 'n' many 'wrong' cells the best move that could 
     * possibly be made in the shortest possible would result '_' swapping place
     * with a cell which already has a wrong character in it. Therefore, we have
     * moved to the case described above but with n+1 'wrong' cells. So on top
     * of the first move that was made an additional 'n' moves are required as
     * a minimum.
     * 
     * Therefore given that 'start' and 'end' have 'n' many cells that are 
     * different a minimum of n-1 moves will be required to move from start
     * to end. Therefore, this method returns an admissible heuristic.
     * 
     * @param start The starting configuration string
     * @param end The destination configuration string
     * @return The lower bound of the integer distance between start and end.
     */
    private static int estimateDistance(String start, String end)
    {
        int length = Math.min(start.length(), end.length());
        int count = 0;
        for(int i=0; i<length; i++){
            if(start.charAt(i)!=end.charAt(i)){
                count++;
            }
        }
        return count-1;
    }

    /**
     * Returns the length of an entered route. That is to say the integer number
     * of steps between the start of the given route and the end of said route.
     * @param route The route to be considered.
     * @return The integer length of the given route.
     */
    private static int actualDistance(LinkedList<String> route)
    {
        return route.size();
    }

    /**
     * Returns a LinkedList of strings representing an optimal route between a starting
     * configuration of an 11 piece puzzle and a given end state. Should return
     * null if no such route exists.
     * @param start The starting configuration
     * @param end The final destination
     * @return The LinkedList representation of an optimal route between start and end
     * or null if no such route exists.
     */    
    public static LinkedList<String> aStar(String start, String end)
    {
        LinkedList<String> route = new LinkedList<String>();
        route.add(start);
        PriorityQueue pairs = new PriorityQueue();
        pairs.add(new Pair(estimateDistance(start, end), route));
        while (true)
        {
            if (pairs.size() == 0) return null; // no solutions exist
            Pair pair = (Pair) pairs.poll(); // retrieve and remove (log)
            route = pair.getRoute();
            String last = route.getLast();
            if (last.equals(end)) return route; // exit loop with solution
            LinkedList<String> nextStates = nextConfig(last);
            for (String next:nextStates){
                if (!route.contains(next)){ //prevents repetion
                    LinkedList<String> nextRoute = new LinkedList<String>(route);
                    nextRoute.addLast(next);
                    int distance = actualDistance(nextRoute);
                    distance += estimateDistance(next, end);
                    pairs.add(new Pair(distance, nextRoute)); // log too
                }
            }
        }
    }

    /**
     * Prints a given route in matrix format to the terminal.
     * @param route The route to be printed.
     */
    public static void printRoute(LinkedList<String> route)
    {
        ArrayList<String> matrixroute = routeArray(route);

        System.out.println(matrixroute.get(0));
        System.out.println(matrixroute.get(1));
        System.out.println(matrixroute.get(2));
        System.out.println(matrixroute.get(3));          
    }

    /**
     * Saves a given route in matrix format to a txt file.
     * @param route The route to be printed.
     */    
    public static void saveRoute(LinkedList<String> route)
    {
        ArrayList<String> matrixroute = routeArray(route);
        String start = route.getFirst();
        String dest = route.getLast();

        String title = start + "2"+ dest;
        try{
            File file = new File(".\\" + title + ".txt");
            PrintWriter output = new PrintWriter(file);
            output.println(matrixroute.get(0));
            output.println(matrixroute.get(1));
            output.println(matrixroute.get(2));
            output.print(matrixroute.get(3));
            output.close();
        } catch (IOException e) {
            // perform no action
        }          
    }
}
