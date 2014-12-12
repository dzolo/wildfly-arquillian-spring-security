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

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Builder for easy creation of {@link UserDetails}.
 *
 * @author Ondřej Fibich <ondrej.fibich@gmail.com>
 */
class UserDetailsBuilder {

    /**
     * Create user detail with given username and encrypted given password.
     *
     * @param username username
     * @param password plain text password
     * @return user detail
     */
    public static UserDetails create(String username, String password) {
        final ShaPasswordEncoder encoder = new ShaPasswordEncoder(256);
        final String hashedPassword = encoder.encodePassword(password, username);
        return new UserDetailsImpl(username, hashedPassword);
    }

}
