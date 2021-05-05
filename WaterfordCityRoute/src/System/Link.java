package System;

public class Link {

    public Node<?> destNode; //Could also store source node if required
    public int cost; //Other link attributes could be similarly stored

    public Link(Node<?> destNode, int cost) {
        this.destNode=destNode;
        this.cost=cost;
    }

    public Node<?> getDestNode() {
        return destNode;
    }

    public void setDestNode(Node<?> destNode) {
        this.destNode = destNode;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
