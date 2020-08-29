package io.github.blackfishlabs.forza.ui.main;

import io.github.blackfishlabs.forza.domain.pojo.LoggedUser;

public class LoggedInUserEvent {

    private final LoggedUser mUser;

    private LoggedInUserEvent(final LoggedUser user) {
        mUser = user;
    }

    static LoggedInUserEvent logged(final LoggedUser user) {
        return new LoggedInUserEvent(user);
    }

    public LoggedUser getUser() {
        return mUser;
    }
}
