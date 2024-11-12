public class Animation extends Action {

    public Animation(Entity entity, int repeatCount) {
        super(entity, null, null, repeatCount);  // Animation actions do not use world or imageStore
    }

    @Override
    public String getActionKind() {
        return "ANIMATION";  // Return the string representation of the action type
    }

    @Override
    public void executeAction(EventScheduler scheduler) {
        System.out.println("Executing animation for entity: " + this.entity.getId() + ", repeatCount: " + this.repeatCount);
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            System.out.println("Scheduling the event for the entity: " + this.entity);
            scheduler.scheduleEvent(this.entity, new Animation(this.entity, Math.max(this.repeatCount - 1, 0)), this.entity.getAnimationPeriod());
        }
    }
}
