import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class DudeNotFull extends Dude {

    public static final int DUDE_NOT_FULL_NUM_PROPERTIES = 4;  // id, position, resourceLimit, actionPeriod, animationPeriod
    public static final int DUDE_NOT_FULL_RESOURCE_LIMIT_IDX = 3;  // Index for resourceLimit in properties
    public static final int DUDE_NOT_FULL_ACTION_PERIOD_IDX = 4;  // Index for actionPeriod
    public static final int DUDE_NOT_FULL_ANIMATION_PERIOD_IDX = 5;  // Index for animationPeriod
    public static final String DUDE_NOT_FULL_KEY = "dudeNotFull";  // Key for DudeNotFull
    public DudeNotFull(String id, Point position, List<PImage> images, int resourceLimit,
                   int resourceCount, double actionPeriod, double animationPeriod) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);  // Default health and healthLimit
    }

    @Override
    public String getKind() {
        return "DUDE_NOT_FULL";
    }


    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        //world.findNearest(this.position, List.of(Stump.class));
        Optional<Entity> target = world.findNearest(this.position, List.of(Tree.class, Sapling.class));

        if (target.isEmpty() || !this.moveToNotFull(world, target.get(), scheduler) || !this.transformNotFull(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }
    public boolean moveToNotFull(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.position)) {
            this.resourceCount += 1;
            target.health--;
            return true;
        } else {
            Point nextPos = this.nextPositionDude(world, target.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }


    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            Entity dude = new DudeFull(this.id, this.position, this.images, this.actionPeriod,
                                        this.animationPeriod, this.resourceLimit, this.resourceCount);

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    private void transformToFull(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        // Create a new DudeFull instance with the same properties as DudeNotFull
        DudeFull newDude = new DudeFull(this.id, this.position, this.images, this.actionPeriod,
                                        this.animationPeriod, this.resourceLimit, this.resourceCount);

        // Remove the DudeNotFull from the world
        world.removeEntityAt(this.position);

        // Add the new DudeFull to the world
        world.addEntity(newDude);

        // Schedule the new DudeFull's activity
        Action activityAction = Action.createActivityAction(newDude, world, imageStore);
        scheduler.scheduleEvent(newDude, activityAction, newDude.getActionPeriod());


    }


    public static Entity createDudeNotFull(String id, Point position, List<PImage> images, int resourceLimit, double actionPeriod, double animationPeriod) {
        return new DudeNotFull(id, position, images, resourceLimit, 0, actionPeriod, animationPeriod);
    }

    @Override
    public double getActionPeriod() {
        return this.actionPeriod; // In case you need to customize this for DudeNotFull
    }
}
