package HMS.src.controller;

import java.util.UUID;

import HMS.src.management.*;

public class DoctorController
{
    public static void addDoctor(String doctorID, User doctor){
        User.registerNewUser(doctorID);
    }
    
}