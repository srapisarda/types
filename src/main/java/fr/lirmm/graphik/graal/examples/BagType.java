package fr.lirmm.graphik.graal.examples;

import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Substitution;

import java.util.Set;

/**
 * Created by Salvatore Rapisarda
 * on 27/03/2017.
 */
public class BagType {
    private final Atom atom;
    private final Set<Substitution> substitutions;

    BagType(Atom atom, Set<Substitution> substitutions) {
        this.atom = atom;
        this.substitutions = substitutions;
    }

    public Atom getAtom() {
        return atom;
    }

    public Set<Substitution> getSubstitutions() {
        return substitutions;
    }



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
        sb.append(")");
        return sb.toString();

    }
}
