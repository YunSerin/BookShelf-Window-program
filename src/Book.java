//1503103 윤세린



import java.io.Serializable;

public class Book implements Serializable{
	private String title;
	private String publisher;
	private String type;
	private Author author;
	
	//construc
	public Book() {
		
	}
	
	public Book(String title, String publisher, String type, Author author) {
		this.title = title;
		this.publisher = publisher;
		this.type = type;
		this.author = author;
	}

	//getters and setters of fields
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
	
	public String print() {
		String bookInfoStr = String.format("제목: %s\n출판사: %s\n종류: %s\n저자: %s\n", 
				title, publisher, type, author.getName());
				
		return bookInfoStr;
	}
	
	public String printDetatil() {
		String authorInfoStr = author.showInfo();

		String bookDetailInfoStr = String.format("제목: %s\n출판사: %s\n종류: %s\n%s", 
				title, publisher, type, authorInfoStr);

		return bookDetailInfoStr;
	}
	
	public void update(String title) {
		this.title = title;
	}
	
	public void update(String title, String publisher) {
		this.title = title;
		this.publisher = publisher;
	}
	
}
