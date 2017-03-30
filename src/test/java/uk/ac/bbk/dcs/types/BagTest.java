package uk.ac.bbk.dcs.types;

import com.google.common.collect.ImmutableSet;
import fr.lirmm.graphik.graal.api.core.*;
import fr.lirmm.graphik.graal.api.factory.TermFactory;
import fr.lirmm.graphik.graal.core.DefaultAtom;
import fr.lirmm.graphik.graal.core.term.DefaultTermFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Salvatore Rapisarda
 * on 30/03/2017.
 */
public class BagTest {

    @Test
    public void bagCreationTest() throws AtomSetException {

        TermFactory tf = DefaultTermFactory.instance();

        List<Term> rterms = new ArrayList<>();
        rterms.add( tf.createVariable("X"));
        rterms.add( tf.createVariable("Y"));
        Predicate rPredicate = new Predicate("r", 2);
        Atom r =  new DefaultAtom(rPredicate, rterms);

        List<Term> sterms = new ArrayList<>();
        sterms.add( tf.createVariable("Y"));
        sterms.add( tf.createVariable("Z"));
        Predicate sPredicate = new Predicate("s", 2);
        Atom s =  new DefaultAtom(sPredicate, sterms);


        Bag b = new Bag( ImmutableSet.of(r,s ));

        System.out.println( "bag:" + b );

        Assert.assertTrue( b.getVariables().size() ==3 );
    }
}
