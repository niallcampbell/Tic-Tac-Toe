import java.util.Scanner;
import java.util.Random;

public class Game
{
    private static String player1 = "X";
    private static String player2 = "O";
    private static boolean player = true; //Indicates which player's turn it is. True for player 1, false for player 2
    
    private static String[][] board = new String[3][3]; //2D array
    
    private static boolean nextMove = true; //Stores whether the next move will go ahead or not
    
    private static int count = 0; //The amount of moves that have taken place, max is 9 moves
    
    public static void main()
    {
        populateBoard(); //Fill the board with spaces
        
        String userInput = "";
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Welcome to the game");
        System.out.println();
        
        //Check to see if they want to play 1 or 2 player
        System.out.println("Would you like to play 1 or 2 player? (1/2)");
        userInput = scan.nextLine();
        System.out.println();
        
        if(userInput.equals("1"))
        {
            // 1 player game
            onePlayerGame();
        }
        else if(userInput.equals("2"))
        {
            // 2 player game
            twoPlayerGame();
        }
        
    }
    
    //Randomise which player goes first
    public static void randomiseTurn()
    {
        Random rand = new Random();
        int turn = rand.nextInt(2) + 1; //2 is the max number and 1 is the minimum
        
        System.out.println("The player who goes first will be chosen at random.");
        System.out.println();
        
        if(turn == 1)
        {
            //Then we will go first
            player = true;
            player1 = "X";
            player2 = "O";
            
            System.out.println("Player 1 is X");
            System.out.println("Player 2 is O");
            System.out.println("Player 1 will go first");
            System.out.println();
            System.out.println();
        }
        else
        {
            //Otherwise the computer will go first
            player = false;
            player1 = "O";
            player2 = "X";
            
            System.out.println("Player 1 is O");
            System.out.println("Player 2 is X");
            System.out.println("Player 2 will go first");
            System.out.println();
            System.out.println();
        }
    }
    
    //Versus the computer
    public static void onePlayerGame()
    {
        System.out.println("One Player Game");
        System.out.println("You will play against the computer");
        
        String userInput = "";
        Scanner scan = new Scanner(System.in);
        
        System.out.println();
        System.out.println("You are player 1");
        System.out.println("The computer is player 2");
        System.out.println();
        
        randomiseTurn(); //Randomise who goes first
        
        //Row and column of board to be updated
        int row = 0;
        int column = 0;
        
        boolean format = false; //True if input by user is in the correct format
        boolean isValidMove = false; //True if the space chosen by the user is free
        boolean hasWon = false; //Indicates if a player has won the game
        
        System.out.println("The board:");
        System.out.println();
        printBoard();
        System.out.println();
        
        while(nextMove == true)
        {
            if(count == 9)
            {
                System.out.println("There are no moves left. The match is a draw.");
                break;
            }
            
            if(player == true)
            {
                System.out.println();
                System.out.println("Player 1's turn");
                
                System.out.println();
                System.out.println("Please enter the row and column number you wish to take, e.g. 1 1 ");
                
                while(format == false || isValidMove == false)
                {
                    //Ensure the format player 1 has entered is correct
                    while(format == false)
                    {
                        userInput = scan.nextLine();
                        
                        format = checkFormat(userInput); //returns true if correct
                        
                        isValidMove = false;
                        
                        if(format == false)
                        {
                            System.out.println("Please enter in correct format");
                        }
                        
                    }
                    
                    //Ensure that the move is valid
                    while(isValidMove == false)
                    {
                        //Break up user input to get values
                        String[] input = userInput.split(" ");
                        row = Integer.parseInt(input[0]) - 1; //Take away 1 as dealing with arrays
                        column = Integer.parseInt(input[1]) - 1; //Take away 1 as dealing with arrays
                            
                        //Check if valid move
                        isValidMove = validMove(row, column);
                            
                        if(isValidMove == false)
                        {
                            System.out.println("This space is already taken");
                            System.out.println("Please enter another valid move");
                            format = false;
                            break; //Go back up and check again if it is the correct format before checking if the new move is valid
                        }
                        
                    }
                }
                
                System.out.println();
                
                //It is a valid move so make the move
                updateBoard(row, column, player);
                
                //Check if the player 1's last move has caused them to win
                hasWon = checkIfWinningMove(player1);
                    
                if(hasWon == true)
                {
                    System.out.println("Player 1 has won!");
                    nextMove = false; //No need for next move
                }
                
            }
            else if(player == false)
            {
                System.out.println();
                System.out.println("Player 2's turn");
                System.out.println();
                
                //Call method to execute player 2's turn
                hasWon = computerTurn();
                
                System.out.println();
                
                if(hasWon == true)
                {
                    nextMove = false; //Player 2 has won
                }
            }
            
            
            //Switch players
            if(player == true)
            {
                player = false;
            }
            else if(player == false)
            {
                player = true;
            }
            
            format = false; //Change format back to false so that the next move can be checked
            isValidMove = false; //Change back to false so that the next move can be checked to see if it is valid
            
        }
    }
    
    //Computer's turn in 1 player game
    //Returns true if the user has made a winning move, otherwise returns false
    /*
    
     * Check to see if there is a winning move
     *      -> Is there 2 O's together?
     *      -> Is there a free slot beside these O's?
     *      -> If so take the move
     *      
     * Print the computer's move
       
    */
    public static boolean computerTurn()
    {
        boolean hasWon = false;
        
        //First check if there are any winning moves in the rows
        boolean winRow = checkRowsForWinningMove();
        
        if(winRow == true)
        {
             System.out.println("Player 2 has won!");
             hasWon = true;
             return hasWon;
        }
        
        
        //Now check if there are any winning moves in the columns
        boolean winCol = checkColumnsForWinningMove();
        
        if(winCol == true)
        {
             System.out.println("Player 2 has won!");
             hasWon = true;
             return hasWon;
        }
        
        
        //Now check if there are any winning moves diagonally
        boolean winDiag = checkDiagonalsForWinningMove();
        
        if(winDiag == true)
        {
             System.out.println("Player 2 has won!");
             hasWon = true;
             return hasWon;
        }
        
        
        
        //Otherwise there are currently no winning moves for the computer to take. 
        //Now instead we must check to see if the opponent has a winnning move available to them after our move.
        //If they do we must block this move by taking that slot. 
        
        
        boolean blockRow = checkRowsToBlockOpponentMove();
        
        if(blockRow == true)
        {
             System.out.println("Player 2 has taken his turn.");
             return hasWon;
        }
        
        boolean blockCol = checkColumnsToBlockOpponentMove();
        
        if(blockCol == true)
        {
             System.out.println("Player 2 has taken his turn.");
             return hasWon;
        }
        
        boolean blockDiag = checkDiagonalsToBlockOpponentMove();
        
        if(blockDiag == true)
        {
             System.out.println("Player 2 has taken his turn.");
             return hasWon;
        }
        
        
        
        //If there are no moves to block either, then we will check if we can make a fork.
        //Check if the middle slot is free. If it is then take it. 
        //If the middle slot is already ours, take a corner slot if there is one free. 
        //If the middle slot is taken, then we want to stop the opponent from being able to create a fork. 
        //Check if there are any corner slots free. If there are, take one of them.
        //Otherwise if there are no corner slots left take a middle slot
        
        
        
        
         if(board[1][1].equals(" "))
        {
            updateBoard(1, 1, player); //Take the middle slot if it is free
        }
        else
        {
            //Take one of the corners
            if(board[0][0].equals(" "))
            {
                updateBoard(0, 0, player);
            }
            else if(board[0][2].equals(" "))
            {
                updateBoard(0, 2, player);
            }
            else if(board[2][0].equals(" "))
            {
                updateBoard(2, 0, player);
            }
            else if(board[2][2].equals(" "))
            {
                updateBoard(2, 2, player);
            }
            else
            {
                //Otherwise if all the corner slot sare taken we must take a middle slot
                if(board[0][1].equals(" "))
                {
                    updateBoard(0, 1, player);
                }
                else if(board[1][0].equals(" "))
                {
                    updateBoard(1, 0, player);
                }
                else if(board[1][2].equals(" "))
                {
                    updateBoard(1, 2, player);
                }
                else if(board[2][1].equals(" "))
                {
                    updateBoard(2, 1, player);
                }
            }
        }
            
        
        
        /*
        
        if(board[1][1].equals(" "))
        {
            updateBoard(1, 1, player); //Take the middle slot if it is free
        }
        else
        {
            //Go through the corners on the board and count the ones that are free
            
            int count = 0;
            
            if(board[0][0].equals(" "))
            {
                count++;
            }
            
            if(board[0][2].equals(" "))
            {
                count++;
            }
            
            if(board[2][0].equals(" "))
            {
                count++;
            }
            
            if(board[2][2].equals(" "))
            {
                count++;
            }
            
            //Only take a corner if there is one free
            if(count != 0)
            {
                int[] myArray = new int[count]; //This array will store the numbers of each corner that is free
            
                int slot = 0; //Used to populate the slots in the array
                
                if(board[0][0].equals(" "))
                {
                    myArray[slot] = 1;
                    slot++;
                }
                
                if(board[0][2].equals(" "))
                {
                    myArray[slot] = 2;
                    slot++;
                }
                
                if(board[2][0].equals(" "))
                {
                    myArray[slot] = 3;
                    slot++;
                }
                
                if(board[2][2].equals(" "))
                {
                    myArray[slot] = 4;
                    slot++;
                }
                
                Random rand = new Random();
                int rndm = rand.nextInt(count + 1) + 1; //Chooses a random slot in the array of free corners (returns a number between 1 and the array size + 1)
                
                //Choice will store the corner number the computer will take
                int choice = myArray[rndm - 1]; //Take 1 from the random number as we are dealing with an array 
                
                if(choice == 1)
                {
                    updateBoard(0, 0, player);
                }
                else if(choice == 2)
                {
                    updateBoard(0, 2, player);
                }
                else if(choice == 3)
                {
                    updateBoard(2, 0, player);
                }
                else if(choice == 4)
                {
                    updateBoard(2, 2, player);
                }
                
            }
            else
            {
                //Check the middle slots
                //Go through the middle slots on the board and count the ones that are free
            
                int countMid = 0; //Will store the count of the number of middle slots free
                
                if(board[0][1].equals(" "))
                {
                    countMid++;
                }
                
                if(board[1][0].equals(" "))
                {
                    countMid++;
                }
                
                if(board[1][2].equals(" "))
                {
                    countMid++;
                }
                
                if(board[2][1].equals(" "))
                {
                    countMid++;
                }
                
                //Only take a middle slot if there is one free
                if(countMid != 0)
                {
                    int[] myArrayMid = new int[countMid]; //This array will store the numbers of each middle that is free
                
                    int slotMid = 0; //Used to populate the slots in the array
                    
                    if(board[0][1].equals(" "))
                    {
                        myArrayMid[slotMid] = 1;
                        slotMid++;
                    }
                    
                    if(board[1][0].equals(" "))
                    {
                        myArrayMid[slotMid] = 2;
                        slotMid++;
                    }
                    
                    if(board[1][2].equals(" "))
                    {
                        myArrayMid[slotMid] = 3;
                        slotMid++;
                    }
                    
                    if(board[2][1].equals(" "))
                    {
                        myArrayMid[slotMid] = 4;
                        slotMid++;
                    }
                    
                    Random randMid = new Random();
                    int rndmMid = randMid.nextInt(countMid + 1) + 1; //Chooses a random slot in the array of free corners (returns a number between 1 and the array size + 1)
                    
                    //Choice will store the corner number the computer will take
                    int choiceMid = myArrayMid[rndmMid - 1]; //Take 1 from the random number as we are dealing with an array 
                    
                    if(choiceMid == 1)
                    {
                        updateBoard(0, 1, player);
                    }
                    else if(choiceMid == 2)
                    {
                        updateBoard(1, 0, player);
                    }
                    else if(choiceMid == 3)
                    {
                        updateBoard(1, 2, player);
                    }
                    else if(choiceMid == 4)
                    {
                        updateBoard(2, 1, player);
                    }
                    
                }
            }*/
            
        
        
        return hasWon;
    }
    
    
    //When it is the computer's turn, this method will check each row for a winning move.
    // If there is a winning move, it will take the move and return true. 
    public static boolean checkRowsForWinningMove()
    {
        int row = 3; //Number of rows
        int column = 0;
        
        boolean hasWon = false; //Indicates if a player has won the game
        
        // Loop through each row and check for a winning move on that row, e.g. see if there are two O's on that row and take the third column to win the game
        //Here i = the row number
        for(int i = 0; i < row; i++) //Loop through the rows
        {
            column = 0; //Reset column back to 0 for checking the next row
            
            //Check if player 2 has a value in the left column of the row
            if(board[i][column].equals(player2))
            {
                //There is a value in the left column of the row. 
                //Now check if there is also a value in the middle column.
                //If there is, this means a winning move is available in the right column. Take it. 
                column++; //Now pointing at middle column
                
                //If the middle column is O and the right column is empty then take the move
                if(board[i][column].equals(player2) && board[i][column + 1].equals(" "))
                {
                    //There is a O in the middle
                    //This means there is a winning move in the right column
                    column++; //pointing at the right column
                    updateBoard(i, column, player); //Take the move
                    
                    hasWon = checkIfWinningMove(player2); //Confirms it is a winning move
                
                    if(hasWon == true)
                    {
                        //printBoard();
                        nextMove = false; //No need for next move
                        return hasWon; //Break out of loop 
                    }
                }
                else
                {
                    //There is no O in the middle column. 
                    //Check if there is a O in the right column instead.
                    //If there is then take the middle column as it is a winning move
                    
                    column++; //Now pointing at right column
                    
                    //If the right column is O and the middle column is empty then take the move
                    if(board[i][column].equals(player2) && board[i][column - 1].equals(" "))
                    {
                        //There is a O in the right column as well as the left and the middle is empty
                        //This means there is a winning move in the middle column
                        
                        updateBoard(i, column, player); //Take the move
                        
                        hasWon = checkIfWinningMove(player2); //Confirms it is a winning move
                    
                        if(hasWon == true)
                        {
                            //printBoard();
                            nextMove = false; //No need for next move
                            return hasWon; //Break out of loop 
                        }
                        
                    }
                }
            }
            //Otherwise there is no O in the left column.
            //Check if there is a O in the middle and right columns. 
            //column is currently pointing at the middle column. Check if there is a O here
            else
            {
                column++; //Now pointing at the middle column
                
                //Check if there is a O in the middle column.
                if(board[i][column].equals(player2))
                {
                    //There is a O in the middle. Now check if there is one in the right column.
                    column++; //Pointing at the right column
                    
                    if(board[i][column].equals(player2))
                    {
                        //There is a winning move if we take the left column as there are O's in the middle and right columns
                        column = 0;
                        
                        //Make sure the space is empty first. If it is take the move
                        if(board[i][column].equals(" "))
                        {
                            updateBoard(i, column, player); //Take the left column
                                
                            hasWon = checkIfWinningMove(player2);
                                
                            if(hasWon == true)
                            {
                                //printBoard();
                                nextMove = false; //No need for next move
                                return hasWon; //Break out of loop 
                            }
                        }
                        
                    }
                }
            }
        }
        
        return hasWon; // There were no winning moves in any of the rows. 
    }
    
    //Check the columns for a potential winning move for the computer
    //Take the move and return true if there is
    public static boolean checkColumnsForWinningMove()
    {
    
        int row = 0; //The row of a given column that we are currently checking 
        int column = 3; //Number of columns
        boolean hasWon = false; //Indicates if a player has won the game
        
        
        // Loop through each column and check for a winning move on that column, i.e. see if there are two O's on that column and take the third row to win the game
        //Here i = the column number that we are checking 
        for(int i = 0; i < column; i++) //Loop through the columns
        {
            row = 0; //Set row back to 0 for checking the next column
            
            //Check if there is a O in the top row of the column
            if(board[row][i].equals(player2))
            {
                //There is a 'O' in the top row of the column. 
                //Now check if there is also a O in the middle row of that column and that the bottom row is empty.
                //If there is, this means a winning move is available in the bottom row of the column. Take it. 
                row++; //Now pointing at middle row
                
                if(board[row][i].equals(player2) && board[row + 1][i].equals(" "))
                {
                    //There is a O in the middle row of the column
                    //This means there is a winning move in the bottom row
                    row++; //pointing at the bottom row
                    updateBoard(row, i, player); //Take the move
                    
                    hasWon = checkIfWinningMove(player2); //Confirms it is a winning move
                
                    if(hasWon == true)
                    {
                        //printBoard();
                        nextMove = false; //No need for next move
                        return hasWon; //Break out of loop 
                    }
                }
                else
                {
                    //There is no O in the middle row. 
                    //Check if there is a O in the bottom row of the column instead.
                    //If there is then take the middle row of that column as it is a winning move
                    
                    row++; //Now pointing at bottom row of the column
                    
                    if(board[row][i].equals(player2))
                    {
                        //There is a O in the bottom right of the column as well as the top row
                        //This means there is a winning move in the middle row if is is empty
                        row--; // now pointing at the middle row
                        
                        if(board[row][i].equals(" "))
                        {
                            updateBoard(row, i, player); //Take the move
                        
                            hasWon = checkIfWinningMove(player2); //Confirms it is a winning move
                    
                            if(hasWon == true)
                            {
                                //printBoard();
                                nextMove = false; //No need for next move
                                return hasWon; //Break out of loop 
                            }
                        }
                        
                    }
                }
            }
            //Otherwise there is no O in the top row of the column.
            //Check if there is a O in the middle and bottom rows of the column. 
            //row is currently pointing at the middle row. Check if there is a O here
            else
            {
                row++; //Now pointing at the middle row of the column
                
                //Check if there is a O in the middle row.
                if(board[row][i].equals(player2))
                {
                    //There is a O in the middle row. Now check if there is one in the bottom row.
                    row++; //Pointing at the bottom row of the column
                    
                    if(board[row][i].equals(player2))
                    {
                        //There is a winning move if we take the top row as there are O's in the middle and bottom rows of the column
                        row = 0;
                        
                        //If the middle row is empty make the move
                        if(board[row][i].equals(" "))
                        {
                            updateBoard(row, i, player); //Take the left column
                                
                            hasWon = checkIfWinningMove(player2);
                                
                            if(hasWon == true)
                            {
                                //printBoard();
                                nextMove = false; //No need for next move
                                return hasWon; //Break out of loop 
                            }
                        }
                        
                    }
                }
            }
        }
        
        return hasWon;
        
    }
    
    
    //Check the diagonals for a potential winning move for the computer
    //Take the move and return true if there is
    public static boolean checkDiagonalsForWinningMove()
    {
        
        boolean hasWon = false; //Becomes true if there is a winning move
        
        //First check if the middle position is ours
        //If it is then check each of the 4 corners 
        //For each corner check if it is free. If it is then check if the opposite corner is free. 
        //If it is then take the move as it is a winning move. 
        //If it is not free then break from this
        
        //Check the middle position
        if(board[1][1].equals(player2))
        {
            //The middle position is ours
            
            //Check the top left sqaure
            if(board[0][0].equals(player2))
            {
                //The top left square is ours as well
                
                //Check the bottom right square to see if it is free
                if(board[2][2].equals(" "))
                {
                    //The bottom right is empty. This is a winning move. Take it.
                    updateBoard(2, 2, player); //Take the move
                    
                    hasWon = checkIfWinningMove(player2);
                    
                    if(hasWon == true)
                    {
                        //printBoard();
                        nextMove = false; //No need for next move
                    }
                }
            }
            else if(board[0][2].equals(player2)) //Check the top right square
            {
                //The top right square is ours as well
                
                //Check the bottom left square to see if it is free
                if(board[2][0].equals(" "))
                {
                    //The bottom left is empty. This is a winning move. Take it.
                    updateBoard(2, 0, player); //Take the move
                    
                    hasWon = checkIfWinningMove(player2);
                    
                    if(hasWon == true)
                    {
                        //printBoard();
                        nextMove = false; //No need for next move
                    }
                }
            }
            else if(board[2][0].equals(player2)) //Check the bottom left square
            {
                //The bottom left square is ours as well
                
                //Check the top right square to see if it is free
                if(board[0][2].equals(" "))
                {
                    //The top right is empty. This is a winning move. Take it.
                    updateBoard(0, 2, player); //Take the move
                    
                    hasWon = checkIfWinningMove(player2);
                    
                    if(hasWon == true)
                    {
                        //printBoard();
                        nextMove = false; //No need for next move
                    }
                }
            }
            else if(board[2][2].equals(player2)) //Check the bottom right square
            {
                //The bottom right square is ours
                
                //Check the top left square to see if it is free
                if(board[0][0].equals(" "))
                {
                    //The top left is empty. This is a winning move. Take it.
                    updateBoard(0, 0, player); //Take the move
                    
                    hasWon = checkIfWinningMove(player2);
                    
                    if(hasWon == true)
                    {
                        //printBoard();
                        nextMove = false; //No need for next move
                    }
                }
            }
        }
    
        
        return hasWon;
    }
    
    //When it is the computer's turn, this method will check each row to see if there is a winning move for the opponent.
    //If there is a winning move for the opponent, the computer will block this move by taking the square and return true. 
    //If there is no move to be blocked it will return false. 
    public static boolean checkRowsToBlockOpponentMove()
    {
        boolean blockMove = false; //Indicates that there is a move to be blocked
        
        int row = 3; //Number of rows
        int column = 0;
        
        // Loop through each row and check for a winning move for the opponent on that row, i.e. see if there are two X's on that row and take the third column as an O to block
        //Here i = the row number
        for(int i = 0; i < row; i++) //Loop through the rows
        {
            column = 0;
            
            //Check if there is a X in the left column of the row
            if(board[i][column].equals(player1))
            {
                //There is a 'X' in the left column of the row. 
                //Now check if there is also a X in the middle column and the right column is empty
                //If there is, this means a winning move is available in the right column. Block it. 
                column++; //Now pointing at middle column
                
                if(board[i][column].equals(player1) && board[i][column + 1].equals(" "))
                {
                    //There is a X in the middle
                    //This means there is a winning move in the right column
                    column++; //pointing at the right column
                    updateBoard(i, column, player); //Take this square to block the opponent winning
                    blockMove = true; //There has been a move blocked
                }
                else
                {
                    //There is no X in the middle column. 
                    //Check if there is a X in the right column instead and that the middle column is empty
                    //If there is then take the middle column as it is a winning move
                    
                    column++; //Now pointing at right column
                    
                    if(board[i][column].equals(player1) && board[i][column - 1].equals(" "))
                    {
                        //There is a O in the right column as well as the left
                        //This means there is a winning move in the middle column
                        column--; // now pointing at the middle column
                        updateBoard(i, column, player); //Take the move to block
                        blockMove = true; //There has been a move blocked
                    }
                }
            }
            //Otherwise there is no X in the left column.
            //Check if there is a X in the middle and right columns. 
            //column is currently pointing at the middle column. Check if there is a X here
            else
            {
                column++; //Now pointing at the middle column
                
                //Check if there is a X in the middle column.
                if(board[i][column].equals(player1))
                {
                    //There is a X in the middle. Now check if there is one in the right column and that the left column is free
                    column++; //Pointing at the right column
                    
                    if(board[i][column].equals(player1) && board[i][0].equals(" "))
                    {
                        //There is a winning move for the opponent in the fetf column. Block it
                        column = 0;
                        updateBoard(i, column, player); //Take the left column to block
                        blockMove = true; //There has been a move blocked
                    }
                }
            }
        }
        
        return blockMove;
    
    }
    
    //When it is the computer's turn, this method will check each column to see if there is a winning move for the opponent.
    //If there is a winning move for the opponent, the computer will block this move by taking the square and return true. 
    //If there is no move to be blocked it will return false. 
    public static boolean checkColumnsToBlockOpponentMove()
    {
    
        boolean blockMove = false; //Indicates that there is a move to be blocked
        
        int row = 0; //Number of rows
        int column = 3;
        
        
        // Loop through each column and check for a winning move on that column, i.e. see if there are two X's on that column and take the third row to block the opponent winning the game
        //Here i = the column number that we are checking 
        for(int i = 0; i < column; i++) //Loop through the columns
        {
            row = 0;
            
            //Check if there is a X in the top row of the column
            if(board[row][i].equals(player1))
            {
                //There is a 'X' in the top row of the column. 
                //Now check if there is also a X in the middle row of that column.
                //If there is, this means a winning move is available in the bottom row of the column. Block it. 
                row++; //Now pointing at middle row
                
                if(board[row][i].equals(player1) && board[row + 1][i].equals(" "))
                {
                    //There is a X in the middle row of the column and the bottom row is empty
                    //This means there is a winning move in the bottom row
                    row++; //pointing at the bottom row
                    updateBoard(row, i, player); //Take the move
                    blockMove = true; //The move has been blocked
                }
                else
                {
                    //There is no X in the middle row. 
                    //Check if there is a X in the bottom row of the column instead.
                    //If there is then take the middle row of that column as it is a winning move
                    
                    row++; //Now pointing at bottom row of the column
                    
                    if(board[row][i].equals(player1) && board[row - 1][i].equals(" "))
                    {
                        //There is a X in the bottom row of the column as well as the top row and the middle row is empty
                        //This means there is a winning move in the middle row
                        row--; // now pointing at the middle row
                        updateBoard(row, i, player); //Take the move
                        blockMove = true; //The move has been blocked
                    }
                }
            }
            //Otherwise there is no X in the top row of the column.
            //Check if there is a X in the middle and bottom rows of the column. 
            //row is currently pointing at the middle row. Check if there is a X here
            else
            {
                row++; //Now pointing at the middle row of the column
                
                //Check if there is a X in the middle row.
                if(board[row][i].equals(player1))
                {
                    //There is a X in the middle row. Now check if there is one in the bottom row.
                    row++; //Pointing at the bottom row of the column
                    
                    if(board[row][i].equals(player1) && board[0][i].equals(" "))
                    {
                        //There is a winning move if we take the top row as there are O's in the middle and bottom rows of the column and the top row is empty
                        row = 0;
                        updateBoard(row, i, player); //Take the left column
                        blockMove = true; //The move has been blocked
                    }
                }
            }
        }
        
        return blockMove;
    
    }
    
    
    //Check the diagonals for a potential winning move for the opponent
    //Take this move to block them winning and return true 
    //Otherwise if there is no move to block then return false
    public static boolean checkDiagonalsToBlockOpponentMove()
    {
        boolean blockMove = false; //Indicates that there is a move to be blocked
    
        //First check if the middle position is an X
        //If it is then check each of the 4 corners 
        //For each corner check if it is an X. If it is then check if the opposite corner is free. 
        //If it is then take the move as it is a winning move so we must block it. 
        //If it is not free then break from this
        
        //Check the middle position
        if(board[1][1].equals(player1))
        {
            //The middle position is an X
            
            //Check the top left sqaure
            if(board[0][0].equals(player1))
            {
                //The top left square is an X as well
                
                //Check the bottom right square to see if it is free
                if(board[2][2].equals(" "))
                {
                    //The bottom right does not have an X in it. This is a winning move. Take it to block them winning.
                    updateBoard(2, 2, player); //Take the move
                    blockMove = true; //Move has been blocked

                }
            }
            else if(board[0][2].equals(player1)) //Check the top right square
            {
                //The top right square is an X as well
                
                //Check the bottom left square to see if it is free
                if(board[2][0].equals(" "))
                {
                    //The bottom left does not have an X in it. This is a winning move. Take it to block them winning.
                    updateBoard(2, 0, player); //Take the move
                    blockMove = true; //Move has been blocked
                }
            }
            else if(board[2][0].equals(player1)) //Check the bottom left square
            {
                //The bottom left square is an X as well
                
                //Check the top right square to see if it is free
                if(board[0][2].equals(" "))
                {
                    //The top right does not have an X in it. This is a winning move. Take it to block them winning.
                    updateBoard(0, 2, player); //Take the move
                    blockMove = true; //Move has been blocked
                }
            }
            else if(board[2][2].equals(player1)) //Check the bottom right square
            {
                //The bottom right square is an X
                
                //Check the top left square to see if it is free
                if(board[0][0].equals(" "))
                {
                    //The top left does not have an X in it. This is a winning move. Take it to block them winning.
                    updateBoard(0, 0, player); //Take the move
                    blockMove = true; //Move has been blocked
                }
            }
        }
        
        return blockMove;
    }
    
    //Versus a friend
    public static void twoPlayerGame()
    {
        System.out.println("Two Player Game");
        System.out.println();
        
        String userInput = "";
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Player 1 is X");
        System.out.println("Player 2 is O");
        System.out.println("Player 1 will go first");

        
        //Row and column of board to be updated
        int row = 0;
        int column = 0;
        
        boolean format = false; //True if input by user is in the correct format
        boolean isValidMove = false; //True if the space chosen by the user is free
        boolean hasWon = false; //Indicates if a player has won the game
        
        printBoard();
        
        while(nextMove == true)
        {
            if(count == 9)
            {
                System.out.println("There are no moves left. The match is a draw.");
                break;
            }
            
            if(player == true)
            {
                System.out.println();
                System.out.println("Player 1's turn");
            }
            else if(player == false)
            {
                System.out.println();
                System.out.println("Player 2's turn");
            }
            
            System.out.println();
            System.out.println("Please enter the row and column number you wish to take, e.g. 1 1 ");
            System.out.println();
            
            while(format == false || isValidMove == false)
            {
                //Ensure format is correct
                while(format == false)
                {
                    userInput = scan.nextLine();
                    
                    format = checkFormat(userInput);
                    
                    isValidMove = false;
                    
                    if(format == false)
                    {
                        System.out.println("Please enter in correct format");
                    }
                    
                }
                
                //Ensure that the move is valid
                while(isValidMove == false)
                {
                    //Break up user input to get values
                    String[] input = userInput.split(" ");
                    row = Integer.parseInt(input[0]) - 1; //Take away 1 as dealing with arrays
                    column = Integer.parseInt(input[1]) - 1; //Take away 1 as dealing with arrays
                        
                    //Check if valid move
                    isValidMove = validMove(row, column);
                        
                    if(isValidMove == false)
                    {
                        System.out.println("This space is already taken");
                        System.out.println("Please enter another valid move");
                        format = false;
                        break; //Go back up and check if it is the correct format before checking if the new move is valid
                    }
                    
                }
            }
            
            updateBoard(row, column, player);
            
            //Check if the player's last move has caused them to win
            if(player == true)
            {
                hasWon = checkIfWinningMove(player1);
                
                if(hasWon == true)
                {
                    System.out.println("Player 1 has won!");
                    nextMove = false; //No need for next move
                }
                
            }
            else if(player == false)
            {
                hasWon = checkIfWinningMove(player2);
                
                if(hasWon == true)
                {
                    System.out.println("Player 2 has won!");
                    nextMove = false; //No need for next move
                }
                
            }
            
            //Switch players
            if(player == true)
            {
                player = false;
            }
            else if(player == false)
            {
                player = true;
            }
            
            format = false; //Change format back
            isValidMove = false; //Change back
            
        }
    }
    
    
    
    public static void populateBoard()
    {
        for(int i = 0; i < 3; i++) //Print Row
        {
            for(int j = 0; j < 3; j++) //Print column
            {
                board[i][j] = " ";
            }
        }
    }
    
    public static void printBoard()
    {
        System.out.println("-------");
        
        for(int i = 0; i < 3; i++) //Print Row
        {
            for(int j = 0; j < 3; j++) //Print column
            {
                System.out.print("|" + board[i][j]);
            }
            System.out.println("|");
            System.out.println("-------");
        }
    }
    
    //Checks format of the user input to ensure it is of format: int space int
    public static boolean checkFormat(String inFormat)
    {
        boolean correct;
        
        String[] input = inFormat.split(" ");
        
        if((input[0].equals("1") || input[0].equals("2") || input[0].equals("3")) && (input[1].equals("1") || input[1].equals("2") || input[1].equals("3")))
        {
            correct = true;
        }
        else
        {
            correct = false;
        }
        
        return correct;
        
    }
    
    public static void updateBoard(int row, int column, boolean value)
    {
        if(value == true) //Player 1
        {
            board[row][column] = player1;
        }
        else if(value == false) //Player 2
        {
            board[row][column] = player2;
        }
        
        printBoard();
        
        count++;
    }
    
    //Check to see if the space has already been taken
    public static boolean validMove(int row, int column)
    {
        boolean valid;
        
        if(board[row][column] == "X" || board[row][column] == "O")
        {
            valid = false;
        }
        else
        {
            valid = true;
        }
        
        return valid;
    }
    
    //Check to see if the move that the user has selected has caused them to win the game
    public static boolean checkIfWinningMove(String player)
    {
        // Check if the player has won by checking winning conditions.
        if(board[0][0] == player && board[0][1] == player && board[0][2] == player || // 1st row
            board[1][0] == player && board[1][1] == player && board[1][2] == player || // 2nd row
            board[2][0] == player && board[2][1] == player && board[2][2] == player || // 3rd row
            board[0][0] == player && board[1][0] == player && board[2][0] == player || // 1st col.
            board[0][1] == player && board[1][1] == player && board[2][1] == player || // 2nd col.
            board[0][2] == player && board[1][2] == player && board[2][2] == player || // 3rd col.
            board[0][0] == player && board[1][1] == player && board[2][2] == player || // Diagonal \ 
            board[2][0] == player && board[1][1] == player && board[0][2] == player) //   Diagonal /
            {
                return true;
        }
        else 
        {
            return false;
        }
    }
}

