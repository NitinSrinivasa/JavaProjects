import java.util.List;
import processing.core.PImage;

public abstract class Dude extends Entity {
    // Constants for indexing properties
    private static final int DUDE_ACTION_PERIOD_IDX = 0;
    private static final int DUDE_ANIMATION_PERIOD_IDX = 1;
    private static final int DUDE_RESOURCE_LIMIT_IDX = 2;
    private static final int DUDE_NUM_PROPERTIES = 3;

    // Constructor for Dude class
    public Dude(String id, Point position, List<PImage> images, int resourceLimit,
                int resourceCount, double actionPeriod, double animationPeriod) {
        // Calling the super constructor to initialize inherited properties
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, 0, resourceLimit);
    }

    @Override
    public String getKind() {
        return "DUDE";
    }

    // Scheduling actions for all Dude subclasses (such as DudeFull and DudeNotFull)
    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        // Schedule the activity action (which will be defined in each subclass) and animation action
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), this.animationPeriod);
    }

    // Abstract method to execute activity; each subclass will define its specific behavior
    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    // Abstract method for getting the action period (to be implemented by subclasses)
    public abstract double getActionPeriod();

    // Getters for resource limit and resource count (if needed for subclasses)
    public int getResourceLimit() {
        return this.resourceLimit;
    }

    public int getResourceCount() {
        return this.resourceCount;
    }

    public void incrementResourceCount(int amount) {
        this.resourceCount += amount;
    }

    public void decrementResourceCount(int amount) {
        this.resourceCount -= amount;
    }


    public Point nextPositionDude(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos) && !world.getOccupancyCell(newPos).getKind().equals("STUMP")) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos) && !world.getOccupancyCell(newPos).getKind().equals("STUMP")) {
                newPos = this.position;
            }
        }

        return newPos;
    }

}
