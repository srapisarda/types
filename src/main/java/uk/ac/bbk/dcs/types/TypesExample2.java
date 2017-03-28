package uk.ac.bbk.dcs.types;

import fr.lirmm.graphik.graal.api.core.*;
import fr.lirmm.graphik.graal.api.forward_chaining.Chase;
import fr.lirmm.graphik.graal.api.forward_chaining.ChaseException;
import fr.lirmm.graphik.graal.api.homomorphism.HomomorphismException;
import fr.lirmm.graphik.graal.core.DefaultAtom;
import fr.lirmm.graphik.graal.core.ruleset.LinkedListRuleSet;
import fr.lirmm.graphik.graal.core.term.DefaultTermFactory;
import fr.lirmm.graphik.graal.forward_chaining.NaiveChase;
import fr.lirmm.graphik.graal.homomorphism.StaticHomomorphism;
import fr.lirmm.graphik.graal.io.dlp.DlgpParser;
import fr.lirmm.graphik.graal.io.dlp.DlgpWriter;
import fr.lirmm.graphik.graal.store.rdbms.DefaultRdbmsStore;
import fr.lirmm.graphik.graal.store.rdbms.driver.HSQLDBDriver;
import fr.lirmm.graphik.util.stream.CloseableIterator;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Salvatore Rapisarda
 * on 26/03/2017.
 */
public class TypesExample2 {

    private static DlgpWriter writer;

    public static void main(String[] args) throws AtomSetException, IOException, HomomorphismException, ChaseException {
        String file = "types4.dlp";
        String squery = "?(X, Y) :- r(X, Y).";

        if (args.length > 0)
            file = args[1];

        if (args.length > 1)
            squery = args[2];

        // 0 - Create a Dlgp writer and a structure to store rules.
        writer = new DlgpWriter();
        ConjunctiveQuery query = DlgpParser.parseQuery(squery);

        // 2 - Parse Animals.dlp (A Dlgp file with rules and facts)
        DlgpParser dlgpParser = new DlgpParser(new File("./src/main/resources/" + file));
        RuleSet ontology = new LinkedListRuleSet();

        List<Atom> facts = new ArrayList<>();
        while (dlgpParser.hasNext()) {
            // 1 - Create a relational database store with HSQLDB (An InMemory Java
            // database system),
            Object o = dlgpParser.next();
            if (o instanceof Atom) {
                facts.add((Atom) o);
            } else if (o instanceof Rule) {
                Rule rule = (Rule) o;
                rule.getBody()
                        .forEach(atom -> {
                            List<Term> terms = new ArrayList<>();
                            for (int item = 0; item < atom.getTerms().size(); item++) {
                                terms.add(DefaultTermFactory.instance().createConstant("a" + item));
                            }
                            Atom a = new DefaultAtom(atom.getPredicate(), terms);
                            facts.add(a);
                        });
                ontology.add(rule);
            }
        }

        facts.forEach(fact -> {
            try {
                BagType bagType = getBagType(ontology, fact, query);
                if (! bagType.getSubstitutions().isEmpty() ){
                    writer.write(bagType);

                }
            } catch (ChaseException | HomomorphismException | IOException | AtomSetException e) {
                e.printStackTrace();
            }
        });

        // Close the Dlgp writer
        writer.close();
    }


    private static BagType getBagType(RuleSet onto, Atom fact, ConjunctiveQuery query ) throws ChaseException, IOException, HomomorphismException, AtomSetException {
        RuleSet ontology = new LinkedListRuleSet(onto);
        Set<Substitution> resultsLabelNull = new HashSet<>();
        BagType bagType = new BagType(fact,  resultsLabelNull);
        AtomSet store = new DefaultRdbmsStore(new HSQLDBDriver( UUID.randomUUID().toString(), null));
        store.add(fact);
        // Chase
        Chase chase = new NaiveChase(ontology, store);
        chase.execute();
        // Homomorphism
        CloseableIterator<Substitution> results = StaticHomomorphism.instance().execute(query, store);
        while (results.hasNext()) {
            Substitution substitution = results.next();
            if (substitution.getValues().stream().anyMatch(p -> p.getLabel().startsWith("EE"))) {
                resultsLabelNull.add(substitution);
            }
        }

        return bagType;
    }


}
