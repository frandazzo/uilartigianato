package applica.feneal.admin.controllers.base.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Applica
 * User: Ciccio Randazzo
 * Date: 10/11/15
 * Time: 3:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class RemoteAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException {
        try {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}