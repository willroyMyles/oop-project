package developement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import application.Main;
import domain.Course;
import domain.Programme;
import domain.PsuedoProgram;
import domain.PsuedoStudent;
import domain.Staff;
import domain.Student;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class FileProcess{


	private ObjectOutputStream writer = null;
	private ObjectInputStream reader = null;
	private List<Staff> staffList = null;


	public Connection connection = Main.connection;




	public FileProcess(){

	}

	public void saveStaff(Staff s){

		try{
			staffList = new ArrayList<>();
			List<Staff> slist = readStaff();
			writer = new ObjectOutputStream(new FileOutputStream("staff"));		
			slist.add(s);

			for(Staff e : slist){
				writer.writeObject(e);
				e.display();
			}


		}

		catch(IOException ex){

		}
		finally{
			try{

				if(writer != null)
					writer.close();
			}catch(IOException ex){

			}
		}


	}


	@SuppressWarnings("finally")
	public List<Staff> readStaff(){
		try{
			reader = new ObjectInputStream(new FileInputStream("staff"));
			staffList = new ArrayList<>();


			while(true){
				Staff temp = (Staff) reader.readObject();
				temp.display();

				staffList.add(temp);
			} 
		}catch(IOException ex){
			//	staffList = new ArrayList<>();
			return staffList;
		}catch(ClassNotFoundException c){

		}catch(NullPointerException y){
			return staffList;
		}

		finally{
			try{
				if(reader!=null)
					reader.close();
			}catch(IOException ex){


			}

			return staffList;
		}

	}



	public int studNum(){
		int   num = 0;

		try{			 
			//	 connection = sqliteConnection.dbConnector();


			PreparedStatement pst = Main.connection.prepareStatement("select count(*) from stud");


			ResultSet resultSet = pst.executeQuery();

			if(resultSet.next())
				num = resultSet.getInt(1);

			num++;



			pst.close();
			return num;

		}
		catch(Exception se){
			//			 se.printStackTrace();
		}

		return num;

	}

	public void saveStudent(Student s){

		try {


			//connection = sqliteConnection.dbConnector();
			String query = "insert into stud (IdNumber,FirstName,LastName,Address,DateEnrolled,Password,ProgramCode,ContactNumber,EnrolmentStatus,noc) values(?,?,?,?,?,?,?,?,?,?)";
			//	@SuppressWarnings("static-access")
			PreparedStatement pst = Main.connection.prepareStatement(query);

			pst.setString(1,s.getStudentId() );
			pst.setString(2, s.getFname() );
			pst.setString(3, s.getLname());
			pst.setString(4,s.getAddress());
			pst.setString(5,s.getDate_enrolled() );
			pst.setString(6, s.getPassword());
			pst.setString(7,s.getProgrammeCode());
			pst.setString(8,s.getContactNumber());
			pst.setString(9,String.valueOf(s.getEnrolmentStatus()));
			pst.setString(10, String.valueOf(s.getNoc()));
			System.out.println("what?!");
			pst.execute();
			JOptionPane.showMessageDialog(null,"Record Added Successfully");

			pst.close();

		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("finally")
	public List<Student> readStudent() {
		// TODO Auto-generated method stub

		///studentList = new ArrayList<>();
		List<Student> studentList = null;
		try{
			reader = new ObjectInputStream(new FileInputStream("student"));
			studentList = new ArrayList<>();


			while(true){
				Student temp = (Student) reader.readObject();		
				temp.display();
				studentList.add(temp);
			} 
		}catch(IOException ex){
			//	studentList = new ArrayList<>();
			return studentList;
		}catch(ClassNotFoundException c){

		}catch(NullPointerException y){
			return studentList;
		}

		finally{
			try{
				if(reader!=null)
					reader.close();
			}catch(IOException ex){


			}

			return studentList;
		}


	}

	@SuppressWarnings("finally")
	public int validateStaff(String pass, String id) {
		// TODO Auto-generated method stub
		try{
			reader = new ObjectInputStream(new FileInputStream("staff"));		
			while(true){
				Staff temp = (Staff) reader.readObject();		
				//	temp.display();
				if(pass.equals(temp.getPassword()) && id.equals(temp.getStaffId())){

					System.out.println(pass + " "+id);
					System.out.println(temp.getPassword() + " "+temp.getStaffId());
					return 1;
				}					
			}

		}catch(IOException ex){

			if(reader!=null)
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return 3;
		}catch(ClassNotFoundException c){
			return 4;
		}catch(NullPointerException y){
			return 5;
		}



	}

	public void saveProgram(Programme p) {

		try {


			//	connection = sqliteConnection.dbConnector();
			String query = "insert into pro (code,Name,maximum,Award,Accreditation) values(?,?,?,?,?)";
			PreparedStatement pst = Main.connection.prepareStatement(query);

			pst.setString(1, p.getCode());
			pst.setString(2, p.getCourseName());
			pst.setInt(3, p.getMaximumNumberOfCourses());
			pst.setString(4, p.getAward());
			pst.setString(5, p.getAccreditation());

			pst.execute();
			JOptionPane.showMessageDialog(null,"Record Added Successfully");

			pst.close();

		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}




	public boolean validateStudent(String user, String pass) {
		// TODO Auto-generated method stub

		//	connection = sqliteConnection.dbConnector();

		String query = "select * from stud where IdNumber = '"+user+"'and Password ='"+pass+"'";
		try {
			PreparedStatement pst = Main.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			if(!rs.isClosed()){
				if(user.equals(rs.getString(1)) && pass.equals(rs.getString(6))){

					pst.close();
					rs.close();
					return true;
				}
				else{
					pst.close();
					rs.close();
					return false;
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}

	public ObservableList<PsuedoStudent> getSTudentList() {

		List<PsuedoStudent> list = new ArrayList<PsuedoStudent>();

		//		connection = sqliteConnection.dbConnector();		
		String query = "select * from stud";
		try{
			PreparedStatement pst = Main.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(!rs.isClosed()){
				while(rs.next()){

					PsuedoStudent S = new PsuedoStudent();
					S.setStudentId(rs.getString(1));
					S.setFname(rs.getString(2));
					S.setLname(rs.getString(3));
					S.setAddress(rs.getString(4));
					S.setDateEnrolled(rs.getString(5));
					//S.setDateEnrolled("test");
					S.setContact(rs.getString(8));

					if(rs.getInt(9)== 0)
						S.setEnrolmentStatus("not enrolled");
					else
						S.setEnrolmentStatus("enrolled");
					list.add(S);					
				}


			}

			ObservableList<PsuedoStudent> data = FXCollections.observableList(list);
			System.out.println("completed");
			return data;


		}catch(Exception e){
			e.printStackTrace();
		}


		return null;
	}



	public ObservableList<Object> getProList(int a) {
		// TODO Auto-generated method stub
		List<Object> list = new ArrayList<>();

		//	connection = sqliteConnection.dbConnector();		
		String query = "select * from pro";
		try{
			PreparedStatement pst = Main.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(!rs.isClosed()){
				while(rs.next()){

					PsuedoProgram S = new PsuedoProgram();
					S.setCode(rs.getString(1));
					S.setName(rs.getString(2));
					S.setMNC(rs.getString(3));
					S.setAward(rs.getString(4));
					S.setAccreditation(rs.getString(5));

					if(a ==1)
						list.add(S.getName());
					else
					list.add(S);					
				}


			}

			
			ObservableList<Object> proData = FXCollections.observableList(list);
			System.out.println("completed");
			return proData;


		}catch(Exception e){
			e.printStackTrace();
		}


		return null;
	}

	
	
	public ObservableList<Object> courseList(int a) {
		List<Object> list = new ArrayList<Object>();

		//	connection = sqliteConnection.dbConnector();		
		try{
			String query = "select * from courses";

			PreparedStatement pst = Main.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(!rs.isClosed()){
				while(rs.next()){

					PsuedoCourse S = new PsuedoCourse();
					S.setCode(rs.getString(1));
					S.setName(rs.getString(2));
					S.setCredits(rs.getString(3));
					S.setCost(rs.getString(5));
					S.setDescription(rs.getString(6));
					S.setPreRequisites(rs.getString(5));


					if(a == 1)
						list.add(S.getName());	
					else
						list.add(S);
				}


			}

			ObservableList<Object> proData = FXCollections.observableList(list);
			System.out.println("completed");
			return proData;


		}catch(Exception e){
			e.printStackTrace();
		}


		return null;
	}

	public Course getCourse(String value) {
		// TODO Auto-generated method stub


		PreparedStatement pst;
		try {
			pst = Main.connection.prepareStatement("select * from courses");
			ResultSet rs = pst.executeQuery();

			if(!rs.isClosed()){
				while(rs.next()){

					if(rs.getString(2).equals(value)){
						Course c = new Course();
						c.setCode(rs.getString(1));
						c.setName(rs.getString(2));
						c.setDescription(rs.getString(6));
						c.setCredits(rs.getString(3));
						c.setPreRequisite(rs.getString(4));
						c.setCost(rs.getString(5));

						return c;
					}


				}

			}

			Course c = new Course();

			return c;



		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




		return null;
	}

	public void saveCourseStud(Course c, String userStud) {
		// TODO Auto-generated method stub
		PreparedStatement pst;


		try {
			
			pst = Main.connection.prepareStatement("select * from stud where idNumber = '"+userStud+"'");
			ResultSet rs1 = pst.executeQuery();
			int noc = Integer.valueOf(rs1.getString(10));
		
			pst = Main.connection.prepareStatement("delete from studCourse where code = 'na'");
			pst.execute();
			
			pst = Main.connection.prepareStatement("select count(*) AS total from studCourse  where studId = '"+userStud+"'  ");
			ResultSet rs2 = pst.executeQuery();
			System.out.println(rs2.getInt("total"));

			if( Integer.valueOf(rs2.getInt("total")) >= noc
			){
				return;
				
			}
			
			
			
			
			pst = Main.connection.prepareStatement("insert into studCourse (studId, Code) values('"+userStud+ "', '"+c.getCode()+"')");
			
			
			
			pst.execute();	
			pst.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ObservableList<PsuedoCourse> getStudCourseData( String userStud) {
		// TODO Auto-generated method stub
		try{
			String query = "select * from (SELECT * FROM courses c , studCourse sc where sc.code = c.code) where studId = '"+userStud+ "'";
			ArrayList<PsuedoCourse> list= null;
			list = new ArrayList<>();

			PreparedStatement pst = Main.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(!rs.isClosed()){
				while(rs.next()){

					PsuedoCourse S = new PsuedoCourse();
					S.setCode(rs.getString(1));
					S.setName(rs.getString(2));
					S.setCredits(rs.getString(3));
					S.setCost(rs.getString(5));
					//	S.setDescription(rs.getString(5));
					//	S.setPreRequisites(rs.getString(1));

					list.add(S);			
				}


			}

			ObservableList<PsuedoCourse> proData = FXCollections.observableList(list);
			System.out.println("completed");
			pst.close();
			return proData;


		}catch(Exception e){
			e.printStackTrace();
		}





		return null;
	}

	public void setEnroll(String userStud) {
		// TODO Auto-generated method stub

		String query = "update stud set EnrolmentStatus = 0 where idNumber = '"+userStud+"'";
		PreparedStatement pst;
		try {
			pst = Main.connection.prepareStatement(query);
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	public ObservableList<PsuedoCourse> removeCourse(String userStud, PsuedoCourse selectedCourse) {
		// TODO Auto-generated method stub

		String query = "delete  from studCourse where code ='"+selectedCourse.getCode()+"' and studId = '"+userStud+"'";
		PreparedStatement pst,pst1;
		
		List list = new ArrayList();

		try {
			pst = Main.connection.prepareStatement(query);
			pst.execute();
			pst.close();
			String query1 = "select * from (SELECT * FROM courses c , studCourse sc where sc.code = c.code) where studId = '"+userStud+"'";
			pst1 = Main.connection.prepareStatement(query1);

			ResultSet rs = pst1.executeQuery();


			if(!rs.isClosed()){
				while(rs.next()){

					PsuedoCourse S = new PsuedoCourse();
					S.setCode(rs.getString(1));
					S.setName(rs.getString(2));
					S.setCredits(rs.getString(3));
					S.setCost(rs.getString(5));
					//	S.setDescription(rs.getString(5));
					//	S.setPreRequisites(rs.getString(1));

					System.out.println(S.getName());
					list.add(S);			

				}


			}

			ObservableList<PsuedoCourse> proData = FXCollections.observableList(list);
			System.out.println("completed");
			//pst.close();
			return proData;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;




	}

	public int genFeeBreak(String userStud) {
		// TODO Auto-generated method stub
		
		try{
			
			PreparedStatement pst1;
			int t = 0;
			
			String query = "select * from (SELECT * FROM courses c , studCourse sc, stud s where sc.code = c.code) where idNumber = '"+userStud+"'";
		pst1 = Main.connection.prepareStatement(query);

		ResultSet rs = pst1.executeQuery();


		if(!rs.isClosed()){
			while(rs.next()){

			t = Integer.valueOf(rs.getString(5)) + t;
		//	t = t+t;
			System.out.println(t);
			}

return t;
		}

	
		System.out.println("completed");
		//pst.close();
		return t;
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
		
		
		return 0;
	}

	public ObservableList getGFB(String userStud) {
		try{
			String query = "select * from (SELECT * FROM courses c , studCourse sc, stud s where sc.code = c.code) where idNumber = '"+userStud+"'";
			ArrayList<PsuedoCourse> list= null;
			list = new ArrayList<>();

			PreparedStatement pst = Main.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(!rs.isClosed()){
				while(rs.next()){

					PsuedoCourse S = new PsuedoCourse();
					S.setCode(rs.getString(1));
					S.setName(rs.getString(2));
				//	S.setCredits(rs.getString(3));
					S.setCost(rs.getString(5));
					
					S.setStudFirst(rs.getString(10));
					S.setStudId(rs.getString(9));
					S.setStudLast(rs.getString(11));
					
					
					//	S.setDescription(rs.getString(5));
					//	S.setPreRequisites(rs.getString(1));

					list.add(S);			
				}


			}

			ObservableList<PsuedoCourse> proData = FXCollections.observableList(list);
			System.out.println("completed");
			pst.close();
			return proData;


		}catch(Exception e){
			e.printStackTrace();
		}





		return null;
	}

	public Programme getProgram(String val) {
		// TODO Auto-generated method stub
		
		try{
			
			String query = "select * from pro where name = '"+val+"'";
			PreparedStatement pst = Main.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();
			
			System.out.println(val);
			
			if(!rs.isClosed()){
				while(rs.next()){
			Programme p = new Programme();
			
			p.setCode(rs.getString(1));
			p.setCourseName(rs.getString(2));
			p.setMaximumNumberOfCourses(Integer.valueOf(rs.getString(3)));
			p.setAward(rs.getString(4));
			p.setAccreditation(rs.getString(5));
			
			return p;
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}


	
	public ObservableList getProListBox() {
		// TODO Auto-generated method stub
		
		
		List<String> list = new ArrayList<>();

		//	connection = sqliteConnection.dbConnector();		
		try{
			String query = "select * from pro";

			PreparedStatement pst = Main.connection.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(!rs.isClosed()){
				while(rs.next()){

					Programme S = new Programme();
					S.setCode(rs.getString(1));
					S.setCourseName(rs.getString(2));
				//	S.setMNC(rs.getString(3));
					S.setAward(rs.getString(4));
					S.setAccreditation(rs.getString(5));


				
						list.add(S.getCourseName());	
					
				}


			}
			
			System.out.println("grrr");

			ObservableList<String> proData = FXCollections.observableList(list);
			System.out.println("completed");
			return proData;


		}catch(Exception e){
			e.printStackTrace();
		}

		
		return null;
	}





}

