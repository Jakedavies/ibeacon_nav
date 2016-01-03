package info.jakedavies.innav.lib.map;

import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.Gson;

import junit.framework.TestCase;

/**
 * Created by jakedavies on 2016-01-02.
 */
public class MapTest extends TestCase {
    public void testGsonCreate() throws Exception {
        Map m = new Map();
        m.addFeature(new Feature());
        m.addFeature(new Feature(10,10,"Dick"));
        Gson g = new Gson();
        String json = g.toJson(m);
        System.out.println("Running");
        System.out.println(json);

    }
    public void testGsonRead() throws  Exception {
        Map m = new Map();
        m.addFeature(new Feature());
        m.addFeature(new Feature(10,10,"Dopne"));
        Gson g = new Gson();
        String json = g.toJson(m);

        Map m2 = g.fromJson(json, Map.class);
        m2.getFeatures().get(1);
    }
}
