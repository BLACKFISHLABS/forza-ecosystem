package io.github.blackfishlabs.forza.ui.importation;

public class CompletedImportationEvent {

    private CompletedImportationEvent() {
    }

    static CompletedImportationEvent newEvent() {
        return new CompletedImportationEvent();
    }
}
