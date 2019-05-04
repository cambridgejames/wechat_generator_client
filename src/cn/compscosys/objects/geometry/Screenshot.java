package cn.compscosys.objects.geometry;

public class Screenshot {
	public int upperLimit;
	public int lowerLimit;
	
	public Screenshot(int upperLimit, int lowerLimit) {
		this.upperLimit = upperLimit;
		this.lowerLimit = lowerLimit;
	}
	
	@Override
	public String toString() {
		return "[Screenshot: From " + upperLimit + " to " + lowerLimit + "]";
	}
}
