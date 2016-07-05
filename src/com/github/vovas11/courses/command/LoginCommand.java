package com.github.vovas11.courses.command;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.github.vovas11.courses.dao.*;

/**
 * {@code LoginCommand} handles access for the existing user
 * 
 * @author vovas11
 * @see DaoFactory
 * @see com.github.vovas11.courses.dao.UserDao
 * @see com.github.vovas11.courses.dao.CourseDao
 * 
 */
public class LoginCommand implements Command {
    /**
     * Defines if the name and password of the user exists and if yes,
     * returns the page for the user. If not, returns the home page
     *
     * @param   request   HTTP request
     * @return the the name of the page the server returns to the client
     */
    @Override
    public String execute(HttpServletRequest request) {
	// gets parameters from the HTTP request
	String login = request.getParameter("login");
	String password = request.getParameter("password");
	
	// gets the link to the DaoFactory and UserDao and CourseDao
	DaoFactory daoFactory = DaoFactory.getInstance();	
	UserDao users = daoFactory.getUserDao();
	CourseDao courses = daoFactory.getCourseDao();
	
	// gets the link to the current session or creates new one
	HttpSession session = request.getSession();
	User user = (User) session.getAttribute(session.getId());
	
	if (user == null) {
	    user = new User();
	    user.setLogin(login);
	    user.setPassword(password);
	    //getting all fields for the object from DB
	    users.getFieldsForUser(user);
	    session.setAttribute(session.getId(), user);
	}

	if (users.isExist(user)) {
	    // Information about the logged user
	    request.setAttribute("user", user);
	    // Getting subscribed courses
	    List<Course> subscribedCourses = courses.getSubscribedCourses(user);
	    request.setAttribute("subscrcourses", subscribedCourses);
	    
	    // Getting all courses from the DB
	    List<Course> courseList = courses.getAvailableCourses(user);
	    request.setAttribute("courses", courseList);
	    return "/courses.jsp";
	} else {
	    return "/index.html";
	}
    }
}
