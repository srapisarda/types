package uk.ac.bbk.dcs.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.gml.GMLReader;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.graal.api.factory.TermFactory;
import fr.lirmm.graphik.graal.core.DefaultAtom;
import fr.lirmm.graphik.graal.core.term.DefaultTermFactory;
import org.junit.Assert;
import org.junit.Test;
import uk.ac.bbk.dcs.util.ImmutableCollectors;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Salvatore Rapisarda
 * on 03/04/2017.
 */
public class TreeDecompositionTest {
    @Test
    public void treeDecomposition_test1() throws IOException {

        ImmutableMap.Builder<String, ImmutableList<String>> builder = new ImmutableMap.Builder<>();
        builder.put("r1", ImmutableList.of("X2", "X3"));
        builder.put("s1", ImmutableList.of("X3", "X4"));
        builder.put("s2", ImmutableList.of("X4", "X5"));
        builder.put("r2", ImmutableList.of("X5", "X6"));
        builder.put("s3", ImmutableList.of("X6", "X7"));
        builder.put("r0", ImmutableList.of("X1", "X2"));
        builder.put("s0", ImmutableList.of("X0", "X1"));

        TermFactory tf = DefaultTermFactory.instance();
        ImmutableSet<Atom> atoms = builder.build().entrySet().stream()
                .map(entry -> {
                    List<Term> rterms = entry.getValue().stream()
                            .map(term -> tf.createVariable(term))
                            .collect(Collectors.toList());
                    Predicate rPredicate = new Predicate(entry.getKey(), entry.getValue().size());
                    return new DefaultAtom(rPredicate, rterms);
                })
                .collect(ImmutableCollectors.toSet());

        Graph graph = new TinkerGraph();
        InputStream in = getClass().getResourceAsStream("/Q7.gml");
        GMLReader.inputGraph(graph, in);
        TreeDecomposition t = new TreeDecomposition(atoms, graph,null );
        Assert.assertNotNull(t);
        Assert.assertEquals( ImmutableSet.copyOf( atoms.asList().get(0).getTerms() ),  t.getRoot().getVariables()  );
        System.out.println(   "root: " +   t.getRoot().getVariables() );
        Assert.assertEquals( 2, t.getChildes().size());
        Assert.assertEquals( 7, t.getSize());
    }

}
