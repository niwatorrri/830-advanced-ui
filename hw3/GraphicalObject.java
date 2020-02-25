import java.awt.*;
// import java.lang.reflect.Field;

public interface GraphicalObject {
    public void draw(Graphics2D graphics, Shape clipShape);
    public BoundaryRectangle getBoundingBox();
    public void moveTo(int x, int y);
    public Group getGroup();
    public void setGroup(Group group);
    public boolean contains(int x, int y);

    // public <T> T get(String fieldName) throws NoSuchFieldException, IllegalAccessException;
    //         throws NoSuchFieldException, IllegalAccessException {
    //     Field field = this.getClass().getDeclaredField(fieldName);
    //     field.setAccessible(true);
    //     return field.get(this);
    // }
}