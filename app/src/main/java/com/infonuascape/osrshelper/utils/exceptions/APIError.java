package com.infonuascape.osrshelper.utils.exceptions;

/**
 * Created by maden on 9/12/14.
 */
public class APIError extends Exception{
   /**
	 *
	 */
	private static final long serialVersionUID = 1L;
    private String error;
   public APIError(String error)
   {
      super(error);
   }
   public String getError()
   {
      return error;
   }
}

