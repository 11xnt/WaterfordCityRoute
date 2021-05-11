package System;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchLogic <T> {

    public Node<T> start;
    public Node<T> end;
    public List<Node<T>> path;


    public SearchLogic(Node<T> start, Node<T> end){
        this.start = start;
        this.end = end;
    }

    //Recursive depth-first search of graph (all paths identified returned)
    public static ArrayList<ArrayList<Integer>> findAllPathsDepthFirst(int from, int[] encountered, int lookingFor) {
        ArrayList<ArrayList<Integer>> result = null, temp2;
        if(encountered[from] == encountered[lookingFor]) { //Found it
            ArrayList<Integer> temp=new ArrayList<>(); //Create new single solution path list
            temp.add(from); //Add current node to the new single path list
            result=new ArrayList<>(); //Create new "list of lists" to store path permutations
            result.add(temp); //Add the new single path list to the path permutations list
            return result; //Return the path permutations list
        }


       //if(encountered==null) encountered =new ArrayList(); //First node so create new (empty) encountered list
        encountered[from] = from; //Add current node to encountered list
        for(int i = 0;i < encountered.length;i++){
            if(encountered[i] != lookingFor) {
                temp2=findAllPathsDepthFirst(i ,encountered,lookingFor); //Use clone of encountered list
                //for recursive call!
                if(temp2!=null) { //Result of the recursive call contains one or more paths to the solution node
                    for(int j = 0; j < encountered.length; j++) //For each partial path list returned
                        encountered[0] = from; //Add the current node to the front of each path list
                    if(result==null) result=temp2; //If this is the first set of solution paths found use it as the result
                    else result.addAll(temp2); //Otherwise append them to the previously found paths
                }
            }
        }
        return result;
    }


    public static <T> CostedPath findCheapestPathDijkstra(Node<?> start, T lookingfor) {
        CostedPath cp = new CostedPath(); //Create result object for cheapest path
        List<Node<?>> encountered = new ArrayList<>(), unencountered = new ArrayList<>(); //Create encountered/unencountered lists
        start.setNodeValue(0); //Set the starting node value to zero
        unencountered.add(start); //Add the start node as the only value in the unencountered list to start
        Node<?> currentNode;
        do { //Loop until unencountered list is empty
            currentNode = unencountered.remove(0); //Get the first unencountered node (sorted list, so will have lowest value)
            encountered.add(currentNode); //Record current node in encountered list
            if (currentNode.getData().equals(lookingfor)) { //Found goal - assemble path list back to start and return it
                cp.getPathList().add(currentNode); //Add the current (goal) node to the result list (only element)
                cp.setPathCost(currentNode.getNodeValue()); //The total cheapest path cost is the node value of the current/goal node
                while (currentNode != start) { //While we're not back to the start node...
                    boolean foundPrevPathNode = false; //Use a flag to identify when the previous path node is identified
                    for (Node<?> n : encountered) { //For each node in the encountered list...
                        for (Link e : n.getAdjList()) //For each edge from that node...
                            if (e.getDestNode() == currentNode && currentNode.getNodeValue() - e.getCost() == n.getNodeValue()) { //If that edge links to the
//current node and the difference in node values is the cost of the edge -> found path node!
                                cp.getPathList().add(0, n); //Add the identified path node to the front of the result list
                                currentNode = n; //Move the currentNode reference back to the identified path node
                                foundPrevPathNode = true; //Set the flag to break the outer loop
                                break; //We've found the correct previous path node and moved the currentNode reference
//back to it so break the inner loop
                            }
                        if (foundPrevPathNode)
                            break; //We've identified the previous path node, so break the inner loop to continue
                    }
                }
//Reset the node values for all nodes to (effectively) infinity so we can search again (leave no footprint!)
                for (Node<?> n : encountered) n.setNodeValue(Integer.MAX_VALUE);
                for (Node<?> n : unencountered) n.setNodeValue(Integer.MAX_VALUE);
                return cp; //The costed (cheapest) path has been assembled, so return it!
            }
//We're not at the goal node yet, so...
            for (Link e : currentNode.getAdjList()) //For each edge/link from the current node...
                if (!encountered.contains(e.getDestNode())) { //If the node it leads to has not yet been encountered (i.e. processed)
                    e.getDestNode().setNodeValue(Integer.min(e.getDestNode().getNodeValue(), currentNode.getNodeValue() + e.getCost())); //Update the node value at the end
                    //of the edge to the minimum of its current value or the total of the current node's value plus the cost of the edge
                    unencountered.add(e.getDestNode());
                }
            Collections.sort(unencountered, (n1, n2) -> n1.nodeValue - n2.nodeValue); //Sort in ascending node value order
        } while (!unencountered.isEmpty());
        return null; //No path found, so return null
    }


//    //Getter/Setter methods
    public Node<T> getStart() {
        return start;
    }

    public void setStart(Node<T> start) {
        this.start = start;
    }

    public Node<T> getEnd() {
        return end;
    }

    public void setEnd(Node<T> end) {
        this.end = end;
    }

    public List<Node<T>> getPath() {
        return path;
    }

    public void setPath(List<Node<T>> path) {
        this.path = path;
    }
}
