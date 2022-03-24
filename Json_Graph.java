package api;

import api.DirectedWeightedGraph;
import com.google.gson.*;

import java.lang.reflect.Type;

public class Json_Graph implements JsonDeserializer<DirectedWeightedGraph>, JsonSerializer<DirectedWeightedGraph> {

    @Override
    public DirectedWeightedGraph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
       DirectedWeightedGraph nGraph = new DWG();
       JsonObject jsonObject = jsonElement.getAsJsonObject();
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
            nGraph.addNode(temp);
        }
        for (JsonElement E : eArr ) {
            JsonObject Je = E.getAsJsonObject();
            int src = Je.get("src").getAsInt();
            int dest = Je.get("dest").getAsInt();
            double weight= Je.get("w").getAsDouble();
            nGraph.connect(src, dest, weight);
        }
        return nGraph;
    }

    @Override
    public JsonElement serialize(DirectedWeightedGraph directedWeightedGraph, Type type, JsonSerializationContext jsonSerializationContext) {
        return null;
    }
}