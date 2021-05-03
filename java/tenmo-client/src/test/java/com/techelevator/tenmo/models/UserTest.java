package com.techelevator.tenmo.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void canInstantiateUser() {
        User user = new User();
        user.setId(222);
        user.setUsername("Steve");
        assertEquals(Integer.valueOf(222), user.getId());
        assertEquals("Steve", user.getUsername());
    }

}