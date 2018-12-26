/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Vladimir
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

package com.devproserv.courses.command;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines application commands.
 *
 * @since 1.0.0
 */
public class Commands {
    /**
     * Login command name.
     */
    private static final String COMMAND_LOGIN = "login";

    /**
     * Sign up command name.
     */
    private static final String COMMAND_SIGNUP = "signup";

    /**
     * Logout command name.
     */
    private static final String COMMAND_LOGOUT = "logout";

    /**
     * Enroll command name.
     */
    private static final String COMMAND_SUBSCRIBE = "subscribe";

    /**
     * Unroll command name.
     */
    private static final String COMMAND_UNROLL = "unsubscribe";

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
        Commands.class
    );

    /**
     * Commands.
     */
    private final Map<String, Supplier<Command>> map = new HashMap<>();

    /**
     * Builds commands. Provides lazy initialization.
     * @return This instance
     */
    public Commands build() {
        this.map.put(Commands.COMMAND_SIGNUP,    SignUp::new);
        this.map.put(Commands.COMMAND_LOGIN,     Login::new);
        this.map.put(Commands.COMMAND_LOGOUT,    Logout::new);
        this.map.put(Commands.COMMAND_SUBSCRIBE, Enroll::new);
        this.map.put(Commands.COMMAND_UNROLL,    Unroll::new);
        return this;
    }

    /**
     * Delivers a path of a page defined by given request.
     *
     * @param request HTTP request
     * @return String with a path defined by parameter "command" in request
     */
    public String path(final HttpServletRequest request) {
        final String par = request.getParameter("command");
        Command command = this.map.get(par).get();
        if (command == null) {
            LOGGER.warn("Invalid command given!");
            command = new NotFound();
        }
        return command.path(request);
    }
}
