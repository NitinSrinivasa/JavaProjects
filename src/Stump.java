import java.util.List;
import processing.core.PImage;

public class Stump extends Entity {
    public Stump(String id, Point position, List<PImage> images) {
        super(id, position, images, 0, 0, 0, 0, 0, 0);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        // Stumps do not perform actions
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        // Stumps don't execute activities
    }

    /**
     * Creates a stump, which is a specific type of Entity.
     * This is assumed to be a "dead" entity or placeholder for a removed tree.
     */
    public static Entity createStump(String id, Point position, List<PImage> images) {
        // This assumes the stump is a specialized entity with its own logic
        return new Stump(id, position, images);  // Stump is a specific subclass of Entity
    }
    @Override
    public String getKind() {
        return "STUMP";
    }
}
