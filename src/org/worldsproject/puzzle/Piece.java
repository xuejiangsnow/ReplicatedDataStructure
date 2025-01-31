package org.worldsproject.puzzle;

import java.io.Serializable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class Piece implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int idSource = 0;
	private final int serial = ++idSource;
	private int x = 0;
	private int y = 0;
	private boolean filter = false;
	
	private Piece top = null;
	private Piece right = null;
	private Piece bottom = null;
	private Piece left = null;

	private transient PuzzleGroup group;

	private Bitmap original;
	private Bitmap display;
	
	private ImageView image;

	private int offset = 0;
	/*
	 * 0 is the correct orientation. 1 is 90 degrees turned clockwise. 2 is 180
	 * degrees turned clockwise. 3 is 270 degrees turned clockwise.
	 */
	private int orientation = 0;

	private boolean isOnDown = false;
	
	public Piece(Context c, Bitmap image, int offset) {
		this.original = image;
		this.display = image;
		this.offset = offset;
		this.group = new PuzzleGroup(this);
		this.group.addPiece(this);
		this.image = new ImageView(c);
		this.image.setImageBitmap(display);
	}

	public void turn() {
		orientation++;
		Matrix m = new Matrix();
		m.setRotate(90, original.getWidth() / 2, original.getHeight() / 2);
		original = Bitmap.createBitmap(original, 0, 0, original.getWidth(),
				original.getHeight(), m, true);

		if (orientation >= 4) {
			orientation = 0;
		}
	}

	public void draw(Canvas c, boolean flag) {
		/**
		if (!flag) {
			c.drawBitmap(display, x, y, null);
		} else {
			Paint paint = new Paint();  
            paint .setStyle(Paint.Style.STROKE);   //����  
            paint .setAlpha(75); 
            c.drawBitmap(display, x, y, paint);
		}
		**/
		c.drawBitmap(display, x, y, null);
			
	}

	public Bitmap getDisplay() {
		return display;
	}
	
	public void setDisplay(Bitmap bitmap) {
		display = bitmap;
	}
	
	public ImageView getImage() {
		return image;
	}
	
	public Piece getTop() {
		return top;
	}

	public void setTop(Piece top) {
		this.top = top;
	}

	public Piece getRight() {
		return right;
	}

	public void setRight(Piece right) {
		this.right = right;
	}

	public Piece getBottom() {
		return bottom;
	}

	public void setBottom(Piece bottom) {
		this.bottom = bottom;
	}

	public Piece getLeft() {
		return left;
	}

	public void setLeft(Piece left) {
		this.left = left;
	}

	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int nx) {
		this.x = nx;
	}
	
	public void setY(int ny) {
		this.y = ny;
	}
	
	public int getHeight() {
		 return this.display.getHeight();
	}
	
	public int getWidth() {
		return this.display.getWidth();
	}

	public int getOrientation() {
		return this.orientation;
	}

	public boolean getIsOnDown() {
		return isOnDown;
	}
	
	public void setIsOnDown(boolean f) {
		isOnDown = f;
	}
	
	public boolean inTop() {
		if (top == null)
			return false;

		return top.inMe(this.x, this.y)
				|| top.inMe(this.x + this.display.getWidth(), y);
	}

	public boolean inRight() {
		if (right == null)
			return false;

		return right.inMe(this.x + this.display.getWidth(), y)
				|| right.inMe(this.x + this.display.getWidth(), y);
	}

	public boolean inBottom() {
		if (bottom == null)
			return false;

		return bottom.inMe(this.x + this.display.getWidth(),
				y + this.display.getHeight())
				|| bottom.inMe(this.x, this.display.getHeight() + y);
	}

	public boolean inLeft() {
		if (left == null)
			return false;

		return left.inMe(this.x, this.y)
				|| left.inMe(this.x, this.y + this.display.getHeight());
	}

	public void addToGroup(PuzzleGroup pg) {
		this.group = pg;
	}

	public PuzzleGroup getGroup() {
		return this.group;
	}

	public void setGroup(PuzzleGroup g) {
		this.group = g;
	}

	public boolean inMe(float x, float y) {
		if (x >= this.x && x <= (this.x + this.display.getWidth())
				&& y >= this.y && y <= (this.y + this.display.getHeight())) {
			return true;
		}

		return false;
	}

	@SuppressLint("NewApi")
	public void snap(Piece p) {
		if (p == null || group.sameGroup(this, p)) {
			return;
		}

		if (p == this.top) {
			int mx = x - p.getX();
			int my = y - (p.getY() + p.getHeight());
			group.translate(mx, my + (offset * 2));
			//group.translate(mx, my);
		}

		if (p == this.right) {
			int mx = x - (p.getX() - this.getWidth());
			int my = y - p.getY();
			group.translate(mx - (offset * 2), my);
			//group.translate(mx, my);
		}

		if (p == this.bottom) {
			int mx = x - p.getX();
			int my = y - (p.getY() - this.getHeight());
			group.translate(mx, my - (offset * 2));
			//group.translate(mx, my);
		}

		if (p == this.left) {
			int mx = x - (p.getX() + p.getWidth());
			int my = y - p.getY();
			group.translate(mx + (offset * 2), my);
			//group.translate(mx, my);
		}

		this.group.addGroup(p.getGroup());
		//this.group.write("group", this.group);
		this.group.write("updategroup", this.group);
		//this.group.write("test", new S());
	}

	public int getSerial() {
		return serial;
	}

	public Bitmap getOriginal() {
		return original;
	}

	public int getOffset() {
		return offset;
	}

	public String toString() {
		return "(" + this.getX() + ", " + this.getY() + ")";
	}
	
	public static void resetSerial() {
		idSource = 0;
	}
	
	public class S {
		public String t1;
		public String t2;
		public String[] temp;
		
		private S() {
			t1 = "asdf";
			t2 = "dsdf";
			temp = new String[2];
			temp[0] = t1;
			temp[1] = t2;
		}
	}

	public void setF(boolean b) {
		filter = b;
	}
	
	public boolean getFilter() {
		return filter;
	}
}
