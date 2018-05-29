package techit.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import techit.model.User;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;
import techit.security.SecurityUtils;

@RestController
public class LoginService {

	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam String username, @RequestParam String password) {
		User user = userDao.getUser(username);
		
		if (user != null && SecurityUtils.checkPassword(password, user.getHash()))
			return SecurityUtils.createJwtToken(user);

		throw new RestException(401, "Username/Password invalid");
	}
	
}
