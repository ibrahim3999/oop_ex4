package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public class DWG implements DirectedWeightedGraph {
    private HashMap<String , EdgeData>jsonEdge;
    private HashMap<Integer , NodeData>jsoNode;
    public HashMap<Integer,HashMap<Integer,EdgeData>>jsonIn;
    public HashMap<Integer,HashMap<Integer,EdgeData>>jsonOut;
    public HashMap<Integer , Integer>jsonMc;
    NodeData nodeData;
     public HashMap<Integer, NodeData> NodesHash;
     public HashMap<Integer,HashMap<Integer,EdgeData>>EdgesHash;
     int MC;

    public DWG(String json)
    {   jsonMc = new HashMap<>();
        jsonIn = new HashMap<>();
        jsonOut = new HashMap<>();
        jsonEdge = new HashMap<>();
        jsoNode = new HashMap<>();
       // this.nodeData=null;
       //this.NodesHash=new HashMap<Integer, NodeData> ();
       //this.EdgesHash=new HashMap<Integer,HashMap<Integer,EdgeData>>();
        this.MC=0;

        JsonObject jsonObject = new JsonObject(json);
        JsonArray nArr = jsonObject.get("Nodes").getAsJsonArray();
        JsonArray eArr = jsonObject.get("Edges").getAsJsonArray();
        for (JsonElement N : nArr ) {

            JsonObject Jn = N.getAsJsonObject();
            int key = Jn.get("id").getAsInt();
            String pos = Jn.get("pos").getAsString();

            String [] posArr = pos.split(",");
            double x = Double.parseDouble(posArr[0]);
            double y = Double.parseDouble(posArr[1]);
            double z = Double.parseDouble(posArr[2]);
            impNodeData temp = new impNodeData(key, new impGeoLocation(x,y,z), 0,"",0);
            addNode(temp);
        }
        for (JsonElement E : eArr ) {
            JsonObject Je = E.getAsJsonObject();
            int src = Je.get("src").getAsInt();
            int dest = Je.get("dest").getAsInt();
            double weight= Je.get("w").getAsDouble();
            connect(src, dest, weight);
        }

    }


    @Override
    public NodeData getNode(int key) {
        return NodesHash.get(key);
    }
    @Override
    public EdgeData getEdge(int src, int dest) {
        if(NodesHash.containsKey(src) && NodesHash.containsKey(dest) &&EdgesHash.get(src).containsKey(dest))
            return EdgesHash.get(src).get(dest);
        return null;
    }
    @Override
    public void addNode(NodeData n) {
        if(NodesHash.containsKey(n.getKey()))
        {
            System.err.println("ADD IS FAIL");
        }
        NodesHash.put(n.getKey(),n);
        EdgesHash.put(n.getKey(),new HashMap<Integer,EdgeData>());
        MC++;
    }
    public void forEachRemaining(Consumer<? super EdgeData> action)
    {
        iter.forEachRemaining(action);
    }



    @Override
    public void connect(int src, int dest, double w) {
    impEdgeData e=new impEdgeData(src,dest,w);
    if(NodesHash.containsKey(src)&& NodesHash.containsKey(dest) && w>0)
        EdgesHash.get(src).put(dest,e);
    else {
        System.err.println("CONNECT FAIL\n");
        return;
    }
    MC++;
    }
    public void forEachRemaining(Consumer<? super Edge> action)
    {
        iter.forEachRemaining(action);
    }
}
    public SpecificNodesIterator(int node_id)
    {
        id = node_id;
        iter = outEdges.get(node_id).values().iterator();
        mc = changes.get(node_id);
    }
    public boolean hasNext()
    {
        if (mc == changes.get(id))
            return iter.hasNext();
        else
            throw new RuntimeException();
    }
    public Edge next()
    {
        if (mc == changes.get(id))
            return iter.next();
        else
            throw new RuntimeException();
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        return this.NodesHash.values().iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        return (Iterator)this.EdgesHash.values().iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        return null;
    }

    @Override
    public NodeData removeNode(int key) {
        if(NodesHash.containsKey(key)) {
            NodesHash.remove(key);
            Set<Integer> Keys=EdgesHash.keySet();
            for(Integer e:Keys)
            {
                if(EdgesHash.get((int)e).containsKey(key))
                    EdgesHash.get((int)e).remove(key);
            }
        }
        else {
            System.err.println("REMOVE NODE FAIL\n");
            return null;
        }
        MC++;
        return this.NodesHash.remove(key);
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        if(getEdge(src,dest)==null)return null;
        impEdgeData e=new impEdgeData(src,dest);
        EdgesHash.get(src).remove(dest);
        MC++;
        return e;
    }

    @Override
    public int nodeSize() {
        return NodesHash.size();
    }

    @Override
    public int edgeSize() {
        return EdgesHash.size();
    }

    @Override
    public int getMC() {
        return MC;

    }
    public void setMC(int m)
    {
        this.MC=m;
    }

    @Override
    public String toString() {
        return "DirectedWeightedGraphClass{" +
                "nodeData=" + nodeData +
                ", NodesHash=" + NodesHash +
                ", EdgesHash=" + EdgesHash +
                ", MC=" + MC +
                '}';
    }
}
