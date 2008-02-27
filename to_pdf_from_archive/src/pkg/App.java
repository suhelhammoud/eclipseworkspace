package pkg;


import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import javax.swing.event.*;



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
public class App extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JLabel lblStatus;
	private JToolBar statusBar;
	private JToolBar bar;
	private JScrollPane scrlList;
	private JPanel panelList;
	private JList list;
	private JButton btnAbout;
	private JSeparator sep;
	private JButton jButton7;
	private JButton btnProcessAllBooks;
	private JButton jButton6;
	private JButton btnAddLinks;
	private JButton jButton5;
	private JButton btnFileOpen;
	private JButton jButton3;
	private JButton jButton2;
	private JButton jButton1;
	private JButton btnShowLog;
	private JButton btnProcess;
	private JCheckBox chkDeleteZip;
	private JCheckBox chktif2pdf;
	private JCheckBox chkDownload;
	private JPanel panelChecks;
	private JScrollPane scrlForm;
	private JSplitPane split;
	private JButton btnGetInfo;
	private JButton btnDeleteLink;
	private DefaultListModel model = new DefaultListModel();
	private panelBook panelbook;
	private JFileChooser chooser;
	//
	private LogFrame logframe=new LogFrame();
	private A wb=new A(logframe);
	boolean inProcess=false;
	private Thread thrd;
	boolean[] b={true,true,false};
	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		App inst = new App();
		inst.setVisible(true);
	}

	public App() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			{
				BorderLayout thisLayout = new BorderLayout();
				getContentPane().setLayout(thisLayout);
			}
			{
				{
				}
				bar = new JToolBar();
				getContentPane().add(bar, BorderLayout.NORTH);
				{
					btnShowLog = new JButton();
					bar.add(btnShowLog);
					btnShowLog.setText("Log");
					btnShowLog.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnShowLogActionPerformed(evt);
						}
					});
				}
				{
					jButton5 = new JButton();
					bar.add(jButton5);
					jButton5.setText("   ");
					jButton5.setEnabled(false);
					jButton5.setPreferredSize(new java.awt.Dimension(20, 21));
				}
				{
					btnGetInfo = new JButton();
					FlowLayout btnGetInfoLayout = new FlowLayout();
					btnGetInfo.setLayout(btnGetInfoLayout);
					bar.add(btnGetInfo);
					btnGetInfo.setText("\u0645\u0639\u0644\u0648\u0645\u0627\u062a \u0627\u0644\u0643\u062a\u0627\u0628");
					btnGetInfo.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnGetInfoActionPerformed(evt);
						}
					});
				}
				{
					jButton1 = new JButton();
					bar.add(jButton1);
					jButton1.setText("  ");
					jButton1.setEnabled(false);
				}
				{
					btnDeleteLink = new JButton();
					bar.add(btnDeleteLink);
					btnDeleteLink.setText("\u062d\u0630\u0641");
					btnDeleteLink.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnDeleteLinkActionPerformed(evt);
						}
					});
				}
				{
					jButton3 = new JButton();
					bar.add(jButton3);
					jButton3.setText("                      ");
					jButton3.setEnabled(false);
				}
				{
					btnProcess = new JButton();
					bar.add(btnProcess);
					btnProcess.setText("\u0643\u062a\u0627\u0628 \u0648\u0627\u062d\u062f");
					btnProcess.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnProcessActionPerformed(evt);
						}
					});
				}
				{
					jButton2 = new JButton();
					bar.add(jButton2);
					jButton2.setText("        ");
					jButton2.setEnabled(false);
				}
				{
					btnProcessAllBooks = new JButton();
					bar.add(btnProcessAllBooks);
					btnProcessAllBooks.setText("\u0643\u0644 \u0627\u0644\u0643\u062a\u0628");
					btnProcessAllBooks.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnProcessAllBooksActionPerformed(evt);
						}
					});
				}
				{
					sep = new JSeparator();
					bar.add(sep);
					sep.setOrientation(SwingConstants.VERTICAL);
				}
				{
					btnAbout = new JButton();
					bar.add(btnAbout);
					btnAbout
						.setText("\u0639\u0646 \u0627\u0644\u0628\u0631\u0646\u0627\u0645\u062c");
					btnAbout.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnAboutActionPerformed(evt);
						}
					});
				}
				{
					jButton7 = new JButton();
					bar.add(jButton7);
					jButton7.setText("      ");
					jButton7.setEnabled(false);
				}
				{
					btnAddLinks=new JButton();
					bar.add(btnAddLinks);
					btnAddLinks
						.setText("\u0625\u0636\u0627\u0641\u0629 \u0631\u0648\u0627\u0628\u0637");
					btnAddLinks.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnAddLinksActionPerformed(evt);
						}
					});
				}
				{
					jButton6 = new JButton();
					bar.add(jButton6);
					jButton6.setText("  ");
					jButton6.setEnabled(false);
				}
				{
					btnFileOpen = new JButton();
					bar.add(btnFileOpen);
					btnFileOpen
						.setText("\u0641\u062a\u062d \u0645\u0644\u0641");
					btnFileOpen.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnFileOpenActionPerformed(evt);
						}
					});
				}
			}
			{
				statusBar = new JToolBar();
				getContentPane().add(statusBar, BorderLayout.SOUTH);
				{
					lblStatus = new JLabel();
					BorderLayout lblStatusLayout = new BorderLayout();
					lblStatus.setLayout(lblStatusLayout);
					statusBar.add(lblStatus);
					lblStatus.setText("Status");
					lblStatus.setPreferredSize(new java.awt.Dimension(601, 14));
				}
			}
			{
				split = new JSplitPane();
				{
					panelList = new JPanel();
					split.add(panelList, JSplitPane.LEFT);
					BorderLayout panelListLayout = new BorderLayout();
					panelList.setLayout(panelListLayout);
					{
						panelChecks = new JPanel();
						GridLayout panelChecksLayout = new GridLayout(1, 1);
						panelChecksLayout.setColumns(1);
						panelChecksLayout.setHgap(5);
						panelChecksLayout.setVgap(5);
						panelList.add(panelChecks, BorderLayout.SOUTH);
						panelChecks.setLayout(panelChecksLayout);
						panelChecks.setSize(172, 100);
						{
							chkDownload = new JCheckBox();
							GridLayout jCheckBox1Layout = new GridLayout(1, 1);
							jCheckBox1Layout.setColumns(1);
							jCheckBox1Layout.setHgap(5);
							jCheckBox1Layout.setVgap(5);
							chkDownload.setLayout(jCheckBox1Layout);
							panelChecks.add(chkDownload);
							chkDownload.setText("DownLoad");
							chkDownload.setSelected(true);
							chkDownload.addChangeListener(new ChangeListener() {
								public void stateChanged(ChangeEvent evt) {

									if(inProcess)return ;
									if(!chkDownload.isSelected()){
										chktif2pdf.setSelected(false);
										chktif2pdf.setEnabled(false);
									}else{
										chktif2pdf.setEnabled(true);
									}

								}
							});
						}
						{
							chktif2pdf = new JCheckBox();
							panelChecks.add(chktif2pdf);
							GridLayout chktif2pdfLayout = new GridLayout(1, 1);
							chktif2pdfLayout.setColumns(1);
							chktif2pdfLayout.setHgap(5);
							chktif2pdfLayout.setVgap(5);
							chktif2pdf.setLayout(chktif2pdfLayout);
							chktif2pdf.setText("To PDF");
							chktif2pdf.setSelected(true);
							chktif2pdf.addChangeListener(new ChangeListener() {
								public void stateChanged(ChangeEvent evt) {

									if (inProcess)
										return;
									if (!chktif2pdf.isSelected()) {
										chkDeleteZip.setSelected(false);
										chkDeleteZip.setEnabled(false);
									} else {
										chkDeleteZip.setEnabled(true);
									}

								}
							});
						}
						{
							chkDeleteZip = new JCheckBox();
							panelChecks.add(chkDeleteZip);
							GridLayout chkDeleteZipLayout = new GridLayout(1, 1);
							chkDeleteZipLayout.setColumns(1);
							chkDeleteZipLayout.setHgap(5);
							chkDeleteZipLayout.setVgap(5);
							chkDeleteZip.setText("Delete Zip");
							chkDeleteZip.setLayout(null);
						}
					}
					{
						scrlList = new JScrollPane();
						panelList.add(scrlList, BorderLayout.CENTER);
						scrlList.setBounds(7, 35, 126, 294);
						scrlList.setPreferredSize(new java.awt.Dimension(
							172,
							200));
						scrlList.setSize(172, 200);
						{
							list = new JList();
							BorderLayout listLayout = new BorderLayout();
							scrlList.setViewportView(list);
							list.setModel(model);
							list.setLayout(null);
							list
								.addListSelectionListener(new ListSelectionListener() {
								public void valueChanged(ListSelectionEvent evt) {
									listValueChanged(evt);
								}
								});
						}

					}
				}
				{
					scrlForm = new JScrollPane();
					split.add(scrlForm, JSplitPane.RIGHT);
					{
						panelbook = new panelBook();
						scrlForm.setViewportView(panelbook);
					}
				}
				getContentPane().add(split, BorderLayout.CENTER);
				split.setPreferredSize(new java.awt.Dimension(640, 424));
			}
			this.setSize(693, 514);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void refList(){
		model.clear();
		for (String iter : wb.currentLinks) {
			model.addElement(iter);
		}
		if(model.size()==0)
			btnDeleteLink.setEnabled(false);
		else
			btnDeleteLink.setEnabled(true);

	}
	private void btnDeleteLinkActionPerformed(ActionEvent evt) {

		Object obj=list.getSelectedValue();
		if (obj==null)return;
		String url=obj.toString();
		int index=list.getSelectedIndex();
		wb.deleteLink(url);
		refList();
		if(model.size()== index )
			list.setSelectedIndex(index-1);
		else
			list.setSelectedIndex(index);




	}


	private void btnGetInfoActionPerformed(ActionEvent evt) {
		String url=(String)list.getSelectedValue();
		if(url==null )return;
		List<String> info=wb.getInfo(url);
		if(info == null)return;
		panelbook.setValues(info);
		
	}

	void processItem(final String[] url){
		if (inProcess)return;
		if(wb==null)return;
		thrd=new Thread(){
			public void run(){
				inProcess=true;
				for (int i = 0; i < url.length; i++) {


					String fileName=A.getFileName(url[i]);
					List<String> info=wb.getInfo(url[i]);
					refList();
					if(info == null)continue;
					panelbook.setValues(info);


					String ftpDir= info.get(1);
					log("ftp org "+ftpDir);

					String ftpFile=A.getFtpFile(ftpDir);

					if(! chkDownload.isSelected()){
						inProcess=false;
						continue;
					}
					//todo 
					wb.deleteLink(url[i]);
					refList();

					lblStatus.setText("Downloading "+ info.get(4));
					log("Going to download "+ ftpFile+ "\nTitle: "+info.get(4));
					log("Size downloaded ...  "+wb.download(ftpFile, fileName)+" bytes");

					if( ! chktif2pdf.isSelected()){
						inProcess=false;
						continue;
					}
					lblStatus.setText("Tif 2 PDF ... "+ info.get(4));
					log("Unzipping "+ ftpFile+ "\nTitle:"+info.get(4));
					wb.unzip(fileName);
					log("Tif to PDF : "+ fileName);
					wb.tif2pdf(fileName);

					File dirfile=new File("unzipped/"+fileName);
					log("Deleting tiff Directory: "+A.deleteDirectory(dirfile));


					if( ! chkDeleteZip.isSelected()){
						inProcess=false;
						continue;
					}
					lblStatus.setText("Deleting downloaded files ");
					File file=new File("downloads/"+fileName+".zip");
					log("Deleting to downloaded files :"+ file.delete());

				}
				inProcess=false;
				chkDownload.setEnabled(b[0]);
				chktif2pdf.setEnabled(b[1]);
				chkDeleteZip.setEnabled(b[2]);
				lblStatus.setText("Done");
			}
		};
		thrd.start();
	}

	public void log(String msg){
		logframe.log(msg);
	}

	private void btnProcessActionPerformed(ActionEvent evt) {
		List<String> l=wb.currentLinks;
		if(l==null)return;
		if(l.size()==0)return;
		
		if(inProcess){
			log("inProcess =" + inProcess);
			return;
		}
		b=new boolean[]{chkDownload.isEnabled(),chktif2pdf.isEnabled(),chkDeleteZip.isEnabled()};
		chkDownload.setEnabled(false);
		chktif2pdf.setEnabled(false);
		chkDeleteZip.setEnabled(false);

		int index=list.getSelectedIndex();
		if(index== -1)return;
		processItem(new String[]{list.getSelectedValue().toString()});

	}




	private void btnShowLogActionPerformed(ActionEvent evt) {
		logframe.setVisible(true);
	}
	
	private void btnFileOpenActionPerformed(ActionEvent evt) {
		chooser=new JFileChooser();
		///
		chooser.addChoosableFileFilter(new TxtFile());
        chooser.setAcceptAllFileFilterUsed(false);
        //
		chooser.setSelectedFile(new File("in.txt"));
		int returnVal = chooser.showOpenDialog(this);
		if(returnVal != JFileChooser.APPROVE_OPTION) return ;

		wb=new A(logframe);
		wb.file=chooser.getSelectedFile();
		List<String> urlLinks;
		try {
			urlLinks=A.urlLinks(wb.file);
			for (String iter : urlLinks) {
				wb.addLink(iter);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		refList();

	}
	
	private void btnAddLinksActionPerformed(ActionEvent evt) {
		PanelAddLinks panel=new PanelAddLinks();
		int result=JOptionPane.showConfirmDialog(this, panel,"Use Ctrl + V to paste links",JOptionPane.YES_NO_OPTION);
		if (result== JOptionPane.OK_OPTION){
			String txt=panel.getText();
			try {
				List<String> urlLinks=A.extractLinks(txt);

				for (String iter : urlLinks) {
					wb.addLink(iter);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			refList();
		}
	}
	
	private void btnProcessAllBooksActionPerformed(ActionEvent evt) {
		List<String> l=wb.currentLinks;
		if(l==null)return;
		if(l.size()==0)return;
		String[] urls=new String[l.size()];
		for (int i = 0; i < urls.length; i++) {
			urls[i]=l.get(i);
		}		
		
		if(inProcess){
			log("inProcess : " + inProcess);
			return;
		}
		b=new boolean[]{chkDownload.isEnabled(),chktif2pdf.isEnabled(),chkDeleteZip.isEnabled()};
		chkDownload.setEnabled(false);
		chktif2pdf.setEnabled(false);
		chkDeleteZip.setEnabled(false);

		processItem(urls);


	}
	
	private void btnAboutActionPerformed(ActionEvent evt) {
		JOptionPane.showMessageDialog(this, "www.nadyelfikr.com");
	}
	
	private void listValueChanged(ListSelectionEvent evt) {
//		String url=(String)list.getSelectedValue();
//		if(url==null )return;
//		List<String> info=wb.getInfo(url);
//		panelbook.setValues(info);
	}

}
