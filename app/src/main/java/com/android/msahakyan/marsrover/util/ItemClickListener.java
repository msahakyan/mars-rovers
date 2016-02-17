package com.android.msahakyan.marsrover.util;

import android.view.View;

/**
 * @author msahakyan
 *         <p/>
 *         This interface should be overridden in viewHolder classes
 *         in order to implement method onClick for related recyclerView items
 */
public interface ItemClickListener {
    void onClick(View view, int position);
}
