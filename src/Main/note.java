package Main;

public class note {

	int no, view;
	String title, publisher, date;
	
	public note(int no, String title, String publisher, String date, int view) {
		this.no = no;
		this.title = title;
		this.publisher = publisher;
		this.view = view;
		this.date = date;
	}
	
	public int getNo() {
		return no;
	}
	public int getView() {
		return view;
	}
	public String getTitle() {
		return title;
	}
	public String getPublisher() {
		return publisher;
	}
	public String getDate() {
		return date;	
	}
}
