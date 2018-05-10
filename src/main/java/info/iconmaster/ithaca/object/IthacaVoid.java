package info.iconmaster.ithaca.object;

public class IthacaVoid extends IthacaObject {
	private IthacaVoid() {}
	
	public static final IthacaVoid VOID = new IthacaVoid();
	
	@Override
	public String toString() {
		return "";
	}
}
