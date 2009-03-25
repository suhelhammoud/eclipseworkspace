
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

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
public class PdcConverter  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	private Robot robot;
	//int fromPage,toPage,delay;
	String pdcviewer="C:\\Program Files\\Lizard Safeguard PDF Viewer\\pdcviewer.exe";
	static String ebook2="C:\\nadyelfikr\\ebook\\2.pdc";
	static String outdir2="C:\\nadyelfikr\\images";

	
	

	public PdcConverter() {

		try {
			robot=new Robot();
			robot.setAutoDelay(100);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void delay(int delay){
		robot.delay(delay);
	}

	public void altTab(){
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_TAB);
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.delay(1000);

	}

	public void escFullScreen(){
		robot.keyPress(KeyEvent.VK_ESCAPE);
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyPress(KeyEvent.VK_F);

		robot.delay(2000);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public void fullScreen(){
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyPress(KeyEvent.VK_B);

		robot.delay(500);

		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyPress(KeyEvent.VK_9);

		robot.delay(500);

		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyPress(KeyEvent.VK_F);

		robot.delay(500);

		robot.keyRelease(KeyEvent.VK_ALT);
	}



	public void captureImage(String outputDir,int[] a){
		BufferedImage screencapture= robot.createScreenCapture(new Rectangle(a[0],a[1],a[2],a[3]));
		long time=new Date().getTime();
		// Save as JPEG
		File file = new File(outputDir+"\\"+time+".jpg");
		try {
			ImageIO.write(screencapture, "jpg", file);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getScreenSize(){
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println(dim.toString());

	}

	/**
	 * 
	 * @return x0,y0,width,height
	 */
	public int[] getPageCoordinates(){
		int[] result=new int[4];
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		System.out.println(dim.toString());

		Color c00=robot.getPixelColor(0, 0);
		//get x0
		for (int i = 0; i < dim.width/2; i++) {
			if( !robot.getPixelColor(i, (int) dim.height/2).equals(c00)){
				result[0]=i;
				break;
			}
		}

		//get y0
		for (int i = 0; i < dim.height/2; i++) {
			if( !robot.getPixelColor((int) dim.width/2, i).equals(c00)){
				result[1]=i;
				break;
			}
		}
		result[2]=dim.width-2*result[0];
		result[3]=dim.height-2*result[1];
		return result;

	}
	public void nextPage(){
		robot.delay(200);
		robot.keyPress(KeyEvent.VK_PAGE_DOWN);

	}





	private Process openPdcFile(String ebook){
		String txtCommand =pdcviewer+" "+ebook;
		Process proc = null;
		try {
			proc=Runtime.getRuntime().exec(txtCommand);
		} catch (Exception e) {
			proc.destroy();
			proc=null;
			e.printStackTrace();
		}
		return proc;
	}
	private void goToPage(int pageNumber) {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.delay(3000);
		robot.keyPress(KeyEvent.VK_D);
		robot.keyPress(KeyEvent.VK_G);

		robot.keyRelease(KeyEvent.VK_ALT);

		String page=String.valueOf(pageNumber);
		char[] digit=page.toCharArray();
		for (char c : digit) {
			robot.keyPress(getKeyEvent(c));
			robot.delay(100);
		}
		robot.keyPress(KeyEvent.VK_ENTER);

	}
	private int getKeyEvent(char chr){
		switch (chr) {
		case '2':
			return KeyEvent.VK_2;
		case '3':
			return KeyEvent.VK_3;
		case '4':
			return KeyEvent.VK_4;
		case '5':
			return KeyEvent.VK_5;
		case '6':
			return KeyEvent.VK_6;
		case '7':
			return KeyEvent.VK_7;
		case '8':
			return KeyEvent.VK_8;
		case '9':
			return KeyEvent.VK_9;
		case '0':
			return KeyEvent.VK_0;
		default:
			return KeyEvent.VK_1;
		}
	}

	private boolean check(String filename) {
		if(! new File(filename).exists()){
			JOptionPane.showMessageDialog(null, "Folder "+ pdcviewer+" does not exist.");
			return false;
		}
		return true;

	}
	public boolean doWork(final String ebook,final String outdir,final int fromPage, final int toPage, final int delay) {

		if(! check(ebook) || ! check(outdir))return false;
		final Process proc=openPdcFile(ebook);


		if(fromPage > toPage){
			JOptionPane.showMessageDialog(null, "End page should be greater than start page.");
			return false;
		}


		try {
			if (proc==null ){
				System.out.println("erro open ebook");
				return false;
			}

			robot.delay(6000);

			goToPage(fromPage);
			robot.delay(3000);

			fullScreen();
			robot.delay(delay);

			int[] boundries=getPageCoordinates();

			for (int i = fromPage; i <= toPage ; i++) {
				captureImage(outdir,boundries);
				robot.delay(delay);
				nextPage();
				robot.delay(300);

			}

			escFullScreen();
			proc.destroy();


		} catch (Exception e) {
			System.err.println("exception at work ");
			e.printStackTrace();
			proc.destroy();
			return false;
		}
		return true;

	}
	
	public static void main(String[] args) {
		new PdcConverter().doWork(ebook2,outdir2,2, 5, 2000);
	}
}