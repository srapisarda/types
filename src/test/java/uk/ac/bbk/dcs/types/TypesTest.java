package uk.ac.bbk.dcs.types;

import com.google.common.collect.ImmutableMap;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.graal.core.DefaultAtom;
import fr.lirmm.graphik.graal.core.TreeMapSubstitution;
import fr.lirmm.graphik.graal.core.term.DefaultTermFactory;
import org.junit.Test;
import sun.jvm.hotspot.utilities.Assert;

/**
 * Created by Salvatore Rapisarda
 * on 28/03/2017.
 */
public class TypesTest {

    @Test
    public void  unionTest() {

        Substitution s1 = new TreeMapSubstitution( );
        Term tx = DefaultTermFactory.instance().createVariable("X");
        Term ee0 = DefaultTermFactory.instance().createConstant("EE0");
        s1.put( tx, ee0 );

        Term ty = DefaultTermFactory.instance().createVariable("Y");
        Term epsilon = DefaultTermFactory.instance().createConstant("epsilon");
        s1.put( ty, epsilon );

        Atom atom1 = new DefaultAtom( new Predicate("A", 1));
        atom1.setTerm(0,   DefaultTermFactory.instance().createConstant("a0") );
        Type type1 = new Type(ImmutableMap.of( tx , atom1), s1 );

        // type2
        Substitution s2 = new TreeMapSubstitution( );
        s2.put(ty, epsilon);

        Term tz = DefaultTermFactory.instance().createVariable("Z");
        s2.put( tz, ee0);

        Atom atom2 = new DefaultAtom( new Predicate("B", 1));
        atom2.setTerm(0,   DefaultTermFactory.instance().createConstant("a0") );
        Type type2 = new Type(ImmutableMap.of( tz , atom2), s2 );

        System.out.println( "type1: " + type1 ) ;
        System.out.println( "type2: " + type2 ) ;

        Type actual = type1.union(type2);

        System.out.println( actual ) ;


        Assert.that( actual.getDomain().size() ==3 , "X, Y, Z");
    }
}
