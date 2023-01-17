package fr.ensma.lias.dbcoreapi.io.tools;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Generates a unique long id in ascending order, avoiding a sorted list of unavailable ids.
 * 
 * @author cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public class IdGenerator {
    private long            currentId;     // the current id value of the
                                           // generator
    private ArrayList<Long> forbiddenIds;  // the table of forbidden ids values
    private boolean         higherThanLast;// true if the current id value is
                                           // higher
                                           // than the last unavailable id
                                           // value, false
                                           // otherwise
    private int             currentIndex;  // the index of the closest forbidden
                                           // value
                                           // higher than the current id value

    @Deprecated
    public IdGenerator( long[] ids ) {
        forbiddenIds = new ArrayList<Long>();
        for ( long id : ids ) {
            forbiddenIds.add( id );
        }
        Collections.sort( forbiddenIds );
        currentIndex = 0;
        currentId = 1; // ids must be strictly positive
        higherThanLast = false;
    }

    public IdGenerator( ArrayList<Long> ids ) {
        forbiddenIds = ids;
        Collections.sort( forbiddenIds );
        currentIndex = 0;
        currentId = 1; // ids must be strictly positive
        higherThanLast = false;
    }

    public IdGenerator() {
        currentIndex = -1; // no unvailable ids
        forbiddenIds = null;
        currentId = 1;
        higherThanLast = true; // since unavailable ids is empty, we consider
                               // the current id higher than the last
                               // unavailable value
    }

    /**
     * Checks if the id is valid (not in the list of already used ids).
     * 
     * @param id
     * @return
     */
    public boolean isValid( long id ) {
        return !forbiddenIds.contains( id );
    }

    /**
     * 
     * @return
     */
    public Long getValidId() {
        Long id = null;

        if ( forbiddenIds.isEmpty() )
            higherThanLast = true;

        if ( higherThanLast ) {// if the current id is higher than the last
                               // unavailable value, it is not necessary to
                               // check if the current id is available
            id = new Long( currentId );
            currentId++;
            return id;
        } else { // in case the current id is not higher than the last available
                 // id, we look for an available id that can be smaller than the
                 // last unavailable value
            if ( currentId > forbiddenIds.get( forbiddenIds.size() - 1 ) ) {
                // first check if the current id did not went higher than the
                // last unavailable value at a previous iteration of the
                // generator
                higherThanLast = true;
                // the current id is available and returned
                id = new Long( currentId );
                // autoincrement the value to foresee next iteration
                currentId++;
                return id;
            } else {
                // the current id is smaller than the last unavailable value
                if ( currentId < forbiddenIds.get( currentIndex ) ) {
                    // current id is strictly smaller than the closest higher
                    // unavailable value (and also strictly higher than the
                    // closest smaller unavailable value), which makes it
                    // available
                    // the current id is available and returned
                    id = new Long( currentId );
                    // autoincrement the value to foresee next iteration
                    currentId++;
                    return id;
                } else {
                    // the current value is equal (potentially higher) than the
                    // value pointed by the currentIndex, so both values will
                    // increased until reaching a new available value
                    while ( currentIndex < forbiddenIds.size() && currentId >= forbiddenIds.get( currentIndex ) ) {
                        currentIndex++;
                        currentId++;
                    }

                    // if the last unavailable value were overpassed, then
                    // higherThanLast can be set to true
                    if ( currentIndex >= forbiddenIds.size() )
                        higherThanLast = true;
                    // the current id is available and returned
                    id = new Long( currentId );
                    // autoincrement the value to foresee next iteration
                    currentId++;
                    return id;
                }
            }
        }
    }

}
