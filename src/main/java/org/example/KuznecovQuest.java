package org.example;

import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class KuznecovQuest {
    static final int ROOMS = 10;
    static final int MONSTER_SLEEP_TIME = 5000;
    static int playerLives;
    static int currentRoom;
    static int monsterRoom;
    static boolean gameOver;
    static String gameResult = "";
    static Timer timer;

    static Random random = new Random();

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в SurnameQuest!");
        resetGame();

        while (!gameOver) {
            System.out.printf("Вы находитесь в комнате %d. У вас осталось жизней: %d%n", currentRoom, playerLives);
            System.out.println("Выберите действие: ");
            System.out.println("1. Обыскать комод");
            System.out.println("2. Осмотреть вещи");
            System.out.println("3. Осмотреть подвал на наличие выхода");

            int action = new Scanner(System.in).nextInt();
            switch (action) {
                case 1 -> searchDresser();
                case 2 -> inspectThings();
                case 3 -> findExit();
                default -> System.out.println("Неверный ввод, попробуйте снова.");
            }
        }

        System.out.println(gameResult);
        System.out.println("Начать заново? (да/нет)");

        if (new Scanner(System.in).nextLine().equalsIgnoreCase("да")) {
            resetGame();
            main(null);
        } else {
            System.out.println("Спасибо за игру!");
            if (timer != null) {
                timer.cancel();
            }
        }
    }


    public static void setRandom(Random newRandom) {
        random = newRandom;
        monsterRoom = random.nextInt(ROOMS) + 1;
    }

    private static void resetGame() {
        playerLives = 3;
        currentRoom = 1;
        monsterRoom = random.nextInt(ROOMS) + 1;
        gameOver = false;
        gameResult = "";

        if (timer != null) {
            timer.cancel();
        }

        startMonsterMovement();
    }

    static void startMonsterMovement() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                monsterRoom = random.nextInt(ROOMS) + 1;
                logEvent("Монстр переместился в комнату " + monsterRoom);
                System.out.println("Введите действия: ");

                if (monsterRoom == currentRoom) {
                    playerLives--;
                    logEvent("Игрок столкнулся с монстром. Осталось жизней: " + playerLives);
                    if (playerLives <= 0) {
                        endGame("Вы проиграли!");
                        timer.cancel();
                    }
                }
            }
        }, 0, MONSTER_SLEEP_TIME);
    }

    static void searchDresser() {
        takeAction("обыскали комод");
    }

    static void inspectThings() {
        takeAction("осмотрели вещи");
    }

    static void findExit() {
        takeAction("искали выход");
    }

    private static void takeAction(String action) {
        logEvent("Игрок " + action);
        if (random.nextBoolean()) {
            playerLives--;
            logEvent("Игрок потерял жизнь. Осталось жизней: " + playerLives);
            if (playerLives <= 0) {
                endGame("Вы проиграли!");
            }
        } else {
            currentRoom++;
            logEvent("Игрок перешёл в комнату " + currentRoom);
            if (currentRoom > ROOMS) {
                endGame("Вы выиграли!");
            }
        }
    }

    private static void endGame(String result) {
        gameOver = true;
        gameResult = result;
        logEvent(result);
        if (timer != null) {
            timer.cancel();
        }
    }

    private static void logEvent(String event) {
        System.out.println(event);
    }
}
