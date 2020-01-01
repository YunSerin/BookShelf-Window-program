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
	
	//�����Լ�
	public static void main(String[] args) { 
		
		bookShelf = new BookShelf();
		try {
			//���� �ִ� ���� ���� �о����
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
		//���� �� �� å ���� ȭ��� ���
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
		shell.setText("����ȭ ����");		//���α׷� ����
		
		lblBookCount = new Label(shell, SWT.NONE);
		lblBookCount.setBounds(10, 10, 148, 15);
		lblBookCount.setText("���� �����ϰ� �ִ� å ���");		//�⺻ �ؽ�Ʈ (å�� ������ ���������� �ε��� �Ǹ� ��Ÿ���� ����)
		
		bookList = new List(shell, SWT.BORDER);
		bookList.setBounds(10, 31, 566, 271);
		
		/*å �߰� ��ư*/
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				insertBookInfo(shell);
			}
		});
		btnNewButton.setBounds(10, 308, 98, 25);
		btnNewButton.setText("Add Book...");
		
		/*å �ҷ����� ��ư*/
		Button btnGetAllBooks = new Button(shell, SWT.NONE);
		btnGetAllBooks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showAllBookInfo();
			}
		});
		btnGetAllBooks.setText("Get All Books");
		btnGetAllBooks.setBounds(114, 308, 98, 25);
		
		/*å ������Ʈ, ������ ��ư*/
		Button btnBookDetailInfo = new Button(shell, SWT.NONE);
		btnBookDetailInfo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateBookInfo(shell);
			}
		});
		btnBookDetailInfo.setText("Book Detail Info...");
		btnBookDetailInfo.setBounds(218, 308, 112, 25);
		
		/*å ã�� ��ư*/
		Button btnSearch = new Button(shell, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = JOptionPane.showInputDialog("�̸��� �Է��ϼ���.");
				serchBookInfo(name);
			}
		});
		btnSearch.setText("Search...");
		btnSearch.setBounds(336, 308, 76, 25);
		
		/*å ���� ��ư*/
		Button btnDelete = new Button(shell, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String name = JOptionPane.showInputDialog("������ å�� ������ �Է��ϼ���."); //������ å�� ������ �Է¹���
				if(bookShelf.selectedBookIndex(name) ==-1 ){//����ڰ� �Է��� å ������ ���ٸ� ��� ���� ���
					JOptionPane.showMessageDialog(null, "������ å�� �����ϴ�","Message",JOptionPane.ERROR_MESSAGE);	
				}else {//����ڰ� �Է��� å ������ �ִٸ� å ������ ����� �� �ٽ� ������ ������ ����
					int result = JOptionPane.showConfirmDialog(null,"���� : "+name+"\n���ǻ� : " + bookShelf.selectBook(name).getPublisher() 
							+ "\n����"+bookShelf.selectBook(name).getType()+"\n�̸� : " + bookShelf.selectBook(name).getAuthor().getName() 
							+ "\n���� : " + bookShelf.selectBook(name).getAuthor().getBirthdate() + "\n���� : " + bookShelf.selectBook(name).getAuthor().getGender()
							+"\n��� : " + bookShelf.selectBook(name).getAuthor().getCareer()
							+"\n�����Ͻðڽ��ϱ�?","Comfirm",JOptionPane.YES_NO_CANCEL_OPTION);
					if(result == JOptionPane.YES_OPTION) {// �����Ѵٴ� ��ư ������ ����
						//����ڰ� yes�� ���� ��� �����Ѵ�
						JOptionPane.showMessageDialog(null, "å�� �����մϴ�.","Message",JOptionPane.ERROR_MESSAGE);
						bookShelf.deleteBook(name);
					}
					
				}
				
				showAllBookInfo();		//�ٽ� å�� ����Ʈ ���� ���ΰ�ħ
			}
		});

		btnDelete.setText("Delete...");
		btnDelete.setBounds(418, 308, 76, 25);
		
		
		/*�ݱ� ��ư*/
		Button btnClose = new Button(shell, SWT.NONE);
		btnClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {//�ݱ� �� å�� ���� ���Ͽ� ����
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
	
	
	
	/*å �߰� �޼ҵ�*/
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
	
	/*å�� ����Ʈ ���� ���ΰ�ħ �޼ҵ�*/
	public static void showAllBookInfo() {
		
		lblBookCount.setText(Integer.toString(bookShelf.getBooks().size())+"���� �ֽ��ϴ�.");		//���� ����� å�� �ִ��� ��ܿ� ���
		bookList.removeAll();
		java.util.Iterator<Book> it = bookShelf.getBooks().iterator();
		while(it.hasNext()) {	//bookShelf�� �ִ� å���� ������ iterator Ŭ������ �̿��Ͽ� ���
			Book b = it.next();
			String tmp_title = b.getTitle();
			String tmp_publisher = b.getPublisher();
			String tmp_type = b.getType();
			String tmp_au = b.getAuthor().getName();
			String add_it = (String)String.format("���� : %s   \t  ���ǻ� : %s      \t���� : %s       \t���� : %s" ,tmp_au, tmp_title, tmp_publisher, tmp_type);
			bookList.add(add_it);
			
		}
	}
	
	/*å ã�� �޼ҵ�*/
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
	
	/*å ���� �޼ҵ�*/
	public void updateBookInfo(Shell shell) {
		int index = bookList.getSelectionIndex();
		if(index == -1) {
			JOptionPane.showMessageDialog(null, "å ��Ͽ��� �������� �� å�� �����ϼ���.","Message",JOptionPane.ERROR_MESSAGE);
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
		// ������ �ִ��� �˻�
		//���� ������ ���ٸ� ó�� �����ϴ� ���̹Ƿ� ������ �߻���Ű�� �ʾƾ� ��
		File bf = new File("mybookshelf.dat");
		if(bf.exists()){
			//������ �ִٸ�
			//���Ͽ� �ִ� å�� ������ �о��  Bookshelf�� �����ְ� ��
			FileInputStream f = new FileInputStream("mybookshelf.dat");
			ObjectInputStream fin = new ObjectInputStream(f);
			bookShelf = (BookShelf)fin.readObject();
			
			fin.close();
		}
	}
	
	private static void writeBooks() throws IOException{ 
		//�ݱ� �� ���Ͽ� å�� ���� ������Ʈ
		FileOutputStream f = new FileOutputStream("mybookshelf.dat");
		ObjectOutputStream fout = new ObjectOutputStream(f);
		
		fout.writeObject(bookShelf);
		fout.close();
		
	}
}
