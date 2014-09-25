package com.infonuascape.osrshelper.utils;

/**
 * Created by maden on 9/12/14.
 */
public class PlayerNotFoundException extends Exception{
   private String userName;
   public PlayerNotFoundException(String userName)
   {
      this.userName = userName;
   }
   public String getUserName()
   {
      return userName;
   }
}

