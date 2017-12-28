/*******************************************************************************
 * Copyright 2017 Rafael López Torres
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.geowe.datastore.configuration.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestErrorHandler {

	private static final Logger logger = Logger.getLogger(RestErrorHandler.class);

	public RestErrorHandler() {
		super();
	}

	@ExceptionHandler(value = { IllegalArgumentException.class, ConstraintViolationException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, Object> processBadRequest(HttpServletRequest req, Exception ex) {
		final Map<String, Object> response = getErrorResponse(req, ex);
		return response;
	}


	private Map<String, Object> getErrorResponse(HttpServletRequest req, Exception ex) {
		final Map<String, Object> response = new HashMap<>();
		response.put("timestamp", System.currentTimeMillis());
		setStatusAndError(ex, response);
		response.put("exception", ex.getClass());
		if (ex instanceof ConstraintViolationException) {
			setMessage(response, (ConstraintViolationException) ex);
		} else {
			response.put("message", ex.getMessage());
		}
		response.put("path", req.getRequestURI().substring(req.getContextPath().length()));
		logger.error("RestErrorHandler captured exception: " + response);
		return response;
	}

	private void setMessage(Map<String, Object> response, ConstraintViolationException ex) {
		final StringBuilder errors = new StringBuilder("");
		ex.getConstraintViolations().stream()
				.forEach(violation -> errors.append(violation.getPropertyPath()).append(": ")
						.append(violation.getMessage()).append(": ").append(violation.getInvalidValue())
						.append(System.lineSeparator()));
		response.put("message", errors.toString());
	}

	private void setStatusAndError(Exception ex, final Map<String, Object> response) {
		if (ex instanceof IllegalArgumentException || ex instanceof ConstraintViolationException) {
			response.put("status", HttpStatus.BAD_REQUEST.value());
			response.put("error", HttpStatus.BAD_REQUEST.name());
		} else if (ex instanceof IllegalArgumentException) {
			response.put("status", HttpStatus.CONFLICT.value());
			response.put("error", HttpStatus.CONFLICT.name());
		} else if (ex instanceof IllegalArgumentException) {
			response.put("status", HttpStatus.NOT_FOUND.value());
			response.put("error", HttpStatus.NOT_FOUND.name());
		}
	}
}
