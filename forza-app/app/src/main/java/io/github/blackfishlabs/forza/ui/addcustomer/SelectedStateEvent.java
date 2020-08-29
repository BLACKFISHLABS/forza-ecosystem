package io.github.blackfishlabs.forza.ui.addcustomer;

import io.github.blackfishlabs.forza.domain.pojo.State;

public class SelectedStateEvent {

    private final State mState;

    private SelectedStateEvent(final State state) {
        mState = state;
    }

    static SelectedStateEvent newEvent(final State state) {
        return new SelectedStateEvent(state);
    }

    public State getState() {
        return mState;
    }
}
