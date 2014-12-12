/*
 * The MIT License
 *
 * Copyright 2014 Ondřej Fibich.
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

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.File;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test of page HTTP basic auth.
 *
 * @author Ondřej Fibich <ondrej.fibich@gmail.com>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class AuthTest {

    /**
     * Test deployment.
     *
     * @return web archive
     */
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(UserDetailsServiceImpl.class.getPackage())
                .addAsResource("spring-security.xml")
                .addAsWebResource(new File("src/main/webapp/index.jsp"))
                .addAsWebResource(new File("src/main/webapp/public.jsp"))
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
    }

    @ArquillianResource
    private URL baseURL;

    @Before
    public void beforeTest() {
        assertNotNull(baseURL);
    }

    @Test
    public void testPublic() throws UnirestException {
        HttpResponse<JsonNode> resp = Unirest.get(baseURL + "public.jsp")
                    .header("accept", "application/json")
                    .asJson();
        assertNotNull(resp);
        assertEquals(200, resp.getStatus());
        assertTrue(resp.getBody().isArray());
        assertEquals(1, resp.getBody().getArray().length());
        assertTrue(resp.getBody().getArray().getJSONObject(0).has("user"));
        assertTrue(resp.getBody().getArray().getJSONObject(0).isNull("user"));
    }

    @Test
    public void testNoLogin() throws UnirestException {
        HttpResponse<String> resp = Unirest.get(baseURL.toString())
                .header("accept", "application/json")
                .asString();
        assertNotNull(resp);
        assertEquals(401, resp.getStatus());
    }

    @Test
    public void testInvalidLogin() throws UnirestException {
        HttpResponse<String> resp = Unirest.get(baseURL.toString())
                .basicAuth("ned", "lannister")
                .header("accept", "application/json")
                .asString();
        assertNotNull(resp);
        assertEquals(401, resp.getStatus());
    }

    @Test
    public void testValidLogin() throws UnirestException {
        HttpResponse<JsonNode> resp = Unirest.get(baseURL.toString())
                .basicAuth("ned", "stark")
                .header("accept", "application/json")
                .asJson();
        assertNotNull(resp);
        assertEquals(200, resp.getStatus());
        assertTrue(resp.getBody().isArray());
        assertEquals(1, resp.getBody().getArray().length());
        assertTrue(resp.getBody().getArray().getJSONObject(0).has("user"));
        assertEquals("ned", resp.getBody().getArray().getJSONObject(0).get("user"));
    }

}