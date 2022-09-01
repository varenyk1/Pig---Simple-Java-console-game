/*
Program name: Pig (as in the dice game)
Summary of program: This program allows the user to play pig (a really simple dice game), but it is designed to feel like a arcade game with the menus and etc. Data from the games (play count and wins) are recorded in files and can be displayed under the stats option.

Programmed by: Nicolas L
Last modified: 7:59 PM Jan 22, 2022 (Nicolas L)

Quick explanation of the txt files:
aiNames.txt holds names for the ai to randomly generate, sampleAiNames.txt can be filled with whatever, it is there if you want to test the function for adding a file of names. playRecord.txt and userWins.txt record stats about the game.
*/


import java.util.Scanner;
import java.io.*;
import java.util.Random;


class Main {
  public static void main(String[] args)  throws IOException {
    new Main(); // new main so i can use non-static methods
  }



  public Main() throws IOException { // new instance of main(), my code will be going here instead
    

    Scanner kb = new Scanner(System.in);
    String playGame = "yes"; // this is to loop back to title screen when game is over
    boolean gameOver = false; // this will help us loop in and out of the game as the user wins/loses


    while(playGame.equalsIgnoreCase("yes")){



      // titlescreen which gives option for playing the game, view stats, or exit
      int userResponse = titleScreen();
      

      // user chooses to view stats
      if(userResponse == 2){ 
        int playTime = getPlayTime();
        int userWins = getWins();
        System.out.print("________________________________________________" + "\n\n\tPig has been played: " + "\u001B[35m" + playTime + "\u001B[0m times.\n\tThe user has won: \u001B[35m" + userWins + "\u001B[0m times.");
        System.out.print("\n\n________________________________________________\n\n\tPress\u001B[35m ENTER\u001B[0m to return to title screen ");
        kb.nextLine();
      }


      // if user chooses exit
      if(userResponse == 3){ // user chooses to exit system
        System.out.print("\nSee you next time!");
        System.exit(0);
      }


      // if user chooses to advance and play the game
      if(userResponse == 1){
        // requestTutorial method
        requestTutorial();


        // selecting ai name
        Pig pigGame = new Pig();

        String aiNameA = "";

        String aiRanQ = "";//asking user whether they want to randomly generate the name for ai or not
        while(!(aiRanQ.equalsIgnoreCase("yes") || aiRanQ.equalsIgnoreCase("no"))){
          System.out.print("\nWould you like to have a randomly selected name for the AI? ");
          aiRanQ = kb.nextLine();
        }


        // if user wants random ai name
        if(aiRanQ.equalsIgnoreCase("yes")){
          aiNameA = randName();
        }


        // this following sequence of if statements is under the case the user doesnt want a randomly generated name for the ai
        if(aiRanQ.equalsIgnoreCase("no")){ 
          System.out.print("\nWould you like to enter a file or a name? ");
          String fileOrName = kb.nextLine();

          // error proofing!!!
          while(!(fileOrName.equalsIgnoreCase("file") || fileOrName.equalsIgnoreCase("name"))){
            System.out.print("\nPlease enter 'file' or 'name': ");
            fileOrName = kb.nextLine();
          }

          // if user chooses to take ai name from a different file (try sampleAiNames.txt if testing this program**)
          if(fileOrName.equalsIgnoreCase("file")){
            System.out.print("\nWhat file would you like to take a name from? ");
            String aiNameFile = kb.nextLine();

            aiNameA = randName(aiNameFile);
          }

          // if user wants to choose a specific name for the ai themselves
          if(fileOrName.equalsIgnoreCase("name")){
            System.out.print("\nPlease enter a name for the AI: ");
            aiNameA = kb.nextLine();
          }
        }//end if user doesnt want randomly generated ai name

        System.out.print("\nGreat, your AI will be named " + aiNameA + ".");
        pigGame.setAiName(aiNameA);


    
        // the following ints and booleans are used for the game to function properly and count different parts of the game
        boolean userTurnOver = false; // turn counter for every possible "player" including ai
        gameOver = false; // checks whether the game is over or not so it doesnt continue when someone wins
        int win = 0;

        int totalUserScore = 0; // these two ints track user and AI's total scores throughout the game
        int totalAiScoreA = 0;



        // following loop is the game itself
        while(gameOver == false){ // put this in a while loop so user can play again

          while(userTurnOver == false){

            if(gameOver == false){//so game doesnt continue if ai wins
              int randomNum = diceRoll(); // resetting the diceRoll
              pigGame.userRoll(randomNum);

              userTurnOver = true; // the users turn is over so this is now true, allowing the ai's turn to follow shortly

              int playerScore = pigGame.getUserScore();
              totalUserScore = playerScore;

              // flavour text + displaying the users score
              System.out.print("\nYour score is " + "\u001B[34m" + totalUserScore + "\u001B[0m" + ".\nPress" + "\u001B[35m" + " ENTER " + "\u001B[0m" + "to continue ");
              kb.nextLine();
            }


          }//end while loop while turnOver == false


          // end of users turn
          while(userTurnOver == true)
          {
            // checking if the player won during the turn that just ended
            if(totalUserScore >= 100){ 
              System.out.print("\nCongratulations! You won.");

              gameOver = true; // these are just to end the game and send program to "do you want to return to title screen" section + recording stats
              playGame = "";
              addPlayTime();
              int userWin = 1;
              addPlayTime(win);
              break;
            }

            // starting the ai's turn
            if(gameOver == false){//so game doesnt continue if user wins

              int aiRandomNum = diceRoll(); // resetting the diceroll each time
              pigGame.aiRoll(aiRandomNum);

              int aiScoreA = pigGame.getAiScoreA(); // using this method to track ai A total score
              totalAiScoreA = aiScoreA;

              System.out.print("\n" + aiNameA + "'s score is now " + "\u001B[31m" + totalAiScoreA + "\u001B[0m" + ".\nPress " + "\u001B[35mENTER " + "\u001B[0m" + "to continue ");
              kb.nextLine();

              userTurnOver = false; // now that the ai's turn is over, we set this boolean back to false so the users turn can shortly commence


              // if ai won during that turn
              if(totalAiScoreA >= 100){
                System.out.print("\n" + aiNameA + " has won the game! Nice try."); // enter userName string (from method   getUserName)

                gameOver = true; // once again, just making sure the game ends right here and recording stats
                playGame = "";
                addPlayTime();
                int userWin = 0;
                addPlayTime(win);
                break;
              }
            }//end if(gameOver == false)

          }//end while loop turnOver == true

        }//end while loop gameOver == false

      } // end of if(userResponse == 1)


      // end of game, asking user if they wanna go back to title screen or not
      if(gameOver == true){
        
        while(!(playGame.equalsIgnoreCase("yes") || playGame.equalsIgnoreCase("no"))){
          System.out.print("\nDo you want to go back to the title screen? ");
          playGame = kb.nextLine();

          // error proofing!!!
          if(!(playGame.equalsIgnoreCase("yes") || playGame.equalsIgnoreCase("no"))){
            System.out.print("\nPlease enter a 'yes' or 'no': ");
            playGame = kb.nextLine();
          }
        }// this loop should make sure that user enters yes or no


        // if user wants to return to title screen so that it puts a bit of space between text in the console to clear things up
        if(playGame.equalsIgnoreCase("yes")){
          System.out.print("\n\n\n\n");
        }


        // just a if statement that says a goodbye message + system exit if user chooses to leave
        if(playGame.equalsIgnoreCase("no")){ 
          System.out.print("\nSee you next time!");
          System.exit(0);
        }
      }//end of if(gameOver == true)


    }//end of while playGame = yes loop


  }//end public Main






  // METHODS !!!!

  /**
  This method asks the user if they want a tutorial for the game (displays if yes)
  Written by: Nicolas L
  */
  public void requestTutorial()
  {
    Scanner kb = new Scanner(System.in); // scanner needed for yes or no from user

    String tutorial = "";

    // this following while loop makes it so the user has to respond with yes or no
    while(!(tutorial.equalsIgnoreCase("yes") || tutorial.equalsIgnoreCase("no"))){
      System.out.print("\n\nWould you like a tutorial? ");
      tutorial = kb.nextLine();
      if(!(tutorial.equalsIgnoreCase("yes") || tutorial.equalsIgnoreCase("no"))){
        System.out.print("Please respond with 'yes' or 'no'.");
      }
    }

    if(tutorial.equalsIgnoreCase("yes")){ 
      System.out.print("\nPig is a very simple dice game. In this game, you roll a dice\nand the amount you roll is added to your score. You can choose\nto continue rolling or hold and move on to the next turn. Be\ncareful though, if your die lands on 1, your turn is over and\nyou lose all points from this turn. To win, you must be the first\nplayer to reach 100 points.");

      System.out.print("\n\nPress" + "\u001B[35m" + " ENTER " + "\u001B[0m" + "to continue ");
      kb.nextLine();
    }
  }//end requestTutorial method



  /**
  This method simulates a dice roll by generating and returning a number from 1-6
  Written by: Nicolas L
  @return int containing random number from 1-6
  */
  public int diceRoll()
  {
    Random rand = new Random();

    int diceHolder = (rand.nextInt(6)+1); // special trick that makes it go from 1-6 instead of from 0-5

    return diceHolder;
  }



  /**
  This method just displays a 'title screen', and makes the user select between the 3 things that the title screen has to offer.
  Written by: Nicolas L
  @return an int based on the users response
  */
  public static int titleScreen()
  {
    Scanner kb = new Scanner(System.in);

    // this just displays the title screen menu
    System.out.print("\n\n\n________________________________________________" + "\u001B[35m" + "\n\n\tWelcome to Pig (the game)! Oink Oink. 🐷" + "\u001B[0m" + "\n\n________________________________________________");
    System.out.print("\n\n\tChoose between A, B, or C\n\tA. Play Pig\n\tB. View Stats\n\tC. Exit Program\n\n");

    String answer = kb.nextLine();
    int response = 0;

    // this ensures the user enters a valid input
    while(!(answer.equalsIgnoreCase("A") || answer.equalsIgnoreCase("B") || answer.equalsIgnoreCase("C"))){
      System.out.print("\nPlease enter either 'A', 'B', or 'C': ");
      answer = kb.nextLine();
    }


    // the following is unnecessary as I could just return answer instead, but I like to keep things in numbers ideally
    if(answer.equalsIgnoreCase("A")){
      response = 1;
    }

    if(answer.equalsIgnoreCase("B")){
      response = 2;
    }

    if(answer.equalsIgnoreCase("C")){
      response = 3;
    }

    return response;
  }//end of titleScreen() method



  /**
  This method chooses a random name from the file aiNames.txt and gives it to the specified ai
  Written by: Nicolas L
  @return randomly selected name from aiNames.txt file
  */
  public String randName() throws IOException
  {
    // creating file for aiNames.txt
    File aiN = new File("aiNames.txt");
    Scanner nameFile = new Scanner(aiN);

    int counter = 0;

    while(nameFile.hasNext()){ // this while loop counts how many lines there are in aiNames.txt
      nameFile.nextLine();
      counter++;
    }
    nameFile.close();


    String randomAiName = ""; // will be filled with the random name selected

    Random rand = new Random();
    int randomNum = (rand.nextInt(counter)+1); // this will choose a random line in the file

    nameFile = new Scanner(aiN);


    // this loop goes through the file and when it hits the selected line, it will copy it into randomAiName
    for(int i = 1; i < (counter+1); i++){ 
      
      if(i == randomNum){
        randomAiName = nameFile.nextLine();
        break;
      }
      nameFile.nextLine();
    }
    nameFile.close();

    return randomAiName;
    
  }//end randName() method



  /**
  This method chooses a random name from a file given to it by the user
  Written by: Nicolas L
  @param a string containing the name of a file the user wants to use for the AI
  @return randomly selected line from file specified in parameter
  */
  public String randName(String fN) throws IOException
  {
    File aiN = new File(fN);
    if(!(aiN.exists())){// this checks if the file was entered or not and exists if the file is missing
      System.out.print("\n\nError. A file is missing, this program cannot run.");
      System.exit(0);
    }
    Scanner nameFile = new Scanner(aiN);

    int counter = 0;

    while(nameFile.hasNext()){ // this while loop counts how many lines there are in aiNames.txt
      nameFile.nextLine();
      counter++;
    }
    nameFile.close();


    String randomAiName = ""; // will be filled with the random name selected

    Random rand = new Random();
    int randomNum = (rand.nextInt(counter)+1); // this will choose a random line in the file

    nameFile = new Scanner(aiN);

    // this loop goes through the file and when it hits the selected line, it will copy it into randomAiName
    for(int i = 1; i < (counter+1); i++){ 
      
      if(i == randomNum){
        randomAiName = nameFile.nextLine();
        break;
      }
      nameFile.nextLine();
    }
    nameFile.close();

    return randomAiName;
  }



  /**
  This method records how many times this game has been played
  Written by: Nicolas L
  */
  public static void addPlayTime() throws IOException
  {
    File prF = new File("playRecord.txt");

    //check if file doesnt exist
    if(!prF.exists()){
      System.out.print("\n\nError. A file is missing, this program cannot run.");
      System.exit(0);
    }

    // using scanner to record the first line of the file into String previousRecord
    Scanner recordFile = new Scanner(prF);
    String previousRecord = recordFile.nextLine();
    recordFile.close();

    // converting the play record to an int so we can increase it by 1
    int currentRecord = Integer.parseInt(previousRecord);
    currentRecord++;

    // creating a printwriter to rewrite the file with the new play record
    FileWriter fw = new FileWriter("playRecord.txt");
    PrintWriter recordPW = new PrintWriter(fw);

    recordPW.println(currentRecord);
    recordPW.close();
  }



  /**
  This method returns how many times this game has been played
  Written by: Nicolas L
  @return int which holds the number in playRecord.txt
  */
  public int getPlayTime() throws IOException
  {
    File prF = new File("playRecord.txt");

    //check if file doesnt exist
    if(!prF.exists()){
      System.out.print("\n\nError. A file is missing, this program cannot run.");
      System.exit(0);
    }

    // using scanner to record the first line of the file
    Scanner recordFile = new Scanner(prF);
    String currentRecord = recordFile.nextLine();
    recordFile.close();

    int counter = Integer.parseInt(currentRecord);// converting current record into an int

    return counter;
  }



  /**
  This method records how many times the user has won against the ai
  Written by: Nicolas L
  */
  public void addPlayTime(int wR) throws IOException
  {
    // holder = wR (wR will always be 1 or 0)
    // read the line from wins.txt and += holder then fileWriter it to be the new one
    File prF = new File("userWins.txt");

    //check if file doesnt exist
    if(!prF.exists()){
      System.out.print("\n\nError. A file is missing, this program cannot run.");
      System.exit(0);
    }

    // creating scanner so we can copy the wins into a string
    Scanner winsFile = new Scanner(prF);
    String previousWins = winsFile.nextLine();
    winsFile.close();

    // convert string to int so we can increase it by wR (parameter)
    int currentWins = Integer.parseInt(previousWins);
    currentWins++;

    // fileWriter to rewrite the file with new int
    FileWriter fw = new FileWriter("userWins.txt");
    PrintWriter winPW = new PrintWriter(fw);

    winPW.println(currentWins);
    winPW.close();
  }



  /**
  This method outputs how many times the user has won against the ai
  Written by: Nicolas L
  */
  public int getWins() throws IOException
  {
    File prF = new File("userWins.txt");

    //check if file doesnt exist
    if(!prF.exists()){
      System.out.print("\n\nError. A file is missing, this program cannot run.");
      System.exit(0);
    }

    // using scanner to record the first line of the file
    Scanner winsFile = new Scanner(prF);
    String currentWins = winsFile.nextLine();
    winsFile.close();

    int counter = Integer.parseInt(currentWins);// converting current record into an int

    return counter;
  }
  

}// end class main