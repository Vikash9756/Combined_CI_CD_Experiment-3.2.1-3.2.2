package com.vikash.cicd;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    @Test
    void createAndFindStudent() {
        StudentService service = new StudentService();
        Student student = service.create("Aman", "CSE");
        assertEquals("Aman", service.findById(student.id()).orElseThrow().name());
    }

    @Test
    void rejectsBlankName() {
        StudentService service = new StudentService();
        assertThrows(IllegalArgumentException.class,
                () -> service.create(" ", "CSE"));
    }

    @Test
    void deleteRemovesStudent() {
        StudentService service = new StudentService();
        Student student = service.create("Neha", "IT");
        assertTrue(service.delete(student.id()));
        assertTrue(service.findById(student.id()).isEmpty());
    }
}
