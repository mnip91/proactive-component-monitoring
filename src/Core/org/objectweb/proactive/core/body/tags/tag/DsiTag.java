package org.objectweb.proactive.core.body.tags.tag;

import java.rmi.server.UID;

import org.objectweb.proactive.core.UniqueID;
import org.objectweb.proactive.core.body.tags.Tag;


/**
 * DsiTag provide a tag to follow the Distributed Service flow
 * by propagating the same UniqueID during a flow execution. 
 */
public class DsiTag extends Tag {

    public static final String IDENTIFIER = "PA_TAG_DSI";
    
    /**
     * Constructor setting the Tag name "PA_TAG_DSI"
     * and an UniqueID as the tag DATA
     */
    public DsiTag(UniqueID id, long cpt) {
        //super(IDENTIFIER, new UniqueID().getCanonString());
        super(IDENTIFIER, "" + id.getCanonString() + "::" + cpt);

    }

    /**
     * This tag return itself at each propagation.
     */
    public Tag apply() {
        // Propagate itself
        return this;
    }
    
}