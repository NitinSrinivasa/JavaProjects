import java.util.*;

import processing.core.PImage;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public class Entity {
    /*
        Static variables: These do not need to made private. But you should move them to new classes if appropriate.

        Variables whose names end in "IDX" are indices, i.e., positions. For example, DUDE_NUM_PROPERTIES indicates
        that Dudes have 3 properties (in addition to their id, x position, y position). The DUDE_ACTION_PERIOD_IDX (0)
        indicates that the Dude's action period is its first property, DUDE_ANIMATION_PERIOD_IDX (1) indicates that
        the Dude's animation period is its second property, and so on.
     */

    // The Sapling's action and animation periods have to be in sync since it grows and gains health at same time.
    public static final double SAPLING_ACTION_ANIMATION_PERIOD = 1.000;
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final String STUMP_KEY = "stump";
    public static final int STUMP_NUM_PROPERTIES = 0;
    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH_IDX = 0;
    public static final int SAPLING_NUM_PROPERTIES = 1;
    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_ANIMATION_PERIOD_IDX = 0;
    public static final int OBSTACLE_NUM_PROPERTIES = 1;
    public static final String DUDE_KEY = "dude";
    public static final int DUDE_ACTION_PERIOD_IDX = 0;
    public static final int DUDE_ANIMATION_PERIOD_IDX = 1;
    public static final int DUDE_RESOURCE_LIMIT_IDX = 2;
    public static final int DUDE_NUM_PROPERTIES = 3;
    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_NUM_PROPERTIES = 0;
    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_ANIMATION_PERIOD_IDX = 0;
    public static final int FAIRY_ACTION_PERIOD_IDX = 1;
    public static final int FAIRY_NUM_PROPERTIES = 2;
    public static final String TREE_KEY = "tree";
    public static final int TREE_ANIMATION_PERIOD_IDX = 0;
    public static final int TREE_ACTION_PERIOD_IDX = 1;
    public static final int TREE_HEALTH_IDX = 2;
    public static final int TREE_NUM_PROPERTIES = 3;
    public static final double TREE_ANIMATION_MAX = 0.600;
    public static final double TREE_ANIMATION_MIN = 0.050;
    public static final double TREE_ACTION_MAX = 1.400;
    public static final double TREE_ACTION_MIN = 1.000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;

    // Instance variables
    private final EntityKind kind;
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int resourceLimit;
    private int resourceCount;
    private final double actionPeriod;
    private final double animationPeriod;
    private int health;
    private final int healthLimit;

    /**
     * Creates a new Entity.
     *
     * @param kind The kind of entity being created.
     * @param id The id of the new entity.
     * @param position The position (x,y coordinate) of this new entity.
     * @param images The image list associated with this entity.
     * @param resourceLimit The resourceLimit for this entity. Not all entities need this.
     * @param resourceCount The resourceCount for this entity. Not all entities need this.
     * @param actionPeriod The actionPeriod for this entity (i.e., how long it takes to perform each activity action).
     *                     Not all entities need this.
     * @param animationPeriod The animationPeriod (i.e., how long it takes to perform one animation).
     *                        Not all entities need this.
     * @param health The entity's starting health. Not all entities need this.
     * @param healthLimit The entity's upper health limit. Not all entities need this.
     */
    public Entity(EntityKind kind, String id, Point position, List<PImage> images, int resourceLimit,
                  int resourceCount, double actionPeriod, double animationPeriod, int health, int healthLimit) {
        this.kind = kind;
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public static Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.getFirst();
            int nearestDistance = nearest.position.distanceSquared(pos);

            for (Entity other : entities) {
                int otherDistance = other.position.distanceSquared(pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public EntityKind getKind() {
        return this.kind;
    }

    public String getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getHealth() {
        return health;
    }

    public PImage getCurrentImage(){
        return this.images.get(this.imageIndex % this.images.size());
    }

    public Point nextPositionDude(WorldModel world, Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz, this.position.y);

        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).kind != EntityKind.STUMP) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).kind != EntityKind.STUMP) {
                newPos = this.position;
            }
        }

        return newPos;
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

    public boolean transformSapling(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = createStump(STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList(STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        } else if (this.health >= this.healthLimit) {
            Entity tree = createTree(TREE_KEY + "_" + this.id, this.position, Functions.getNumFromRange(TREE_ACTION_MAX, TREE_ACTION_MIN), Functions.getNumFromRange(TREE_ANIMATION_MAX, TREE_ANIMATION_MIN), Functions.getIntFromRange(TREE_HEALTH_MAX, TREE_HEALTH_MIN), imageStore.getImageList(TREE_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void nextImage() {
        this.imageIndex = this.imageIndex + 1;
    }

    public boolean transformTree(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.health <= 0) {
            Entity stump = createStump(STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList(STUMP_KEY));

            world.removeEntity(scheduler, this);

            world.addEntity(stump);

            return true;
        }

        return false;
    }

    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.kind == EntityKind.TREE) {
            return this.transformTree(world, scheduler, imageStore);
        } else if (this.kind == EntityKind.SAPLING) {
            return this.transformSapling(world, scheduler, imageStore);
        } else {
            throw new UnsupportedOperationException(String.format("transformPlant not supported for %s", this));
        }
    }

    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity dude = createDudeNotFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit, this.images);

        world.removeEntity(scheduler, this);

        world.addEntity(dude);
        dude.scheduleActions(scheduler, world, imageStore);
    }

    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.resourceCount >= this.resourceLimit) {
            Entity dude = createDudeFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit, this.images);

            world.removeEntity(scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(dude);
            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        switch (this.kind) {
            case DUDE_FULL:
                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
                break;

            case DUDE_NOT_FULL:
                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
                break;

            case OBSTACLE:
                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
                break;

            case FAIRY:
                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
                break;

            case SAPLING:
                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
                break;

            case TREE:
                scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
                scheduler.scheduleEvent(this, Action.createAnimationAction(this, 0), getAnimationPeriod());
                break;

            default:
        }
    }

    public void executeDudeFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.position, new ArrayList<>(List.of(EntityKind.HOUSE)));

        if (fullTarget.isPresent() && this.moveToFull(world, fullTarget.get(), scheduler)) {
            this.transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    public void executeDudeNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest(this.position, new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));

        if (target.isEmpty() || !this.moveToNotFull(world, target.get(), scheduler) || !this.transformNotFull(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    public void executeFairyActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest(this.position, new ArrayList<>(List.of(EntityKind.STUMP)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().position;

            if (this.moveToFairy(world, fairyTarget.get(), scheduler)) {

                Entity sapling = createSapling(SAPLING_KEY + "_" + fairyTarget.get().id, tgtPos, imageStore.getImageList(SAPLING_KEY), 0);

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
    }

    public void executeTreeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!this.transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    public void executeSaplingActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.health++;
        if (!this.transformPlant(world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, Action.createActivityAction(this, world, imageStore), this.actionPeriod);
        }
    }

    public double getAnimationPeriod() {
        return switch (this.kind) {
            case DUDE_FULL, DUDE_NOT_FULL, OBSTACLE, FAIRY, SAPLING, TREE -> this.animationPeriod;
            default ->
                    throw new UnsupportedOperationException(String.format("getAnimationPeriod not supported for %s", this.kind));
        };
    }

    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
    }

    /**
     * Creates a new House.
     * @param id The new House's id.
     * @param position The new House's position (x,y coordinate) in the World.
     * @param images Images to use for House.
     * @return a new Entity whose type is House.
     */
    public static Entity createHouse(String id, Point position, List<PImage> images) {
        return new Entity(EntityKind.HOUSE, id, position, images, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Creates a new Obstacle.
     * @param id The new Obstacle's id.
     * @param position The Obstacle's x,y position in the World.
     * @param animationPeriod The time (seconds) taken for each animation.
     * @param images Images to use for the Obstacle.
     * @return a new Entity whose type is Obstacle.
     */
    public static Entity createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
        return new Entity(EntityKind.OBSTACLE, id, position, images, 0, 0, 0, animationPeriod, 0, 0);
    }

    /**
     * Creates a new Tree.
     * @param id The new Tree's id.
     * @param position The Tree's x,y position in the World.
     * @param actionPeriod The time (seconds) taken for each activity (checking its health).
     * @param animationPeriod The time (seconds) taken for each animation.
     * @param health The Tree's starting health.
     * @param images Images to use for the Tree.
     * @return a new Entity whose type is Tree.
     */
    public static Entity createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
        return new Entity(EntityKind.TREE, id, position, images, 0, 0, actionPeriod, animationPeriod, health, 0);
    }

    /**
     * Creates a new Stump.
     * @param id The new Stump's id.
     * @param position The Stump's x,y position in the world.
     * @param images Images to use for the Stump.
     * @return a new Entity whose type is Stump.
     */
    public static Entity createStump(String id, Point position, List<PImage> images) {
        return new Entity(EntityKind.STUMP, id, position, images, 0, 0, 0, 0, 0, 0);
    }

    /**
     * Creates a new Sapling.
     * @param id The new Sapling's id.
     * @param position The Sapling's x,y position in the World.
     * @param images Images to use for the Sapling.
     * @param health The Sapling's starting health.
     *               Note that the Sapling also has an upper health limit, after which it will turn into a tree.
     *               Like the Tree, the Sapling also has an action and animation period, but those are not parameters
     *               since they need to be kept in sync with each other. The Sapling's activity is to grow.
     * @return a new Entity whose type is Sapling.
     */
    public static Entity createSapling(String id, Point position, List<PImage> images, int health) {
        return new Entity(EntityKind.SAPLING, id, position, images, 0, 0, SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, 0, SAPLING_HEALTH_LIMIT);
    }

    /**
     * Creates a new Fairy.
     * @param id The Fairy's id
     * @param position The Fairy's x,y location in the World.
     * @param actionPeriod The time (seconds) taken for each activity (turning a Stump into a Sapling).
     * @param animationPeriod The time (seconds) taken for each animation.
     * @param images Images to use for the Fairy.
     * @return a new Entity whose type is Fairy.
     */
    public static Entity createFairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
        return new Entity(EntityKind.FAIRY, id, position, images, 0, 0, actionPeriod, animationPeriod, 0, 0);
    }

    /**
     * Creates a new DudeNotFull.
     * @param id The Dude's id.
     * @param position The Dude's x,y position in the World.
     * @param actionPeriod The time (seconds) taken for each activity (finding and chopping down a tree or sapling.)
     * @param animationPeriod The time (seconds) taken for each animation.
     * @param resourceLimit The amount of wood this Dude can carry before becoming a DudeFull.
     * @param images Images to use for the Dude.
     * @return a new Entity whose type is DudeNotFull.
     */
    public static Entity createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new Entity(EntityKind.DUDE_NOT_FULL, id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
    }

    /**
     * Creates a new DudeFUll
     * @param id The Dude's id.
     *           If a DudeNotFull turns into a DudeFull, it will still have the same id.
     * @param position The Dude's x,y position in the World.
     * @param actionPeriod The time (seconds) taken for each activity (going to the House and turning into a DudeNotFull).
     * @param animationPeriod The time (seconds) taken for each animation.
     * @param resourceLimit The amount of wood this Dude can carry.
     * @param images Images to use for the Dude.
     * @return a new Entity whose type is DudeFull.
     */
    public static Entity createDudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
        return new Entity(EntityKind.DUDE_FULL, id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
    }
}
