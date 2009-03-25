
import org.apache.log4j.Logger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
public class Scanner {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(Scanner.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;




	private Robot robot;
	//int fromPage,toPage,delay;
	String pdcviewer="C:\\Program Files\\Lizard Safeguard PDF Viewer\\pdcviewer.exe";
	static String ebook2="C:\\nadyelfikr\\ebook\\2.pdc";
	static String outdir2="C:\\nadyelfikr\\images";

	
	

	public Scanner() {
		try {
			robot=new Robot();
			robot.setAutoDelay(100);
		}catch(Exception e){
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public void altTab(){
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_TAB);
		
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public void escapeFullScreen(){
		robot.keyPress(KeyEvent.VK_ESCAPE);
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyPress(KeyEvent.VK_F);
		robot.delay(500);
		
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public void fullScreen(){
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyPress(KeyEvent.VK_B);
		robot.delay(200);
		
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyPress(KeyEvent.VK_9);
		robot.delay(200);

		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyPress(KeyEvent.VK_F);
		robot.delay(200);

		robot.keyRelease(KeyEvent.VK_ALT);
	}



	private BufferedImage rotate(BufferedImage image) {
		AffineTransform tx = new AffineTransform();
//	    tx.scale(scalex, scaley);
//	    tx.shear(shiftx, shifty);
//	    tx.translate(x, y);
	    tx.rotate(1.57079632679, image.getWidth()/2, image.getHeight()/2);
	    
	    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
	    image = op.filter(image, null);
	    return image;

	}
	public void captureImage(String outputDir,int[] a){
		BufferedImage screencapture= robot.createScreenCapture(new Rectangle(a[0],a[1],a[2],a[3]));
		//screencapture=rotate(screencapture);
		long time=new Date().getTime();
		// Save as JPEG
		File file = new File(outputDir+"\\"+time+".jpg");
		try {
			ImageIO.write(screencapture, "jpg", file);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
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
		//get x1
		result[2]=dim.width-2*result[0]-2;
//		for (int i = dim.width; i >= dim.width/2-1; i--) {
//			if( !robot.getPixelColor(i, (int) dim.height/2).equals(c00)){
//				result[2]=i;
//				break;
//			}
//		}

		//get y0
//		result[3]=dim.height-2*result[1];
for (int i = dim.height; i >= dim.height/2; i--) {
			if( !robot.getPixelColor((int) dim.width/2, i).equals(c00)){
				result[3]=i-2;
				break;
			}
		}

		return result;

	}
	public void nextPage(){
		robot.delay(200);
		robot.keyPress(KeyEvent.VK_PAGE_DOWN);

	}





	private Process openPdcFile(String pdcviewer, String ebook){
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
			logger.error( "Folder "+ filename+" does not exist.");
			return false;
		}
		return true;

	}
	public boolean doWork(final String ebook,final String outdir,final int fromPage, final int toPage, final int delay) {
		if(fromPage > toPage){
			logger.error( "End page should be greater than start page.");
			return false;
		}
		if(! check(ebook) || ! check(outdir))return false;
		final Process proc=openPdcFile(pdcviewer, ebook);

		try {
			if (proc==null ){
				System.out.println("erro open ebook");
				return false;
			}

			robot.delay(4000);

			goToPage(fromPage);
			robot.delay(300);

			fullScreen();
			robot.delay(delay);

			int[] boundries=getPageCoordinates();

			for (int i = fromPage; i <= toPage ; i++) {
				captureImage(outdir,boundries);
				robot.delay(delay);
				nextPage();
				//robot.delay(200);

			}

			escapeFullScreen();
			proc.destroy();


		} catch (Exception e) {
			logger.error("exception at work " +e.getMessage());
			e.printStackTrace();
			proc.destroy();
			return false;
		}
		return true;

	}
	
	public static void main(String[] args) {
		new Scanner().doWork(ebook2,outdir2,100, 140, 50);
	}
}
