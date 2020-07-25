package PCManager;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class MG_PCLenderContents {
	private StringProperty lenderId;
	private StringProperty name;
	private StringProperty ip;
	private StringProperty pc;
	private StringProperty pcId;
	private StringProperty password;

	public MG_PCLenderContents(StringProperty lenderId, StringProperty name, StringProperty ip, StringProperty pc,
			StringProperty pcId, StringProperty password) {
		this.lenderId = lenderId;
		this.name = name;
		this.ip = ip;
		this.pc = pc;
		this.pcId = pcId;
		this.password = password;
	}

	public StringProperty getLenderIdProperty() {
		return lenderId;
	}

	public StringProperty getNameProperty() {
		return name;
	}

	public StringProperty getIpProperty() {
		return ip;
	}

	public StringProperty getPcProperty() {
		return pc;
	}

	public StringProperty getPcIdProperty() {
		return pcId;
	}

	public StringProperty getPasswordProperty() {
		return password;
	}
}
