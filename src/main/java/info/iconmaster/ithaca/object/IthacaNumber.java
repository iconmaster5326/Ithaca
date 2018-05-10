package info.iconmaster.ithaca.object;

import java.math.BigDecimal;

public class IthacaNumber extends IthacaObject {
	BigDecimal value;

	public IthacaNumber(Number value) {
		super();
		this.value = new BigDecimal(String.valueOf(value));
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
