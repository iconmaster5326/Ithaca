package info.iconmaster.ithaca.object;

public abstract class IthacaObject {
	@Override
	public String toString() {
		return "#<"+getClass().getName()+"@"+Long.toHexString(getClass().hashCode())+">";
	}
	
	public boolean isTruthy() {
		return true;
	}
}
