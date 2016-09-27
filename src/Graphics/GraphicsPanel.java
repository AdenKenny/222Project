package Graphics;

import gameWorld.Entity;
import gameWorld.Location;
import gameWorld.Room;
import gameWorld.World;
import gameWorld.characters.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by kiwij on 22-Sep-16.
 */
public class GraphicsPanel extends JPanel {

    // The number of squares the player can see to either side.
    private static final int viewWidth = 10;
    // The number of squares the player can see ahead of them.
    private static final int viewDistance = 10;

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

    // The size of each square to be displayed. Refreshed on each render.
    int squarePixelHeight;
    int squarePixelWidth;

    private Player viewer;
    private Room room;

    public GraphicsPanel(Player inViewer, Room initRoom){
        super();
        viewer = inViewer;
        room = initRoom;
    }

    public void setRoom(Room newRoom){
        room = newRoom;
    }

    @Override
    public void paint(Graphics graphics) {
        render(viewer, room, graphics);
    }

    private void render(Player player, Room room, Graphics graphics){
        //Refresh the size of a square.
        squarePixelHeight = (getHeight() / 2) / viewDistance;
        squarePixelWidth = getWidth() / (viewWidth * 2);
        int[] viewerLocation = locateEntityInRoom(player, room);
        for (int forwardDelta = viewDistance; forwardDelta > 0; --forwardDelta){
            for (int absSideDelta = viewWidth;absSideDelta > 0; --absSideDelta){
                //Render right
                renderEntity(player.facing(), viewerLocation[0], viewerLocation[1], absSideDelta, forwardDelta, room, graphics);
                //Render left
                renderEntity(player.facing(), viewerLocation[0], viewerLocation[1], -absSideDelta, forwardDelta, room, graphics);
            }
        }
    }

    private void renderEntity(World.Direction viewerDirection, int viewerY, int viewerX, int sideDelta, int forwardDelta, Room room, Graphics graphics){
        int[] absoluteTarget = calculateCoordinatesFromRelativeDelta(viewerDirection, viewerY, viewerX, sideDelta, forwardDelta);
        Entity entity = getEntityAtLocation(room, absoluteTarget);
        if (entity != null) {
            int[] originPixel = calculateOriginPixelFromRelativeDelta(sideDelta, forwardDelta);
            Side side = calculateSide(viewerDirection, entity.facing(), new int[] {viewerY, viewerX}, absoluteTarget);
            graphics.drawImage(loadImage(entity.name(), side), originPixel[1], originPixel[0], squarePixelWidth, squarePixelHeight * 2, null);
        }
    }

    private Image loadImage(String name, Side side){
        try {
            return ImageIO.read(this.getClass().getClassLoader().getResource(resolveImageName(name, side)));
        } catch (IOException ioe){
            return null;
        }
    }

    private String resolveImageName(String name, Side side){
        return String.format("%s/%s", name, resolveSideComponent(side));
    }

    private String resolveSideComponent(Side side){
        switch (side){
            case Front:
                return "front";
            case Right:
                return "right";
            case Back:
                return "back";
            case Left:
                return "left";
            default:
                return "front";
        }
    }

    private Entity getEntityAtLocation(Room room, int[] location){
        Location[][] locations = room.locations();
        // Check if location is in the room.
        if (location[0] < 0 || location[0] >= locations.length || location[1] < 0 || location[1] >= locations[0].length){
            return locations[location[0]][location[1]].entity();
        } else {
            return null;
        }
    }

    /**
     * Returns the location of the entity in the room as an array in the form {y, x}.
     * If the entity is not present, y and x will be less than 0.
     * @param entity
     * @param room
     * @return {y, x}
     */
    private int[] locateEntityInRoom(Entity entity, Room room){
        Location[][] locations = room.locations();
        for (int y = 0; y < locations.length; ++y){
            for (int x = 0; x < locations.length; ++x){
                if (locations[y][x].entity() == entity) {
                    return new int[] {y, x};
                }
            }
        }
        return new int[]{-1, -1};
    }

    /**
     * Calculate a position from a delta that is relative to an entity's location and facing direction;
     * For instance, a relative delta of -5, 3 is 5 units to the left of the viewer and 3 units in front of them.
     * @param viewerDirection
     * @param viewerY
     * @param viewerX
     * @param sideDelta negative is left, positive is right.
     * @param forwardDelta
     * @return {y, x}
     */
    private int[] calculateCoordinatesFromRelativeDelta(World.Direction viewerDirection, int viewerY, int viewerX,
                                                         int sideDelta, int forwardDelta){
        return applyDelta(
                sumDelta(
                        calculateAbsoluteSideDelta(viewerDirection, sideDelta),
                        calculateAbsoluteForwardDelta(viewerDirection, forwardDelta)
                )
                ,viewerY
                ,viewerX);
    }

    /**
     * Convert from a direction and relative side delta to an absolute delta.
     * @param viewerDirection
     * @param sideDelta
     * @return {delta y, delta x}
     */
    private int[] calculateAbsoluteSideDelta(World.Direction viewerDirection, int sideDelta){
        switch(viewerDirection) {
            case NORTH:
                return new int[] {
                        0,
                        sideDelta
                };
            case EAST:
                return new int[] {
                    sideDelta,
                    0
                };
            case SOUTH:
                return new int[] {
                    0,
                    -sideDelta
                };
            case WEST:
                return new int[] {
                    -sideDelta,
                    0
                };
            default:
                return new int[]{
                    0,
                    0
                };
        }
    }

    /**
     * Calculates an absolute delta from a direction and a forward delta.
     * @param viewerDirection
     * @param forwardDelta
     * @return {y, x}
     */
    private int[] calculateAbsoluteForwardDelta(World.Direction viewerDirection, int forwardDelta){
        switch(viewerDirection) {
            case NORTH:
                return new int[] {
                        -forwardDelta,
                        0
                };
            case EAST:
                return new int[] {
                        0,
                        forwardDelta
                };
            case SOUTH:
                return new int[] {
                        forwardDelta,
                        0
                };
            case WEST:
                return new int[] {
                        0
                        -forwardDelta
                };
            default:
                return new int[]{
                        0,
                        0
                };
        }
    }

    /**
     * Returns the sum of the two deltas.
     * @param a {y, x}
     * @param b {y, x}
     * @return {y, x}
     */
    private int[] sumDelta(int[] a, int[] b){
        return new int[] {a[0] + b[0], a[1] + b[1]};
    }
    
    private int[] applyDelta(int[] delta, int y, int x){
        return new int[] {y + delta[0], x + delta[1]};
    }

    /**
     * Calculate the origin pixel for the specified relative delta.
     * @param sideDelta
     * @param forwardDelta
     * @return
     */
    private int[] calculateOriginPixelFromRelativeDelta(int sideDelta, int forwardDelta){
        int halfHeight = getHeight() / 2;
        // Calculate the origin pixel of the entity on the far left.
        // Translate sideOffset from 0 is center to zero is far left.
        sideDelta += viewWidth;
        // Invert forward offset, as the maximum distance should be far Y;
        forwardDelta = viewDistance - forwardDelta;
        // The origin of the image is two square heights above the bottom left.
        return new int[] {halfHeight + (forwardDelta * squarePixelHeight) + (2 * squarePixelHeight) , sideDelta * squarePixelWidth};
    }

    private Side calculateSide(World.Direction viewerDirection, World.Direction observedDirection, int[] observer, int[] observed) {
        return sideFromPerspectiveAndDirection(
                calculatePerspective(
                        viewerDirection,
                        observer[1],
                        observer[0],
                        observed[1],
                        observed[0]
                ),
                observedDirection
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
