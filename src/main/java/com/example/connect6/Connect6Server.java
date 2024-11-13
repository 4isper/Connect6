package com.example.connect6;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Connect6Server extends UnicastRemoteObject implements Connect6Interface {
    private final int[][] board;
    private final int boardSize = 19;
    private int currentPlayer = 1;
    private int moveCount = 0;
    private AtomicInteger playerCount = new AtomicInteger(0);
    private boolean firstMove = true;

    protected Connect6Server() throws RemoteException {
        board = new int[boardSize][boardSize];
        resetBoard();
    }

    @Override
    public synchronized int register() throws RemoteException {
        if (playerCount.get() >= 2) {
            return -1;
        }
        int assignedPlayer = playerCount.incrementAndGet();
        System.out.println("Player " + assignedPlayer + " has joined the game.");
        return assignedPlayer;
    }

    @Override
    public synchronized boolean placePiece(int x, int y, int player) throws RemoteException {
        if (player != currentPlayer) {
            System.out.println("Player " + player + " attempted to move out of turn.");
            return false;
        }

        if (x >= 0 && x < boardSize && y >= 0 && y < boardSize && board[x][y] == 0) {
            board[x][y] = player;
            moveCount++;

            if (moveCount == 1 && firstMove) {
                currentPlayer = 2;
                moveCount = 0;
                firstMove = false;
            } else if (moveCount == 2) {
                currentPlayer = (currentPlayer == 1) ? 2 : 1;
                moveCount = 0;
            }
            System.out.println("Player " + player + " placed a piece at (" + x + ", " + y + ").");
            return true;
        }
        System.out.println("Player " + player + " attempted to place a piece at invalid position (" + x + ", " + y + ").");
        return false;
    }

    @Override
    public synchronized int getCurrentPlayer() throws RemoteException {
        return currentPlayer;
    }

    @Override
    public int[][] getBoard() throws RemoteException {
        return board;
    }

    @Override
    public int checkWin() throws RemoteException {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int player = board[i][j];
                if (player != 0 && (checkDirection(i, j, player, 1, 0) ||
                        checkDirection(i, j, player, 0, 1) ||
                        checkDirection(i, j, player, 1, 1) ||
                        checkDirection(i, j, player, 1, -1))) {
                    return player;
                }
            }
        }
        return 0;
    }

    private boolean checkDirection(int x, int y, int player, int dx, int dy) {
        int count = 0;
        for (int i = 0; i < 6; i++) {
            int nx = x + i * dx;
            int ny = y + i * dy;
            if (nx >= 0 && nx < boardSize && ny >= 0 && ny < boardSize && board[nx][ny] == player) {
                count++;
            } else {
                break;
            }
        }
        return count == 6;
    }

    @Override
    public synchronized void resetBoard() throws RemoteException {
        for (int[] row : board) {
            Arrays.fill(row, 0);
        }
        currentPlayer = 1;
        moveCount = 0;
        System.out.println("Board has been reset.");
    }

    public static void main(String[] args) {
        try {
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            Connect6Server server = new Connect6Server();
            java.rmi.Naming.rebind("rmi://localhost/Connect6Server", server);
            System.out.println("Connect6 Server is ready.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}

