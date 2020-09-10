package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	private StudentDbUtil studentDbUtil;
	
	//inject the connection pool object
	
	@Resource(name="jdbc/web_student_tracker")
	private DataSource dataSource;
	


	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		//can put custom work in this init
		//work we notrmally do on constuctors, can do in init
		
		//create out student db util... and pass in the conn pool/datasource
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		}
		catch(Exception exc) {
			throw new ServletException(exc);
		}
	}
	
	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		 // list the students ... in MVC fashion
		try {
			
			//read the "command" parameter
			String theCommand = request.getParameter("command");
			
			//route to the appropriate method
			if(theCommand == null) {
				theCommand ="LIST";
			}
			
			
			//list the students... in mvc fashion
			switch (theCommand) {
			
			
			case "LIST":
				listStudents(request, response);
				break;
		
				
			case "ADD":
				addStudent(request, response);
				break;
				
			case "UPDATE":
				updateStudent(request, response);
			break;
				
			
			
			case "LOAD":
				loadStudent(request, response);
				break;
			
			case "DELETE":
				deleteStudent(request, response);
				break;
				
			default:
				listStudents(request, response);
			
			
			}
			
			
			
			
//		listStudents(request, response);
//		//helper method-
		}
		catch (Exception exc) {
			throw new ServletException(exc);
		}
		
		
	}



	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// TODO Auto-generated method stub
		
		// read student id from form data
		String theStudentId = request.getParameter("studentId");
		
		// delete student form database
		studentDbUtil.deleteStudent(theStudentId);
		
		//send them back to "list students" page
		listStudents(request, response);
		
	}



	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		//read student info from form data
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
				
		
		//create a new student object
		Student theStudent = new Student(id, firstName, lastName, email);
		
		
		//perform update on database
		studentDbUtil.updateStudent(theStudent);
		
		//send them back to the "list students" page
		listStudents(request, response);
		
	}



	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception{
		// TODO Auto-generated method stub
		
		//read student id from form data
		String theStudentId = request.getParameter("studentId");
		
		
		//get student form database (db util)
				Student theStudent = studentDbUtil.getStudent(theStudentId);
		
		//place student in the request attribute
				request.setAttribute("THE_STUDENT", theStudent);
		
		//send to jsp page: update-student-form.jsp
		RequestDispatcher dispatcher = 
				request.getRequestDispatcher("/update-student-form.jsp");
		dispatcher.forward(request, response);
		
		
	}



	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		
		// read student info from form data
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		
		// create a new Student object
		Student theStudent = new Student(firstName, lastName, email);
		
	
		//add the student to the database
		studentDbUtil.addStudent(theStudent);
		// send back to main page (the student list)
		listStudents(request, response);
	}

	
	
	
	
	
	
	
	
	
	


	private void listStudents(HttpServletRequest request, HttpServletResponse response) 
	throws Exception {
		// TODO Auto-generated method stub
		
		// get students from db util
		//getStudents returns a list
		List<Student> students = studentDbUtil.getStudents();
		
		
		// add students to the request
		//set attribute name, object reference
		request.setAttribute("STUDENT_LIST", students);
		
		
		// send to JSP page (view)
		RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
		dispatcher.forward(request, response);
		
		
	}







}
