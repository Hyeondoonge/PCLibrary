package PCManager;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class MG_PCTableContents {
	private StringProperty ip;
	private StringProperty company;
	private StringProperty model;
	private StringProperty cpu;
	private StringProperty gpu;
	private IntegerProperty ramSize;
	private StringProperty id;
	private StringProperty password;
	private StringProperty state;
	
	public MG_PCTableContents(StringProperty ip, StringProperty company, StringProperty model, 
			StringProperty cpu, StringProperty gpu, IntegerProperty ramSize, 
			StringProperty id, StringProperty password, StringProperty state) {
		
		this.ip = ip;
		this.company = company;
		this.model = model;
		this.cpu = cpu;
		this.gpu = gpu;
		this.ramSize = ramSize;
		this.id = id;
		this.password = password;
		this.state = state;
	}
	
	public StringProperty getIpProperty() {
		return ip;
	}
	
	public StringProperty getCompanyProperty() {
		return company;
	}
	
	public StringProperty getModelProperty() {
		return model;
	}
	
	public StringProperty getCPUProperty() {
		return cpu;
	}
	
	public StringProperty getGPUProperty() {
		return gpu;
	}
	
	public IntegerProperty getRAMSizeProperty() {
		return ramSize;
	}
	
	public StringProperty getIdProperty() {
		return id;
	}
	
	public StringProperty getPasswordProperty() {
		return password;	
	}
	
	public StringProperty getStateProperty() {
		return state;
	}
}


