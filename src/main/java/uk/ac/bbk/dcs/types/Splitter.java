package uk.ac.bbk.dcs.types;

import com.google.common.collect.ImmutableList;
import uk.ac.bbk.dcs.util.ImmutableCollectors;

/**
 * Created by :
 *      Salvatore Rapisarda
 *      Stanislav Kikot
 * <p>
 * on 19/04/2017.
 */
public class Splitter {
    TreeDecomposition root;
    ImmutableList<Splitter> children;


    Splitter(TreeDecomposition t){
        root = t;
        TreeDecomposition splitter = root.getSplitter();

        children = splitter
                .getChildes()
                .stream()
                .map(Splitter::new)
                .collect(ImmutableCollectors.toList());

    }

}
