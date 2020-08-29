package io.github.blackfishlabs.forza.ui.login;

import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;

public class CompletedLoginEvent {

    private final LoggedUser mUser;

    private CompletedLoginEvent(final LoggedUser user) {
        mUser = user;
    }

    static CompletedLoginEvent newEvent(final LoggedUser user) {
        return new CompletedLoginEvent(user);
    }

    public LoggedUser getUser() {
        return mUser;
    }
}
