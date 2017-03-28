package uk.ac.bbk.dcs.types;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.core.Term;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by Salvatore Rapisarda
 * on 27/03/2017.
 */
public class BagType {
    private final Atom atom;
    private final Set<Substitution> substitutions;
    private TreeMap<Term, Term> map = new TreeMap<>();

    BagType(Atom atom, Set<Substitution> substitutions) {
        this.atom = atom;
        this.substitutions = substitutions;
        substitutions.forEach( substitution -> {
                    for (Term term : substitution.getTerms())
                        this.map.put(term, substitution.createImageOf(term));
                });


    }

//    public Atom getAtom() {
//        return atom;
//    }

    Set<Substitution> getSubstitutions() {
        return substitutions;
    }

    Set<Term> getVar1(){

        return map.entrySet().stream()
                .filter( e -> e.getValue().getLabel().startsWith("a") )
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    Set<Term> getVar2(){

        return map.entrySet().stream()
                .filter( e -> e.getValue().getLabel().startsWith("EE") )
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

    }

   // todo : override  hash function


    // todo: comparison implementation

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(atom);
        if (!(substitutions == null || substitutions.isEmpty())) {
            sb.append(", ");
            for (Substitution substitution : substitutions) {
                sb.append("(");
                sb.append(substitution);
                sb.append(")");
                sb.append(", ");
            }
            sb.replace(sb.length() - 2, sb.length(), "");
        }
        sb.append( ", (var1 (").append(getVar1()).append("))" );
        sb.append( ", (var2 (").append(getVar2()).append("))" );
        sb.append(" ) " );

        return sb.toString();

    }
}
