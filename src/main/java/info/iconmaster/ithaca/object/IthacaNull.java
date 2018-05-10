package info.iconmaster.ithaca.object;

public class IthacaNull extends IthacaObject {
	private IthacaNull() {}
	
	public static final IthacaNull NULL = new IthacaNull();
	
	@Override
	public String toString() {
		return "()";
	}
}
