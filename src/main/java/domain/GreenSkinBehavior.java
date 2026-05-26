package domain;

import java.awt.*;

public class GreenSkinBehavior extends AbstractSkinBehavior {
    private static final long serialVersionUID = 1L;

    private boolean shield;
    private boolean slowedDown;

    public GreenSkinBehavior() {
        this.shield = true;
        this.slowedDown = false;
    }

    @Override
    public int getSpeed(Player player) {
        return 1;
    }

    @Override
    public int getDrawSize() { return 30; }

    @Override
    public int getDrawPadding() { return 5; }

    @Override
    public Color getColor(Player player) {
        if (slowedDown) return new Color(0, 120, 0);
        return Color.GREEN;
    }

    // Sobrescribe la plantilla porque este skin si tiene escudo
    @Override
    public boolean hasShield() { return shield; }

    // Sobrescribe la plantilla para absorber el primer golpe
    @Override
    public boolean onEnemyHit(Player player) {
        if (shield) {
            shield = false;
            slowedDown = true;
            return false; // sobrevive
        }
        return super.onEnemyHit(player); // llama a la plantilla para que muera
    }

    // Sobrescribe la plantilla para recargar escudo
    @Override
    public void resetAfterRespawn(Player player) {
        shield = true;
        slowedDown = false;
    }

    @Override
    public Skin getSkinType() { return Skin.GREEN; }

    public boolean isSlowedDown() { return slowedDown; }

    public boolean isShielded() { return shield; }
}