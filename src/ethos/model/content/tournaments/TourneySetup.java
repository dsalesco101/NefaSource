package ethos.model.content.tournaments;

/**
 * 
 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/6/19
 * 
 */
public class TourneySetup {
	private final int tournamentEquipment[];
	private final TourneyItem tournamentInventory[];
	private final int ammoAmount;
	private final String prayerBlocked;
	private final String magicBook;
	private final int commonItems[];
	private final int uncommonItems[];
	private final int rareItems[];
	private final int veryRareItems[];
	
	public TourneySetup(int[] tournamentEquipment, TourneyItem[] tournamentInventory, int ammoAmount, String prayerBlocked, String magicBook, int commonItems[], int uncommonItems[], int rareItems[], int veryRareItems[]) {
		this.tournamentEquipment = tournamentEquipment;
		this.tournamentInventory = tournamentInventory;
		this.ammoAmount = ammoAmount;
		this.prayerBlocked = prayerBlocked;
		this.magicBook = magicBook;
		this.commonItems = commonItems;
		this.uncommonItems = uncommonItems;
		this.rareItems = rareItems;
		this.veryRareItems = veryRareItems;
	}

	public int[] getTournamentEquipment() {
		return tournamentEquipment;
	}

	public TourneyItem[] getTournamentInventory() {
		return tournamentInventory;
	}

	public int getAmmoAmount() {
		return ammoAmount;
	}

	public String getPrayerBlocked() {
		return prayerBlocked;
	}

	public String getMagicBook() {
		return magicBook;
	}

	public int[] getCommonItems() {
		return commonItems;
	}

	public int[] getUncommonItems() {
		return uncommonItems;
	}

	public int[] getRareItems() {
		return rareItems;
	}

	public int[] getVeryRareItems() {
		return veryRareItems;
	}
}
