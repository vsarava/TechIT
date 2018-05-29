package techit.rest.advice;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.SignatureException;
import techit.model.User;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;
import techit.security.SecurityUtils;

@ControllerAdvice("techit.rest.controller")
public class RestControllerAdvice {

	@Autowired
	UserDao userDao;

	@ModelAttribute("currentUser")
	public Object authenticateUser(@RequestHeader(value = "Authorization", required = false) String bearerToken) {

		ObjectMapper mapper =new ObjectMapper();
		try {
			String token = bearerToken.contains("Bearer") ? bearerToken.split("Bearer")[1] : bearerToken;
			User user = mapper.convertValue(SecurityUtils.getUser(token), User.class);
			return user;
		} catch (NullPointerException ex) {
			throw new RestException(401, "Access denied! User not logged in.");
		} catch(SignatureException se) {
			throw new RestException(401, "User Unauthorized.");
		}
	}

}

