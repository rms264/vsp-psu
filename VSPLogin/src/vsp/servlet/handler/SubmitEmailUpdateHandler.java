package vsp.servlet.handler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vsp.exception.SqlRequestException;
import vsp.exception.ValidationException;
import vsp.utils.Validate;

public class SubmitEmailUpdateHandler extends BaseServletHandler implements
		ServletHandler {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String userName = request.getRemoteUser();
			if (userName == null || userName.isEmpty()) {
				userName = (String) request.getSession().getAttribute(
						"userName");
			}
			if (userName != null && !userName.isEmpty()) {
				String email = request.getParameter("email");
				String verify_email = request.getParameter("verifyemail");

				List<String> errors = new ArrayList<String>();
				if (email.equals(verify_email)) {
					try {
						if (!Validate.emailExistsInDb(email)) {
							if (Validate.validateEmail(email)) {

								vsp.updateUserEmail(userName, email);
								request.setAttribute("emailUpdate",
										"Email has been successfully changed");
								/*
								 * AccountData userAccount =
								 * vsp.getAccountInfo(userName);
								 * request.setAttribute("userAccount",
								 * userAccount); dispatchUrl = "UserInfo.jsp";
								 */
								dispatchUrl = "UpdateEmail.jsp";
							} else {
								errors.add("Incorrect Email address.");
								request.setAttribute("errors", errors);
								dispatchUrl = "UpdateEmail.jsp";
							}
						} else {
							errors.add("Email address already exists.");
							request.setAttribute("errors", errors);
							dispatchUrl = "UpdateEmail.jsp";
						}
					} catch (SQLException | SqlRequestException
							| ValidationException e) {
						errors.add(e.getMessage());
						request.setAttribute("errors", errors);
						dispatchUrl = "UpdateEmail.jsp";
					}
				} else {
					errors.add("Email addresses do not match.");
					request.setAttribute("errors", errors);
					dispatchUrl = "UpdateEmail.jsp";
				}
			} else {
				errors.add("Unknown user name");
				request.setAttribute("errors", errors);
				dispatchUrl = "updateEmail.jsp";
			}
		} finally {
			request.getSession().removeAttribute("userName");
		}
	}
}
