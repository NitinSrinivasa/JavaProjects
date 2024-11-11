import java.util.List;
import processing.core.PImage;

public abstract class Entity {

    // Constants
    public static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000;
    public static final int SAPLING_HEALTH_LIMIT = 5;

    public static final int SAPLING_NUM_PROPERTIES = 1;  // Assuming there are 4 properties for Sapling
    public static final int SAPLING_ID_IDX = 0;
    public static final int SAPLING_POSITION_IDX = 1;
    public static final int SAPLING_ACTION_PERIOD_IDX = 2;
    public static final int SAPLING_ANIMATION_PERIOD_IDX = 3;

    public static final int DUDE_NOT_FULL_NUM_PROPERTIES = 4;  // id, position, resourceLimit, actionPeriod, animationPeriod
    public static final int DUDE_NOT_FULL_RESOURCE_LIMIT_IDX = 3;  // Index for resourceLimit in properties
    public static final int DUDE_NOT_FULL_ACTION_PERIOD_IDX = 4;  // Index for actionPeriod
    public static final int DUDE_NOT_FULL_ANIMATION_PERIOD_IDX = 5;  // Index for animationPeriod
    public static final String DUDE_NOT_FULL_KEY = "dudeNotFull";  // Key for DudeNotFull



    public static final String STUMP_KEY = "stump";
    public static final int STUMP_NUM_PROPERTIES = 0;
    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH_IDX = 0;
    //public static final int SAPLING_NUM_PROPERTIES = 1;
    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_ANIMATION_PERIOD_IDX = 0;
    public static final int OBSTACLE_NUM_PROPERTIES = 1;
    public static final String DUDE_KEY = "dude";
    public static final int DUDE_ACTION_PERIOD_IDX = 0;
    public static final int DUDE_ANIMATION_PERIOD_IDX = 1;
    public static final int DUDE_RESOURCE_LIMIT_IDX = 2;
    public static final int DUDE_NUM_PROPERTIES = 3;
    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_NUM_PROPERTIES = 0;
    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_ANIMATION_PERIOD_IDX = 0;
    public static final int FAIRY_ACTION_PERIOD_IDX = 1;
    public static final int FAIRY_NUM_PROPERTIES = 2;
    public static final String TREE_KEY = "tree";
    public static final int TREE_ANIMATION_PERIOD_IDX = 0;
    public static final int TREE_ACTION_PERIOD_IDX = 1;
    public static final int TREE_HEALTH_IDX = 2;
    public static final int TREE_NUM_PROPERTIES = 3;
    public static final double TREE_ANIMATION_MAX = 0.600;
    public static final double TREE_ANIMATION_MIN = 0.050;
    public static final double TREE_ACTION_MAX = 1.400;
    public static final double TREE_ACTION_MIN = 1.000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;
    public static final int HOUSE_CAPACITY_IDX = 3;
    public static final int HOUSE_ACTION_PERIOD_IDX = 4;
    public static final int HOUSE_ANIMATION_PERIOD_IDX = 5;

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
            System.out.println("Warning: No images available for entity: " + this.getId());
            return; // Early exit if no images
        }

        // Log the image switch
        System.out.println("Switching image for entity: " + this.getId());

        // Update the image index and wrap around if needed
        //this.imageIndex = (this.imageIndex + 1) % this.images.size();
        this.imageIndex = (this.imageIndex + 1);
        System.out.println("New image index: " + this.imageIndex); // Debugging line
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
