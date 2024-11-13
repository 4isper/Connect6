package com.example.connect6;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Connect6Interface extends Remote {
    boolean placePiece(int x, int y, int player) throws RemoteException;
    int[][] getBoard() throws RemoteException;
    int checkWin() throws RemoteException;
    void resetBoard() throws RemoteException;
    int getCurrentPlayer() throws RemoteException;
    int register() throws RemoteException;
}
