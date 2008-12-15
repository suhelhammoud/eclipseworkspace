import javax.servlet.*;
import javax.servlet.http.*;

import server.ISpace;
import server.ISpaceConfig;
import server.MyFile;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;

public class UploadServlet extends HttpServlet {

	private String web_application_dir;
	private String web_temp_upload_dir;
	private String upload_redirect_address;
	public void init( ){
		web_application_dir =ISpaceConfig.get("web_application_dir");
		web_temp_upload_dir = ISpaceConfig.get("web_temp_upload_dir");
		upload_redirect_address = ISpaceConfig.get("upload_redirect_address");

		//create the tmp directory if not existed
		File mydir=new File(web_application_dir+"/"+web_temp_upload_dir);
		if(mydir.exists())ISpace.deleteDir(mydir);
		mydir.mkdirs();

	}

	public void doPost(HttpServletRequest request, 
			HttpServletResponse response)
	throws ServletException, java.io.IOException {

		//file limit size of 20 MB
		MultipartRequest mpr = new MultipartRequest(request,
				web_application_dir+"/"+web_temp_upload_dir,
				20 * 1024 * 1024,new DefaultFileRenamePolicy() );
		Enumeration<String> filesIndex = mpr.getFileNames( );

		// get the file id
		String fileId=mpr.getParameter("fileId").trim();
		int intId=Integer.parseInt(fileId);
		MyFile myFile= new MyFile(intId);

		String index=filesIndex.nextElement( );
		//get the file
		File file=mpr.getFile(index);
		//get the fileName
		String orginalFileName= mpr.getOriginalFileName(index);
		//fill myFile with the data
		myFile.readFromFile(file, orginalFileName);

		System.out.println("in servelet myfile:"+myFile);

		ISpace.uploadMyFile(myFile);

		response.sendRedirect(upload_redirect_address);
		//		response.setContentType("text/html");
		//		java.io.PrintWriter out = response.getWriter( );
		//
		//		out.println("<html>");
		//		out.println("<head>");
		//		out.println("<title>Servlet upload</title>");  
		//		out.println("</head>");
		//		out.println("<body>");
		//
		//		for (int i = 1; filesIndex.hasMoreElements( );i++){
		//			String iter=filesIndex.nextElement( );
		//			
		//			out.println("The name of uploaded file> " +iter+"-id-"+fileId+"---"+ i +
		//					" is: " + mpr.getFilesystemName(iter) 
		//					+ "<br/> "+ mpr.getOriginalFileName(iter)
		//					+ "<br><br>");
		//		}
		//		out.println("</body>");
		//		out.println("</html>");
		//

	} 

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	 throws ServletException, java.io.IOException {

		throw new ServletException("GET method used with " +
				getClass( ).getName( )+": POST method required.");
	} 
}