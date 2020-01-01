//1503103 윤세린



import java.io.*;
import java.util.*;

public class BookMgt {
	private static Scanner scan;
	private static BookShelf bookShelf;

	public static void main(String[] args){	
		bookShelf = new BookShelf();
		
		try {//데이터를 저장해논 파일 읽기 시도
			readBooks();
		}catch (ClassNotFoundException | IOException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		
		scan = new Scanner(System.in);

		while (true) { //메뉴 선택 무한루프
			int menu = 0;
			try {
				menu = showMenu();
			}catch(Exception e) {
				System.out.println("1부터 7 사이의 숫자중에 입력하십시오.");
			}
			if (menu == 7)
				break;
			switch(menu) {
			case 1: insertBookInfo(); break;
			case 2: showAllBookInfo(); break;
			case 3: selectBookInfo(); break;
			case 4: updateBookInfo(); break;
			case 5: removeBookFromBookShelf(); break;
			case 6: clearBookShelf(); break;
			}
			
			System.out.println();
		}
		
		//종료하기 전 데이터를 저장하는 메소드
		try {
			writeBooks();
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("프로그램을 종료합니다.");
		scan.close();
	}

	//종료하기 전 데이터를 저장하는 메소드
	private static void writeBooks() throws IOException{ 
		FileOutputStream f = new FileOutputStream("mybookshelf.dat");
		ObjectOutputStream fout = new ObjectOutputStream(f);
		
		fout.writeObject(bookShelf);
		System.out.println("책을 저장했습니다.");
		fout.close();
		
	}
	
	//데이터를 저장해논 파일을 읽는 메소드
	private static void readBooks() throws IOException, ClassNotFoundException {
		// 파일이 있는지 검사
		//만약 파일이 없다면 처음 시작하는 것이므로 오류를 발생시키지 않아야 함
		File bf = new File("mybookshelf.dat");
		if(bf.exists()){
			//파일이 있다면
			//파일에 있는 책의 정보를 읽어와  Bookshelf에 갖고있게 함
			FileInputStream f = new FileInputStream("mybookshelf.dat");
			ObjectInputStream fin = new ObjectInputStream(f);
			bookShelf = (BookShelf)fin.readObject();
			System.out.println("책을 읽어왔습니다.");
			fin.close();
		}
	}
	
	//메뉴판 출력 메소드
	public static int showMenu() throws Exception{ 
		System.out.println("========================================================");
		System.out.println("1.책 추가 2.모든 책 검색 3.책 검색 4.책 수정 5.책 삭제 6.모든 책 삭제 7.종료");
		System.out.println("========================================================");
		int menu = -1;
		try {
			System.out.print("메뉴를 선택하세요 >> ");
			menu = Integer.parseInt(scan.nextLine());
			if (menu<1 || menu>7) {
				throw new Exception();
			}
		}catch(NumberFormatException e){
			System.out.println("메뉴 선택을 위해 숫자를 입력하십시오.");
		}
		System.out.println();
		return menu;
	}
	
	// 책 추가 메소드
	public static void insertBookInfo() {	
		
		// 사용자로부터 책 제목 받기
		System.out.println("책의 정보를 입력하세요.(제목, 출판사, 책종류 순으로 입력)");
		System.out.print("제목 >> ");
		String title = scan.nextLine();
		System.out.print("출판사 >> ");
		String publisher = scan.nextLine();
		System.out.print("책종류 >> ");
		String type = scan.nextLine();
		System.out.println();
		
		//사용자로부터 저자 정보 받기
		System.out.println("저자 정보를 입력하세요.(이름, 생년월일, 성별(남:1, 여:2), 경력 순으로 입력)");
		System.out.print("이름 >> ");
		String name = scan.nextLine();
		System.out.print("생년월일 >> ");
		String birthdate = scan.nextLine();
		System.out.print("성별 >> ");
		int g = Integer.parseInt(scan.nextLine());
		System.out.print("경력 >> ");
		String career = scan.nextLine();
		
		//저자의 젠더 정보 입력 받기
		Gender gender = Gender.UNKNOWN;
		if(g == 1) gender = Gender.MAN;
		else if(g == 2) gender = Gender.WOMAN;
		
		//Author 객체 생성
		Author author = new Author(name, birthdate, gender, career);
		//생성한 저자 개체를 바탕으로 Book 개체 생성
		Book book = new Book(title, publisher, type, author);
		//생성한 Book개체를 bookShelf의 LinkedList에 추가.
		bookShelf.getBooks().add(book);
	}
	
	//책 정보 출력 메소드
	public static void showAllBookInfo() {	
		String deco = new String(new char[60]).replace("\0", "=");
		System.out.println(deco);
		System.out.println(String.format("총 %d권의 책이 있습니다.", bookShelf.getBooks().size()));	
		System.out.println(deco);
		Iterator<Book> it = bookShelf.getBooks().iterator();
		while(it.hasNext()) {
			System.out.println(it.next().print());
		}
		
	}
	
	//책 검색 메소드
	public static void selectBookInfo() {	
		System.out.print("검색할 책의 제목을 입력하세요 >> ");
		String title = scan.nextLine();
		
		//입력받은 제목을 바탕으로 책 검색 후 인스턴스 생성
		Book selectedBook = bookShelf.selectBook(title);
		if(selectedBook == null){
			System.out.println("검색된 책이 없습니다.");
			return;
		}
		System.out.println(selectedBook.printDetatil());		
	}

	
	//책의 정보를 수정하는 메소드
	public static void updateBookInfo() {
		//제목을 입력받아 Book인스턴스 반환
		System.out.print("수정할 책의 제목을 입력하세요 >> ");
		String title = scan.nextLine();
		Book selectedBook = bookShelf.selectBook(title);
		if(selectedBook==null){
			System.out.println("수정할 책이 없습니다.");
			return;
		}
		
		//변경할 정보 사용자로부터 입력받음
		System.out.print("새 제목을 입력하세요 >> ");
		String newtitle = scan.nextLine();
		
		System.out.print("출판사도 수정하시겠습니까?(y or n) >> ");
		String confirm = scan.nextLine().toLowerCase();
		
		if(confirm.equals("y")){
			System.out.print("새 출판사를 입력하세요 >> ");
			String newpublisher = scan.nextLine();
			selectedBook.update(newtitle, newpublisher);
		}
		else{
			selectedBook.update(newtitle);
		}
		
		System.out.println("수정이 완료되었습니다.");
	}

	//사용자로부터 책의 제목을 받아 삭제하는 메소드
	public static void removeBookFromBookShelf() {
		
		//삭제할 책의 제목을 입력받아 Book을 반환하여 인스턴스를 만듬
		System.out.print("삭제할 책의 제목을 입력하세요 >> ");
		String title = scan.nextLine();
		Book selectedBook = bookShelf.selectBook(title);
		if(selectedBook == null){
			System.out.println("삭제할 책이 없습니다.");
			return;
		}
		
		//선택된 책의 정보 출력
		System.out.println(selectedBook.printDetatil());
		
		//사용자에게 재확인
		System.out.print("책을 정말 삭제하세겠습니까?(y or n) >> ");
		String confirm = scan.nextLine().toLowerCase();

		if(confirm.equals("n")) return;
		int msg = bookShelf.deleteBook(title);		
		if(msg == 1){
			System.out.println("책을 삭제하였습니다.");		
		}
	}
	
	//책장의 책을 모두 삭제하는 메소드
	public static void clearBookShelf() {
		System.out.print("책을 모두 삭제하세겠습니까?(y or n) >> ");
		String confirm = scan.nextLine().toLowerCase();
		
		if(confirm.equals("y")){
			bookShelf.clearBookShelf();
			System.out.println("책을 모두 삭제하였습니다.");
		}
	}	
}
