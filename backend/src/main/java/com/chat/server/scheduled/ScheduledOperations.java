package com.chat.server.scheduled;

import com.chat.server.model.User;
import com.chat.server.oauth2.service.TokenManager;
import com.chat.server.service.UserService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created on 04.12.2015.
 */
public class ScheduledOperations {
    @Autowired
    private UserService userService;

    @Value("${url.logout}")
    private String logoutUrl;
    @Value("${headers.user-agent}")
    private String USER_AGENT;


    /**
     * Logout non active users
     * This method started every hour and clean system from non active users
     */
    @Scheduled(cron="${cron.logoutNonActiveUsers}")
    public void logoutNonActiveUsers(){
        System.out.println("****** ScheduledOperations -- LogoutNonActiveUsers  " + new Date());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);
        Date compareDate = cal.getTime();

        Map<String, UserDetails> validUsers = TokenManager.getInstance().getValidUsers();
        for( Map.Entry<String,UserDetails> entry: validUsers.entrySet() ){
            String token = entry.getKey();
            String login = entry.getValue().getUsername();
            logoutUser( token, login, compareDate );
        }
    }

    private void logoutUser(String token, String login, Date compareDate){
        User user = userService.findUserByLogin(login);
        Date lastRequest = user.getLastRequest();
        if ( lastRequest.before( compareDate ) ){

            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(logoutUrl);
            request.setHeader("User-Agent", USER_AGENT);
            request.setHeader("AccessToken", token);
            try{
                client.execute(request);
            } catch(IOException ex){
                System.out.println("Error during logout user with id=" + user.getId());
            }

        }
    }

}
