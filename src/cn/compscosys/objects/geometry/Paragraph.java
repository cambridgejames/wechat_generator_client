package cn.compscosys.objects.geometry;

public class Paragraph {
	public int x;
	public int singleLine;
	public int doubleLine;
	public int height;
	
	public Paragraph(int x, int singleLine, int doubleLine, int height) {
		this.x = x;
		this.singleLine = singleLine;
		this.doubleLine = doubleLine;
		this.height = height;
	}
	
	@Override
	public String toString() {
		return "[Paragraph: (" + x + ", " + singleLine + ", " + doubleLine + ", " + height + ")]";
	}
}
