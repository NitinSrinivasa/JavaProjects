import java.util.List;
import processing.core.PImage;

public class Obstacle extends Entity {
    public Obstacle(String id, Point position, List<PImage> images, double animationPeriod) {
        super(id, position, images, 0, 0, 0, animationPeriod, 0, 0);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), this.animationPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        // Obstacles don't perform activities, so leave this empty
    }
    public static Entity createObstacle(String id, Point position, List<PImage> images, double animationPeriod) {
    // Create and return an instance of Obstacle using the constructor
        return new Obstacle(id, position, images, animationPeriod);
    }
    @Override
    public String getKind() {
        return "OBSTACLE";
    }
}
