package uk.ac.bbk.dcs.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Streams;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Predicate;
import uk.ac.bbk.dcs.util.ImmutableCollectors;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static com.tinkerpop.blueprints.Direction.IN;
import static com.tinkerpop.blueprints.Direction.OUT;

/**
 * Created by :
 * Salvatore Rapisarda
 * Stanislav Kikot
 * <p>
 * on 30/03/2017.
 */
public class TreeDecomposition {
    private Bag root;
    //    private ImmutableMap< Bag, ImmutableSet<Bag>> successor;
    private ImmutableMap<Predicate, Atom> mapCqAtoms;

    private ImmutableList<TreeDecomposition> childes = null;

    TreeDecomposition(ImmutableSet<Atom> cqAtoms, Graph graph, Vertex v) throws IOException {
        this.mapCqAtoms = cqAtoms.stream().collect(ImmutableCollectors.toMap(Atom::getPredicate, atom -> atom));


        Vertex vertex = null;
        if (v != null) {
            vertex = v;
        } else {
            Optional<Vertex> first = Streams.stream(graph.getVertices()).findFirst();
            if (first.isPresent())
                vertex = first.get();
        }

        ImmutableList.Builder<TreeDecomposition> childBuilder = new ImmutableList.Builder<>();

        root = getBagFromVertex(vertex);

        for (Edge edge : vertex.getEdges(OUT)) {
            Graph g = getSubGraph(graph, vertex, edge);
            childBuilder.add(new TreeDecomposition(cqAtoms, g, edge.getVertex(IN)));
        }

        this.childes = childBuilder.build();



    }


    private Graph getSubGraph(Graph graph, Vertex vertex, Edge edge) {
        Graph g = new TinkerGraph();
        graph.getVertices().forEach(v -> {
            if (!vertex.equals(v)) {
                Vertex vertex1 = g.addVertex(v.getId());
                vertex1.setProperty("label", v.getProperty("label"));
            }
        });

        graph.getEdges().forEach(e -> {
            if (!edge.equals(e)) {
                g.addEdge(e.getId(), e.getVertex(OUT), e.getVertex(IN), e.getLabel());
            }
        });

        return g;
    }


    private Bag getBagFromVertex(Vertex vertex) {
        String label = vertex.getProperty("label");
        String[] split = label.split("    ");
        if (split.length != 2)
            throw new RuntimeException("Incorrect vertex label.");

        ImmutableList<String> predicates = Arrays.stream(
                getSpittedItems(split[0]))
                .map(String::toLowerCase)
                .collect(ImmutableCollectors.toList());

        ImmutableSet<Atom> atoms = mapCqAtoms.entrySet()
                .stream()
                .filter(entry -> predicates.contains(entry.getKey().getIdentifier().toString()))
                .map(Map.Entry::getValue)
                .collect(ImmutableCollectors.toSet());


        return new Bag(atoms);

    }

    private String[] getSpittedItems(String val) {
        return val.replace("{", "")
                .replace("}", "")
                .split(", ");
    }

    public Bag getRoot() {
        return root;
    }

    public ImmutableList<TreeDecomposition> getChildes() {
        return childes;
    }
    // todo: implement this;
}
