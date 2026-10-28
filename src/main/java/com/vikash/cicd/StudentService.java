package com.vikash.cicd;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StudentService {
    private final Map<Long, Student> students = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public StudentService() {
        create("Vikash Yadav", "CSE Data Science");
    }

    public List<Student> findAll() {
        return students.values().stream()
                .sorted(Comparator.comparing(Student::id)).toList();
    }

    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(students.get(id));
    }

    public Student create(String name, String course) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        if (course == null || course.isBlank()) throw new IllegalArgumentException("Course is required");
        long id = sequence.incrementAndGet();
        Student student = new Student(id, name.trim(), course.trim());
        students.put(id, student);
        return student;
    }

    public boolean delete(Long id) {
        return students.remove(id) != null;
    }
}
