package uk.ac.bbk.dcs.types;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Predicate;
import uk.ac.bbk.dcs.util.ImmutableCollectors;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by :
 *          Salvatore Rapisarda
 *          Stanislav Kikot
 *
 * on 30/03/2017.
 */
public class TreeDecomposition {
    private Bag root;
    private ImmutableMap< Bag, ImmutableSet<Bag>> successor;
    private ImmutableMap<Predicate, Atom> mapCqAtoms;



    TreeDecomposition (ImmutableSet <Atom> cqAtoms, Graph graph) throws IOException {
        this.mapCqAtoms = cqAtoms.stream().collect(ImmutableCollectors.toMap(atom -> atom.getPredicate(), atom-> atom ));
        for(Vertex vertex :  graph.getVertices()){
            if (root==null)
                root =getBagFromVertix(vertex);

        }


    }


    private Bag getBagFromVertix ( Vertex vertex){
        String label = vertex.getProperty("label");
        String[] split = label.split("    ");
        if ( split.length != 2)
            throw  new RuntimeException("Incorrect vertex label.");

        List<String> predicates = Arrays.asList(
                getSpittedItems(split[0]))
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        ImmutableSet<Atom> atoms = mapCqAtoms.entrySet()
                .stream()
                .filter(entry -> predicates.contains(entry.getKey().getIdentifier().toString()))
                .map(entry -> entry.getValue())
                .collect(ImmutableCollectors.toSet());


        return new Bag(atoms);

    }

    private String [] getSpittedItems( String val ){
        return val.replace("{", "")
                .replace("}", "")
                .split(", ");
    }

    public Bag getRoot() {
        return root;
    }

    // todo: implement this;
}
