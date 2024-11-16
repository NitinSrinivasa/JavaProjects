import java.util.List;
import processing.core.PImage;

public class House extends Entity {
    private int capacity;  // The maximum number of entities or resources it can store
    private int storedResources;  // The current number of resources it stores
    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_NUM_PROPERTIES = 0;
    public static final int HOUSE_CAPACITY_IDX = 3;
    public static final int HOUSE_ACTION_PERIOD_IDX = 4;
    public static final int HOUSE_ANIMATION_PERIOD_IDX = 5;

    public House(String id, Point position, List<PImage> images, int capacity, double actionPeriod, double animationPeriod) {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod, 0, 0);  // House doesn't need a resource count at first
        this.capacity = capacity;
        this.storedResources = 0;
    }

    public void storeResources(int resources) {
        if (this.storedResources + resources <= this.capacity) {
            this.storedResources += resources;
        }
    }

    public int getStoredResources() {
        return this.storedResources;
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        //scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        //scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), this.animationPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        // Implement house-specific activities, like interacting with other entities or replenishing resources
        // For example, houses could occasionally release stored resources, perform repairs, or generate other entities
    }

    // Static method to create a House
    public static Entity createHouse(String id, Point position, List<PImage> images, int capacity, double actionPeriod, double animationPeriod) {
        return new House(id, position, images, capacity, actionPeriod, animationPeriod);
    }

    @Override
    public String getKind() {
        return "HOUSE";
    }
}
