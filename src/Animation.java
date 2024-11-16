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
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity, new Animation(this.entity, Math.max(this.repeatCount - 1, 0)), this.entity.getAnimationPeriod());
        }
    }
}
