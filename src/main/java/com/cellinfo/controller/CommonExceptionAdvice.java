package com.cellinfo.controller;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cellinfo.entity.Result;
import com.cellinfo.exception.ControllerException;
import com.cellinfo.exception.ServiceException;
import com.cellinfo.util.ExceptionDesc;
import com.cellinfo.utils.ResultUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@ControllerAdvice
@ResponseBody
public class CommonExceptionAdvice {

  private static Logger logger = LoggerFactory.getLogger(CommonExceptionAdvice.class);

  /**
   * 400 - Bad Request
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public Result<String> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
    logger.error("缺少请求参数", e);
    return ResultUtil.error(400,"required_parameter_is_not_present");
  }

  /**
   * 400 - Bad Request
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public Result<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    logger.error("参数解析失败", e);
    return ResultUtil.error(400,"could_not_read_json");
  }

  /**
   * 400 - Bad Request
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Result<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    logger.error("参数验证失败", e);
    BindingResult result = e.getBindingResult();
    FieldError error = result.getFieldError();
    String field = error.getField();
    String code = error.getDefaultMessage();
    String message = String.format("%s:%s", field, code);
    return ResultUtil.error(400,message);
  }

  /**
   * 400 - Bad Request
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  public Result<String> handleBindException(BindException e) {
    logger.error("参数绑定失败", e);
    BindingResult result = e.getBindingResult();
    FieldError error = result.getFieldError();
    String field = error.getField();
    String code = error.getDefaultMessage();
    String message = String.format("%s:%s", field, code);
    return ResultUtil.error(400,message);
  }

  /**
   * 400 - Bad Request
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public Result<String> handleServiceException(ConstraintViolationException e) {
    logger.error(ExceptionDesc.CONSTRAINT_VIOLATION_EXCEPTION, e);
    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
    ConstraintViolation<?> violation = violations.iterator().next();
    String message = violation.getMessage();
    return ResultUtil.error(400,ExceptionDesc.CONSTRAINT_VIOLATION_EXCEPTION + message);
  }

  /**
   * 400 - Bad Request
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ValidationException.class)
  public Result<String> handleValidationException(ValidationException e) {
    logger.error(ExceptionDesc.VALIDATION_EXCEPTION, e);
    return ResultUtil.error(400,ExceptionDesc.VALIDATION_EXCEPTION);
  }

  /**
   * 405 - Method Not Allowed
   */
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public Result<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    logger.error(ExceptionDesc.UNSUPPORT_THE_METHOD , e);
    return ResultUtil.error(405,ExceptionDesc.UNSUPPORT_THE_METHOD);
  }

  /**
   * 415 - Unsupported Media Type
   */
  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public Result<String> handleHttpMediaTypeNotSupportedException(Exception e) {
    logger.error(ExceptionDesc.UNSUPPORT_MEDIA_TYPE, e);
    return ResultUtil.error(405,ExceptionDesc.UNSUPPORT_MEDIA_TYPE);
  }

  /**
   * 500 - Internal Server Error
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(ServiceException.class)
  public Result<String> handleServiceException(ServiceException e) {
    logger.error(ExceptionDesc.CONTROLLER_BUSINESS_EXCEPTION, e);
    return ResultUtil.error(500,"业务逻辑异常：" + e.getMessage());
  }

  
  /**
   * 500 - Internal Server Error
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(ControllerException.class)
  public Result<String> handleControllerException(ServiceException e) {
    logger.error(ExceptionDesc.SERVICE_BUSINESS_EXCEPTION, e);
    return ResultUtil.error(500, e.getMessage());
  }

  /**
   * 500 - Internal Server Error
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public Result<String> handleException(Exception e) {
    logger.error(ExceptionDesc.EXCEPTION, e);
    return ResultUtil.error(500,ExceptionDesc.EXCEPTION + e.getMessage());
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(DataIntegrityViolationException.class)
  public Result<String> handleException(DataIntegrityViolationException e) {
    logger.error(ExceptionDesc.DB_OPRATION_EXCEPTION, e);
    return ResultUtil.error(500,ExceptionDesc.DB_OPRATION_EXCEPTION);
  }
  
  
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(ExpiredJwtException.class)
  public Result<String> handleExpiredJwtException(ExpiredJwtException e) {
	  
	  
    logger.error(ExceptionDesc.EXPIREDJWT_EXCEPTION, e);
    return ResultUtil.error(500,ExceptionDesc.EXPIREDJWT_EXCEPTION);
  }
  
  
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(UnsupportedJwtException.class)
  public Result<String> handleUnsupportedJwtException(UnsupportedJwtException e) {
	  
	  
    logger.error(ExceptionDesc.UNSUPPORTEDJWT_EXCEPTION, e);
    return ResultUtil.error(500,ExceptionDesc.UNSUPPORTEDJWT_EXCEPTION);
  }
  
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(MalformedJwtException.class)
  public Result<String> handleMalformedJwtException(MalformedJwtException e) {
	  
	  
    logger.error(ExceptionDesc.MALFORMEDJWT_EXCEPTION, e);
    return ResultUtil.error(500,ExceptionDesc.MALFORMEDJWT_EXCEPTION);
  }
  
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(SignatureException.class)
  public Result<String> handleSignatureException(SignatureException e) {
	  
	  
    logger.error(ExceptionDesc.SIGNATURE_EXCEPTION, e);
    return ResultUtil.error(500,ExceptionDesc.SIGNATURE_EXCEPTION);
  }
  
  
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(IllegalArgumentException.class)
  public Result<String> IllegalArgumentExceptionException(IllegalArgumentException e) {
	  
	  
    logger.error(ExceptionDesc.ILLEGAL_ARGUMENT, e);
    return ResultUtil.error(500,ExceptionDesc.ILLEGAL_ARGUMENT);
  }
  
}