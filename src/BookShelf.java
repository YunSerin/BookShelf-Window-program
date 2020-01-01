//1503103 윤세린


import java.io.*;
import java.util.*;

public class BookShelf implements Serializable {
	public static final int MAXCOUNT = 100;
	private LinkedList<Book> books = new LinkedList<Book>();
	
	//books의 getter & setter
	public LinkedList<Book> getBooks() {
		return books;
	}
	public void setBooks(LinkedList<Book> books) {
		this.books = books;
	}
	
	//제목을 파라미터로 Book 인스턴스를 반환하는 메소드
		public Book selectBook(String title) {
			if(selectedBookIndex(title)!=-1) {
				return books.get(selectedBookIndex(title));
			}else {
				return null;
			}
		}
		
	//제목을 바탕으로 index를 반환하는 메소드
		public int selectedBookIndex(String title) {
			for(int i = 0; i < books.size(); i++) {
				if(books.get(i).getTitle().equals(title)) {
					return i;
				}
			}
			return -1;
		}
		
	//books의 정보들을 출력하는 메소드
	public String[] printBooks() {
		String[] booksInfoStr = new String[books.size()];
		for(int i = 0; i < books.size(); i++) {
			booksInfoStr[i] = books.get(i).print();
		}
		
		return booksInfoStr;
	}
	
	//책을 삭제하는 메소드
	public int deleteBook(String title) {
		if(books.size() == 0) return 0;
		books.remove(selectBook(title));
		return 1;
	}
	
	public void clearBookShelf() {	
		books = null;
	}
}
