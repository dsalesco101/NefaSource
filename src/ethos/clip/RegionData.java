package ethos.clip;

public class RegionData {
	private int regionHash, landscape, objects;

	public int getRegionHash() {
		return regionHash;
	}

	public int getLandscape() {
		return landscape;
	}

	public int getObjects() {
		return objects;
	}

	public RegionData(int regionHash, int landscape, int objects) {
		this.regionHash = regionHash;
		this.landscape = landscape;
		this.objects = objects;
	}
	
}