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
    //Agenda list based breadth-first graph traversal (tail recursive)
//    public static void traverseGraphBreadthFirst(List<Node<?>> agenda, List<Node<?>> encountered ){
//        if(agenda.isEmpty()) return; // if agenda is empty -> return nothing.
//        Node<?> next=agenda.remove(0);
//        System.out.println(next.data);
//        if(encountered==null) encountered=new ArrayList<>(); //First node so create new (empty) encountered list
//        encountered.add(next);
//        for(Node<?> adjNode : next.adjList)
//            if(!encountered.contains(adjNode) && !agenda.contains(adjNode)) agenda.add(adjNode); //Add children to end of agenda
//        traverseGraphBreadthFirst(agenda, encountered ); //Tail call
//    }

//    //Interface method to allow just the starting node and the goal node data to match to be specified
//    public static <T> List<Node<?>> findPathBreadthFirst(Node<?> startNode, T lookingfor){
//        List<List<Node<?>>> agenda=new ArrayList<>(); //Agenda comprised of path lists here!
//        List<Node<?>> firstAgendaPath=new ArrayList<>();
//        List<Node<?>> resultPath;
//        firstAgendaPath.add(startNode);
//        agenda.add(firstAgendaPath);
//        resultPath=findPathBreadthFirst(agenda,null,lookingfor); //Get single BFS path (will be shortest)
//        Collections.reverse(resultPath); //Reverse path (currently has the goal node as the first item)
//        return resultPath;
//    }

//    //Agenda list based breadth-first graph search returning a single reversed path (tail recursive)
//    public static <T> List<Node<?>> findPathBreadthFirst(List<List<Node<?>>> agenda,
//                                                                List<Node<?>> encountered, T lookingfor){
//        if(agenda.isEmpty()) return null; //Search failed
//        List<Node<?>> nextPath=agenda.remove(0); //Get first item (next path to consider) off agenda
//        Node<?> currentNode=nextPath.get(0); //The first item in the next path is the current node
//        if(currentNode.data.equals(lookingfor)) return nextPath; //If that's the goal, we've found our path (so return it)
//        if(encountered==null) encountered=new ArrayList<Node<?>>(); //First node considered in search so create new (empty) encountered list
//        encountered.add(currentNode); //Record current node as encountered so it isn't revisited again
//        for(Node<?> adjNode : currentNode.adjList) //For each adjacent node
//            if(!encountered.contains(adjNode)) { //If it hasn't already been encountered
//                List<Node<?>> newPath=new ArrayList<>(nextPath); //Create a new path list as a copy of
////the current/next path
//                newPath.add(0,adjNode); //And add the adjacent node to the front of the new copy
//                agenda.add(newPath); //Add the new path to the end of agenda (end->BFS!)\
//            }
//        return findPathBreadthFirst(agenda,encountered,lookingfor); //Tail call
//    }





//    public static class CostedPath{
//        public int pathCost=0;
//        public List<Node<?>> pathList=new ArrayList<>();
//    }

    /*
    This is the code from notes.
    Need to be adjusted
     */


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
