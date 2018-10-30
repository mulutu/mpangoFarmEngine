package com.mpangoEngine.core.validator;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.mpangoEngine.core.model.MyUser;
import com.mpangoEngine.core.service.UserService;
import com.mpangoEngine.core.service.impl.SecurityServiceImpl;

@Component
public class UserValidator implements Validator {

	private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);

	@Override
	public boolean supports(Class<?> aClass) {
		return MyUser.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		MyUser user = (MyUser) o;

		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
		//if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
			//errors.rejectValue("username", "Size.userForm.username");
		//}
		//if (userService.findByUsername(user.getUsername()) != null) {
			//errors.rejectValue("username", "Duplicate.userForm.username");
		//}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
		if (user.getPassword().length() < 4 || user.getPassword().length() > 32) {
			errors.rejectValue("password", "Size.userForm.password");
		}

		//if (!user.getPasswordConfirm().equals(user.getPassword())) {
			//errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
		//}
	}
}