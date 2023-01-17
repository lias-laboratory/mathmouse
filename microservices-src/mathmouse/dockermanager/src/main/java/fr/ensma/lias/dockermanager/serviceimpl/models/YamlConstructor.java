package fr.ensma.lias.dockermanager.serviceimpl.models;

import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlConstructor extends Constructor {
    @Override
    protected Object constructObject( Node node ) {

        if ( node.getTag() == Tag.MAP ) {
            Map<String, Object> map = (HashMap<String, Object>) super.constructObject( node );
            // If the map has the typeId and limit attributes
            // return a new Item object using the values from the map
        }
        // In all other cases, use the default constructObject.
        return super.constructObject( node );
    }
}
