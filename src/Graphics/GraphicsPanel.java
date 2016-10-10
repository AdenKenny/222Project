package Graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import gameWorld.Entity;
import gameWorld.World;
import gameWorld.World.Direction;
import gameWorld.characters.Character;
import gameWorld.rooms.Room;

/**
 * Created by kiwij on 22-Sep-16.
 */
public class GraphicsPanel extends JPanel implements MouseListener {

    // The number of squares the character can see to either side.
    private static final int viewWidth = 4;
    // The number of squares the character can see ahead of them.
    private static final int viewDistance = 5;

    private GraphicsClickListener clickListener;

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

    private Character viewer;
    
    private ImageCache cache;

    /**
     * Create a new GraphicsPanel that displays the given room from the perspective of the given character.
     * @param inViewer
     * @param initRoom
     */
    public GraphicsPanel(Character inViewer){
        super();
        cache = new ImageCache();
        if (inViewer == null){
        	throw new IllegalArgumentException("Null is an unacceptable parameter for inViewer!");
        }
        viewer = inViewer;
        addMouseListener(this);
    }

    /**
     * Set the object that the GraphicsPanel will notify if there is a click.
     * @param input
     */
    public void setGraphicsClickListener(GraphicsClickListener input){
        clickListener = input;
    }

    @Override
    public void mouseClicked(MouseEvent event){
        int x = event.getX();
        int y = event.getY();
        Entity entity = calculateClickedEntity(y, x);
        clickListener.onClick(entity, SwingUtilities.isRightMouseButton(event), x, y);
    }

    private Entity calculateClickedEntity(int y, int x){
        // Calculate the upper most bound of all rendered items.
        int yOrigin = (getHeight() / 3) - (squarePixelHeight * 3);
        // If click was above the maximum location, then then it cannot be a click on an entity.
        if (y < yOrigin){
            return null;
        } else {
            int adjustedClickY = y - yOrigin;
            int forwardDelta = adjustedClickY / squarePixelHeight;
            // Convert from absolute to relative.
            int sideDelta = (x / squarePixelWidth) - viewDistance;
            // Find the entity at the calculated location.
            Entity result = getEntityAtLocation(viewer.room(), calculateCoordinatesFromRelativeDelta(viewer.facing(), viewer.yPos(), viewer.xPos(),
                    sideDelta, forwardDelta));
            //If the result was null, check the square behind where there was a click, as an entity may have its lower half behind this upper half.
            //Then check again if still null, to check the square behind that.
            for (int i = 0; i < 3; ++i){
	            if (result == null){
	                forwardDelta += 1;
	                //Check that the target object is in view.
	                if (forwardDelta < viewDistance){
	                    result = getEntityAtLocation(viewer.room(), calculateCoordinatesFromRelativeDelta(viewer.facing(), viewer.yPos(), viewer.xPos(),
	                            sideDelta, forwardDelta));
	                }
	
	            }
            }
            return result;
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        render(viewer,graphics);
    }

    private void render(Character character, Graphics graphics){
    	Room room = character.room();
        // Refresh the size of a square.
    	int height = getHeight();
        squarePixelHeight = (height - (height / 3)) / (viewDistance - 1);
        squarePixelWidth = getWidth() / (viewWidth * 2);
        renderCeiling(graphics);
        renderFloor(graphics);
        int[] viewerLocation = locateViewer(character);
        // If player is found, render.
        if (viewerLocation[0] >= 0) {
            for (int forwardDelta = viewDistance; forwardDelta > 0; --forwardDelta) {
                for (int absSideDelta = viewWidth; absSideDelta >= 0; --absSideDelta) {
                    //Render right
                    renderEntity(character.facing(), viewerLocation[0], viewerLocation[1], absSideDelta, forwardDelta, room, graphics);
                    //Render left
                    renderEntity(character.facing(), viewerLocation[0], viewerLocation[1], -absSideDelta, forwardDelta, room, graphics);
                }
            }
        }
    }

    private void renderCeiling(Graphics graphics){
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0,getWidth(), getHeight() / 2);
    }

    private void renderFloor(Graphics graphics){
        try {
            Image image = cache.getImage("/resources/graphics/floor.png");
            int height = getHeight();
            graphics.drawImage(image, 0, height / 3, getWidth(), height - (height / 3),  null);
        } catch (IOException ioe){
        }
    }

    private void renderEntity(World.Direction viewerDirection, int viewerY, int viewerX, int sideDelta, int forwardDelta, Room room, Graphics graphics){
        int[] absoluteTarget = calculateCoordinatesFromRelativeDelta(viewerDirection, viewerY, viewerX, sideDelta, forwardDelta);
        if (isLocationDoor(absoluteTarget, room)){
        	renderDoor(sideDelta, forwardDelta, graphics);
        } if (isLocationWall(absoluteTarget, room)){
        	renderWall(sideDelta, forwardDelta, graphics);
        } else if (isLocationBlackSpace(absoluteTarget, room)){
        	renderBlackSpace(sideDelta, forwardDelta, graphics);
        } else {
	        Entity entity = getEntityAtLocation(room, absoluteTarget);
	        //Don't render null entities or the viewer.
	        if (entity != null && entity != viewer) {
	        	String name;
	        	if (entity.isPlayer()){
	        		name = "player";
	        	} else {
	        		name = entity.name();
	        	}
	            int[] originPixel = calculateOriginPixelFromRelativeDelta(sideDelta, forwardDelta);
	            Side side = calculateSide(viewerDirection, entity.facing(), new int[] {viewerY, viewerX}, absoluteTarget);
	            graphics.drawImage(loadImage(name, side), originPixel[1], originPixel[0], squarePixelWidth, calculateScaledSpriteHeight(forwardDelta), null);
	        }
        }
    }
    
    private void renderWall(int sideDelta, int forwardDelta, Graphics graphics){
    	int[] location = calculateOriginPixelFromRelativeDelta(sideDelta, forwardDelta);
    	try {
			graphics.drawImage(cache.getImage("/resources/graphics/wall.png"), location[1], location[0], squarePixelWidth, calculateScaledSpriteHeight(forwardDelta), null);
		} catch (IOException e) {
		}
    }
    
    private void renderDoor(int sideDelta, int forwardDelta, Graphics graphics){
    	int[] location = calculateOriginPixelFromRelativeDelta(sideDelta, forwardDelta);
    	try {
			graphics.drawImage(cache.getImage("/resources/graphics/door.png"), location[1], location[0], squarePixelWidth, calculateScaledSpriteHeight(forwardDelta), null);
		} catch (IOException e) {
		}
    }
    
    private void renderBlackSpace(int sideDelta, int forwardDelta, Graphics graphics){
    	int[] location = calculateOriginPixelFromRelativeDelta(sideDelta, forwardDelta);
    	graphics.fillRect(location[1], location[0], squarePixelWidth, calculateScaledSpriteHeight(forwardDelta));
    }
    
    private Image loadImage(String name, Side side){
        try {
            return cache.getImage(resolveImageName(name, side));
        } catch (IOException ioe){
            return null;
        }
    }

    private String resolveImageName(String name, Side side){
        return String.format("/resources/graphics/%s/%s.png", name, resolveSideComponent(side));
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
        Entity[][] locations = room.entities();
        // Check if location is in the room.
        if (location[0] < 0 || location[0] >= locations.length || location[1] < 0 || location[1] >= locations[0].length){
            return null;
        } else {
            return locations[location[0]][location[1]];
        }
    }
    
    private boolean isLocationWall(int[] location, Room room){
    	return !isLocationBlackSpace(location, room) && (location[0] == -1 || location[0] == room.entities().length || location[1] == -1 || location[1] == room.entities()[0].length);
    }
    
    private boolean isLocationBlackSpace(int[] location, Room room){
    	return location[0] < -1 || location[0] > room.entities().length ||location[1] < -1 || location[1] > room.entities()[0].length;
    }
    
    private boolean isLocationDoor(int[] location, Room room){
    	Entity[][] entities = room.entities();
    	//Check for the north door.
    	if (location[0] == -1 && location[1] == entities[0].length / 2){
    		return room.hasDoor(Direction.NORTH);
    	//Check for the East door
    	} else if (location[1] == entities[0].length && location[0] == entities.length / 2){
    		return room.hasDoor(Direction.EAST);
    	//Check the south door.
    	} else if (location[0] == entities.length && location[1] == entities[0].length / 2){
    		return room.hasDoor(Direction.SOUTH);
    	} else if (location[1] == -1 && location[0] == entities.length / 2){
    		return room.hasDoor(Direction.WEST);
    	} else return false;
    }
    

    /**
     * Returns the location of the viewer in their current room.
     * @param entity
     * @param room
     * @return {y, x}
     */
    private int[] locateViewer(Character viewer){
        if (viewer == null){
        	return new int[] {-1, -1};
        } else {
        	return new int[] { viewer.yPos(), viewer.xPos()};
        }
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
                        0,
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
        // Calculate the origin pixel of the entity on the far left.
        // Translate sideOffset from 0 is center to zero is far left.
        sideDelta += viewWidth;
        // Invert forward offset, as the maximum distance should be far Y;
        forwardDelta = viewDistance - forwardDelta;
        // The origin of the image is two square heights above the bottom left.
        return new int[] {(int) ((forwardDelta * squarePixelHeight * 1) - squarePixelHeight * forwardDelta * 0.75), sideDelta * squarePixelWidth};
    }
    
    private int calculateScaledSpriteHeight(int forwardDelta){
    	return (int) ((squarePixelHeight * 3) + (0.75 * squarePixelHeight * (viewDistance - forwardDelta)));
    }

    protected Side calculateSide(World.Direction viewerDirection, World.Direction observedDirection, int[] observer, int[] observed) {
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
                return Perspective.South;
            case EAST:
                return Perspective.West;
            case SOUTH:
                return Perspective.North;
            case WEST:
                return Perspective.East;
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
					default:
						break;
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
				default:
					break;
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
				default:
					break;
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
				default:
					break;
                }
                break;
		default:
			break;
        }
        return Side.Front;
    }

    // These methods are required for the MouseListener interface, but do not do anything.

    @Override
    public void mousePressed(MouseEvent event){
        // Do nothing
    }

    @Override
    public void mouseReleased(MouseEvent event){
        // Do nothing
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        // Do nothing
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        // Do nothing
    }

}
