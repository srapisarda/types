package uk.ac.bbk.dcs.types;

import com.google.common.collect.Streams;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.gml.GMLReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Salvatore Rapisarda
 * on 03/04/2017.
 */
public class BlueprintsTest {
    @Test
    public void bluePrint01Test() throws IOException {


        Graph graph = new TinkerGraph();
        InputStream in = getClass().getResourceAsStream("/blueprints_test.gml");

        GMLReader.inputGraph(graph, in);

        Assert.assertNotNull( graph.getEdges());
        Assert.assertEquals( 6, Streams.stream( graph.getEdges()).count() );
        Assert.assertEquals( 6, Streams.stream( graph.getVertices()).count() );
    }


    @Test
    public void bluePrint_Q7_Test() throws IOException {


        Graph graph = new TinkerGraph();
        InputStream in = getClass().getResourceAsStream("/Q7.gml");

        GMLReader.inputGraph(graph, in);

        Assert.assertNotNull( graph.getEdges());
        Assert.assertEquals( 6, Streams.stream( graph.getEdges()).count() );
        Assert.assertEquals( 7, Streams.stream( graph.getVertices()).count() );


        Object label = graph.getVertex(1).getProperty("label");
        Assert.assertNotNull(label);
    }
}
