/*
 * The MIT License
 *
 * Copyright 2014 Ondřej Fibich <ondrej.fibich@gmail.com>.
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.kopaid.example.wildfly.spring.security;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * User account provider and simple static storage.
 *
 * @author Ondřej Fibich <ondrej.fibich@gmail.com>
 */
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger LOG =
            Logger.getLogger(UserDetailsServiceImpl.class.getName());

    /**
     * Static password storage
     */
    private static final Map<String, UserDetails> users = new HashMap<>();

    // user accounts
    static {
        users.put("ned", UserDetailsBuilder.create("ned", "stark"));
        users.put("jaime", UserDetailsBuilder.create("jaime", "lannister"));
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        if (!users.containsKey(username)) {
            LOG.log(Level.INFO, "user with username '{0}' not found", username);
            throw new UsernameNotFoundException(
                    Objects.toString(username) + "not found");
        }
        LOG.log(Level.INFO, "user with username '{0}' found", username);
        return users.get(username);
    }

}
