package behavior;

import java.util.List;
import graphics.object.GraphicalObject;

public class ChoiceBehavior implements Behavior {
     public ChoiceBehavior(int type, boolean firstOnly);
     public List<GraphicalObject> getSelection();

     public static final int SINGLE = 0;
     public static final int MULTIPLE = 1;
}