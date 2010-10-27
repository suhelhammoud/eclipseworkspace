package hasim.gui;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;

import eduni.simjava.Sim_entity;
import eduni.simjava.Sim_system;
import hasim.CTag;
import hasim.HCounter;
import hasim.HJobTracker;
import hasim.HTAG;
import hasim.JobInfo;
import hasim.RF;
import hasim.core.Datum;
import hasim.core.HMachine;
import hasim.json.JsonJob;
import hasim.json.JsonRealRack;

public class MRSimTest1 {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MRSimTest1.class);

	static HSimulator sim;
	List<JobInfo> jobs;
	List<JobInfo> resultJobs=new ArrayList<JobInfo>();

//	public MRSimTest1(String jobDir) {
//		this.jobDir=jobDir;
//	}
	
	List<String> fileNames(String dirName){
		List<String> result=new ArrayList<String>();
		File dir=new File(dirName);
		if( ! dir.exists() || dir.isFile()){
			logger.error(dirName + " does not exist or it is a file");
		}
		for (File file : dir.listFiles()) {
			result.add(file.getAbsolutePath());
//			break;//to be deleted later
		}
		Collections.sort(result);
		return result;
	}
	
	List<JobInfo> getJobs(List<String> fileNames, Sim_entity user, int returnTag){
		List<JobInfo> result=new ArrayList<JobInfo>();
		for (String fileName : fileNames) {
			JobInfo jobinfo=new JobInfo(fileName, user,
					HMachine.getAll());
			jobinfo.setReturnTag(returnTag);
			result.add(jobinfo);
		}
		return result;
	}

	public static boolean guiSim=true;
	
	

	
//	void bulkTest(){
//		sim.getJobTracker().getHUser().submitJobTest(this);
//
//	}
	
	

	void testInit(Sim_entity user){

		if(guiSim)return;
		
		
		if(true)return;
		
		logger.info("+++++++++++++++++++++++++++");
	

//		System.out.println(Arrays.toString(fileNames(jobDir).toArray()));
		List<String> files = fileNames(jobDir);
		List<JobInfo> jobs=getJobs(files, 
				user, HTAG.jobinfo_complete.id);
		

		for (int i = 0; i < files.size(); i++) {
			String jobFile=files.get(i);
			JobInfo jobinfo = jobs.get(i);
		
			assert jobinfo.getUser() != null;
			try {
				logger.info("submit jobid =  " +jobinfo.getId()+ 
						", user ="+ jobinfo.getUser());

				sim.submitJob(jobFile, jobinfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			JobInfo jobinfo_return=(JobInfo)Datum.collectOne(
					user, HTAG.jobinfo_complete.id());
			assert jobinfo_return.getId() == jobinfo.getId();
			resultJobs.add(jobinfo_return);
//			
			logger.info("got job return");
 		}
		logger.info("jobtest created");
		
	}
//	@Override
//	public void submit(Sim_entity user) {
//
//		testInit(user);
//		printResult();
////		System.out.println(printAllJobInfoHeader());
////		System.out.println(printTimes());
////		sim.stopSimulator();
//	}
	
	String printTimes(){
		
		StringBuffer sb=new StringBuffer("\nconf\trecords\ts-map\ts-reduce\ts-total");
		for (JobInfo jobinfo : resultJobs) {
			double records =jobinfo.getJob().getData().getRecords();

			double avM = jobinfo.getCounters().get(CTag.avMappersTime);
			double avR = jobinfo.getCounters().get(CTag.avReducersTime);
			double avJ = jobinfo.getCounters().get(CTag.JOB_TOTAL_TIME);
			
			String type="a";
			String jobName= jobinfo.getJob().getJobName();
			if(jobName.contains("-j"))
				type="j";
			else if(jobName.contains("-c"))
				type="c";
			sb.append("\n"+type+"\t"+records+"\t"+avM+"\t"+avR+"\t"+avJ);
 		}
		return sb.toString();
	}

	String printJobInfo(JobInfo jobinfo){
		StringBuffer sb=new StringBuffer();
		
		String type="a";
		String jobName= jobinfo.getJob().getJobName();
		if(jobName.contains("-j"))
			type="j";
		else if(jobName.contains("-c"))
			type="c";

		double records =jobinfo.getJob().getData().getRecords();

		double avM = jobinfo.getCounters().get(CTag.avMappersTime);
		double avR = jobinfo.getCounters().get(CTag.avReducersTime);
		double avJ = jobinfo.getCounters().get(CTag.JOB_TOTAL_TIME);
		
		double spilledM = jobinfo.getmCounter().get(CTag.SPILLED_RECORDS);
		double spilledR = jobinfo.getrCounter().get(CTag.SPILLED_RECORDS);
		double spilledJ = jobinfo.getCounters().get(CTag.SPILLED_RECORDS);
	
		sb.append("\n"+type+"\t"+records+"\t"+
				avM+"\t"+avR+"\t"+avJ+"\t"+
				spilledM+"\t"+spilledR+"\t"+spilledJ
				);
		return sb.toString();
	}

	String printAllJobInfoHeader() {
		String result = "\nConf\tRecords" +
				"\tS-M-FBR\tS-M-FBW\tS-M-Spilled\tS-M-Time" +
				"\tS-R-FBR\tS-R-FBW\tS-R-Spilled\tS-R-Time" +
				"\tS-J-FBR\tS-J-FBW\tS-J-Spilled\tS-J-Time" +
				"\tS-shuffle\tS-C-input\tS-C-output\tS-R-input";
		return result;
	}
	String printAllJobInfo(JobInfo jobinfo){
		StringBuffer sb=new StringBuffer();
		HCounter c=jobinfo.getCounters();
		HCounter m=jobinfo.getmCounter();
		HCounter r=jobinfo.getrCounter();
		
		String conf="a";
		String jobName= jobinfo.getJob().getJobName();
		if(jobName.contains("j"))
			conf="j";
		else if(jobName.contains("c"))
			conf="c";

		double records =jobinfo.getJob().getData().getRecords();

		double m_fbr= m.get(CTag.FILE_BYTES_READ);
		double m_fbw=m.get(CTag.FILE_BYTES_WRITTEN);
		double m_spilled=m.get(CTag.SPILLED_RECORDS);
		double m_time= c.get(CTag.avMappersTime);

		double r_fbr= r.get(CTag.FILE_BYTES_READ);
		double r_fbw=r.get(CTag.FILE_BYTES_WRITTEN);
		double r_spilled=r.get(CTag.SPILLED_RECORDS);
		double r_time= c.get(CTag.avReducersTime);
		
		double j_fbr= c.get(CTag.FILE_BYTES_READ);
		double j_fbw=c.get(CTag.FILE_BYTES_WRITTEN);
		double j_spilled=c.get(CTag.SPILLED_RECORDS);
		double j_time= c.get(CTag.JOB_TOTAL_TIME);
		
		double shuffle= c.get(CTag.SHUFFLE);
		double c_input=c.get(CTag.COMBINE_INPUT_RECORDS);
		double c_output= c.get(CTag.COMBINE_OUTPUT_RECORDS);
		double r_input= c.get(CTag.REDUCE_INPUT_RECORDS);
		
		String out= conf+
					"\t"+records+
					
					"\t"+m_fbr+
					"\t"+m_fbw+
					"\t"+m_spilled+
					"\t"+m_time+
					
					"\t"+r_fbr+
					"\t"+r_fbw+
					"\t"+r_spilled+
					"\t"+r_time+
					
					"\t"+j_fbr+
					"\t"+j_fbw+
					"\t"+j_spilled+
					"\t"+j_time+
					
					"\t"+shuffle+
					"\t"+c_input+
					"\t"+c_output+
					"\t"+r_input;
		sb.append(out);
		return sb.toString();
	}
	
	void printResult(){
		assert resultJobs.size()>0;
		
		logger.info("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		
		System.out.println(printAllJobInfoHeader());
		
		for (JobInfo jobinfo : resultJobs) {
			System.out.println(printAllJobInfo(jobinfo));
		}
		
		System.out.println("\n-------------------+++++++++++++++++++++++++++-------------------");
	}
	
	final static int jobNumber=1;
	final static String jobDir ="/media/SHARE/phd/terasort/terajobs/cluster/"+jobNumber+"/jobs";
	final static String rackDir="/media/SHARE/phd/terasort/terajobs/cluster/"+jobNumber+"/rack";

	public static void main(String[] args) throws Exception {
		Sim_system.initialise();
		
		guiSim=false;
		String rackFile=RF.firstFileInDir(rackDir);
		
		String initRDir="results";
		initRDir ="/home/hadoop/django-projects/count/mrsim/results";
		


		HSimulator.resultDir =RF.newResultDir("results");
		logger.info("creat new Result dir "+ HSimulator.resultDir);
		
		//copy rack file to the result dir
		RF.copy(rackFile, RF.get(HSimulator.resultDir, RF.jsonRack));
		
		sim = new HSimulator();
		
		sim.setJobTracker(new HJobTracker("JobTracker", rackFile,null));
		
		sim.getJobTracker().createEntities(HSimulator.resultDir);
		sim.start();//start simulation thread
		logger.info("sim started");		
		
	
	}

	public static int totalLast=0;
	public static void buildRoutTables( HJobTracker tracker){
		List<Integer> list=new ArrayList<Integer>(HMachine.getAll());
		
		
		assert ! list.isEmpty();
		Collections.sort(list);
		for (int i = 0; i < list.size()-1; i++) {
			for (int j = i+1; j < list.size(); j++) {
				Integer src=list.get(i);
				Integer dist=list.get(j);
				String msg1=src+":"+dist;
//				logger.info(">>>>>>>>>>> : "+msg1);
				double size=1.0;
				HMachine.get(src).sim_msg(dist, size, HMachine.USER_NONE,HTAG.add_pending_shuffle.id, msg1);	
				
				String msg2=dist+":"+src;
//				logger.info(">>>>>>>>>>> : "+msg2);

				HMachine.get(dist).sim_msg(src, size, HMachine.USER_NONE,HTAG.add_pending_shuffle.id, msg2);	
				
				totalLast+=2;
			}
		}
		System.out.println("--total--"+ totalLast);
	}

}


