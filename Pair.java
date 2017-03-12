import java.util.LinkedList;

/**
 * A pair linking a route through an 11 piece puzzle with an 
 * integer ranking.
 * 
 * @author Luke Carroll 
 * @version 17/2/2017
 */
public class Pair implements Comparable<Pair>
{
    private int rank;
    private LinkedList<String> route;
    
    /**
     * Constructor for Pair class using a given route through an 11 piece
     * puzzle and an integer ranking.
     * @param rank The integer rank of the given route.
     * @param route A route through a 
     */
    Pair(int rank, LinkedList<String> route)
    {
        this.rank = rank;
        this.route = route;
    }    
    
    /**
     * Returns the integer ranking.
     * @return The integer ranking
     */
    public int getRank()
    {
        return rank;
    }

    /**
     * Returns the route through the 11 piece puzzle.
     * @return The puzzle route
     */
    public LinkedList<String> getRoute()
    {
        return route;
    }

    /**
     * Compares 2 pair object to see which one has a higher rank.
     * @param pair The pair to be compared with
     * @return 1 if this pair is ranked higher, -1 if it is ranked
     * lower or 0 for any other case.
     */
    public int compareTo(Pair pair)
    {
        if (rank > pair.getRank()) return 1;
        else if (rank < pair.getRank()) return -1;
        else return 0;
    }
}