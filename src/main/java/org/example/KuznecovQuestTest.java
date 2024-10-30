package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KuznecovQuestTest {

    private Random randomMock;
    private Timer timerMock;

    @BeforeEach
    void setUp() {
        KuznecovQuest.playerLives = 3;
        KuznecovQuest.currentRoom = 1;
        KuznecovQuest.monsterRoom = 5;
        KuznecovQuest.gameOver = false;
        KuznecovQuest.gameResult = "";
        randomMock = mock(Random.class);
        KuznecovQuest.setRandom(randomMock);
        timerMock = mock(Timer.class);
    }

    @AfterEach
    void tearDown() {
        KuznecovQuest.setRandom(new Random());
    }

    @Test
    void testSearchDresser_losesLife() {
        when(randomMock.nextBoolean()).thenReturn(true);
        KuznecovQuest.searchDresser();
        assertEquals(2, KuznecovQuest.playerLives);
        assertFalse(KuznecovQuest.gameOver);
    }

    @Test
    void testInspectThings_movesToNextRoom() {
        when(randomMock.nextBoolean()).thenReturn(false);
        KuznecovQuest.inspectThings();
        assertEquals(2, KuznecovQuest.currentRoom);
        assertFalse(KuznecovQuest.gameOver);
    }

    @Test
    void testFindExit_winsGame() {
        KuznecovQuest.currentRoom = 10;
        KuznecovQuest.findExit();
        assertTrue(KuznecovQuest.gameOver);
        assertEquals("Вы выиграли!", KuznecovQuest.gameResult);
    }

    @Test
    void testMonsterMovement_playerLosesLife() {
        KuznecovQuest.monsterRoom = 1;

        KuznecovQuest.startMonsterMovement();

        assertEquals(2, KuznecovQuest.playerLives);
        assertFalse(KuznecovQuest.gameOver);
    }

    @Test
    void testPlayerLosesAllLives() {
        when(randomMock.nextBoolean()).thenReturn(true);
        KuznecovQuest.playerLives = 1;
        KuznecovQuest.searchDresser();
        assertTrue(KuznecovQuest.gameOver);
        assertEquals("Вы проиграли!", KuznecovQuest.gameResult);
    }

    @Test
    void testMonsterMovesToRandomRoom() {
        when(randomMock.nextInt(10)).thenReturn(2);
        KuznecovQuest.startMonsterMovement();
        assertEquals(3, KuznecovQuest.monsterRoom);
    }

    @Test
    void testEndGameStopsTimerOnLoss() {
        when(randomMock.nextBoolean()).thenReturn(true);
        KuznecovQuest.playerLives = 1;
        KuznecovQuest.startMonsterMovement();
        KuznecovQuest.searchDresser();

        assertTrue(KuznecovQuest.gameOver);
        assertEquals("Вы проиграли!", KuznecovQuest.gameResult);
    }

    @Test
    void testEndGameStopsTimerOnWin() {
        KuznecovQuest.currentRoom = 9;
        when(randomMock.nextBoolean()).thenReturn(false);
        KuznecovQuest.findExit();
        assertTrue(KuznecovQuest.gameOver);
        assertEquals("Вы выиграли!", KuznecovQuest.gameResult);
    }
}
