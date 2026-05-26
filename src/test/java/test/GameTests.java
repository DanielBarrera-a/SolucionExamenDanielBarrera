package test;

import domain.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas unitarias para The DOPO Hardest Game.
 * Nomenclatura: should + comportamiento esperado + condición.
 * Orientada a cobertura de ramas con JaCoCo sobre el paquete domain.
 */
class GameTests {

    private CellType[][] makeBoard() {
        CellType[][] board = new CellType[5][5];
        for (int r = 0; r < 5; r++)
            for (int c = 0; c < 5; c++)
                board[r][c] = CellType.EMPTY;
        for (int i = 0; i < 5; i++) {
            board[0][i] = CellType.WALL;
            board[4][i] = CellType.WALL;
            board[i][0] = CellType.WALL;
            board[i][4] = CellType.WALL;
        }
        board[1][1] = CellType.SAFE_START;
        board[3][3] = CellType.SAFE_END;
        return board;
    }

    private TheDOPOHardestGame makeGame(List<Enemy> enemies, List<Coin> coins) {
        return new TheDOPOHardestGame(
                makeBoard(), new Position(1, 1),
                enemies, coins, 60, GameMode.PLAYER, Skin.RED
        );
    }

    private TheDOPOHardestGame makeGameWithTime(int seconds) {
        return new TheDOPOHardestGame(
                makeBoard(), new Position(1, 1),
                new ArrayList<>(), new ArrayList<>(),
                seconds, GameMode.PLAYER, Skin.RED
        );
    }

    // Player

    @Test
    void shouldHaveZeroDeathsWhenPlayerIsCreated() {
        Player player = new Player(new Position(1, 1), Skin.RED);
        assertEquals(0, player.getDeaths());
    }

    @Test
    void shouldIncrementDeathsWhenAddDeathIsCalled() {
        Player player = new Player(new Position(1, 1), Skin.RED);
        player.addDeath();
        player.addDeath();
        assertEquals(2, player.getDeaths());
    }

    @Test
    void shouldDecrementDeathsWhenReduceDeathIsCalledWithDeathsAboveZero() {
        Player player = new Player(new Position(1, 1), Skin.RED);
        player.addDeath();
        player.addDeath();
        player.reduceDeath();
        assertEquals(1, player.getDeaths());
    }

    @Test
    void shouldNotGoBelowZeroWhenReduceDeathIsCalledWithZeroDeaths() {
        Player player = new Player(new Position(1, 1), Skin.RED);
        player.reduceDeath();
        assertEquals(0, player.getDeaths());
    }

    @Test
    void shouldReturnSpeedTwoWhenSkinIsBlue() {
        Player player = new Player(new Position(1, 1), Skin.BLUE);
        assertEquals(2, player.getSpeed());
    }

    @Test
    void shouldReturnSpeedOneWhenSkinIsRed() {
        Player player = new Player(new Position(1, 1), Skin.RED);
        assertEquals(1, player.getSpeed());
    }

    @Test
    void shouldAbsorbHitAndNotDieWhenGreenPlayerHasShield() {
        Player player = new Player(new Position(1, 1), Skin.GREEN);
        boolean died = player.applyEnemyHit();
        assertFalse(died);
        assertEquals(0, player.getDeaths());
    }

    @Test
    void shouldDieWhenGreenPlayerHasNoShieldAndTakesHit() {
        Player player = new Player(new Position(1, 1), Skin.GREEN);
        player.applyEnemyHit();
        boolean died = player.applyEnemyHit();
        assertTrue(died);
        assertEquals(1, player.getDeaths());
    }

    @Test
    void shouldDieWhenRedPlayerTakesHit() {
        Player player = new Player(new Position(1, 1), Skin.RED);
        boolean died = player.applyEnemyHit();
        assertTrue(died);
        assertEquals(1, player.getDeaths());
    }

    @Test
    void shouldApplyTemporarySkinWhenSkinCoinIsCollected() {
        Player player = new Player(new Position(1, 1), Skin.RED);
        player.applySkin(Skin.BLUE);
        assertEquals(Skin.BLUE, player.getActiveSkin());
    }

    @Test
    void shouldRestoreOriginalSkinWhenResetSkinIsCalled() {
        Player player = new Player(new Position(1, 1), Skin.RED);
        player.applySkin(Skin.GREEN);
        player.resetSkin();
        assertEquals(Skin.RED, player.getActiveSkin());
    }

    // CoinFactory

    @Test
    void shouldCreateYellowCoinWhenTypeIsYellow() throws GameException {
        Coin coin = CoinFactory.create("YELLOW", new Position(1, 1));
        assertInstanceOf(YellowCoin.class, coin);
    }

    @Test
    void shouldCreateSkinCoinWhenTypeIsRedSkin() throws GameException {
        Coin coin = CoinFactory.create("RED_SKIN", new Position(1, 1));
        assertInstanceOf(SkinCoin.class, coin);
    }

    @Test
    void shouldThrowGameExceptionWhenCoinTypeIsUnknown() {
        assertThrows(GameException.class,
                () -> CoinFactory.create("UNKNOWN", new Position(1, 1)));
    }

    // EnemyFactory

    @Test
    void shouldCreatePatrolEnemyWhenTypeIsPatrol() throws GameException {
        String[] parts = {"ENEMY", "PATROL", "1", "1", "1", "3", "3", "3", "3", "1"};
        Enemy enemy = EnemyFactory.create("PATROL", new Position(1, 1), false, parts);
        assertInstanceOf(PatrolEnemy.class, enemy);
    }

    @Test
    void shouldThrowGameExceptionWhenEnemyTypeIsUnknown() {
        assertThrows(GameException.class,
                () -> EnemyFactory.create("GHOST", new Position(1, 1), false, new String[]{}));
    }

    // MachineFactory

    @Test
    void shouldCreateRandomMachineWhenTypeIsRandom() throws GameException {
        MachinePlayer machine = MachineFactory.create("RANDOM");
        assertInstanceOf(RandomMachine.class, machine);
    }

    @Test
    void shouldCreateExpertMachineWhenTypeIsExpert() throws GameException {
        MachinePlayer machine = MachineFactory.create("EXPERT");
        assertInstanceOf(ExpertMachine.class, machine);
    }

    @Test
    void shouldThrowGameExceptionWhenMachineTypeIsUnknown() {
        assertThrows(GameException.class, () -> MachineFactory.create("GODMODE"));
    }

    // TheDOPOHardestGame

    @Test
    void shouldNotMovePlayerWhenDestinationIsAWall() {
        TheDOPOHardestGame game = makeGame(new ArrayList<>(), new ArrayList<>());
        game.movePlayer(-1, 0);
        assertEquals(new Position(1, 1), game.getPlayer().getPosition());
    }

    @Test
    void shouldActivateGameOverWhenTimeReachesZero() {
        TheDOPOHardestGame game = makeGameWithTime(1);
        game.tickTime();
        assertTrue(game.isGameOver());
    }

    @Test
    void shouldActivateVictoryWhenPlayerReachesSafeEndWithNoCoins() {
        TheDOPOHardestGame game = makeGame(new ArrayList<>(), new ArrayList<>());
        game.movePlayer(1, 0);
        game.movePlayer(1, 0);
        game.movePlayer(0, 1);
        game.movePlayer(0, 1);
        assertTrue(game.isVictory());
    }

    @Test
    void shouldRemoveCoinWhenPlayerCollectsIt() {
        List<Coin> coins = new ArrayList<>();
        coins.add(new YellowCoin(new Position(1, 2)));
        TheDOPOHardestGame game = makeGame(new ArrayList<>(), coins);
        game.movePlayer(0, 1);
        assertTrue(game.getCoins().isEmpty());
    }

    @Test
    void shouldTeleportPlayerToRespawnWhenCollidingWithEnemy() throws GameException {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(EnemyFactory.create("BASIC_BLUE", new Position(1, 2), true,
                new String[]{"ENEMY", "BASIC_BLUE", "1", "2", "HORIZONTAL"}));
        TheDOPOHardestGame game = makeGame(enemies, new ArrayList<>());
        game.movePlayer(0, 1);
        assertEquals(new Position(1, 1), game.getPlayer().getPosition());
    }

    @Test
    void shouldReturnFalseWhenPositionIsOutsideBoardBounds() {
        TheDOPOHardestGame game = makeGame(new ArrayList<>(), new ArrayList<>());
        assertFalse(game.isValidPosition(-1, 0));
        assertFalse(game.isValidPosition(0, -1));
        assertFalse(game.isValidPosition(10, 0));
        assertFalse(game.isValidPosition(0, 10));
    }

    @Test
    void shouldUpdateRespawnWhenPlayerStepsOnSafeMidZone() {
        CellType[][] board = makeBoard();
        board[2][2] = CellType.SAFE_MID;
        TheDOPOHardestGame game = new TheDOPOHardestGame(
                board, new Position(1, 1),
                new ArrayList<>(), new ArrayList<>(),
                60, GameMode.PLAYER, Skin.RED
        );
        game.movePlayer(1, 0);
        game.movePlayer(0, 1);
        assertEquals(new Position(2, 2), game.getPlayer().getRespawnPosition());
    }

    @Test
    void shouldReturnMoveWhenRandomMachineIsAsked() throws GameException {
        MachinePlayer machine = MachineFactory.create("RANDOM");
        TheDOPOHardestGame game = new TheDOPOHardestGame(
                makeBoard(), new Position(1, 1),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), 60, GameMode.PVM, Skin.RED
        );
        int[] move = machine.nextMove(game, game.getPlayer());
        assertNotNull(move);
        assertEquals(2, move.length);
    }

    @Test
    void shouldPatrolEnemyFollowWaypointsInOrder() throws GameException {
        String[] parts = {"ENEMY", "PATROL", "1", "1", "1", "2", "1", "3"};
        PatrolEnemy patrol = (PatrolEnemy) EnemyFactory.create("PATROL",
                new Position(1, 1), false, parts);
        TheDOPOHardestGame game = makeGame(new ArrayList<>(List.of(patrol)), new ArrayList<>());
        Position before = new Position(patrol.getPosition().getRow(), patrol.getPosition().getCol());
        patrol.move(game);
        assertNotEquals(before, patrol.getPosition());
    }

    // ── Pruebas Moneda Pulso ──

    @Test
    void shouldCreatePulseCoinFromFactory() throws GameException {
        Coin coin = CoinFactory.create("PULSE", new Position(1, 1));
        assertInstanceOf(PulseCoin.class, coin);
    }

    @Test
    void shouldActivatePulseWhenPulseCoinIsCollected() {
        List<Coin> coins = new ArrayList<>();
        coins.add(new PulseCoin(new Position(1, 2)));
        TheDOPOHardestGame game = makeGame(new ArrayList<>(), coins);
        game.movePlayer(0, 1); // recoge la moneda
        assertTrue(game.isPulseActive());
        assertTrue(game.getCoins().isEmpty());
    }

    @Test
    void shouldFreezeEnemiesWhilePulseIsActive() throws GameException {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(EnemyFactory.create("BASIC_BLUE", new Position(2, 2), true,
                new String[]{"ENEMY", "BASIC_BLUE", "2", "2", "HORIZONTAL"}));
        List<Coin> coins = new ArrayList<>();
        coins.add(new PulseCoin(new Position(1, 2)));
        TheDOPOHardestGame game = makeGame(enemies, coins);
        game.movePlayer(0, 1); // recoge pulso
        Position enemyPosBefore = new Position(
                enemies.get(0).getPosition().getRow(),
                enemies.get(0).getPosition().getCol());
        game.moveEnemies(); // no debe moverse
        assertEquals(enemyPosBefore, enemies.get(0).getPosition());
    }

    @Test
    void shouldFreezeGameTimeWhilePulseIsActive() {
        List<Coin> coins = new ArrayList<>();
        coins.add(new PulseCoin(new Position(1, 2)));
        TheDOPOHardestGame game = makeGame(new ArrayList<>(), coins);
        game.movePlayer(0, 1); // recoge pulso
        int timeBefore = game.getTimeRemaining();
        game.tickTime();
        assertEquals(timeBefore, game.getTimeRemaining()); // no cambió
    }

    @Test
    void shouldNotKillPlayerWhenTouchingEnemyDuringPulse() throws GameException {
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(EnemyFactory.create("BASIC_BLUE", new Position(1, 3), true,
                new String[]{"ENEMY", "BASIC_BLUE", "1", "3", "HORIZONTAL"}));
        List<Coin> coins = new ArrayList<>();
        coins.add(new PulseCoin(new Position(1, 2)));
        TheDOPOHardestGame game = makeGame(enemies, coins);
        game.movePlayer(0, 1); // recoge pulso en (1,2)
        game.movePlayer(0, 1); // va a (1,3) donde está el enemigo
        assertEquals(0, game.getPlayer().getDeaths()); // no murió
    }

    @Test
    void shouldResetPulseTimerWhenSecondPulseCoinCollected() {
        List<Coin> coins = new ArrayList<>();
        coins.add(new PulseCoin(new Position(1, 2)));
        coins.add(new PulseCoin(new Position(1, 3)));
        TheDOPOHardestGame game = makeGame(new ArrayList<>(), coins);
        game.movePlayer(0, 1); // recoge primer pulso
        game.updatePulse(2.0); // pasan 2 segundos, queda 1s
        game.movePlayer(0, 1); // recoge segundo pulso
        assertEquals(3.0, game.getPulseTimeRemaining(), 0.01); // se reinició a 3s
    }

    @Test
    void shouldDeactivatePulseAfterTimeExpires() {
        List<Coin> coins = new ArrayList<>();
        coins.add(new PulseCoin(new Position(1, 2)));
        TheDOPOHardestGame game = makeGame(new ArrayList<>(), coins);
        game.movePlayer(0, 1); // recoge pulso
        assertTrue(game.isPulseActive());
        game.updatePulse(3.5); // pasan 3.5 segundos
        assertFalse(game.isPulseActive()); // ya se desactivó
    }

    @Test
    void shouldChangeSkinToImmunityOnPulseCollection() {
        List<Coin> coins = new ArrayList<>();
        coins.add(new PulseCoin(new Position(1, 2)));
        TheDOPOHardestGame game = makeGame(new ArrayList<>(), coins);
        game.movePlayer(0, 1);
        assertEquals(Skin.IMMUNITY, game.getPlayer().getActiveSkin());
    }

    @Test
    void shouldRestoreSkinAfterPulseExpires() {
        List<Coin> coins = new ArrayList<>();
        coins.add(new PulseCoin(new Position(1, 2)));
        TheDOPOHardestGame game = makeGame(new ArrayList<>(), coins);
        game.movePlayer(0, 1);
        assertEquals(Skin.IMMUNITY, game.getPlayer().getActiveSkin());
        game.updatePulse(3.5);
        assertEquals(Skin.RED, game.getPlayer().getActiveSkin()); // volvió a RED
    }

    @Test
    void shouldRequirePulseCoinForVictory() {
        List<Coin> coins = new ArrayList<>();
        coins.add(new PulseCoin(new Position(2, 2)));
        TheDOPOHardestGame game = makeGame(new ArrayList<>(), coins);
        // Ir a SAFE_END sin recoger la moneda
        game.movePlayer(1, 0);
        game.movePlayer(1, 0);
        game.movePlayer(0, 1);
        game.movePlayer(0, 1); // (3,3) = SAFE_END
        assertFalse(game.isVictory()); // no ganó porque falta la moneda
    }
}