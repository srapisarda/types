package uk.ac.bbk.dcs.types;

import com.google.common.collect.ImmutableList;
import uk.ac.bbk.dcs.util.ImmutableCollectors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        List<Splitter> child1 = new ArrayList<>();
        if( splitter != root ) {
            TreeDecomposition secondChildren = t.remove(splitter);
            child1.add( new Splitter(secondChildren) );
        }


        child1.addAll( splitter
                .getChildes()
                .stream()
                .map(Splitter::new)
                .collect(Collectors.toList()));


        this.children = ImmutableList.copyOf(child1);

    }







}
