//1503103 ������


import java.io.*;
import java.util.*;

public class BookShelf implements Serializable {
	public static final int MAXCOUNT = 100;
	private LinkedList<Book> books = new LinkedList<Book>();
	
	//books�� getter & setter
	public LinkedList<Book> getBooks() {
		return books;
	}
	public void setBooks(LinkedList<Book> books) {
		this.books = books;
	}
	
	//������ �Ķ���ͷ� Book �ν��Ͻ��� ��ȯ�ϴ� �޼ҵ�
		public Book selectBook(String title) {
			if(selectedBookIndex(title)!=-1) {
				return books.get(selectedBookIndex(title));
			}else {
				return null;
			}
		}
		
	//������ �������� index�� ��ȯ�ϴ� �޼ҵ�
		public int selectedBookIndex(String title) {
			for(int i = 0; i < books.size(); i++) {
				if(books.get(i).getTitle().equals(title)) {
					return i;
				}
			}
			return -1;
		}
		
	//books�� �������� ����ϴ� �޼ҵ�
	public String[] printBooks() {
		String[] booksInfoStr = new String[books.size()];
		for(int i = 0; i < books.size(); i++) {
			booksInfoStr[i] = books.get(i).print();
		}
		
		return booksInfoStr;
	}
	
	//å�� �����ϴ� �޼ҵ�
	public int deleteBook(String title) {
		if(books.size() == 0) return 0;
		books.remove(selectBook(title));
		return 1;
	}
	
	public void clearBookShelf() {	
		books = null;
	}
}
