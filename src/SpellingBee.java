/**
 * Stefan Perkovic March 17 2023
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        gen("", letters);
        System.out.println(words);
    }

    /**
     * Generates all possible words with the given letters
     */
     public void gen(String word, String letters){
         if (letters.length() == 0){
             words.add(word);
             return;
         }
        for (int i = 0; i < letters.length(); i++){
            gen(word + letters.charAt(i), letters.substring(0, i) + letters.substring(i + 1));
        }
         /**
          * Don't want to add blank space
          */
        if (word.length() != 0){
            words.add(word);
        }
     }



    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        words = mergeSort(words);
    }

    /**
     * Sorts through the array and orders all words lexographically
     */
    public ArrayList<String> mergeSort(ArrayList<String> fullWord){
        if (fullWord.size() <= 1){
            return fullWord;
        }

        ArrayList<String> firstHalf = new ArrayList<String>();
        ArrayList<String> secondHalf = new ArrayList<String>();
        int lenWords = fullWord.size() / 2;
        for (int i = 0; i < lenWords; i++) {
            firstHalf.add(fullWord.remove(0));
        }
        lenWords = fullWord.size();
        for (int i = 0; i < lenWords; i++){
            secondHalf.add(fullWord.remove(0));
        }

        firstHalf = mergeSort(firstHalf);
        secondHalf = mergeSort(secondHalf);

        /**
         * Merges both the sorted halves
         */
        while(!firstHalf.isEmpty() || !secondHalf.isEmpty()){
            if (firstHalf.isEmpty()){
                fullWord.add(secondHalf.remove(0));
            }
            else if (secondHalf.isEmpty()){
                fullWord.add(firstHalf.remove(0));
            }
            else if (firstHalf.get(0).compareTo(secondHalf.get(0)) < 0){
                fullWord.add(firstHalf.remove(0));
            }
            else{
                fullWord.add(secondHalf.remove(0));
            }
        }
        return fullWord;

    }
    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.

    /**
     * Applies binary search to check if words are real
     */
    public void checkWords() {
        for (int i = 0; i < words.size(); i++){
            int midpoint = DICTIONARY_SIZE / 2;
            String word = words.get(i);
            int start = 0;
            int end = DICTIONARY_SIZE;

            while (start != end) {
                if (word.compareTo(DICTIONARY[midpoint]) == 0){
                    break;
                }
                else if (word.compareTo(DICTIONARY[midpoint]) < 0) {
                    end = midpoint - 1;
                    midpoint = start + (end - start) / 2;
                }
                else {
                    start = midpoint + 1;
                    midpoint = start + (end - start) / 2;
                }
            }

            if (!word.equals(DICTIONARY[midpoint])){
                words.remove(i);
                i--;
            }
        }

    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
