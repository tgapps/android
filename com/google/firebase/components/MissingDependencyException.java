package com.google.firebase.components;

public class MissingDependencyException extends DependencyException {
    public MissingDependencyException(String msg) {
        super(msg);
    }
}
