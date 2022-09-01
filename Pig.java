/*
class Pig holds method that make up the actual game of pig, as opposed to methods that work with recording information/stats about the game or displaying menus
*/

import java.util.Scanner;
import java.util.Random; 
import java.io.*;


public class Pig
{

  // defining fields
  private int aiCount; // unused- was gonna be for multiple ai but ran out of time
  private int userScore = 0; // counter for user total score
  private int aiScoreA = 0; // for first ai total score
  private String aiName; // holds the recorded name of the ai so that it can be referenced



  // these are for changing text colour in console
  public final String ANSI_RESET = "\u001B[0m";
  public final String ANSI_RED = "\u001B[31m";
  public final String ANSI_BLUE = "\u001B[34m";
  public final String ANSI_PURPLE = "\u001B[35m";



  // methods
  
  /**
  This method simulates a dice roll (1-6) for the user and gives the option to roll again or hold. Also cancels if user rolls 1.
  Written by: Nicolas L
  @param an int from 1-6, or diceRoll() method
  */
  public void userRoll(int uP)
  {
    Scanner kb = new Scanner(System.in);
    Random rand = new Random();

    int diceHolder = uP; // holds the initial roll (from 1-6)

    // flavour text + printing initial dice roll to console
    System.out.print(ANSI_BLUE + "\n________________________________________________" + ANSI_RESET + "\nIt is now your turn.");
    System.out.print("\nYou rolled a " + diceHolder + "!");

    while(diceHolder != 1){ // if initial dice roll is 1

      // giving user option to roll again
      System.out.print("\nWould you like to roll again? If not, you will hold. ");
      String answer = kb.nextLine();

      // following while loop ensures that the user enters a yes or a no
      while(!(answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("roll") || answer.equalsIgnoreCase("hold"))){

        System.out.print("\nWould you like to roll again? If not, you will hold. ");
        answer = kb.nextLine();

        if(!(answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("roll") || answer.equalsIgnoreCase("hold"))){
          System.out.print("\nPlease enter 'roll' / 'yes' to roll, or 'hold' / 'no' to hold: ");
        }
      }

      // following loop is if user chooses to continue rolling
      if(answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("roll")){
        int tempHold = (rand.nextInt(6)+1);

        System.out.print("\nYou rolled a " + tempHold);

        // if user rerolls a number other than 1
        if(tempHold != 1){
          diceHolder += tempHold;
          System.out.print("\nYour current score this turn is " + diceHolder);

          answer = ""; // reset answer so it asks if user wants to roll again
        }

        // if user rerolls a 1 (sends them to the diceHolder == 1 loop below)
        if(tempHold == 1){
          diceHolder = 1;
        }
        
      }// end of if user chooses to continue rolling

      // following loop is if user chooses to hold
      if(answer.equalsIgnoreCase("no") || answer.equalsIgnoreCase("hold")){
        userScore += diceHolder;
        break;
      }

    }// end of if dice roll != 1

    if(diceHolder == 1){
      System.out.print("\nYou lost your turn.");
    }
  }// end of method



  /**
  This simple method returns how many points the user has in int userPoints
  Written by: Nicolas L
  @return int userPoints
  */
  public int getUserScore()
  {
    return userScore;
  }



  /**
  This method simulates an ai rolling the dice, and then having a chance to continue rolling each time.
  Written by: Nicolas L
  @param an int from 1-6, or diceRoll() method
  */
  public void aiRoll(int aiP)
  {

    Random rand = new Random();
    int diceHolder = aiP; // holds initial dice roll

    // flavour text + announcing dice roll
    System.out.print(ANSI_RED + "\n________________________________________________" + ANSI_RESET + "\nIt is now " + aiName + "'s turn.");
    System.out.print("\nThey rolled a " + diceHolder + "!");

    // while the initial dice roll is not 1
    while(diceHolder != 1){
      int answer = 0;

      while(answer == 0){
        answer = (rand.nextInt(2)+1); // randomly picks between 2 numbers, deciding whether ai rolls again or not
      }

      if(answer > 1){ // if ai chooses to roll again (a 1/2 chance)
        int tempHold = (rand.nextInt(6)+1);
        System.out.print("\n\n" + aiName + " chose to roll again! " + aiName + " got a " + tempHold);

        // if ai rerolls a number other than 1
        if(tempHold != 1){
          diceHolder += tempHold;
          System.out.print("\nTheir current score this turn is " + diceHolder);

          answer = 0;
        }

        // if ai rerolls a 1 (sends them to diceHolder == 1 loop, ending turn)
        if(tempHold == 1){
          diceHolder = 1;
        }

      }//end if ai chooses to roll again

      // if ai 'chooses' to hold (1/2 chance)
      if(answer == 1){ 
        System.out.print("\n\n" + aiName + " chose to hold.");
        aiScoreA += diceHolder;
        break;
      }

    }//end of if initial dice isnt a 1

    // if diceHolder == 1 loop, ends the ai turn
    if(diceHolder == 1){
      System.out.print("\n" + aiName + " lost their turn.");
    }

  }//end aiRoll() method



  /**
  This simple method returns aiPointsA so that it can be displayed during rounds
  Written by: Nicolas L
  @return whatever number is currently held in int aiScoreA
  */
  public int getAiScoreA()
  {
    return aiScoreA;
  }



  /**
  This method sets the ai's name so that the methods withing the Pig class can use it
  Written by: Nicolas L
  @param a string that contains the ai's name
  */
  public void setAiName(String rAiN)
  {
    aiName = rAiN;
  }



  // unused methods (setAiCount, getAiCount) (i was gonna add options for more ai but ran out of time)

  /**
  This method asks the user how many ai they want to play versus 
  *unused because didnt have enough time to implement this feature
  */
  public void setAiCount()
  {
    Scanner kb = new Scanner (System.in);
    int aiC = 0;

    System.out.print("\nHow many AI would you like to play against?\n");

    while(aiC > 3 || aiC < 1){
      System.out.print("Please enter a number from 1-3: "); // this ensures ai is 1-3
      aiC = kb.nextInt();
    }

    if(aiC <= 3 || aiC >= 1){
      aiCount = aiC;
    }
  }//end setAiCount() method



  /**
  This method just returns what int aiCount has been set to.
  @return number held in int aiCount
  */
  public int getAiCount()
  {
    return aiCount;
  }

}//end class Pig