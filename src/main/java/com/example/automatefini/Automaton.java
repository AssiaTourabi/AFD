package com.example.automatefini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

//hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii
public class Automaton {

    private int numStates, numTerminals, numFinalStates;
    private char terminals[];
    private State states[];
    private Transition transitions[];

    private State inState;
    private State fiStates[];

    public Automaton() {
    }

    public Automaton(int numStates, int numTerminals, int numFinalStates, char[] terminals, State[] states,
            Transition[] transitions, State inState, State[] fiStates) {
        this.numStates = numStates;
        this.numTerminals = numTerminals;
        this.numFinalStates = numFinalStates;
        this.terminals = terminals;
        this.states = states;
        this.transitions = transitions;
        this.inState = inState;
        this.fiStates = fiStates;
    }

    public Automaton(char[] terminals, State[] states, Transition[] transitions, State inState, State[] fiStates) {
        this.terminals = terminals;
        this.states = states;
        this.transitions = transitions;
        this.inState = inState;
        this.fiStates = fiStates;
    }

    public int getNumStates() {
        return numStates;
    }

    public void setNumStates(int numStates) {
        this.numStates = numStates;
    }

    public int getNumTerminals() {
        return numTerminals;
    }

    public void setNumTerminals(int numTerminals) {
        this.numTerminals = numTerminals;
    }

    public int getNumFinalStates() {
        return numFinalStates;
    }

    public void setNumFinalStates(int numFinalStates) {
        this.numFinalStates = numFinalStates;
    }

    public char[] getTerminals() {
        return terminals;
    }

    public void setTerminals(char[] terminals) {
        this.terminals = terminals;
    }

    public State[] getStates() {
        return states;
    }

    public void setStates(State[] states) {
        this.states = states;
    }

    public Transition[] getTransitions() {
        return transitions;
    }

    public void setTransitions(Transition[] transitions) {
        this.transitions = transitions;
    }

    public State getInState() {
        return inState;
    }

    public void setInState(State inState) {
        this.inState = inState;
    }

    public State[] getFiStates() {
        return fiStates;
    }

    public void setFiStates(State[] fiStates) {
        this.fiStates = fiStates;
    }

    @Override
    public String toString() {
        return "Automaton [numStates=" + numStates + ", numTerminals=" + numTerminals + ", numFinalStates="
                + numFinalStates + ", terminals=" + Arrays.toString(terminals) + ", states=" + Arrays.toString(states)
                + ", transitions=" + Arrays.toString(transitions) + ", inState=" + inState + ", fiStates="
                + Arrays.toString(fiStates) + "]";
    }

    public Automaton(String filename) {
        BufferedReader infile = null;
        numStates = 0;
        numTerminals = 0;
        try {
            infile = new BufferedReader(new FileReader(filename));

            numTerminals = Integer.parseInt(infile.readLine());
            String strTerms[] = infile.readLine().split(" ");
            terminals = new char[numTerminals];
            for (int i = 0; i < numTerminals; i++) {
                terminals[i] = strTerms[i].charAt(0);
            }

            numStates = Integer.parseInt(infile.readLine());

            states = new State[numStates];
            for (int i = 0; i < numStates; i++) {
                states[i] = new State(i, false);
            }

            transitions = new Transition[numStates * numTerminals];
            for (int i = 0; i < (numStates * numTerminals); i++) {
                String pathStr[] = infile.readLine().split(" ");
                int sourceState = Integer.parseInt(pathStr[0]);
                int targetState = Integer.parseInt(pathStr[2]);
                char terminal = pathStr[1].charAt(0);

                transitions[i] = new Transition(states[sourceState], terminal, states[targetState]);
            }

            numFinalStates = Integer.parseInt(infile.readLine());
            String[] fs = infile.readLine().trim().split(" ");
            for (int tmp = 0; tmp < numFinalStates; tmp++) {
                int finalStateIndex = Integer.parseInt(fs[tmp]);
                states[finalStateIndex].setFinal(true);
            }

            infile.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            System.out.println("Malformed input file!");
            exception.printStackTrace();
        }
    }

    public boolean[] getFinalStates() {
        boolean[] finalStates = new boolean[numStates];
        for (int i = 0; i < numStates; i++) {
            finalStates[i] = states[i].isFinal();
        }
        return finalStates;
    }

    public boolean accepts(String input) {
        int currentState = 0;
        for (char symbol : input.toCharArray()) {
            int terminalIndex = indexOfTerminal(symbol);
            if (terminalIndex == -1) {
                System.out.println("Invalid symbol '" + symbol + "' in the input string.");
                return false;
            }
            currentState = transitions[currentState * numTerminals + terminalIndex].getTargetState().getStateNumber();

        }
        return states[currentState].isFinal();
    }

    private int indexOfTerminal(char terminal) {
        for (int i = 0; i < numTerminals; i++) {
            if (terminals[i] == terminal) {
                return i;
            }
        }
        return -1;
    }

    public void printConfiguration() {
        System.out.println("DFA Configuration:");
        System.out.println("Q (States): " + Arrays.toString(states));
        System.out.println("Σ (Alphabet): " + Arrays.toString(terminals));

        System.out.println("δ (Transition Function):");
        for (Transition transition : transitions) {
            System.out.println("  δ(" + transition.getSourceState().getStateNumber() +
                    ", '" + transition.getTerminal() + "') = " +
                    transition.getTargetState().getStateNumber());
        }

        System.out.println("q0 (Initial State): " + states[0].getStateNumber());

        System.out.println("F (Final States): " +
                Arrays.toString(getFinalStates()));

        System.out.println("\n------------------------ DFA Transitions:");
        System.out.println(numStates);
        System.out.println(new String(terminals));
        System.out.println(numStates * numTerminals);

        for (Transition transition : transitions) {
            System.out.println(transition.getSourceState().getStateNumber() +
                    " " + transition.getTerminal() +
                    " " + transition.getTargetState().getStateNumber());
        }

        System.out.println(numFinalStates);
        System.out.println(formatBooleanArray(getFinalStates()));
    }

    private String formatBooleanArray(boolean[] array) {
        StringBuilder result = new StringBuilder();
        for (boolean value : array) {
            result.append(value).append(" ");
        }
        return result.toString().trim();
    }

    public static Automaton readAutomatonFromFile(File file) throws IOException {
        State[] states = null;
        char[] terminals = null;
        Transition[] transitions = null;
        State initialState = null;
        State[] finalStates = null;

        List<String> lines = readLinesFromFile(file);

        // Récupérer les états de la première ligne
        String[] statesStr = lines.get(0).split(", ");
        states = new State[statesStr.length];
        for (int i = 0; i < statesStr.length; i++) {
            int stateNumber = Integer.parseInt(statesStr[i].substring(1));
            states[i] = new State(stateNumber, false); // Initialiser chaque état avec son numéro
        }

        String[] terminalsStr = lines.get(1).split(",");
        terminals = new char[terminalsStr.length];
        for (int i = 0; i < terminalsStr.length; i++) {
            terminals[i] = terminalsStr[i].charAt(0);
        }

        // System.out.println(terminals[1]);

        // Récupérer les transitions à partir de la troisième ligne
        List<Transition> transitionsList = new ArrayList<>();
        for (int i = 2; i < lines.size() - 2; i++) {
            String[] parts = lines.get(i).split(",");
            if (parts.length == 3) {
                // Créer et ajouter une transition
                State sourceState = findState(states, Integer.parseInt(parts[0].substring(1)));
                char terminal = parts[1].charAt(0);
                State targetState = findState(states, Integer.parseInt(parts[2].substring(1)));
                transitionsList.add(new Transition(sourceState, terminal, targetState));
            }
        }
        transitions = transitionsList.toArray(new Transition[0]);

        // Récupérer l'état initial à partir de l'avant-dernière ligne
        initialState = findState(states, Integer.parseInt(lines.get(lines.size() - 2).substring(1)));

        // Récupérer les états finaux à partir de la dernière ligne
        String[] finalStatesStr = lines.get(lines.size() - 1).split(", ");
        finalStates = new State[finalStatesStr.length];
        for (int i = 0; i < finalStatesStr.length; i++) {
            finalStates[i] = findState(states, Integer.parseInt(finalStatesStr[i].substring(1)));
        }

        return new Automaton(terminals, states, transitions, initialState, finalStates);
    }

    private static State findState(State[] states, int stateNumber) {
        for (State state : states) {
            if (state.getStateNumber() == stateNumber) {
                return state;
            }
        }
        return null; // Si l'état n'est pas trouvé
    }

    private static List<String> readLinesFromFile(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    public static Automaton union(Automaton automaton1, Automaton automaton2) {
        // Récupérer les attributs des deux automates
        int numStates1 = automaton1.getNumStates();
        int numStates2 = automaton2.getNumStates();
        int numTerminals1 = automaton1.getNumTerminals();
        int numTerminals2 = automaton2.getNumTerminals();
        char[] terminals1 = automaton1.getTerminals();
        char[] terminals2 = automaton2.getTerminals();
        State[] states1 = automaton1.getStates();
        State[] states2 = automaton2.getStates();
        Transition[] transitions1 = automaton1.getTransitions();
        Transition[] transitions2 = automaton2.getTransitions();
        State initialState1 = automaton1.getInState();
        State initialState2 = automaton2.getInState();
        State[] finalStates1 = automaton1.getFiStates();
        State[] finalStates2 = automaton2.getFiStates();

        // Créer un nouvel ensemble d'états finaux
        Set<State> newFinalStates = new HashSet<>();
        Collections.addAll(newFinalStates, finalStates1);
        Collections.addAll(newFinalStates, finalStates2);

        // Ajouter l'état initial à l'ensemble des états finaux si nécessaire
        boolean newInitialStateIsFinal = initialState1.isFinal() || initialState2.isFinal();
        State newInitialState = new State(0, newInitialStateIsFinal);

        // Créer un nouvel ensemble d'états
        State[] newStates = new State[numStates1 + numStates2];
        for (int i = 0; i < numStates1; i++) {
            newStates[i] = new State(i, false);
        }
        for (int i = 0; i < numStates2; i++) {
            newStates[numStates1 + i] = new State(numStates1 + i, false);
        }

        // Créer un nouvel ensemble de transitions
        Set<Transition> newTransitions = new HashSet<>();
        addTransitions(newTransitions, transitions1, 0);
        addTransitions(newTransitions, transitions2, numStates1);

        // Créer un nouvel automate avec les attributs appropriés
        Automaton unionAutomaton = new Automaton(
                numStates1 + numStates2,
                numTerminals1 + numTerminals2,
                newFinalStates.size(),
                concatArrays(terminals1, terminals2),
                newStates,
                newTransitions.toArray(new Transition[newTransitions.size()]),
                newInitialState,
                newFinalStates.toArray(new State[newFinalStates.size()]));

        return unionAutomaton;
    }

    private static void addTransitions(Set<Transition> transitionsSet, Transition[] transitions, int offset) {
        for (Transition transition : transitions) {
            transitionsSet.add(new Transition(
                    new State(transition.getSourceState().getStateNumber() + offset, false),
                    transition.getTerminal(),
                    new State(transition.getTargetState().getStateNumber() + offset, false)));
        }
    }

    // Méthode utilitaire pour concaténer deux tableaux de caractères
    private static char[] concatArrays(char[] arr1, char[] arr2) {
        char[] result = new char[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, result, 0, arr1.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
    }

    public static List<String> toMathematicalForm(Automaton automaton) {
        List<String> lines = new ArrayList<>();

        // Forme mathématique pour les états
        StringBuilder statesLine = new StringBuilder();
        statesLine.append("Q={");

        for (State state : automaton.states) {
            statesLine.append("q").append(state.getStateNumber());
            if (state.isFinal()) {
                statesLine.append(" (Final State)");
            }
            statesLine.append(", ");
        }
        statesLine.delete(statesLine.length() - 2, statesLine.length()); // Supprimer la dernière virgule et l'espace
        statesLine.append("}");
        lines.add(statesLine.toString());

        // Forme mathématique pour l'alphabet
        StringBuilder alphabetLine = new StringBuilder();
        alphabetLine.append("Σ = {");
        for (char terminal : automaton.terminals) {
            alphabetLine.append(terminal).append(", ");
        }
        alphabetLine.delete(alphabetLine.length() - 2, alphabetLine.length()); // Supprimer la dernière virgule et
                                                                               // l'espace
        alphabetLine.append("}");
        lines.add(alphabetLine.toString());

        // Forme mathématique pour les transitions
        StringBuilder transitionsLine = new StringBuilder();
        transitionsLine.append("δ = {\n");
        for (Transition transition : automaton.transitions) {
            transitionsLine.append("\tq").append(transition.getSourceState().getStateNumber())
                    .append(", ").append(transition.getTerminal()).append(", ")
                    .append("q").append(transition.getTargetState().getStateNumber()).append(",\n");
        }
        transitionsLine.append("}");
        lines.add(transitionsLine.toString());

        // Forme mathématique pour l'état initial
        lines.add("q0 = q" + automaton.inState.getStateNumber());

        // Forme mathématique pour les états finaux
        StringBuilder finalStatesLine = new StringBuilder();
        finalStatesLine.append("F = {");
        for (State finalState : automaton.fiStates) {
            finalStatesLine.append("q").append(finalState.getStateNumber()).append(", ");
        }
        finalStatesLine.delete(finalStatesLine.length() - 2, finalStatesLine.length()); // Supprimer la dernière virgule
                                                                                        // et l'espace
        finalStatesLine.append("}");
        lines.add(finalStatesLine.toString());

        return lines;
    }
}