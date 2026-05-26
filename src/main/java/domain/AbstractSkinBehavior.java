package domain;

/**
 * Es la clase que da el comportamiento a las skins
 */
public abstract class AbstractSkinBehavior implements SkinBehavior {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean hasShield() {
        return false;
    }

    @Override
    public boolean onEnemyHit(Player player) {
        player.addDeath();
        return true; // murió
    }

    @Override
    public void resetAfterRespawn(Player player) {
        // Por defecto, no hay estado extra que resetear
    }
}