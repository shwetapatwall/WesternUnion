package com.wu.assignment;

import com.intuit.karate.junit5.Karate;
import com.intuit.karate.junit5.Karate.Test;

public class TestHerokuRunner {

    @Test
    public Karate runTest(){

       return Karate.run("classpath:features/HerokuAPI.feature");

    }
    @Test
    public Karate getToken(){

        return Karate.run("classpath:features/AuthToken.feature");

    }

}
