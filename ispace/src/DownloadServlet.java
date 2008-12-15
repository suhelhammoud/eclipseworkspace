

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import server.ISpace;
import server.ISpaceConfig;
import server.MyFile;

/**
 * Servlet implementation class DownloadServlet
 */
public class DownloadServlet extends HttpServlet {

	private String web_application_dir;
	private String web_temp_download_dir;

	public void init( ){
		web_application_dir =ISpaceConfig.get("web_application_dir");
		web_temp_download_dir = ISpaceConfig.get("web_temp_download_dir"); 
		

		//create the tmp directory if not existed
		File mydir=new File(web_application_dir+"/"+web_temp_download_dir);
		if(mydir.exists())ISpace.deleteDir(mydir);
		mydir.mkdirs();

	}
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String fileId= request.getParameter("fileId");
		MyFile myFile= ISpace.downLoadMyFile(fileId);
		String savedFileName = ISpace.saveLocal(myFile, web_application_dir+"/"+web_temp_download_dir);
		
		
		String fileDownloadLink=web_temp_download_dir+"/"+savedFileName;
		
		response.setContentType("text/html");
		java.io.PrintWriter out = response.getWriter( );

		out.println("<html>");
		out.println("<head>");
		out.println("<title>Servlet upload</title>");  
		out.println("</head>");
		out.println("<body>");
		out.println("<a href="+ fileDownloadLink+">download link</a>");
		out.println("</body>");
		out.println("</html>");

		//out.close();
		//response.sendRedirect("http://localhost:8080/ispace/tmp/t.rar");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
