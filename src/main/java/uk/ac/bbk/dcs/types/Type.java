package uk.ac.bbk.dcs.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.graal.api.core.Variable;
import fr.lirmm.graphik.graal.core.term.DefaultTermFactory;
import uk.ac.bbk.dcs.types.uk.ac.bbk.dcs.util.ImmutableCollectors;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Salvatore Rapisarda
 * on 27/03/2017.
 */
public class Type {
    private final Map<Term, Atom> genAtom;
    private final Substitution homomorphism;

    Type (Atom atom, Substitution homomorphism) {
        this.genAtom = new HashMap<>();
        homomorphism.getTerms().forEach(
                term->  this.genAtom.put(  DefaultTermFactory.instance().createVariable( "v" +  term.getLabel() )  ,  atom)
        );
        this.homomorphism = homomorphism;

    }


    ImmutableList<Term> getVar1() {
        return homomorphism.getTerms()
                        .stream()
                        .filter(t -> homomorphism.createImageOf(t).getLabel().startsWith("a"))
                        .collect(ImmutableCollectors.toList());
    }

    ImmutableList<Term> getVar2() {

        ImmutableList<Term> ee = homomorphism.getTerms()
                .stream()
                .filter(t -> homomorphism.createImageOf(t).getLabel().startsWith("EE"))
                .collect(ImmutableCollectors.toList());


        ImmutableList.Builder<Term> builder = new ImmutableList.Builder<>();
        if ( ! ee.isEmpty()){
            genAtom
                    .values()
                    .stream()
                    .distinct()
                    .forEach( atom-> atom.getTerms().forEach(term -> builder.add( DefaultTermFactory.instance().createVariable( "v" +  term.getLabel() ) ) ));
        }


        return  builder.build();
    }

    ImmutableSet<Term> getDomain() {
        return ImmutableSet.copyOf(homomorphism.getTerms());
    }

    Type union ( Type type){

        // todo: merge atoms and homomorphism

        if ( type == null )
            return this;



        // Type  t = new Type( this.a )

        return null;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(genAtom);
        if (homomorphism != null ) {
            sb.append(", ");
            sb.append("(");
            sb.append(homomorphism);
            sb.append(")");
        }
        sb.append(", (var1 (").append(getVar1()).append("))");
        sb.append(", (var2 (").append(getVar2()).append("))");
        sb.append(" ) ");

        return sb.toString();

    }
}
