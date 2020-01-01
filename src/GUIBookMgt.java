import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.ibm.icu.util.BytesTrie.Iterator;

import org.eclipse.swt.widgets.Label;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class GUIBookMgt {
	public GUIBookMgt() {
	}

	protected Shell shell;
	private static BookShelf bookShelf ;
	private static List bookList;
	private static Label lblBookCount;
	/**
	 * Launch the application.
	 * @param args
	 */
	
	//메인함수
	public static void main(String[] args) { 
		
		bookShelf = new BookShelf();
		try {
			//기존 있는 파일 정보 읽어오기
			readBooks();
			GUIBookMgt window = new GUIBookMgt();
			window.open();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		

	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		
		shell.open();
		shell.layout();
		//오픈 할 시 책 정보 화면애 출력
		showAllBookInfo();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(614, 387);
		shell.setText("나이화 서재");		//프로그램 제목
		
		lblBookCount = new Label(shell, SWT.NONE);
		lblBookCount.setBounds(10, 10, 148, 15);
		lblBookCount.setText("내가 소장하고 있는 책 목록");		//기본 텍스트 (책장 정보가 성공적으로 로딩이 되면 나타나지 않음)
		
		bookList = new List(shell, SWT.BORDER);
		bookList.setBounds(10, 31, 566, 271);
		
		/*책 추가 버튼*/
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				insertBookInfo(shell);
			}
		});
		btnNewButton.setBounds(10, 308, 98, 25);
		btnNewButton.setText("Add Book...");
		
		/*책 불러오기 버튼*/
		Button btnGetAllBooks = new Button(shell, SWT.NONE);
		btnGetAllBooks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showAllBookInfo();
			}
		});
		btnGetAllBooks.setText("Get All Books");
		btnGetAllBooks.setBounds(114, 308, 98, 25);
		
		/*책 업데이트, 상세정보 버튼*/
		Button btnBookDetailInfo = new Button(shell, SWT.NONE);
		btnBookDetailInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateBookInfo(shell);
			}
		});
		btnBookDetailInfo.setText("Book Detail Info...");
		btnBookDetailInfo.setBounds(218, 308, 112, 25);
		
		/*책 찾기 버튼*/
		Button btnSearch = new Button(shell, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = JOptionPane.showInputDialog("이름을 입력하세요.");
				serchBookInfo(name);
			}
		});
		btnSearch.setText("Search...");
		btnSearch.setBounds(336, 308, 76, 25);
		
		/*책 삭제 버튼*/
		Button btnDelete = new Button(shell, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = JOptionPane.showInputDialog("삭제할 책의 제목을 입력하세요."); //삭제할 책의 제목을 입력받음
				if(bookShelf.selectedBookIndex(name) ==-1 ){//사용자가 입력한 책 제목이 없다면 경고 문구 출력
					JOptionPane.showMessageDialog(null, "삭제할 책이 없습니다","Message",JOptionPane.ERROR_MESSAGE);	
				}else {//사용자가 입력한 책 제목이 있다면 책 정보를 출력한 후 다시 삭제할 것인지 물음
					int result = JOptionPane.showConfirmDialog(null,"제목 : "+name+"\n출판사 : " + bookShelf.selectBook(name).getPublisher() 
							+ "\n종류"+bookShelf.selectBook(name).getType()+"\n이름 : " + bookShelf.selectBook(name).getAuthor().getName() 
							+ "\n나이 : " + bookShelf.selectBook(name).getAuthor().getBirthdate() + "\n성별 : " + bookShelf.selectBook(name).getAuthor().getGender()
							+"\n경력 : " + bookShelf.selectBook(name).getAuthor().getCareer()
							+"\n삭제하시겠습니까?","Comfirm",JOptionPane.YES_NO_CANCEL_OPTION);
					if(result == JOptionPane.YES_OPTION) {// 삭제한다는 버튼 누르면 삭제
						//사용자가 yes를 누른 경우 삭제한다
						JOptionPane.showMessageDialog(null, "책을 삭제합니다.","Message",JOptionPane.ERROR_MESSAGE);
						bookShelf.deleteBook(name);
					}
					
				}
				
				showAllBookInfo();		//다시 책장 리스트 정보 새로고침
			}
		});

		btnDelete.setText("Delete...");
		btnDelete.setBounds(418, 308, 76, 25);
		
		
		/*닫기 버튼*/
		Button btnClose = new Button(shell, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {//닫기 전 책장 정보 파일에 저장
					writeBooks();
					System.exit(0);
				}catch (IOException e1) {
					System.out.println(e1.getMessage());
				}
			}
		});
		btnClose.setText("Close");
		btnClose.setBounds(500, 308, 76, 25);

	}
	
	
	
	/*책 추가 메소드*/
	public void insertBookInfo(Shell shell) {
		DlgAddBook dlg = new DlgAddBook(shell);
		if(dlg.open() != Dialog.OK) {
			return;
		}
		
		String title = dlg.title;
		String publisher = dlg.publisher;
		String type = dlg.bookType;
		
		String name = dlg.authorName;
		String birthdate = dlg.birthDate;
		String career = dlg.career;
		Gender gender = dlg.gender;
		
		Author author = new Author(name,birthdate, gender, career);
		Book book = new Book(title, publisher, type, author);
		
		bookShelf.getBooks().add(book);
		showAllBookInfo();
		
	}
	
	/*책장 리스트 정보 새로고침 메소드*/
	public static void showAllBookInfo() {
		
		lblBookCount.setText(Integer.toString(bookShelf.getBooks().size())+"권이 있습니다.");		//현재 몇권의 책이 있는지 상단에 출력
		bookList.removeAll();
		java.util.Iterator<Book> it = bookShelf.getBooks().iterator();
		while(it.hasNext()) {	//bookShelf에 있는 책들의 정보를 iterator 클래스를 이용하여 출력
			Book b = it.next();
			String tmp_title = b.getTitle();
			String tmp_publisher = b.getPublisher();
			String tmp_type = b.getType();
			String tmp_au = b.getAuthor().getName();
			String add_it = (String)String.format("제목 : %s   \t  출판사 : %s      \t종류 : %s       \t저자 : %s" ,tmp_au, tmp_title, tmp_publisher, tmp_type);
			bookList.add(add_it);
			
		}
	}
	
	/*책 찾기 메소드*/
	public void serchBookInfo(String name) {
		Book selectedBook = bookShelf.selectBook(name);
		DlgDetailInfo dlg = new DlgDetailInfo(shell);
		
		dlg.title = selectedBook.getTitle();
		dlg.publisher = selectedBook.getPublisher();
		dlg.bookType = selectedBook.getType();
		dlg.authorName = selectedBook.getAuthor().getName();
		dlg.birthDate = selectedBook.getAuthor().getBirthdate();
		dlg.gender = selectedBook.getAuthor().getGender();
		dlg.career = selectedBook.getAuthor().getCareer();
		
		if(dlg.open() != Dialog.OK) {
			return;
			
		}
		selectedBook.update(dlg.title, dlg.publisher);
	}
	
	/*책 수정 메소드*/
	public void updateBookInfo(Shell shell) {
		int index = bookList.getSelectionIndex();
		if(index == -1) {
			JOptionPane.showMessageDialog(null, "책 목록에서 상세정보를 볼 책을 선택하세요.","Message",JOptionPane.ERROR_MESSAGE);
		}else {
			Book selectedBook = bookShelf.getBooks().get(index);
			DlgDetailInfo dlg = new DlgDetailInfo(shell);
			
			dlg.title = selectedBook.getTitle();
			dlg.publisher = selectedBook.getPublisher();
			dlg.bookType = selectedBook.getType();
			dlg.authorName = selectedBook.getAuthor().getName();
			dlg.birthDate = selectedBook.getAuthor().getBirthdate();
			dlg.gender = selectedBook.getAuthor().getGender();
			dlg.career = selectedBook.getAuthor().getCareer();
			
			if(dlg.open() != Dialog.OK) {
				return;
				
			}
			selectedBook.update(dlg.title, dlg.publisher);
			showAllBookInfo();
		}
		
		
	}
	
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
			
			fin.close();
		}
	}
	
	private static void writeBooks() throws IOException{ 
		//닫기 전 파일에 책장 정보 업데이트
		FileOutputStream f = new FileOutputStream("mybookshelf.dat");
		ObjectOutputStream fout = new ObjectOutputStream(f);
		
		fout.writeObject(bookShelf);
		fout.close();
		
	}
}
