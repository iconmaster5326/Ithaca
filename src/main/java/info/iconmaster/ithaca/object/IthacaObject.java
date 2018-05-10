package info.iconmaster.ithaca.object;

public abstract class IthacaObject {
	@Override
	public String toString() {
		return "#<"+getClass().getSimpleName()+"@"+Long.toHexString(getClass().hashCode())+">";
	}
}
