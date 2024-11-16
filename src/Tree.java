import java.util.List;
import processing.core.PImage;

public class Tree extends Entity {
    public static final double TREE_ACTION_MAX = 1.400;
    public static final double TREE_ACTION_MIN = 1.000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;
    public static final String TREE_KEY = "tree";
    public static final int TREE_ANIMATION_PERIOD_IDX = 0;
    public static final int TREE_ACTION_PERIOD_IDX = 1;
    public static final int TREE_HEALTH_IDX = 2;
    public static final int TREE_NUM_PROPERTIES = 3;
    public static final double TREE_ANIMATION_MAX = 0.600;
    public static final double TREE_ANIMATION_MIN = 0.050;


    public Tree(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health) {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod, health, TREE_HEALTH_MAX);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), this.animationPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        // Attempt to transform the tree, if health <= 0
        if (!this.transformTree(world, scheduler, imageStore)) {
            // If transformation isn't done, schedule next activity
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

        /**
     * Transforms the tree into a stump if its health is <= 0.
     * @param world The world in which the tree exists.
     * @param scheduler The event scheduler for managing scheduled events.
     * @param imageStore The image store used for creating stump images.
     * @return true if the tree was transformed, false if it still has health.
     */
    public boolean transformTree(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            // Create a stump and replace the tree in the world
            Entity stump = Stump.createStump(Stump.STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList(Stump.STUMP_KEY));

            // Remove the tree from the world and add the stump
            world.removeEntity(scheduler, this);
            world.addEntity(stump);


            return true;
        }
        return false;  // No transformation if the tree still has health
    }

    public static Entity createTree(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod, int health) {
        return new Tree(id, position, images, actionPeriod, animationPeriod, health);
    }

    @Override
    public String getKind() {
        return "TREE";
    }
}
