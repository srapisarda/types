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


import java.util.*;

import static com.tinkerpop.blueprints.Direction.IN;
import static com.tinkerpop.blueprints.Direction.OUT;
import static java.util.function.Function.identity;

/**
 * Created by :
 *      Salvatore Rapisarda
 *      Stanislav Kikot
 * <p>
 * on 30/03/2017.
 */
class TreeDecomposition {
    private Bag root;
    private ImmutableMap<Predicate, Atom> mapCqAtoms;
    private ImmutableList<TreeDecomposition> childes = null;

    /**
     * {@link TreeDecomposition} Constructor
     * @param cqAtoms {@link ImmutableList} of {@link Atom}s
     * @param graph {@link Graph}
     * @param v {@link Vertex} which is null if is root
     * @throws  RuntimeException if the tree does not contains any vertexes
     */
    TreeDecomposition(ImmutableSet<Atom> cqAtoms, Graph graph, Vertex v) throws RuntimeException {

        this.mapCqAtoms = cqAtoms.stream()
                .collect(ImmutableCollectors.toMap(Atom::getPredicate, identity()));

        final Vertex vertex;
        if (v != null) {
            vertex = v;
        } else {
            Optional<Vertex> first = Streams.stream(graph.getVertices()).findFirst();
            if (first.isPresent())
                vertex = first.get();
            else
                throw new RuntimeException("Vertex cannot be null!!"); // Todo: Add proper exception

        }

        root = getBagFromVertex(vertex);
        this.childes = Streams.stream(vertex.getEdges(OUT)).map(edge -> {
            Graph g = getSubGraph(graph, vertex, edge);
            return new TreeDecomposition(cqAtoms, g, edge.getVertex(IN));
        }).collect(ImmutableCollectors.toList());

    }

    private TreeDecomposition (ImmutableMap<Predicate, Atom> cqAtoms, Bag root, ImmutableList<TreeDecomposition> childes  ){
        this.mapCqAtoms = cqAtoms;
        this.root = root;
        this.childes = childes;

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
        String[] split = label.split(" {4}");
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

    /**
     * This method returns the size of the {@link TreeDecomposition}
      * @return an int value
     */
    int  getSize(){
        return getSize( this );
    }

    private  int getSize(TreeDecomposition t  ){
        if ( t.childes == null || t.childes.isEmpty())
            return 1;
        else {
             return t.childes
                     .stream()
                     .mapToInt(this::getSize)
                     .sum() + 1;
        }
    }


    private String[] getSpittedItems(String val) {
        return val.replace("{", "")
                .replace("}", "")
                .split(", ");
    }

    Bag getRoot() {
        return root;
    }

    ImmutableList<TreeDecomposition> getChildes() {
        return childes;
    }


    TreeDecomposition getSplitter(){
        return  getSplitter( this,  this.getSize());
    }

    // todo: make it tailrec
    private TreeDecomposition getSplitter(  TreeDecomposition t, int rootSize){
        if ( t.getSize() <=  (rootSize/2) + 1)
            return  t ;
        else {
            int maxsize =-1;
            TreeDecomposition child = null;
            for (TreeDecomposition c: t.getChildes()){
                int size = c.getSize();
                if (  size > maxsize){
                    child=c;
                    maxsize = size;
                }
            }

            return getSplitter(child, rootSize);
        }
    }



    TreeDecomposition remove ( TreeDecomposition s  ){
        List<TreeDecomposition> ret = new ArrayList<>();
        for (TreeDecomposition c: this.getChildes()) {
            if ( c!= s){
                ret.add( c.remove(s) );
            }
        }

        return  new TreeDecomposition( mapCqAtoms,  root,   ImmutableList.copyOf( ret) ) ;
    }




}
