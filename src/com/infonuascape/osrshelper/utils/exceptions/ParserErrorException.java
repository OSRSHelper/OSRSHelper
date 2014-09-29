package com.infonuascape.osrshelper.utils.exceptions;

/**
 * Created by maden on 9/12/14.
 */
public class ParserErrorException extends Exception{
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

