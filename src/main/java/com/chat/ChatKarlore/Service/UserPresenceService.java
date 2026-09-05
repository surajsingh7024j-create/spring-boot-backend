package com.chat.ChatKarlore.Service;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserPresenceService {
    private final ConcurrentHashMap<String, Set<String>>userSessions =new ConcurrentHashMap<>();
    //user ko online  karo
  public void  online (String username,String sessionId ){
      if (username == null|| username.isBlank()|| sessionId==null||sessionId.isBlank()){
          return ;
      }userSessions.computeIfAbsent(username,key->ConcurrentHashMap.newKeySet()).add(sessionId);
      System.out.println("USER ONLINE:"+username);
  }
  public Boolean isOnline(String username)
  {if(username==null||username.isBlank()){return false;}
      Set<String> sessions= userSessions.get(username);
      return sessions !=null&&! sessions.isEmpty();
  }
    public void offline (String username,String sessionId){
      if (username==null||username.isBlank()||sessionId==null||sessionId.isBlank()){return;}
      Set<String> sessions=userSessions.get(username);
      if(sessions!=null){sessions.remove(sessionId);
      if(sessions.isEmpty()){userSessions.remove(username);
        System.out.println("USER OFFLINE:"+username);}}
    }

}
