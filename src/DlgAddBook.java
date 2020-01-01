import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;

public class DlgAddBook extends TitleAreaDialog {
	private Text txtTitle;
	private Text txtPublisher;
	private Text txtAuthor;
	private Text txtCareer;
	private Combo cboBookType;
	private Button rbtnWoman;
	private Button rbtnMan;
	private DateTime dtBirthday;
	
	public String title;
	public String publisher;
	public String bookType;
	public String authorName;
	public String career;
	public String birthDate;
	public Gender gender;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DlgAddBook(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("책 정보와 저자 정보를 입력하세요.");		//다이얼로그 설명
		setTitle("책 추가");		//다이얼로그 제목
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Label label = new Label(container, SWT.NONE);
		label.setBounds(32, 10, 56, 15);
		label.setText("책 제목");
		
		Label label_1 = new Label(container, SWT.NONE);
		label_1.setBounds(32, 44, 56, 15);
		label_1.setText("출판사");
		
		Label label_2 = new Label(container, SWT.NONE);
		label_2.setBounds(32, 77, 56, 15);
		label_2.setText("종류");
		
		txtTitle = new Text(container, SWT.BORDER);
		txtTitle.setBounds(113, 7, 211, 21);
		
		txtPublisher = new Text(container, SWT.BORDER);
		txtPublisher.setBounds(113, 41, 211, 21);
		
		txtAuthor = new Text(container, SWT.BORDER);
		txtAuthor.setBounds(113, 135, 211, 21);
		
		Label label_3 = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_3.setBounds(37, 119, 284, 2);
		
		Label label_4 = new Label(container, SWT.NONE);
		label_4.setText("저자");
		label_4.setBounds(32, 138, 56, 15);
		
		Label label_5 = new Label(container, SWT.NONE);
		label_5.setText("생년월일");
		label_5.setBounds(32, 169, 56, 15);
		
		Label label_6 = new Label(container, SWT.NONE);
		label_6.setText("경력");
		label_6.setBounds(32, 202, 56, 15);
		
		Label label_7 = new Label(container, SWT.NONE);
		label_7.setText("성별");
		label_7.setBounds(32, 234, 56, 15);
		
		txtCareer = new Text(container, SWT.BORDER);
		txtCareer.setBounds(113, 199, 211, 21);
		
		rbtnMan = new Button(container, SWT.RADIO);
		rbtnMan.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		rbtnMan.setBounds(113, 234, 91, 16);
		rbtnMan.setText("남자");
		
		rbtnWoman = new Button(container, SWT.RADIO);
		rbtnWoman.setText("여자");
		rbtnWoman.setBounds(230, 233, 91, 16);
		
		cboBookType = new Combo(container, SWT.NONE);
		cboBookType.setBounds(113, 74, 211, 23);
		cboBookType.setText("소설");
		String types[] = {"소설", "자기계발", "시","공학"};		//책 타입 콤보박스의 리스트
		cboBookType.setItems(types);
		cboBookType.select(0);		//기본 설정 index:0 (소설)
		cboBookType.setEnabled(true);
		dtBirthday = new DateTime(container, SWT.BORDER);
		dtBirthday.setBounds(113, 162, 211, 24);

		return area;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void okPressed() {
		//ok 누를 시 메소드
		title = txtTitle.getText();
		publisher = txtPublisher.getText();
		bookType = cboBookType.getText();
		authorName = txtAuthor.getText();
		career = txtCareer.getText();
		birthDate = String.format("%4d-%2d-%2d", dtBirthday.getYear(),dtBirthday.getMonth(),dtBirthday.getDay()); //생년월일 포맷
		if(rbtnMan.getSelection() == true) gender = Gender.MAN;
		else gender = Gender.WOMAN;
		
		super.okPressed();
	}
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}


}
