import java.util.List;
import processing.core.PImage;

public abstract class Entity {



    // Entity-specific fields
    protected String id;
    protected Point position;
    protected List<PImage> images;
    protected int imageIndex;
    protected int resourceLimit;
    protected int resourceCount;
    protected double actionPeriod;
    protected double animationPeriod;
    protected int health;
    protected int healthLimit;

    // Constructor
    public Entity(String id, Point position, List<PImage> images, int resourceLimit,
                  int resourceCount, double actionPeriod, double animationPeriod,
                  int health, int healthLimit) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
    }

    // Abstract methods
    public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    public abstract String getKind();

    // Getters and Setters
    public String getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getHealth() {
        return health;
    }

    public PImage getCurrentImage() {
        return this.images.get(this.imageIndex % this.images.size());
    }

    public void nextImage() {
        if (this.images == null || this.images.isEmpty()) {
            return; // Early exit if no images
        }
        this.imageIndex = (this.imageIndex + 1);

    }

    // Get animation period
    public double getAnimationPeriod() {
        return this.animationPeriod;
    }
    // Default log method (can be overridden in subclasses)
    public String log() {
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
    }
}
