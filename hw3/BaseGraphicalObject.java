// import java.awt.Graphics2D;
// import java.awt.Shape;


// public abstract class BaseGraphicalObject implements GraphicalObject {
//     // this is just an example. Assume every object that inherits from this must
//     // have x,y and fields
//     // all the other properties will need the same kind of thing implemented
//     int x = 0;
//     int y = 0;
//     // you would have similar fields for all other parameters that can be
//     // constrained
//     Constraint<Integer> xc = null;
//     Constraint<Integer> yc = null;

//     // now keep track of which constraints use my values. This might alternatively
//     // go in an object for the
//     // attribute itself.
//     // somehow the constraints need to set these
//     Constraint<Integer> usesMy_x[] = null;
//     Constraint<Integer> usesMy_y[] = null;

//     protected void noteValChange(Constraint<Integer> listOfUsers[], int newval) {
//         // do something to tell each of the constraints in listOfUsers, that the value
//         // they depend on is now outofdate
//         // in the real implementation, there probably needs to be a lot more parameters
//         // and state
//         // might want to be smarter and only do this when the value is different than
//         // the old value
//     }

//     public BaseGraphicalObject() {
//         // TODO Auto-generated constructor stub
//     }

//     public int getX() {
//         if (xc != null) { // then there is a constraint on x
//             return xc.getValue();
//         } else
//             return x;
//     }

//     public void setX(int newx) {
//         if (xc != null) { // then there is a constraint on x
//             xc.setValue(newx);
//             x = newx;
//         } else
//             x = newx;
//         noteValChange(usesMy_x, newx);
//     }

//     public void setX(Constraint<Integer> newxc) { // two overloaded versions of setX - with a value or with a constraint
//         /*
//          * first probably need to check if there was already a constraint there and
//          * clean up
//          */
//         xc = newxc;
//         /* might need to do something so the constraint is set up properly */
//         /* need to get hte value of x somehow */
//         // now tell others my value has changed
//         noteValChange(usesMy_x, x);
//     }

//     public int getY() {
//         if (yc != null) { // then there is a constraint on y
//             return yc.getValue();
//         } else
//             return y;
//     }

//     public void setY(int newy) {
//         if (yc != null) { // then there is a constraint on y
//             yc.setValue(newy);
//         } else
//             y = newy;
//     }

//     public void setY(Constraint<Integer> newyc) {// two overloaded versions of setY - with a value or with a constraint
//         /*
//          * first probably need to check if there was already a constraint there and
//          * clean up
//          */
//         yc = newyc;
//         /* might need to do something so the constraint is set up properly */
//     }

//     @Override
//     public void draw(Graphics2D graphics, Shape clipShape) {
//         // TODO Auto-generated method stub
//     }

//     @Override
//     public BoundaryRectangle getBoundingBox() {
//         // TODO Auto-generated method stub
//         return null;
//     }

//     @Override
//     public void moveTo(int x, int y) {
//         // TODO Auto-generated method stub

//     }

//     @Override
//     public Group getGroup() {
//         // TODO Auto-generated method stub
//         return null;
//     }

//     @Override
//     public void setGroup(Group group) {
//         // TODO Auto-generated method stub

//     }

//     @Override
//     public boolean contains(int x, int y) {
//         // TODO Auto-generated method stub
//         return false;
//     }
// }