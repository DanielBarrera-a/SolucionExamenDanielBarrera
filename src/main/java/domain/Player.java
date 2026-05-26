package domain;

/**
 * Esta clase representa al jugador del juego.
 * Hereda de Entity.
 */
public class Player extends Entity {
    private static final long serialVersionUID = 1L;

    private int deaths;
    private Position respawnPosition;
    private Skin skin;
    private Skin originalSkin;
    private Skin temporarySkin;

    private SkinBehavior skinBehavior;
    private SkinBehavior temporaryBehavior;

    public Player(Position position, Skin skin) {
        super(new Position(position.getRow(), position.getCol()));
        this.skin = skin;
        this.originalSkin = skin;
        this.temporarySkin = null;
        this.deaths = 0;
        this.respawnPosition = new Position(position.getRow(), position.getCol());
        this.skinBehavior = SkinBehaviorFactory.create(skin);
        this.temporaryBehavior = null;
    }

    public SkinBehavior getActiveBehavior() {
        return (temporaryBehavior != null) ? temporaryBehavior : skinBehavior;
    }

    public int getSpeed() {
        return getActiveBehavior().getSpeed(this);
    }

    /** Aplica una skin temporal por moneda skin. */
    public void applySkin(Skin newSkin) {
        this.temporarySkin = newSkin;
        this.temporaryBehavior = SkinBehaviorFactory.create(newSkin);
    }

    /** Recupera la skin original (al morir o al recoger otra moneda). */
    public void resetSkin() {
        this.temporarySkin = null;
        this.temporaryBehavior = null;
    }

    /** Devuelve la skin activa (temporal si hay, si no la original). */
    public Skin getActiveSkin() {
        return (temporarySkin != null) ? temporarySkin : skin;
    }

    public boolean applyEnemyHit() {
        boolean died = getActiveBehavior().onEnemyHit(this);
        if (died) {
            resetSkin();
        }
        return died;
    }

    public void resetShield() {
        getActiveBehavior().resetAfterRespawn(this);
    }

    public boolean isShielded() {
        SkinBehavior active = getActiveBehavior();
        return active.hasShield();
    }

    public boolean isSlowedDown() {
        SkinBehavior active = getActiveBehavior();
        if (active instanceof GreenSkinBehavior) {
            return ((GreenSkinBehavior) active).isSlowedDown();
        }
        return false;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        deaths++;
    }

    public void reduceDeath() {
        if (deaths > 0) deaths--;
    }

    public Skin getSkin() {
        return skin;
    }

    public Position getRespawnPosition() {
        return respawnPosition;
    }

    public void setRespawnPosition(Position pos) {
        this.respawnPosition = new Position(pos.getRow(), pos.getCol());
    }
}