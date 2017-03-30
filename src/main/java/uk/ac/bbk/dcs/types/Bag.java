package uk.ac.bbk.dcs.types;

import com.google.common.collect.ImmutableSet;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Term;
import uk.ac.bbk.dcs.util.ImmutableCollectors;

/**
 * Created by:
 *      Salvatore Rapisarda
 *      Stanislav Kikot
 *
 * on 27/03/2017.
 */
public class Bag {

    private final ImmutableSet <Atom> atoms;
    private final ImmutableSet <Term> variables;


    /**
     * This constructor is used for Hypertree's Bag
     * @param atoms {@link ImmutableSet} of {@link Atom}
     */
    Bag(ImmutableSet <Atom> atoms) {
        this.atoms =atoms;
        variables = atoms.stream()
                .flatMap(p -> p.getTerms().stream())
                .collect(ImmutableCollectors.toSet());
    }

//    Bag(ImmutableSet <Atom> atoms, ImmutableSet<Variable> variables) {
//        this.atoms =atoms;
//        this.variables = variables;
//
//    }

    ImmutableSet<Term> getVariables() {
        return variables;
    }

    @Override
    public String toString() {
        StringBuilder sb;
        sb = new StringBuilder("((atoms:  ");
        sb.append(atoms);
        sb.append("), ");
        sb.append("(variables: ");
        sb.append(variables);
        sb.append("))");

        return sb.toString();

    }
}
