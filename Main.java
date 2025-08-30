package assign1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class Main {

    public static boolean errorChecked;

    public Main() {
        errorChecked = false;
    }

    public static void main(String[] args) throws IOException{
        //grabs terminal input
        InputStreamReader stringInput = new InputStreamReader(System.in);
        BufferedReader stringReader = new BufferedReader(stringInput);
        
        //calls prompt
        prompt(stringReader, new Stack<>());
    }

    public static void prompt(BufferedReader inputString, Stack<String> finalStack) throws IOException {
        System.out.print("expander > ");
        
        //translates input into readable string
        String readableLine = inputString.readLine();

        if(readableLine.length() >= 1) { //expand actions if more than 1 character
            stringExpander(readableLine, finalStack, 0);
        }
        
        //new prompt and reset
        finalStack.clear();
        errorChecked = false;
        prompt(inputString, finalStack);
    }

    static void stringExpander(String currentLine, Stack<String> finalStack, int index) {
        String[] actions = currentLine.split(";");

        //check index doesn't go over the no. existing actions & end recursion (errors)
        if(index >= actions.length || errorChecked == true) {
            return;
        }

        String currentAction = actions[index];
        char firstChar = currentAction.charAt(0);

        //actions
        if(isLetter(firstChar)){ //letter action
                finalStack.push(currentAction);
        }
        else if(firstChar == '*') { //'*' action
            //check if stack is empty and if number is within range
            if(finalStack.empty() || currentAction.length() > 3) {
                errorReporter(finalStack, currentAction);
            }

            //create and repeat action
            int repeat = currentAction.charAt(1) - '0';
            String topString = finalStack.pop(); //removes and saves top of stack
            removeRep(repeat, finalStack, currentAction, topString);
        }
        else if(firstChar == '+') { //'+' action
            //check if stack is empty and if action value is correctly in range
            if(finalStack.empty() || currentAction.length() > 2) {
                errorReporter(finalStack, currentAction);
            }

            //pop top 2 strings
            String topString = finalStack.pop();
            if(!finalStack.empty()) { //after taking top check if there is second string to join later
                String secondTopString = finalStack.pop();
                stringJoin(topString, secondTopString, finalStack, currentAction);
            } else {
                errorReporter(finalStack, currentAction);
            }
            
        }
        else { //other actions - ?, !, etc.
            errorReporter(finalStack, currentAction);
        }

        //iterate through all actions
        stringExpander(currentLine, finalStack, index + 1);

        //print final Stack
        if(!finalStack.empty() && errorChecked == false) {
            System.out.println(finalStack);
            errorChecked = true;
        }
    }

    static void removeRep(int repeat, Stack<String> finalStack, String action, String topString) {
        //end of repeats
        if(repeat == 0) {
            return;
        }

        //In between character checker
        String spacingChar = null;
        if(action.length() == 3) {
            spacingChar = action.substring(2);
        }

        if(repeat > 0){
            if(finalStack.empty()) { //if first pop resulted in empty
                finalStack.push(topString);
            } else if(spacingChar == null) { //no D character
                finalStack.push(finalStack.pop() + topString);
            } else { //D character exists
                finalStack.push(finalStack.pop() + spacingChar + topString);
            }
        }
        removeRep(repeat-1, finalStack, action, topString);
    }

    static void stringJoin(String topString, String secTopString, Stack<String> finalStack, String action) {
        //In between character checker
        String spacingChar = null;
        if(action.length() == 2) {
            spacingChar = action.substring(1);
        }

        if(spacingChar == null) { //no D character
            finalStack.push(secTopString + topString);
        } else { //D character exists
            finalStack.push(secTopString + spacingChar + topString);
        }
    }

    //letter check
    public static boolean isLetter(char let) {
        if((let >= 'a' && let <= 'z') || (let >= 'A' && let <='Z') || let == ' ') {
            return true;
        }
        return false;
    }

    //number check
    public static boolean isDigit(char num) {
        if(num >= '0' && num <= '9'){
            return true;
        }
        return false;
    }

    public static void errorReporter(Stack<String> finalStack, String action) {
        if(finalStack.empty()) {
            System.out.println("error in action: empty stack");
        }
        else {
            System.out.println("error in action: " + action);
        }
        errorChecked = true;
    }
}