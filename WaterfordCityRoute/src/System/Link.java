package System;

public class Link {

    public Node<?> destNode; //Could also store source node if required
    public int cost; //Other link attributes could be similarly stored

    public Link(Node<?> destNode, int cost) {
        this.destNode=destNode;
        this.cost=cost;
    }


}
