package io.github.blackfishlabs.forza.ui.widget.recyclerview;

import android.view.View;

public interface OnItemClickListener {

    void onSingleTapUp(View view, int position);

    void onLongPress(View view, int position);
}