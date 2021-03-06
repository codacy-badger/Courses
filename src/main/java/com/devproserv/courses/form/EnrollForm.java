/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Vladimir
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.devproserv.courses.form;

import com.devproserv.courses.model.Response;
import com.devproserv.courses.model.Student;
import com.devproserv.courses.validation.VldNumber;
import com.devproserv.courses.validation.results.VldResult;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles enroll form.
 *
 * @since 0.5.0
 */
public final class EnrollForm {
    /**
     * Course JSP page file name.
     */
    public static final String STUDENT_PAGE = "/courses.jsp";

    /**
     * Login JSP page file name.
     */
    public static final String LOGIN_PAGE = "/login.jsp";

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EnrollForm.class);

    /**
     * Course handler.
     */
    private final CourseHandling handling;

    /**
     * Primary constructor.
     *
     * @param handling Course handler
     */
    public EnrollForm(final CourseHandling handling) {
        this.handling = handling;
    }

    /**
     * Checks all data user inputs in web forms.
     *
     * @param request HTTP request
     * @return Response containing a path to the same page in case
     *  of invalidated data or path to further page if validation is successful
     */
    public Response validate(final HttpServletRequest request) {
        final Optional<HttpSession> sessionopt = Optional.ofNullable(request.getSession(false));
        return sessionopt.map(session -> this.userResponse(request, session))
            .orElse(new Response(EnrollForm.LOGIN_PAGE, Collections.emptyMap()));
    }

    /**
     * Creates response if session is live and user can be null.
     *
     * @param request HTTP request
     * @param session HTTP session
     * @return Response
     */
    private Response userResponse(final HttpServletRequest request, final HttpSession session) {
        final Optional<Student> studentopt = Optional
            .ofNullable((Student) session.getAttribute(session.getId()));
        return studentopt.map(student -> this.fullResponse(student, request))
            .orElse(new Response(EnrollForm.LOGIN_PAGE, Collections.emptyMap()));
    }

    /**
     * Creates response taking into account live session and user.
     *
     * @param student Student
     * @param request HTTP request
     * @return Response
     */
    private Response fullResponse(final Student student, final HttpServletRequest request) {
        final String param = request.getParameter(this.handling.courseIdParameter());
        final VldResult vld = new VldNumber(param).validate();
        final Response response;
        if (vld.valid()) {
            response = this.validPath(student, param);
        } else {
            response = this.invalidPath(student, vld);
        }
        return response;
    }

    /**
     * Forms an error message in case of invalid path.
     *
     * @param student Student
     * @param result Validation result
     * @return Response
     */
    private Response invalidPath(final Student student, final VldResult result) {
        LOGGER.info("Invalid parameter for login {}", student.getLogin());
        final Response response = student.response();
        final Map<String, Object> payload = new HashMap<>(response.getPayload());
        payload.put(this.handling.errorMessageParameter(), result.reason().orElse(""));
        return new Response(response.getPath(), payload);
    }

    /**
     * Creates a course.
     *
     * @param student Student
     * @param param Number
     * @return Response
     */
    private Response validPath(final Student student, final String param) {
        return this.handling.response(Integer.parseInt(param), student);
    }
}
