package info.iconmaster.ithaca.object;

public class IthacaBool extends IthacaObject {
	private IthacaBool() {}
	
	public static final IthacaBool TRUE = new IthacaBool();
	public static final IthacaBool FALSE = new IthacaBool();
	
	@Override
	public String toString() {
		if (this == TRUE) {
			return "#t";
		} else {
			return "#f";
		}
	}
}
