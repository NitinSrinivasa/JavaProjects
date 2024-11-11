import java.util.List;
import processing.core.PImage;

public class House extends Entity {
    private int capacity;  // The maximum number of entities or resources it can store
    private int storedResources;  // The current number of resources it stores

    public House(String id, Point position, List<PImage> images, int capacity, double actionPeriod, double animationPeriod) {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod, 0, 0);  // House doesn't need a resource count at first
        this.capacity = capacity;
        this.storedResources = 0;
    }

    public void storeResources(int resources) {
        if (this.storedResources + resources <= this.capacity) {
            this.storedResources += resources;
        } else {
            System.out.println("House cannot store more resources. Capacity exceeded.");
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
