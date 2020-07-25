package PCManager;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class MG_StudentContents {
	private StringProperty idColumn;
	private StringProperty nicknameColumn;
	private StringProperty nameColumn;
	private StringProperty phoneNumColumn;
	private StringProperty emailColumn;

	public MG_StudentContents(StringProperty idColumn, StringProperty nicknameColumn, StringProperty nameColumn,
			StringProperty phoneNumColumn, StringProperty emailColumn) {
		this.idColumn = idColumn;
		this.nicknameColumn = nicknameColumn;
		this.nameColumn = nameColumn;
		this.phoneNumColumn = phoneNumColumn;
		this.emailColumn = emailColumn;
	}
	
	public StringProperty getIdProperty() {
		return idColumn;
	}
	
	public StringProperty getNicknameProperty() {
		return nicknameColumn;
	}
	
	public StringProperty getNameProperty() {
		return nameColumn;
	}
	
	public StringProperty getPhoneNumProperty() {
		return phoneNumColumn;
	}
	
	public StringProperty getEmailProperty() {
		return emailColumn;
	}
}
