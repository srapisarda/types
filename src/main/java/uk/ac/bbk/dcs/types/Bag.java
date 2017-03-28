package uk.ac.bbk.dcs.types;

import com.google.common.collect.ImmutableList;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.core.Term;
import uk.ac.bbk.dcs.util.ImmutableCollectors;

import java.util.TreeMap;

/**
 * Created by Salvatore Rapisarda
 * on 27/03/2017.
 */
public class Bag {
    private final Atom atom;
    private final Substitution substitution;
    private TreeMap<Term, Term> map = new TreeMap<>();

    Bag(Atom atom, Substitution substitutions) {
        this.atom = atom;
        this.substitution = substitutions;
//        substitutions.forEach( substitution -> {
//                    for (Term term : substitution.getTerms())
//                        this.map.put(term, substitution.createImageOf(term));
//                });


    }



//    public Atom getAtom() {
//        return atom;
//    }

    Substitution getSubstitutions() {
        return substitution;
    }

    ImmutableList<Term> getVar1() {
        return substitution.getTerms()
                .stream()
                .filter(t -> substitution.createImageOf(t).getLabel().startsWith("a"))
                .collect(ImmutableCollectors.toList());
    }

    ImmutableList<Term> getVar2() {
        return substitution.getTerms()
                .stream()
                .filter(t -> substitution.createImageOf(t).getLabel().startsWith("EE") )
                .collect(ImmutableCollectors.toList());
    }



    // todo : override  hash function


    // todo: comparison implementation

//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder("(");
//        sb.append(atom);
//        if (!(substitutions == null || substitutions.isEmpty())) {
//            sb.append(", ");
//            for (Substitution substitution : substitutions) {
//                sb.append("(");
//                sb.append(substitution);
//                sb.append(")");
//                sb.append(", ");
//            }
//            sb.replace(sb.length() - 2, sb.length(), "");
//        }
//        sb.append( ", (var1 (").append(getVar1()).append("))" );
//        sb.append( ", (var2 (").append(getVar2()).append("))" );
//        sb.append(" ) " );
//
//        return sb.toString();
//
//    }
}
