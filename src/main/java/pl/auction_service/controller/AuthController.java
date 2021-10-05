package pl.auction_service.controller;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

//    @RequestMapping(value = "/login", method = RequestMethod.POST)
//    private ResponseEntity<Authentication> login(@RequestParam String username, @RequestParam String password){
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if(auth.isAuthenticated())
//            return new ResponseEntity<Authentication>(auth, HttpStatus.OK);
//
//        return null;
//    }

//    @RequestMapping(value = "/registry", method = RequestMethod.POST)
//    public ResponseEntity registry(@RequestParam String username, @RequestParam String password){
//
//    }

}
