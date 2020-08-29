package io.github.blackfishlabs.forza.ui.settings;

public class CompletedSettingsEvent {

    private CompletedSettingsEvent() {
    }

    static CompletedSettingsEvent newEvent() {
        return new CompletedSettingsEvent();
    }
}
