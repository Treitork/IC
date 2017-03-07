/* 
 * File:   main.cpp
 * Author: cristian
 *
 * Created on March 5, 2017, 10:49 AM
 */

#include <cstdlib>
#include <iostream>
#include <list>
#include <fstream>
#include <math.h>
#include "Board.h"
using namespace std;

struct node {
    int x;
    int y;
    double gn;
    double hn;
    double fn;
    int parentX;
    int parentY;

    bool operator==(const node& a) const {
        return (x == a.x && y == a.y);
    }

    node& operator=(const node& a) {
        x = a.x;
        y = a.y;
        gn = a.gn;
        hn = a.hn;
        fn = a.fn;
        parentX = a.parentX;
        parentY = a.parentY;
        return *this;
    }
};

node getLowerFn(list<node> myList) {
    node actualMinor = myList.front();
    for (list<node>::iterator it = myList.begin(); it != myList.end(); ++it) {
        if (actualMinor.fn > it->fn) {
            actualMinor = *it;
        }
    }
    return actualMinor;
}

double getDistance(int x1, int y1, int x2, int y2) {
    return sqrt(pow((x2 - x1), 2) + pow((y2 - y1), 2));
}

void initializeNode(node &target, int x, int y, double gn, double hn, double fn) {
    target.y = y;
    target.x = x;
    target.gn = gn;
    target.hn = hn;
    target.fn = fn;
}

int readBoard(Board &board, int &xIni, int &yIni, int &xEnd, int &yEnd, int &size) {
    string line;
    ifstream myfile("board.txt");
    bool sizeIsKnown = false;
    int row, value;
    if (myfile.is_open()) {
        while (getline(myfile, line)) {
            if (!sizeIsKnown) {
                size = atoi(line.c_str());
                row = size;
                sizeIsKnown = true;
            } else {
                for (int i = 1; i <= size; i++) {
                    switch (line[i - 1]) {
                        case 'X':
                            value = -1;
                            break;
                        case 'S':
                            value = 1;
                            break;
                        case 'I':
                            xIni = row;
                            yIni = i;
                            value = 2;
                            break;
                        case 'F':
                            xEnd = i;
                            yEnd = row;
                            value = 3;
                            break;
                        default:
                            break;
                    }
                    board.fill(row, i, value);
                }
                row--;
            }
        }
        myfile.close();
    } else {
        cout << "board.txt not found" << endl;
        return -1;
    }
    return 0;
}

//dir possible values: 0 to 7, 0 is bottom, clockwise direction
//x and y are coordinates of current node
//size is the size of the board

bool checkCorrectPosition(Board board, int dir, int x, int y, int size, int &finalX, int &finalY) {
    bool check = true;
    switch (dir) {
        case 0:
            y--;
            break;
        case 1:
            y--;
            x--;
            break;
        case 2:
            x--;
            break;
        case 3:
            x--;
            y++;
            break;
        case 4:
            y++;
            break;
        case 5:
            x++;
            y++;
            break;
        case 6:
            x++;
            break;
        case 7:
            x++;
            y--;
            break;
    }
    if (x > size || x <= 0 || y > size || y <= 0) {
        check = false;
    } else if (board.getValue(y, x) != 1) {
        check = false;
        if (board.getValue(y, x) == 3) { //the end
            check = true;
        }
    }
    finalX = x;
    finalY = y;
    return check;
}

bool listContainsNode(list<node> myList, node target, node &nodeFound) {
    bool found = false;
    for (list<node>::iterator it = myList.begin(); it != myList.end(); ++it) {
        if (*it == target) { //operator ==
            found = true;
            nodeFound = *it; //operator =
        }
    }
    return found;
}

void displayLista(list<node> myList) {
    cout << "---------------------List------------------------" << endl;
    for (list<node>::iterator it = myList.begin(); it != myList.end(); ++it) {
        cout << it->x << ":" << it->y << endl;
    }
    cout << "---------------------End------------------------" << endl;
}

void displaySolution(list<node> myList, int x, int y) {
    list<node> aux;
    cout << "Numero | Fila | Columna" << endl;
    cout << "-----------------------" << endl;
    while (x != -1 && y != -1) {
        for (list<node>::iterator it = myList.begin(); it != myList.end(); ++it) {
            if (it->x == x && it->y == y) {
                aux.push_front(*it);
                x = it->parentX;
                y = it->parentY;
                break;
            }
        }
    }
    int i = 0;
    for (list<node>::iterator it = aux.begin(); it != aux.end(); ++it) {
        cout << i <<":\t(" << it->x << ",\t" << it->y << ")" << endl;
        i++;
    }
}

void aStar() {
    list<node> open, closed;
    int xIni, yIni, xEnd, yEnd, size, successorX, successorY;
    double currentCost;
    Board board;
    if (readBoard(board, xIni, yIni, xEnd, yEnd, size) != 0)
        return;
    //board is filled from here
    node start, current, successor, successorFound;
    initializeNode(start, xIni, yIni, 0, getDistance(xIni, yIni, xEnd, yEnd), getDistance(xIni, yIni, xEnd, yEnd));
    start.parentX = -1;
    start.parentY = -1;
    //initial node filled
    open.push_back(start);
    //astar algorithm
    while (!open.empty()) {
        current = getLowerFn(open);
        if (board.getValue(current.y, current.x) == 3) {
            closed.push_back(current);
            break; //we found the solution
        }
        //generate each node successor
        for (int dir = 0; dir < 8; dir++) {
            if (checkCorrectPosition(board, dir, current.x, current.y, size, successorX, successorY)) {
                successor.x = successorX;
                successor.y = successorY;
                currentCost = current.gn + getDistance(current.x, current.y, successor.x, successor.y);
                if (listContainsNode(open, successor, successorFound)) {
                    if (successorFound.gn <= currentCost) {
                        continue;
                    }
                    open.remove(successorFound); //delete successor
                    successorFound.gn = currentCost; //update
                    successorFound.parentX = current.x;
                    successorFound.parentY = current.y;
                    successorFound.fn = successorFound.gn + successorFound.hn;
                    open.push_back(successorFound); //insert
                } else if (listContainsNode(closed, successor, successorFound)) {
                    if (successorFound.gn <= currentCost) {
                        continue;
                    }
                    //move found from closed to open
                    closed.remove(successorFound);
                    successorFound.gn = currentCost; //update
                    successorFound.parentX = current.x;
                    successorFound.parentY = current.y;
                    successorFound.fn = successorFound.gn + successorFound.hn;
                    open.push_back(successorFound);
                } else {
                    successor.gn = currentCost;
                    successor.hn = getDistance(successor.x, successor.y, xEnd, yEnd);
                    successor.fn = successor.gn + successor.hn;
                    successor.parentX = current.x;
                    successor.parentY = current.y;
                    open.push_back(successor);
                }
            }
        }
        //displayLista(open); //debug
        open.remove(current);
        //displayLista(open); //debug
        //displayLista(closed); //debug
        closed.push_back(current);
        //displayLista(closed); //debug
    }
    if (board.getValue(current.y, current.x) == 3) {
        displaySolution(closed, current.x, current.y);
    } else {
        cout << "Ha ocurrido un error, no se ha podido alcanzar la meta" << endl;
    }

}

int main(int argc, char** argv) {
    aStar();
    return 0;
}



