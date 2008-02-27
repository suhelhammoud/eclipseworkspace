package pkg;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.WindowConstants;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class panelBook extends javax.swing.JPanel {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JLabel id;
	private JLabel copyright;
	private JLabel ocr_digitizer;
	private JLabel process_digitizer;
	private JLabel lblID;
	private JLabel lblCopyRight;
	private JTextField txtKeywords;
	private JTextField txtAuthor;
	private JTextField txtTitle;
	private JSeparator jSeparator1;
	private JLabel lblOcrDigitizer;
	private JLabel lblProcessDigitizer;
	private JLabel lblCondition;
	private JLabel lblScandigitizer;
	private JLabel lblScannedPages;
	private JLabel lblColor;
	private JLabel lblLanguage;
	private JLabel lblPages;
	private JLabel lblPublishDate;
	private JLabel lblPublisher;
	private JLabel lblName;
	private JLabel condition;
	private JLabel scan_digitizer;
	private JLabel language_code;
	private JLabel name;
	private JLabel pages;
	private JLabel publication_date;
	private JLabel author;
	private JLabel keywords;
	private JLabel scanned_pages;
	private JLabel color;
	private JLabel puplisher;
	private JLabel title;

	/**
	 * Auto-generated main method to display this 
	 * JPanel inside a new JFrame.
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new panelBook());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public panelBook() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.0, 0.1};
			thisLayout.columnWidths = new int[] {95, 7};
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(545, 383));
			{
				id = new JLabel();
				this.add(id, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				id.setText("ID");
			}
			{
				title = new JLabel();
				this.add(title, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				title.setText("Title");
			}
			{
				puplisher = new JLabel();
				this.add(puplisher, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				puplisher.setText("Publisher");
			}
			{
				color = new JLabel();
				this.add(color, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				color.setText("Color");
			}
			{
				scanned_pages = new JLabel();
				this.add(scanned_pages, new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				scanned_pages.setText("Scanned Pages");
			}
			{
				keywords = new JLabel();
				this.add(keywords, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				keywords.setText("Keywords");
			}
			{
				author = new JLabel();
				this.add(author, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				author.setText("Author");
			}
			{
				publication_date = new JLabel();
				this.add(publication_date, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				publication_date.setText("Publication Date");
			}
			{
				pages = new JLabel();
				this.add(pages, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				pages.setText("pages");
			}
			{
				name = new JLabel();
				this.add(name, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				name.setText("Name");
			}
			{
				language_code = new JLabel();
				this.add(language_code, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				language_code.setText("Language");
			}
			{
				scan_digitizer = new JLabel();
				this.add(scan_digitizer, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				scan_digitizer.setText("Scan Digitizer");
			}
			{
				condition = new JLabel();
				this.add(condition, new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				condition.setText("Condition");
			}
			{
				process_digitizer = new JLabel();
				this.add(process_digitizer, new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				process_digitizer.setText("Process Digitizer");
			}
			{
				ocr_digitizer = new JLabel();
				this.add(ocr_digitizer, new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				ocr_digitizer.setText("OCR Digitizer");
			}
			{
				copyright = new JLabel();
				this.add(copyright, new GridBagConstraints(0, 16, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				copyright.setText("Copyright");
			}
			{
				lblID = new JLabel();
				this.add(lblID, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblID.setText("         ");
				lblID.setAutoscrolls(true);
			}
			{
				lblName = new JLabel();
				this.add(lblName, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblName.setText("   ");
				lblName.setAutoscrolls(true);
			}
			{
				lblPublisher = new JLabel();
				this.add(lblPublisher, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblPublisher.setText("      ");
				lblPublisher.setAutoscrolls(true);

			}
			{
				lblPublishDate = new JLabel();
				this.add(lblPublishDate, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblPublishDate.setText("      ");
				lblPublishDate.setAutoscrolls(true);
			}
			{
				lblPages = new JLabel();
				this.add(lblPages, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblPages.setText("    ");
				lblPages.setAutoscrolls(true);
			}
			{
				lblLanguage = new JLabel();
				this.add(lblLanguage, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblLanguage.setText("   ");
				lblLanguage.setAutoscrolls(true);
			}
			{
				lblColor = new JLabel();
				this.add(lblColor, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblColor.setText("   ");
				lblColor.setAutoscrolls(true);
			}
			{
				lblScannedPages = new JLabel();
				this.add(lblScannedPages, new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblScannedPages.setText("       ");
				lblScannedPages.setAutoscrolls(true);
			}
			{
				lblScandigitizer = new JLabel();
				this.add(lblScandigitizer, new GridBagConstraints(1, 12, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblScandigitizer.setText("                     ");
				lblScandigitizer.setAutoscrolls(true);
			}
			{
				lblCondition = new JLabel();
				this.add(lblCondition, new GridBagConstraints(1, 13, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblCondition.setText("                  ");
				lblCondition.setAutoscrolls(true);
			}
			{
				lblProcessDigitizer = new JLabel();
				this.add(lblProcessDigitizer, new GridBagConstraints(1, 14, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblProcessDigitizer.setText("        ");
				lblProcessDigitizer.setAutoscrolls(true);
			}
			{
				lblOcrDigitizer = new JLabel();
				this.add(lblOcrDigitizer, new GridBagConstraints(1, 15, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblOcrDigitizer.setText("               ");
				lblOcrDigitizer.setAutoscrolls(true);
			}
			{
				lblCopyRight = new JLabel();
				this.add(lblCopyRight, new GridBagConstraints(1, 16, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 15, 0, 0), 0, 0));
				lblCopyRight.setText("          ");
				lblCopyRight.setAutoscrolls(true);
			}
			{
				jSeparator1 = new JSeparator();
				this.add(jSeparator1, new GridBagConstraints(
						1,
						14,
						1,
						1,
						0.0,
						0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.NONE,
						new Insets(0, 0, 0, 0),
						0,
						0));
			}
			{
				txtTitle = new JTextField();
				this.add(txtTitle, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
				txtTitle.setText("     ");
				txtTitle.setEditable(false);
			}
			{
				txtAuthor = new JTextField();
				this.add(txtAuthor, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
				txtAuthor.setText("             ");
				txtAuthor.setEditable(false);
			}
			{
				txtKeywords = new JTextField();
				this.add(txtKeywords, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 10, 0, 10), 0, 0));
				txtKeywords.setText("                 ");
				txtKeywords.setEditable(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setValues(List<String> lst){
		if(lst.size()<18)return ;
		lblID.setText(lst.get(2));
		lblName.setText(lst.get(3));
		txtTitle.setText(lst.get(4));
		txtAuthor.setText(lst.get(5));
		lblPublisher.setText(lst.get(6));
		lblPublishDate.setText(lst.get(7));
		lblPages.setText(lst.get(8));
		lblLanguage.setText(lst.get(9));
		lblColor.setText(lst.get(10));
		lblScannedPages.setText(lst.get(11));
		txtKeywords.setText(lst.get(12));
		//lblKeywords.setText(GetAddresses.getTag(txt, "keywords"));
		lblScandigitizer.setText(lst.get(13));
		lblCondition.setText(lst.get(14));
		lblProcessDigitizer.setText(lst.get(15));
		lblOcrDigitizer.setText(lst.get(16));
		lblCopyRight.setText(lst.get(17));

		
	}
	public void setValues(String txt){
		lblID.setText(A.getTag(txt, "ID")+"\nnewline");
		lblName.setText(A.getTag(txt, "name"));
		txtTitle.setText(multiLine(A.getTag(txt, "title"),8));
		txtAuthor.setText(A.getTag(txt, "author"));
		lblPublisher.setText(A.getTag(txt, "publisher"));
		lblPublishDate.setText(A.getTag(txt, "publication_date"));
		lblPages.setText(A.getTag(txt, "pages"));
		lblLanguage.setText(A.getTag(txt, "language_code"));
		lblColor.setText(A.getTag(txt, "color"));
		lblScannedPages.setText(A.getTag(txt, "scanned_pages"));
		txtKeywords.setText(multiLine(A.getTag(txt, "keywords"),8));
		//lblKeywords.setText(GetAddresses.getTag(txt, "keywords"));
		lblScandigitizer.setText(A.getTag(txt, "scan_digitizer"));
		lblCondition.setText(A.getTag(txt, "condition"));
		lblProcessDigitizer.setText(A.getTag(txt, "process_digitizer"));
		lblOcrDigitizer.setText(A.getTag(txt, "ocr_digitizer"));
		lblCopyRight.setText(A.getTag(txt, "copyright"));

		//lbl.setText(GetAddresses.getTag(txt, ""));
	}

	static String multiLine(String txt, int i){
		if(txt.length()< i)return txt;
		String[] arr=txt.trim().split(" ");
		if (arr==null)return txt;

		String result=arr[0];
		int line=arr[0].length();

		for (int j = 1; j < arr.length; j++) {
			result+=" "+ arr[j];
			line+=arr[j].length()+1;
			if(line >i){
				result+="\n";
				line=0;
			}
		}
		return result;
	}

}
