package com.atomtex.spectrumgenerator.util;

/**
 * The exception was design to signalize about problem with nuclear library. In that situation
 * a normal work of the application is impossible
 */
public class NuclideLibraryException extends Exception {

    public NuclideLibraryException() {
        super();
    }

    public NuclideLibraryException(String message) {
        super(message);
    }
}