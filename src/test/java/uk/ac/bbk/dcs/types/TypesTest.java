package uk.ac.bbk.dcs.types;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Predicate;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.graal.core.DefaultAtom;
import fr.lirmm.graphik.graal.core.TreeMapSubstitution;
import fr.lirmm.graphik.graal.core.term.DefaultTermFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Salvatore Rapisarda
 * on 28/03/2017.
 */
public class TypesTest {
    private Type type1;
    private Type type2;

    private Term tx;
    private Term ty;


    @Before
    public void setUp(){
        Substitution s1 = new TreeMapSubstitution( );

        tx = DefaultTermFactory.instance().createVariable("X");
        Term ee0 = DefaultTermFactory.instance().createConstant("EE0");
        s1.put( tx, ee0 );

        ty = DefaultTermFactory.instance().createVariable("Y");
        Term epsilon = DefaultTermFactory.instance().createConstant("epsilon");
        s1.put( ty, epsilon );

        Atom atom1 = new DefaultAtom( new Predicate("A", 1));
        atom1.setTerm(0,   DefaultTermFactory.instance().createConstant("a0") );
        type1 = new Type(ImmutableMap.of( tx , atom1), s1 );

        // type2
        Substitution s2 = new TreeMapSubstitution( );
        s2.put(ty, epsilon);

        Term tz = DefaultTermFactory.instance().createVariable("Z");
        s2.put( tz, ee0);

        Atom atom2 = new DefaultAtom( new Predicate("B", 1));
        atom2.setTerm(0,   DefaultTermFactory.instance().createConstant("a0") );
        type2 = new Type(ImmutableMap.of( tz , atom2), s2 );

    }


    @Test
    public void  unionTest() {
        System.out.println("union test");
        System.out.println( "type1: " + type1 ) ;
        System.out.println( "type2: " + type2 ) ;

        Type actual = type1.union(type2);

        System.out.println( "union: type1 U type2: " +  actual ) ;

        Assert.assertTrue(actual.getDomain().size() ==3 );
    }

    @Test
    public void  projectionTest() {
        System.out.println("projection test");
        System.out.println( "type1: " + type1 ) ;
        System.out.println( "type2: " + type2 ) ;

        Type actual = type1.union(type2)
                .projection(ImmutableSet.of(tx,ty));

        System.out.println( "projection : proj(x, y) from (type1 U type2): " +  actual ) ;

        Assert.assertTrue( actual.getDomain().size() ==2 );
    }



}
