package com.carterbee.signup.registration;

import org.hibernate.annotations.NaturalId;

public record RegistrationRequest( 
     String firstName,
     String lastName,
    @NaturalId(mutable = true)
     String email,
     String password,
     String role
){
}
