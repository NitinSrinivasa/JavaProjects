import java.util.ArrayList;
import java.util.List;
import processing.core.PImage;
import java.util.Optional;

public class Fairy extends Entity {

    public Fairy(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod, 1, 0);  // Assuming 0 for health/other properties
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        // Schedule the next activity and animation for the fairy
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, this.imageIndex), this.animationPeriod); // Use imageIndex from base class
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        System.out.println("Fairy is executing activity");

        // Find the nearest stump (or any other target)
        Optional<Entity> fairyTarget = world.findNearest(this.position, List.of(Stump.class));

        //System.out.println("Fairy Target:" + fairyTarget.get().getKind());
        if (fairyTarget.isPresent()) {
            System.out.println("In executeActivity in Fairy - Target is present");
            Point tgtPos = fairyTarget.get().getPosition();  // Get the target position

            // Try to move towards the target
            if (this.moveToFairy(world, fairyTarget.get(), scheduler)) {
                System.out.println("In executeActivity in Fairy - Movetofairy");
                // Create a new sapling entity if the fairy reaches the target
                Entity sapling = Sapling.createSapling(SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList(SAPLING_KEY), Sapling.SAPLING_ACTION_ANIMATION_PERIOD, Entity.SAPLING_ACTION_ANIMATION_PERIOD);

                // Add the sapling to the world and schedule its actions
                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        // Reschedule the next activity for the fairy
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
    }


    // Static factory method to create a Fairy instance
    public static Entity createFairy(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        return new Fairy(id, position, images, actionPeriod, animationPeriod);
    }

    @Override
    public String getKind() {
        return "FAIRY";  // Return the type of the entity
    }

    public void executeAnimationAction(EventScheduler scheduler) {
        System.out.println("in Fairy executeAnimationAction");
        super.nextImage();  // Use base class method for image cycling

        // Reschedule the animation to continue indefinitely
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, this.imageIndex), this.animationPeriod);
    }

    public boolean moveToFairy(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.position.adjacent(target.position)) {
            world.removeEntity(scheduler, target);
            return true;
        } else {
            Point nextPos = this.nextPositionFairy(world, target.position);

            if (!this.position.equals(nextPos)) {
                world.moveEntity(scheduler, this, nextPos);
            }
            return false;
        }
    }

    public Point nextPositionFairy(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }
}
    /*
    // Move the fairy towards the target (e.g., a stump)
    public boolean moveToFairy(WorldModel world, Entity target, EventScheduler scheduler) {
        // Get the current position and target's position
        Point targetPos = target.getPosition();
        Point currentPos = getPosition();

        // Calculate the direction to move towards the target (diagonal or straight)
        int dx = Integer.compare(targetPos.x, currentPos.x);  // -1, 0, or 1
        int dy = Integer.compare(targetPos.y, currentPos.y);  // -1, 0, or 1

        // Calculate the new position by moving one step towards the target
        Point newPosition = new Point(currentPos.x + dx, currentPos.y + dy);

        // Ensure the new position is within bounds and not occupied
        if (world.withinBounds(newPosition) && !world.isOccupied(newPosition)) {
            // Move the fairy to the new position
            world.moveEntity(scheduler, this, newPosition);
            return true;  // Successfully moved
        }

        return false;  // Failed to move
    }
}
*/