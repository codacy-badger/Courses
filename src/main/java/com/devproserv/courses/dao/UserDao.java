package com.devproserv.courses.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.devproserv.courses.model.Administrator;
import com.devproserv.courses.model.Lecturer;
import com.devproserv.courses.model.Student;
import com.devproserv.courses.model.User;
import com.devproserv.courses.model.User.Role;

import static com.devproserv.courses.config.MainConfig.SELECT_USER_SQL;
import static com.devproserv.courses.config.MainConfig.SELECT_LOGIN_SQL;
import static com.devproserv.courses.config.MainConfig.INSERT_USER_SQL;
import static com.devproserv.courses.config.MainConfig.INSERT_STUDENT_SQL;
import static com.devproserv.courses.config.MainConfig.GET_USER_FIELDS_SQL;

/**
 * Provides CRUD methods for communication between application and database.
 * Implements operations with user tables (users and students).
 * 
 * @author vovas11
 */
public class UserDao {

    private Connection connection;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    // Signing up methods

    /**
     * Checks if the specified login exists in the database. The method is used during sign up procedure.
     * 
     * @param login login (user name)
     * @return {@code true} if the user exists and {@code false} if does not
     */
    public boolean loginExists(String login) {
        try (PreparedStatement prepStmt = connection.prepareStatement(SELECT_LOGIN_SQL)) {
            prepStmt.setString(1, login);
            ResultSet result = prepStmt.executeQuery();
            /* returns true if result is not empty */
            return result.next();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Request to database failed", e);
        }
        return true; // less changes in the database if something is wrong
    }

    /**
     * Creates new instance of {@link Student} with given parameters,
     * checks if the user with specified login exists in the database, and if no
     * inserts the user into the database (tables 'users' and 'students')
     * 
     * @param login argument representing login
     * @param password argument representing password
     * @param firstName argument representing first name
     * @param lastName argument representing last name
     * @param faculty argument representing faculty
     * 
     * @return {@code true} if the user has been created successfully and {@code false} if is not
     */
    public boolean createUser(String login, String password, String firstName, String lastName, String faculty) {
        Student student = new Student();
        student.setLogin(login);
        student.setPassword(password);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setRole(Role.STUD);
        student.setFaculty(faculty);
        
        return insertUser(student);
    }

    /**
     * Executes request into the database (tables 'users' and 'students') to insert the current user.
     * 
     * @param user the current user
     * @return {@code true} if the user has been created successfully and {@code false} if is not
     */
    private boolean insertUser(User user) {
        Student student;
        if (user instanceof Student) {
            student = (Student) user;
            try (
                PreparedStatement prepStmtOne = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement prepStmtTwo = connection.prepareStatement(INSERT_STUDENT_SQL)
            ) {
                /* inserts data into the table 'users' */
                prepStmtOne.setString(1, student.getFirstName());
                prepStmtOne.setString(2, student.getLastName());
                prepStmtOne.setString(3, student.getLogin());
                prepStmtOne.setString(4, student.getPassword());
                prepStmtOne.setString(5, student.getRole().toString());
                /* returns false if inserting fails */
                if (prepStmtOne.executeUpdate() == 0) return false;
                /* returns autogenerated ID and assigns it to the user instance */
                ResultSet generatedKey = prepStmtOne.getGeneratedKeys();
                if (generatedKey.next()) {
                    student.setId(generatedKey.getInt(1));
                }
                /* inserts data into the table 'students' */
                prepStmtTwo.setInt(1, student.getId());
                prepStmtTwo.setString(2, student.getFaculty());
                return prepStmtTwo.executeUpdate() != 0;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Request to database failed", e);
            }
        }
        return false;
    }

    // Login methods

    /**
     * Checks if the user with specified login and password exists in the database.
     * The method is used during login procedure
     * 
     * @param login login
     * @param password password
     *
     * @return {@code true} if the user exists
     */
    public boolean userExists(String login, String password) {
        try (
            PreparedStatement prepStmt = connection.prepareStatement(SELECT_USER_SQL)
        ) {
            prepStmt.setString(1, login);
            prepStmt.setString(2, password);
            ResultSet result = prepStmt.executeQuery();
            /* returns true if the result query contains at least one row */
            return result.next();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Request to database failed", e);
        }
        return false;
    }

    /**
     * Creates new instance of {@link User} with given login and password.
     * Login and password should match to ones in the database
     * (the method should be called after {@code userExists} method.
     * 
     * @param login argument representing login.
     * @param password argument representing password
     * 
     * @return new instance of Student, Lecturer or Administrator
     */
    public User getUser(String login, String password) {
        User user = null;
        switch (getUserRole(login, password)) {
        case STUD:
            Student student = new Student();
            student.setLogin(login);
            student.setPassword(password);
            student.setRole(Role.STUD);
            getStudentFields(student);
            user = student;
            break;
        case LECT:
            Lecturer lecturer = new Lecturer();
            lecturer.setLogin(login);
            lecturer.setPassword(password);
            lecturer.setRole(Role.LECT);
            user = lecturer;
            break;
        case ADMIN:
            Administrator admin = new Administrator();
            admin.setLogin(login);
            admin.setPassword(password);
            admin.setRole(Role.ADMIN);
            user = admin;
            break;
        }
        return user;
    }

    /**
     * Executes query to database to define user role in the system.
     * 
     * @param login login
     * @param password password
     * @return {@code true} if the user exists
     */
    private Role getUserRole(String login, String password) {
        Role role = Role.STUD;
        try (
            PreparedStatement prepStmt = connection.prepareStatement(SELECT_USER_SQL)
        ) {
            prepStmt.setString(1, login);
            prepStmt.setString(2, password);
            ResultSet result = prepStmt.executeQuery();
            if (result.next()) {
                role = Role.valueOf(result.getString(6));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Request to database failed", e);
        }
        return role;
    }

    /**
     * Adds rest of the fields into the object.
     *
     * @param student the current user
     */
    private void getStudentFields(Student student) {
        try (
            PreparedStatement prepStmt = connection.prepareStatement(GET_USER_FIELDS_SQL)
        ) {
            prepStmt.setString(1, student.getLogin());
            ResultSet result = prepStmt.executeQuery();
            while (result.next()) {
                student.setId(result.getInt(1));
                student.setFirstName(result.getString(2));
                student.setLastName(result.getString(3));
                student.setFaculty(result.getString(4));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Request to database failed", e);
        }
    }
}