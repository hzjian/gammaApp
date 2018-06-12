package com.cellinfo.exception;

import org.springframework.security.core.AuthenticationException;

public class GroupDisableException extends AuthenticationException {

	public GroupDisableException(String msg) {
		super(msg);
	}

	public GroupDisableException(String msg, Throwable t) {
		super(msg, t);
	}
}
