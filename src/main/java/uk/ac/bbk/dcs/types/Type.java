package uk.ac.bbk.dcs.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import fr.lirmm.graphik.graal.api.core.Atom;
import fr.lirmm.graphik.graal.api.core.Substitution;
import fr.lirmm.graphik.graal.api.core.Term;
import fr.lirmm.graphik.graal.core.TreeMapSubstitution;
import fr.lirmm.graphik.graal.core.term.DefaultTermFactory;
import uk.ac.bbk.dcs.util.ImmutableCollectors;

/**
 * Created by :
 *      Salvatore Rapisarda
 *      Stanislav Kikot
 *
 * on 27/03/2017.
 */
public class Type {
    private final ImmutableMap<Term, Atom> genAtom;
    private final Substitution homomorphism;


    Type (ImmutableMap<Term, Atom> genAtoms, Substitution homomorphism) {
        this.genAtom = genAtoms;
        this.homomorphism = homomorphism;

    }

    ImmutableList<Term> getVar1() {
        return homomorphism.getTerms()
                        .stream()
                        .filter(t -> homomorphism.createImageOf(t).getLabel().startsWith("epsilon"))
                        .collect(ImmutableCollectors.toList());
    }

    ImmutableList<Term> getVar2() {

        ImmutableList<Term> ee = homomorphism.getTerms()
                .stream()
                .filter(t -> homomorphism.createImageOf(t).getLabel().startsWith("EE"))
                .collect(ImmutableCollectors.toList());

        ImmutableList.Builder<Term> builder = new ImmutableList.Builder<>();
        if ( ! ee.isEmpty()){
            genAtom.values()
                    .stream()
                    .distinct()
                    .forEach( atom-> atom.getTerms()
                            .forEach(term -> builder.add(
                                    DefaultTermFactory
                                            .instance().createVariable( "v" +  term.getLabel()))));
        }


        return  builder.build();
    }


    /**
     * Thi method returns the type domain
     * @return a set of {@link Term}s which is the domain
     */
    ImmutableSet<Term> getDomain() {
        return ImmutableSet.copyOf(homomorphism.getTerms());
    }


    /**
     * This method makes a union of the self object and the type
     * We assume that the types are compatible,
     * which it means that they are coincide on their common domain
     * @param type is a {@link Type}
     * @return a {@link Type}
     */
    Type union ( Type type){
        if ( type == null )
            return new  Type(this.genAtom, this.homomorphism );

        ImmutableMap.Builder<Term, Atom> genAtomBuilder = new ImmutableMap.Builder<>();
        genAtomBuilder.putAll(this.genAtom).putAll(type.genAtom);

        Substitution substitution = new  TreeMapSubstitution (homomorphism);
        substitution.put( type.homomorphism);

        ImmutableMap<Term, Atom> genAtoms = genAtomBuilder.build();

        return new Type(genAtoms, substitution );
    }


    /**
     * This method  returns the projection of a give type on to a set of variables
     * @param dest is a set of variables
     * @return a new {@link Type}
     */
    Type projection(ImmutableSet<Term> dest ){
        Substitution homomorphismProj= new TreeMapSubstitution();
        homomorphism.getTerms().forEach( term-> {
            Term variable = homomorphism.createImageOf(term);
            if ( dest.contains(term) ){
                homomorphismProj.put(term,variable);
            }
        });

        ImmutableMap<Term, Atom> genAtomProj =
                genAtom.entrySet()
                        .stream()
                        .filter(entry -> dest.contains(entry.getKey()))
                        .collect(ImmutableCollectors.toMap());

        return new Type( genAtomProj, homomorphismProj);

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
