package Main;

import java.util.Date;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Post {
	
	private final SimpleIntegerProperty no;
	private final SimpleStringProperty title;
	private final SimpleStringProperty publisher;
	private final SimpleStringProperty date;
	private final SimpleIntegerProperty view;
	private final SimpleStringProperty content;
	
	public Post(int no, String title, String publisher, String date, int view, String content) {
		this.no = new SimpleIntegerProperty(no);
		this.title= new SimpleStringProperty(title);
		this.publisher = new SimpleStringProperty(publisher);
		this.date = new SimpleStringProperty(date);
		this.view =new SimpleIntegerProperty(view);
		this.content =new SimpleStringProperty(content);
	}
	
	public Integer getNo() {
		return no.get();
	}
	public String getTitle() {
		return title.get();
	}
	public String getPublisher() {
		return publisher.get();
	}
	public String getDate() {
		return date.get();
	}
	public Integer getView() {
		return view.get();
	}
	public SimpleStringProperty getContent() {
		return content;
	}
	
}
