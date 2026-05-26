package domain;

import java.awt.*;

/**
 * Moneda Pulso si la coges congela a todos los enemigos
 * durante 3 aprox segundos y cambia la skin del jugador volviednolo imune
 */
public class PulseCoin extends Coin {
    private static final long serialVersionUID = 1L;

    public PulseCoin(Position position) {
        super(position);
    }

    @Override
    public void onCollected(Player player, TheDOPOHardestGame game) {
        game.activatePulse();
        player.resetSkin();
        player.applySkin(Skin.IMMUNITY);
    }

    @Override
    public void draw(Graphics g, int offsetX, int offsetY, int cellSize) {
        int x = offsetX + getPosition().getCol() * cellSize;
        int y = offsetY + getPosition().getRow() * cellSize;

        // Círculo cyan con borde púrpura
        g.setColor(Color.CYAN);
        g.fillOval(x + 6, y + 6, 28, 28);

        g.setColor(new Color(128, 0, 255)); // púrpura
        g.drawOval(x + 6, y + 6, 28, 28);

        // Letra P en el centro
        g.setColor(Color.WHITE);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        g.drawString("P", x + 15, y + 26);
    }
}