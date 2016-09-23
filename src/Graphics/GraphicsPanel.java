package Graphics;

import gameWorld.Location;
import gameWorld.World;

import javax.swing.*;

/**
 * Created by kiwij on 22-Sep-16.
 */
public class GraphicsPanel extends JPanel {

    public enum Side {
        Front,
        Right,
        Back,
        Left
    }

    private enum Perspective {
        North,
        East,
        South,
        West
    }

    private Side calculateSide(Location observer, Location observed) {
        //Validate
        if (observer == null || observer.entity() == null || observed == null | observed.entity() == null){
            return Side.Front;
        };
        return sideFromPerspectiveAndDirection(
                calculatePerspective(
                        observer.entity().facing(),
                        observer.xPos(),
                        observer.yPos(),
                        observed.xPos(),
                        observed.yPos()
                ),
                observed.entity().facing()
        );
    }

    // a is observer, b is observed
    private Perspective calculatePerspective(World.Direction observerDirection, int ax, int ay, int bx, int by){
        int deltaX = ax - bx;
        int deltaY = ay - by;
        int absDeltaX = Math.abs(deltaX);
        int absDeltaY = Math.abs(deltaY);
        if (absDeltaX < absDeltaY){
            return calculateNSPerspective(deltaY);
        } else if (absDeltaX > absDeltaY){
            return calculateEWPerspective(deltaX);
        } else {
            return perspectiveFromViewerDirection(observerDirection);
        }
    }

    private Perspective calculateNSPerspective(int deltaY){
        if (deltaY > 0){
             return Perspective.South;
        } else {
            return Perspective.North;
        }
    }

    private Perspective calculateEWPerspective(int deltaX){
        if (deltaX > 0){
            return Perspective.East;
        } else {
            return Perspective.West;
        }
    }

    private Perspective perspectiveFromViewerDirection(World.Direction viewerDirection){
        switch (viewerDirection){
            case NORTH:
                return Perspective.West;
            case EAST:
                return Perspective.South;
            case SOUTH:
                return Perspective.East;
            case WEST:
                return Perspective.North;
            default:
                return Perspective.South;
        }
    }

    private Side sideFromPerspectiveAndDirection(Perspective viewerPerspective, World.Direction observedDirection){
        switch (viewerPerspective){
            case North:
                    switch (observedDirection){
                        case NORTH:
                            return Side.Front;
                        case EAST:
                            return Side.Left;
                        case SOUTH:
                            return Side.Back;
                        case WEST:
                            return Side.Right;
                    }
                    break;
            case East:
                switch (observedDirection){
                    case NORTH:
                        return Side.Right;
                    case EAST:
                        return Side.Front;
                    case SOUTH:
                        return Side.Left;
                    case WEST:
                        return Side.Back;
                }
                break;
            case South:
                switch (observedDirection){
                    case NORTH:
                        return Side.Back;
                    case EAST:
                        return Side.Right;
                    case SOUTH:
                        return Side.Front;
                    case WEST:
                        return Side.Left;
                }
                break;
            case West:
                switch (observedDirection){
                    case NORTH:
                        return Side.Left;
                    case EAST:
                        return Side.Back;
                    case SOUTH:
                        return Side.Right;
                    case WEST:
                        return Side.Front;
                }
                break;
        }
        return Side.Front;
    }

}
