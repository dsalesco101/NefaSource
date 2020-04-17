package ethos.model.content.trails;

import java.util.Arrays;
import java.util.List;

public enum RewardLevel {
	SHARED, EASY, MEDIUM, HARD, MASTER;
	
	RewardLevel() {}
	
	public static final List<RewardLevel> VALUES = Arrays.asList(values());
	
	public static List<RewardLevel> getValues() {
		return VALUES;
	}
}
