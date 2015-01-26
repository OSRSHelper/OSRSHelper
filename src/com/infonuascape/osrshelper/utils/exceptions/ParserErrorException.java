package com.infonuascape.osrshelper.utils.exceptions;

/**
 * Created by maden on 9/12/14.
 */
public class ParserErrorException extends Exception{
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String error;
   public ParserErrorException(String error)
   {
      this.error = error;
   }
   public String getError()
   {
      return error;
   }
}

