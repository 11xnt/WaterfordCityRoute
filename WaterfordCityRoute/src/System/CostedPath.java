package System;

import java.util.ArrayList;
import java.util.List;

public class CostedPath{

    public int pathCost=0;
    public List<Node<?>> pathList=new ArrayList<>();

    public int getPathCost() {
        return pathCost;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }

    public List<Node<?>> getPathList() {
        return pathList;
    }

    public void setPathList(List<Node<?>> pathList) {
        this.pathList = pathList;
    }
}

