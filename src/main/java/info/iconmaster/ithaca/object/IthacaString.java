package info.iconmaster.ithaca.object;

public class IthacaString extends IthacaObject {
	public String value;

	public IthacaString(String value) {
		super();
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
