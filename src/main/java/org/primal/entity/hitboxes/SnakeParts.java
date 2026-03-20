package org.primal.entity.hitboxes;

import org.primal.entity.animal.SnakeEntity;
import org.primal.entity.parts.SnakePart;

@SuppressWarnings("all")
public class SnakeParts {

    private final SnakeEntity parentSnake;

    private final SnakePart head;
    private final SnakePart segment1;
    private final SnakePart segment2;
    private final SnakePart segment3;
    private final SnakePart tail;
    private final SnakePart[] subEntities;

    public SnakeParts(SnakeEntity parentSnake) {
        this.head    = new SnakePart(parentSnake, 10f/16f, 5f/16f);
        this.segment1= new SnakePart(parentSnake, 10f/16f, 5f/16f);
        this.segment2= new SnakePart(parentSnake, 10f/16f, 5f/16f);
        this.segment3= new SnakePart(parentSnake, 10f/16f, 5f/16f);
        this.tail    = new SnakePart(parentSnake, 10f/16f, 5f/16f);
        this.subEntities = new SnakePart[]{this.head, this.segment1, this.segment2, this.segment3, this.tail};
        this.parentSnake = parentSnake;
    }

    public SnakePart[] getSubEntities() {
        return subEntities;
    }

    public void positionParts(){
        float yawRad = (float) Math.toRadians(this.parentSnake.getYRot());
        double dirX =  Math.sin(yawRad);
        double dirZ = -Math.cos(yawRad);

        double bodyY = -0.025D;

        // Start
        double accumulated = -13.45d * (1d/16d);

        double forward = -20d * (1d / 16d);
        double headHalfWidth = this.head.getBbWidth() * 0.5D;

        double offset = forward + headHalfWidth;

        double headX = this.parentSnake.getX() + dirX * offset;
        double headZ = this.parentSnake.getZ() + dirZ * offset;
        double headY = this.parentSnake.getY() + (-0.5d * (1d / 16d));

        this.head.setPos(headX, headY, headZ);

        for (int i = 1; i < this.subEntities.length; i++) {

            SnakePart part = this.subEntities[i];
            // Use part width so they actually touch
            double halfWidth = part.getBbWidth() * 0.5D;

            // Move forward by previous segment size
            accumulated += halfWidth;

            double x = this.parentSnake.getX() + dirX * accumulated;
            double y = this.parentSnake.getY() + bodyY;
            double z = this.parentSnake.getZ() + dirZ * accumulated;

            part.setPos(x, y, z);

            // Prepare spacing for next segment
            accumulated += halfWidth;
        }
    }

    public SnakePart getHead() {
        return head;
    }

    public SnakePart getTail() {
        return tail;
    }
}
