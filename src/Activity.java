public class Activity extends Action {

    public Activity(Entity entity, WorldModel world, ImageStore imageStore) {
        super(entity, world, imageStore, 0);  // Activity actions do not use repeatCount
    }

    @Override
    public String getActionKind() {
        return "ACTIVITY";  // Return the string representation of the action type
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        switch (this.entity.getKind()) {
            case "SAPLING" -> this.entity.executeActivity(this.world, this.imageStore, scheduler);
            case "TREE" -> this.entity.executeActivity(this.world, this.imageStore, scheduler);
            case "FAIRY" -> this.entity.executeActivity(this.world, this.imageStore, scheduler);
            case "DUDE_NOT_FULL" -> this.entity.executeActivity(this.world, this.imageStore, scheduler);
            case "DUDE_FULL" -> this.entity.executeActivity(this.world, this.imageStore, scheduler);
            default -> throw new UnsupportedOperationException(String.format("Activity not supported for %s", this.entity.getKind()));
        }
    }
}
