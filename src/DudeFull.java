import java.util.List;
import java.util.Optional;

import processing.core.PImage;

public class DudeFull extends Dude {
    // Constructor for DudeFull, now passing all required parameters to the superclass constructor
    public DudeFull(String id, Point position, List<PImage> images, double actionPeriod,
                double animationPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    @Override
    public String getKind() {
        return "DUDE_FULL";
    }
/*
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        // Look for a nearby House to deliver resources to
        House house = findHouse(world);

        if (house != null) {
            // Deliver resources to the house
            house.storeResources(1);  // Deliver 1 resource
            System.out.println(this.id + " delivered resources to the house.");

            // Transform this DudeFull into a DudeNotFull with resource count reset to 0
            DudeNotFull newDude = new DudeNotFull(
                    this.id,
                    this.position,
                    this.images,
                    this.resourceLimit,
                    0,  // Reset resource count to 0
                    this.actionPeriod,
                    this.animationPeriod
            );

            // Replace DudeFull with DudeNotFull in the world
            world.removeEntityAt(this.position);
            world.addEntity(newDude);

            // Schedule the activity for the new DudeNotFull
            scheduler.scheduleEvent(newDude, Action.createActivityAction(newDude, world, imageStore), newDude.getActionPeriod());
        } else {
            // No house found, reschedule activity for DudeFull
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }
*/
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        //world.findNearest(this.position, List.of(Tree.class, Sapling.class));
        Optional<Entity> fullTarget = world.findNearest(this.position, List.of(House.class));

        if (fullTarget.isPresent() && this.moveToFull(world, fullTarget.get(), scheduler)) {
            this.transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity dude = new DudeNotFull(this.id, this.position, this.images,this.resourceLimit, this.resourceCount,this.actionPeriod,
                                        this.animationPeriod );

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        ((Dude) dude).scheduleActions(scheduler, world, imageStore);
    }

    public boolean moveToFull(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.position)) {
            return true;
        } else {
            Point nextPos = this.nextPositionDude(world, target.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }



    private House findHouse(WorldModel world) {
        // Logic to find an available house where the DudeFull can deliver resources
        // This could search within a certain radius, or find a house nearby
        return world.getNearbyHouse(this.position); // Placeholder for actual house lookup method
    }

    // Return the action period for DudeFull
    @Override
    public double getActionPeriod() {
        return this.actionPeriod; // Action period is passed from the constructor and stored as a field
    }
}
