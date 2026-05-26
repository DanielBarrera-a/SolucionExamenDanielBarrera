package presentation;

import domain.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import domain.SkinBehavior;

/**
 * Panel principal del juego.
 * ningun cambio para dibujarla correctamente.
 */
public class GamePanel extends JPanel implements ActionListener {
    private GameWindow window;
    private TheDOPOHardestGame game;
    private Timer timer;
    private final int CELL_SIZE = 40;

    // ── Diagonal: teclas actualmente presionadas ───────────────────────────
    private final java.util.Set<Integer> pressedKeys = new java.util.HashSet<>();

    public GamePanel(GameWindow window, TheDOPOHardestGame game) {
        this.window = window;
        this.game = game;
        setBackground(Color.WHITE);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
                if (game.isVictory() || game.isGameOver()) return;

                if (e.getKeyCode() == KeyEvent.VK_G) {
                    timer.stop();
                    window.saveGame();
                    timer.start();
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    timer.stop();
                    int choice = JOptionPane.showOptionDialog(
                            GamePanel.this,
                            "Juego pausado. ¿Qué deseas hacer?",
                            "Pausa",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            new String[]{"Continuar", "Guardar y salir", "Salir sin guardar"},
                            "Continuar");
                    if (choice == 1) {
                        boolean saved = window.saveGame();
                        if (saved) window.showMainMenu();
                        else timer.start();
                    } else if (choice == 2) {
                        window.showMainMenu();
                    } else {
                        timer.start();
                    }
                    return;
                }

                // Movimiento Player 1 (flechas + diagonal)
                int dx = 0, dy = 0;
                if (pressedKeys.contains(KeyEvent.VK_UP))    dy -= 1;
                if (pressedKeys.contains(KeyEvent.VK_DOWN))  dy += 1;
                if (pressedKeys.contains(KeyEvent.VK_LEFT))  dx -= 1;
                if (pressedKeys.contains(KeyEvent.VK_RIGHT)) dx += 1;
                if (dx != 0 || dy != 0) {
                    int steps = game.getPlayer().getSpeed();
                    for (int i = 0; i < steps; i++) {
                        game.movePlayer(dy, dx);
                        if (checkGameState()) return;
                    }
                    repaint();
                }

                // Movimiento Player 2 (WASD + diagonal)
                int dx2 = 0, dy2 = 0;
                if (pressedKeys.contains(KeyEvent.VK_W)) dy2 -= 1;
                if (pressedKeys.contains(KeyEvent.VK_S)) dy2 += 1;
                if (pressedKeys.contains(KeyEvent.VK_A)) dx2 -= 1;
                if (pressedKeys.contains(KeyEvent.VK_D)) dx2 += 1;
                if (dx2 != 0 || dy2 != 0) {
                    int steps2 = (game.getPlayer2() != null) ? game.getPlayer2().getSpeed() : 1;
                    for (int i = 0; i < steps2; i++) {
                        game.movePlayer2(dy2, dx2);
                        if (checkGameState()) return;
                    }
                    repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });

        timer = new Timer(500, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        game.updatePulse(0.5); // el timer dispara cada 500ms = 0.5s
        game.tickTime();
        game.moveEnemies();
        repaint();
        checkGameState();
    }

    private boolean checkGameState() {
        if (game.isVictory()) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "¡Victoria! Has completado el nivel " + window.getCurrentLevel() + ".");
            window.levelCompleted();
            return true;
        } else if (game.isGameOver()) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over. Se acabó el tiempo.");
            window.showMainMenu();
            return true;
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int offsetX = (getWidth() - game.getCols() * CELL_SIZE) / 2;
        int offsetY = (getHeight() - game.getRows() * CELL_SIZE) / 2;

        // Dibujar tablero
        for (int r = 0; r < game.getRows(); r++) {
            for (int c = 0; c < game.getCols(); c++) {
                CellType type = game.getCell(r, c);
                if (type == CellType.WALL) {
                    g.setColor(Color.DARK_GRAY);
                } else if (type == CellType.SAFE_START || type == CellType.SAFE_END || type == CellType.SAFE_MID) {
                    g.setColor(new Color(144, 238, 144));
                } else {
                    g.setColor(new Color(240, 240, 240));
                }
                g.fillRect(offsetX + c * CELL_SIZE, offsetY + r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(offsetX + c * CELL_SIZE, offsetY + r * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // Cada moneda sabe cómo dibujarse a sí misma (OCP)
        for (Coin coin : game.getCoins()) {
            coin.draw(g, offsetX, offsetY, CELL_SIZE);
        }

        // Cada elemento especial sabe cómo dibujarse a sí mismo (OCP)
        for (SpecialElement se : game.getSpecialElements()) {
            if (se.isActive()) se.draw(g, offsetX, offsetY, CELL_SIZE);
        }

        // Cada enemigo sabe cómo dibujarse a sí mismo (OCP)
        for (Enemy enemy : game.getEnemies()) {
            enemy.draw(g, offsetX, offsetY, CELL_SIZE);
        }

        // Dibujar jugadores
        drawPlayer(g, game.getPlayer(), offsetX, offsetY);
        if (game.getPlayer2() != null) {
            drawPlayer(g, game.getPlayer2(), offsetX, offsetY);
        }

        // HUD
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Tiempo: " + game.getTimeRemaining(), 20, 30);

        if (game.getPlayer2() != null) {
            g.drawString("P1 Muertes: " + game.getPlayer().getDeaths(), 150, 30);
            g.drawString("P2 Muertes: " + game.getPlayer2().getDeaths(), 150, 50);
        } else {
            g.drawString("Muertes: " + game.getPlayer().getDeaths(), 150, 30);
        }

        g.drawString("Monedas: " + game.getCoins().size(), 280, 30);

        if (game.isPulseActive()) {
            g.setColor(new Color(0, 180, 255)); // color cyan
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("⚡ PULSO: " + String.format(java.util.Locale.US, "%.1f", game.getPulseTimeRemaining()) + "s", 400, 30);
        }

        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.setColor(Color.GRAY);
        g.drawString("[G] Guardar   [ESC] Pausa", 20, getHeight() - 10);
    }

    private void drawPlayer(Graphics g, Player p, int offsetX, int offsetY) {
        SkinBehavior behavior = p.getActiveBehavior();
        g.setColor(behavior.getColor(p));
        int size = behavior.getDrawSize();
        int padding = behavior.getDrawPadding();
        g.fillRect(
                offsetX + p.getPosition().getCol() * CELL_SIZE + padding,
                offsetY + p.getPosition().getRow() * CELL_SIZE + padding,
                size, size
        );
        if (p.isShielded()) {
            g.setColor(Color.WHITE);
            g.drawRect(
                    offsetX + p.getPosition().getCol() * CELL_SIZE + padding,
                    offsetY + p.getPosition().getRow() * CELL_SIZE + padding,
                    size, size
            );
        }
    }
}