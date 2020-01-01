//1503103 ������



import java.io.*;
import java.util.*;

public class BookMgt {
	private static Scanner scan;
	private static BookShelf bookShelf;

	public static void main(String[] args){	
		bookShelf = new BookShelf();
		
		try {//�����͸� �����س� ���� �б� �õ�
			readBooks();
		}catch (ClassNotFoundException | IOException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		
		scan = new Scanner(System.in);

		while (true) { //�޴� ���� ���ѷ���
			int menu = 0;
			try {
				menu = showMenu();
			}catch(Exception e) {
				System.out.println("1���� 7 ������ �����߿� �Է��Ͻʽÿ�.");
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
		
		//�����ϱ� �� �����͸� �����ϴ� �޼ҵ�
		try {
			writeBooks();
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("���α׷��� �����մϴ�.");
		scan.close();
	}

	//�����ϱ� �� �����͸� �����ϴ� �޼ҵ�
	private static void writeBooks() throws IOException{ 
		FileOutputStream f = new FileOutputStream("mybookshelf.dat");
		ObjectOutputStream fout = new ObjectOutputStream(f);
		
		fout.writeObject(bookShelf);
		System.out.println("å�� �����߽��ϴ�.");
		fout.close();
		
	}
	
	//�����͸� �����س� ������ �д� �޼ҵ�
	private static void readBooks() throws IOException, ClassNotFoundException {
		// ������ �ִ��� �˻�
		//���� ������ ���ٸ� ó�� �����ϴ� ���̹Ƿ� ������ �߻���Ű�� �ʾƾ� ��
		File bf = new File("mybookshelf.dat");
		if(bf.exists()){
			//������ �ִٸ�
			//���Ͽ� �ִ� å�� ������ �о��  Bookshelf�� �����ְ� ��
			FileInputStream f = new FileInputStream("mybookshelf.dat");
			ObjectInputStream fin = new ObjectInputStream(f);
			bookShelf = (BookShelf)fin.readObject();
			System.out.println("å�� �о�Խ��ϴ�.");
			fin.close();
		}
	}
	
	//�޴��� ��� �޼ҵ�
	public static int showMenu() throws Exception{ 
		System.out.println("========================================================");
		System.out.println("1.å �߰� 2.��� å �˻� 3.å �˻� 4.å ���� 5.å ���� 6.��� å ���� 7.����");
		System.out.println("========================================================");
		int menu = -1;
		try {
			System.out.print("�޴��� �����ϼ��� >> ");
			menu = Integer.parseInt(scan.nextLine());
			if (menu<1 || menu>7) {
				throw new Exception();
			}
		}catch(NumberFormatException e){
			System.out.println("�޴� ������ ���� ���ڸ� �Է��Ͻʽÿ�.");
		}
		System.out.println();
		return menu;
	}
	
	// å �߰� �޼ҵ�
	public static void insertBookInfo() {	
		
		// ����ڷκ��� å ���� �ޱ�
		System.out.println("å�� ������ �Է��ϼ���.(����, ���ǻ�, å���� ������ �Է�)");
		System.out.print("���� >> ");
		String title = scan.nextLine();
		System.out.print("���ǻ� >> ");
		String publisher = scan.nextLine();
		System.out.print("å���� >> ");
		String type = scan.nextLine();
		System.out.println();
		
		//����ڷκ��� ���� ���� �ޱ�
		System.out.println("���� ������ �Է��ϼ���.(�̸�, �������, ����(��:1, ��:2), ��� ������ �Է�)");
		System.out.print("�̸� >> ");
		String name = scan.nextLine();
		System.out.print("������� >> ");
		String birthdate = scan.nextLine();
		System.out.print("���� >> ");
		int g = Integer.parseInt(scan.nextLine());
		System.out.print("��� >> ");
		String career = scan.nextLine();
		
		//������ ���� ���� �Է� �ޱ�
		Gender gender = Gender.UNKNOWN;
		if(g == 1) gender = Gender.MAN;
		else if(g == 2) gender = Gender.WOMAN;
		
		//Author ��ü ����
		Author author = new Author(name, birthdate, gender, career);
		//������ ���� ��ü�� �������� Book ��ü ����
		Book book = new Book(title, publisher, type, author);
		//������ Book��ü�� bookShelf�� LinkedList�� �߰�.
		bookShelf.getBooks().add(book);
	}
	
	//å ���� ��� �޼ҵ�
	public static void showAllBookInfo() {	
		String deco = new String(new char[60]).replace("\0", "=");
		System.out.println(deco);
		System.out.println(String.format("�� %d���� å�� �ֽ��ϴ�.", bookShelf.getBooks().size()));	
		System.out.println(deco);
		Iterator<Book> it = bookShelf.getBooks().iterator();
		while(it.hasNext()) {
			System.out.println(it.next().print());
		}
		
	}
	
	//å �˻� �޼ҵ�
	public static void selectBookInfo() {	
		System.out.print("�˻��� å�� ������ �Է��ϼ��� >> ");
		String title = scan.nextLine();
		
		//�Է¹��� ������ �������� å �˻� �� �ν��Ͻ� ����
		Book selectedBook = bookShelf.selectBook(title);
		if(selectedBook == null){
			System.out.println("�˻��� å�� �����ϴ�.");
			return;
		}
		System.out.println(selectedBook.printDetatil());		
	}

	
	//å�� ������ �����ϴ� �޼ҵ�
	public static void updateBookInfo() {
		//������ �Է¹޾� Book�ν��Ͻ� ��ȯ
		System.out.print("������ å�� ������ �Է��ϼ��� >> ");
		String title = scan.nextLine();
		Book selectedBook = bookShelf.selectBook(title);
		if(selectedBook==null){
			System.out.println("������ å�� �����ϴ�.");
			return;
		}
		
		//������ ���� ����ڷκ��� �Է¹���
		System.out.print("�� ������ �Է��ϼ��� >> ");
		String newtitle = scan.nextLine();
		
		System.out.print("���ǻ絵 �����Ͻðڽ��ϱ�?(y or n) >> ");
		String confirm = scan.nextLine().toLowerCase();
		
		if(confirm.equals("y")){
			System.out.print("�� ���ǻ縦 �Է��ϼ��� >> ");
			String newpublisher = scan.nextLine();
			selectedBook.update(newtitle, newpublisher);
		}
		else{
			selectedBook.update(newtitle);
		}
		
		System.out.println("������ �Ϸ�Ǿ����ϴ�.");
	}

	//����ڷκ��� å�� ������ �޾� �����ϴ� �޼ҵ�
	public static void removeBookFromBookShelf() {
		
		//������ å�� ������ �Է¹޾� Book�� ��ȯ�Ͽ� �ν��Ͻ��� ����
		System.out.print("������ å�� ������ �Է��ϼ��� >> ");
		String title = scan.nextLine();
		Book selectedBook = bookShelf.selectBook(title);
		if(selectedBook == null){
			System.out.println("������ å�� �����ϴ�.");
			return;
		}
		
		//���õ� å�� ���� ���
		System.out.println(selectedBook.printDetatil());
		
		//����ڿ��� ��Ȯ��
		System.out.print("å�� ���� �����ϼ��ڽ��ϱ�?(y or n) >> ");
		String confirm = scan.nextLine().toLowerCase();

		if(confirm.equals("n")) return;
		int msg = bookShelf.deleteBook(title);		
		if(msg == 1){
			System.out.println("å�� �����Ͽ����ϴ�.");		
		}
	}
	
	//å���� å�� ��� �����ϴ� �޼ҵ�
	public static void clearBookShelf() {
		System.out.print("å�� ��� �����ϼ��ڽ��ϱ�?(y or n) >> ");
		String confirm = scan.nextLine().toLowerCase();
		
		if(confirm.equals("y")){
			bookShelf.clearBookShelf();
			System.out.println("å�� ��� �����Ͽ����ϴ�.");
		}
	}	
}
