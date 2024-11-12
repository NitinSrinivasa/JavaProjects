public abstract class Action {
    protected final Entity entity;
    protected final WorldModel world;
    protected final ImageStore imageStore;
    protected final int repeatCount;

    public Action(Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    // Abstract method to be implemented by subclasses to get the action type
    public abstract String getActionKind();

    // Factory method to create ActivityAction or AnimationAction
    public static Action createActivityAction(Entity entity, WorldModel world, ImageStore imageStore) {
        return new Activity(entity, world, imageStore);
    }

    public static Action createAnimationAction(Entity entity, int repeatCount) {
        return new Animation(entity, repeatCount);
    }

    // Method to execute the action
    public abstract void executeAction(EventScheduler scheduler);
}
