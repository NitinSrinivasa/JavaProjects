import java.util.List;
import processing.core.PImage;

public class Sapling extends Entity {
    public static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000;
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final int SAPLING_NUM_PROPERTIES = 1;
     //public static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000;
    //public static final int SAPLING_HEALTH_LIMIT = 5;

    //public static final int SAPLING_NUM_PROPERTIES = 1;  // Assuming there are 4 properties for Sapling
    public static final int SAPLING_ID_IDX = 0;
    public static final int SAPLING_POSITION_IDX = 1;
    public static final int SAPLING_ACTION_PERIOD_IDX = 2;
    public static final int SAPLING_ANIMATION_PERIOD_IDX = 3;
    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH_IDX = 0;
    //public static final int SAPLING_NUM_PROPERTIES = 1;

    public Sapling(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        super(id, position, images, 0, 0, actionPeriod, animationPeriod, 0, SAPLING_HEALTH_LIMIT);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), this.animationPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        // Increment the health of the sapling (growth process)
        this.health++;

        // Attempt to transform the sapling into a tree
        if (!this.transformPlant(world, scheduler, imageStore)) {
            // If it hasn't transformed into a tree, reschedule the activity
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    public static Entity createSapling(String id, Point position, List<PImage> images, double actionPeriod, double animationPeriod) {
        return new Sapling(id, position, images, actionPeriod, animationPeriod);
    }
    @Override
    public String getKind() {
        return "SAPLING";
    }

    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            // Transform the sapling into a stump if health is <= 0
            Stump stump = new Stump(Stump.STUMP_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList(Stump.STUMP_KEY));
            world.removeEntity(scheduler, this);  // Remove the sapling from the world
            world.addEntity(stump);     // Add the stump to the world
            return true;  // Transformation to stump was successful
        } else if (this.health >= this.healthLimit) {
            // Use Functions to assign random values for tree properties
            double treeActionPeriod = Functions.getNumFromRange(Tree.TREE_ACTION_MAX, Tree.TREE_ACTION_MIN);
            double treeAnimationPeriod = Functions.getNumFromRange(Tree.TREE_ANIMATION_MAX, Tree.TREE_ANIMATION_MIN);
            int treeHealth = Functions.getIntFromRange(Tree.TREE_HEALTH_MAX, Tree.TREE_HEALTH_MIN);

            // Transform the sapling into a tree
            Entity tree = new Tree(Tree.TREE_KEY + "_"+this.getId(), this.getPosition(), imageStore.getImageList(Tree.TREE_KEY),
                                   treeActionPeriod, treeAnimationPeriod, treeHealth);
            world.removeEntity(scheduler, this);  // Remove the sapling from the world
            world.addEntity(tree);     // Add the tree to the world
            tree.scheduleActions(scheduler, world, imageStore);  // Schedule actions for the new tree
            return true;  // Transformation to tree was successful
        }

        // Not ready to transform yet
        return false;
    }



}
