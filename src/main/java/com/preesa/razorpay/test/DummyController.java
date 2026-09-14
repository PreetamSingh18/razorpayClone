package com.preesa.razorpay.test;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/api/dummy")
@RequiredArgsConstructor
public class DummyController {
    
    @PostMapping("/test")
    public ResponseEntity<String>test( @RequestBody Object o){
         
           return ResponseEntity.ok("Test");
    }
}
