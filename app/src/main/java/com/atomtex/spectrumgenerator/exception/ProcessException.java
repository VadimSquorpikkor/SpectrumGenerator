package com.atomtex.spectrumgenerator.exception;

/**
 * Common exception that can be thrown during different process
 *
 * @author stanislav.kleinikov@gmail.com
 */
public class ProcessException extends Exception {

    public ProcessException() {
    }

    public ProcessException(String message) {
        super(message);
    }
}
