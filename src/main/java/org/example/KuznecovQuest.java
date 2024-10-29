package org.example;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class KuznecovQuest {
    private static final int ROOMS = 10; // Замените на значение из config.rooms
    private static final int MONSTER_SLEEP_TIME = 2000; // Замените на значение из config.monsterSleepTime
    private static int playerLives = 3; // Замените на значение из config.playerLives
    private static int currentRoom = 1;
    private static int monsterRoom = new Random().nextInt(ROOMS) + 1;
    private static boolean gameOver = false;
    private static String gameResult = "";

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в SurnameQuest!");

        // Запуск передвижения монстра
        startMonsterMovement();

        // Основной игровой цикл
        while (!gameOver) {
            System.out.printf("Вы находитесь в комнате %d. У вас осталось жизней: %d%n", currentRoom, playerLives);
            System.out.println("Выберите действие: ");
            System.out.println("1. Обыскать комод");
            System.out.println("2. Осмотреть вещи");
            System.out.println("3. Осмотреть подвал на наличие выхода");

            int action = new java.util.Scanner(System.in).nextInt();
            switch (action) {
                case 1 -> searchDresser();
                case 2 -> inspectThings();
                case 3 -> findExit();
                default -> System.out.println("Неверный ввод, попробуйте снова.");
            }
        }

        System.out.println(gameResult);
        System.out.println("Начать заново? (да/нет)");

        if (new java.util.Scanner(System.in).nextLine().equalsIgnoreCase("да")) {
            main(null); // Рестарт игры
        } else {
            System.out.println("Спасибо за игру!");
        }
    }

    // Метод для запуска передвижения монстра
    private static void startMonsterMovement() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                monsterRoom = new Random().nextInt(ROOMS) + 1;
                logEvent("Монстр переместился в комнату " + monsterRoom);

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

    private static void searchDresser() {
        takeAction("обыскали комод");
    }

    private static void inspectThings() {
        takeAction("осмотрели вещи");
    }

    private static void findExit() {
        takeAction("искали выход");
    }

    private static void takeAction(String action) {
        logEvent("Игрок " + action);
        if (new Random().nextBoolean()) {
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
    }

    private static void logEvent(String event) {
        System.out.println(event);
    }
}
