package finiteStateMachine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class FiniteStateMachine {
    private final int alphabetLength;
    private final int stateQuantity;
    private final int startState;
    private final int acceptStateQuantity;
    private final List<Integer> acceptStates = new LinkedList<>();
    private ArrayList<Character>[][] transitionFunction;
    private int[][] neighbourhoods;
    private ArrayList<ArrayList<Integer>> allWordsInteger = new ArrayList<>();
    private HashSet<ArrayList<Character>> allWords = new HashSet<>();

    FiniteStateMachine(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        alphabetLength = Integer.parseInt(scanner.nextLine());
        stateQuantity = Integer.parseInt(scanner.nextLine());
        startState = Integer.parseInt(scanner.nextLine());
        String acceptStateLine = scanner.nextLine();
        acceptStateQuantity = Integer.parseInt(acceptStateLine.substring(0, 1));
        for (int i = 0; i < acceptStateQuantity; i++) {
            acceptStates.add(Integer.parseInt(acceptStateLine.substring(i * 2 + 2, i * 2 + 3)));
        }
        initTransitionFunction();
        initNeighbourhoods();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int state1 = Integer.parseInt(line.substring(0, 1));
            int state2 = Integer.parseInt(line.substring(4, 5));
            neighbourhoods[state1][state2] += 1;
            char letter = line.charAt(2);
            transitionFunction[state1][state2].add(letter);
        }
    }

    private void initTransitionFunction() {
        transitionFunction = new ArrayList[stateQuantity][stateQuantity];
        for (int i = 0; i < stateQuantity; i++) {
            for (int j = 0; j < stateQuantity; j++) {
                transitionFunction[i][j] = new ArrayList<>();
            }

        }
    }

    private void initNeighbourhoods() {
        neighbourhoods = new int[stateQuantity][stateQuantity];
        for (int i = 0; i < stateQuantity; i++) {
            for (int j = 0; j < stateQuantity; j++) {
                neighbourhoods[i][j] = 0;
            }
        }
    }


    private void findPathInMachine(Integer startState, ArrayList<Integer> currentWord) {

        if (acceptStates.contains(startState)) {
            ArrayList<Integer> word = new ArrayList<>(currentWord);
            allWordsInteger.add(word);
        } else {
            for (Integer i = 0; i < stateQuantity; i++) {
                if (neighbourhoods[startState][i] > 0) {
                    neighbourhoods[startState][i] -= 1;
                    currentWord.add(i);
                    findPathInMachine(i, currentWord);
                    neighbourhoods[startState][i] += 1;
                    currentWord.remove(currentWord.lastIndexOf(i));
                }

            }
        }

    }

    private ArrayList<ArrayList<Character>> convertPathToCharsArray(ArrayList<Integer> wordInt) {
        Integer currentState = wordInt.get(0);
        ArrayList<ArrayList<Character>> charsArray = new ArrayList<>();
        for (int i = 1; i < wordInt.size(); i++) {
            Integer nextState = wordInt.get(i);
            ArrayList<Character> chars = new ArrayList<>(transitionFunction[currentState][nextState]);
            charsArray.add(chars);
            currentState = nextState;
        }
        return charsArray;
    }


    private ArrayList<Integer> convertPathToIntegerArray(ArrayList<Integer> wordInt) {
        ArrayList<Integer> path = new ArrayList<>();
        int currentState = wordInt.get(0);
        for (int i = 1; i < wordInt.size(); i++) {
            int nextState = wordInt.get(i);
            path.add(currentState * stateQuantity + nextState);
            currentState = nextState;
        }
        return path;
    }

    private void makeWordsFromCharsArray(ArrayList<ArrayList<Character>> charsArray, int start,
                                         ArrayList<Character> current, ArrayList<Integer> path) {
        if (start == charsArray.size()) {
            if (checkWord(path, current)) {
                ArrayList<Character> word = new ArrayList<>(current);
                allWords.add(word);
            }

        } else {
            for (int j = 0; j < charsArray.get(start).size(); j++) {
                current.add(charsArray.get(start).get(j));
                makeWordsFromCharsArray(charsArray, start + 1, current, path);
                current.remove(current.size() - 1);
            }
        }
    }


    private void setAllWords() {
        ArrayList<Integer> word = new ArrayList<>();
        word.add(startState);
        findPathInMachine(startState, word);
        for (ArrayList<Integer> integers : allWordsInteger) {
            ArrayList<Character> current = new ArrayList<>();
            makeWordsFromCharsArray(convertPathToCharsArray(integers), 0, current,
                    convertPathToIntegerArray(integers));
        }
    }

    public void printAllWords() {
        setAllWords();
        for (ArrayList<Character> word : allWords) {
            StringBuilder builder = new StringBuilder();
            for (Character ch : word) {
                if (ch != "e".charAt(0))
                    builder.append(ch);
            }
            System.out.println(builder.toString());
        }
    }

    private boolean checkWord(ArrayList<Integer> integerWord, ArrayList<Character> word) {
        HashMap<Integer, ArrayList<Character>> dep = new HashMap<>();
        for (int i = 0; i < integerWord.size(); i++) {
            if (dep.containsKey(integerWord.get(i))) {
                if (dep.get(integerWord.get((i))).contains(word.get(i))) {
                    return false;
                } else {
                    dep.get(integerWord.get(i)).add(word.get(i));
                }
            } else {
                ArrayList<Character> letters = new ArrayList<>();
                letters.add(word.get(i));
                dep.put(integerWord.get(i), letters);
            }
        }
        return true;
    }
}
