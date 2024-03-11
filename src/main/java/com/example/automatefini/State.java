package com.example.automatefini;

public class State {
    private int stateNumber;
    private boolean isFinal;
    private String hello;

    public State(int stateNumber, boolean isFinal) {
        this.stateNumber = stateNumber;
        this.isFinal = isFinal;
    }

    public int getStateNumber() {
        return stateNumber;
    }

    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public String toString() {
        return "q" + stateNumber + (isFinal ? " (Final State)" : "");
    }

    public void setStateNumber(int stateNumber) {
        this.stateNumber = stateNumber;
    }

    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

}
