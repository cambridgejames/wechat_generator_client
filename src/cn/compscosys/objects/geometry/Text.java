package cn.compscosys.objects.geometry;

public class Text {
	public int x;
	public int y;
	public int height;
	
	public Text(int x, int y, int height) {
		this.x = x;
		this.y = y;
		this.height = height;
	}
	
	@Override
	public String toString() {
		return "[Point: (" + x + ", " + y + ", " + height + ")]";
	}
}
