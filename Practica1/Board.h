
/* 
 * File:   Board.h
 * Author: cristian
 *
 * Created on March 5, 2017, 11:23 AM
 */

#ifndef BOARD_H
#define BOARD_H
class Board{
    int board[10][10];
    int sizeBoard;
public:
    Board();
    void fill(int x, int y, int value);
    int getValue(int x, int y);
};

Board::Board () {
    for(int x = 0; x < 10; x++){
        for(int y = 0; y < 10; y++){
            board[x][y] = 0;
        }
    }
}

void Board::fill(int x, int y, int value){
    board[x][y] = value;
}

int Board::getValue(int x, int y){
    return board[x][y];
}

#endif /* BOARD_H */

