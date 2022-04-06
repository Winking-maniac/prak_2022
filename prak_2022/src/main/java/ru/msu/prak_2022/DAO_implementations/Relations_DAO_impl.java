package ru.msu.prak_2022.DAO_implementations;

import lombok.NonNull;
import lombok.val;
import org.apache.tomcat.jni.Local;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import javax.crypto.Cipher;
import java.time.LocalTime;
import java.util.AbstractMap.SimpleEntry;
//import static java.lang.Boolean.*;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;

@Repository
public class Relations_DAO_impl {
    @Autowired
    private Student_DAO_impl student_dao;
    @Autowired
    private Teacher_DAO_impl teacher_dao;
    @Autowired
    private Course_DAO_impl course_dao;
    @Autowired
    private Company_DAO_impl company_dao;
    @Autowired
    private Lesson_DAO_impl lesson_dao;

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(LocalSessionFactoryBean session_factory) {
        this.sessionFactory = session_factory.getObject();
    }

    //Student-course relation
    public status enroll(@NonNull Student student, @NonNull Course course) {
        try (Session session = sessionFactory.openSession()) {
            session.saveOrUpdate(new StudentCourse(student.getStudent_id(), course.getCourse_id()));
            return status.OK;
        }
    }

    public status unenroll(@NonNull Student student, @NonNull Course course) {
        try (Session session = sessionFactory.openSession()) {
            val courses = student_dao.courses(student);
            if (courses.getKey() != status.OK) return courses.getKey();
            if (!courses.getValue().contains(course)) return status.RELATION_NOT_FOUND;
            session.delete(new StudentCourse(student.getStudent_id(), course.getCourse_id()));
            return status.OK;
        }
    }

    public SimpleEntry<status, Boolean> is_student(@NonNull Student student, @NonNull Course course) {
        try (Session session = sessionFactory.openSession()) {
            val courses = student_dao.courses(student);
            if (courses.getKey() != status.OK) return new SimpleEntry<>(courses.getKey(), null);
            if (!courses.getValue().contains(course)) return new SimpleEntry<>(status.OK, FALSE);
            return new SimpleEntry<>(status.OK, TRUE);
        }
    }

    // Teacher-company
    public SimpleEntry<status, Boolean> is_member(@NonNull Teacher teacher, @NonNull Company company) {
        try (Session session = sessionFactory.openSession()) {
            val companies = teacher_dao.companies(teacher);
            if (companies.getKey() != status.OK) return new SimpleEntry<>(companies.getKey(), null);
            if (!companies.getValue().contains(company)) return new SimpleEntry<>(status.OK, FALSE);
            return new SimpleEntry<>(status.OK, TRUE);
        }
    }

    public status approve(@NonNull Teacher teacher, @NonNull Company company) {
        try (Session session = sessionFactory.openSession()) {
            val companies = teacher_dao.invites(teacher);
            if (companies.getKey() != status.OK) return companies.getKey();
            if (!companies.getValue().contains(company)) return status.RELATION_NOT_FOUND;
            session.saveOrUpdate(new CompanyTeacher(teacher.getTeacher_id(), company.getCompany_id(), teacher, company, true));
            return status.OK;
        }
    }

    public status reject(@NonNull Teacher teacher, @NonNull Company company) {
        try (Session session = sessionFactory.openSession()) {
            val companies = teacher_dao.invites(teacher);
            if (companies.getKey() != status.OK) return companies.getKey();
            if (!companies.getValue().contains(company)) return status.RELATION_NOT_FOUND;
            session.delete(new CompanyTeacher(teacher.getTeacher_id(), company.getCompany_id(), teacher, company, false));
            return status.OK;
        }
    }

    public status retire(@NonNull Teacher teacher, @NonNull Company company) {
        try (Session session = sessionFactory.openSession()) {
            val companies = teacher_dao.companies(teacher);
            if (companies.getKey() != status.OK) return companies.getKey();
            if (!companies.getValue().contains(company)) return status.RELATION_NOT_FOUND;
            session.delete(new CompanyTeacher(teacher.getTeacher_id(), company.getCompany_id(), teacher, company, true));
            return status.OK;
        }
    }

    public status invite(@NonNull Teacher teacher, @NonNull Company company) {
        try (Session session = sessionFactory.openSession()) {
            val companies = teacher_dao.companies(teacher);
            if (companies.getKey() != status.OK) return companies.getKey();
            if (companies.getValue().contains(company)) return status.ALREADY_MEMBER;
            session.saveOrUpdate(new CompanyTeacher(teacher.getTeacher_id(), company.getCompany_id(), teacher, company, false));
            return status.OK;
        }
    }

    public status fire(@NonNull Teacher teacher, @NonNull Company company) {
        try (Session session = sessionFactory.openSession()) {
            val companies = teacher_dao.companies(teacher);
            if (companies.getKey() != status.OK) return companies.getKey();
            if (!companies.getValue().contains(company)) return status.RELATION_NOT_FOUND;
            session.delete(new CompanyTeacher(teacher.getTeacher_id(), company.getCompany_id(), teacher, company, true));
            return status.OK;
        }
    }

    // Teacher-course
    public SimpleEntry<status, Boolean> is_admin(@NonNull Teacher teacher, @NonNull Course course) {
        try (Session session = sessionFactory.openSession()) {
            val companies = teacher_dao.courses(teacher);
            if (companies.getKey() != status.OK) return new SimpleEntry<>(companies.getKey(), null);
            if (!companies.getValue().contains(course)) return new SimpleEntry<>(status.RELATION_NOT_FOUND, null);

            TeacherCourse rel = session.get(TeacherCourse.class, new TeacherCourseId(teacher.getTeacher_id(), course.getCourse_id()));
            return new SimpleEntry<>(status.OK, rel.is_admin());
        }
    }

    // Company-course
    public status create_course(@NonNull Company company, @NonNull Course course) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(course);
            session.save(new CompanyCourse(company.getCompany_id(), course.getCourse_id(), true));
            session.getTransaction().commit();
            return status.OK;
        }
    }

    public SimpleEntry<status, Boolean> is_author(@NonNull Company company, @NonNull Course course) {
        try (Session session = sessionFactory.openSession()) {
            val courses = company_dao.courses(company);
            if (courses.getKey() != status.OK) return new SimpleEntry<>(courses.getKey(), null);
            if (!courses.getValue().contains(course)) return new SimpleEntry<>(status.RELATION_NOT_FOUND, null);

            CompanyCourse rel = session.get(CompanyCourse.class, new CompanyCourseId(company.getCompany_id(), course.getCourse_id()));

            return new SimpleEntry<>(status.OK, TRUE);
        }
    }

    public status update_course(@NonNull Company company, @NonNull Course course) {
        try (Session session = sessionFactory.openSession()) {
            val rel = is_author(company, course);
            if (rel.getKey() != status.OK) return status.FORBIDDEN;
            if (rel.getValue() != TRUE) return status.FORBIDDEN;

            session.saveOrUpdate(course);
            return status.OK;
        }
    }

    public status delete_course(@NonNull Company company, @NonNull Course course) {
        try (Session session = sessionFactory.openSession()) {
            val rel = is_author(company, course);
            if (rel.getKey() != status.OK) return status.FORBIDDEN;
            if (rel.getValue() != TRUE) return status.FORBIDDEN;

            session.delete(course);
            return status.OK;
        }
    }

    public status grant_admin(@NonNull Company company, @NonNull Course course, @NonNull Teacher teacher) {
        try (Session session = sessionFactory.openSession()) {
            val rel = is_member(teacher, company);
            if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
            if (rel.getKey() != status.OK) return rel.getKey();
            if (rel.getValue() != TRUE) return status.FORBIDDEN;

            val rel1 = is_admin(teacher, course);
            if (rel1.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
            if (rel1.getKey() != status.OK) return rel1.getKey();
            if (rel1.getValue() != TRUE) return status.FORBIDDEN;

            session.update(new TeacherCourse(course.getCourse_id(), teacher.getTeacher_id(), true));
            return status.OK;
        }
    }

    public status revoke_admin(@NonNull Company company, @NonNull Course course, @NonNull Teacher teacher) {
        try (Session session = sessionFactory.openSession()) {
            val rel = is_member(teacher, company);
            if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
            if (rel.getKey() != status.OK) return rel.getKey();
            if (rel.getValue() != TRUE) return status.FORBIDDEN;

            val rel1 = is_admin(teacher, course);
            if (rel1.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
            if (rel1.getKey() != status.OK) return rel1.getKey();
            if (rel1.getValue() != TRUE) return status.FORBIDDEN;

            session.update(new TeacherCourse(course.getCourse_id(), teacher.getTeacher_id(), false));
            return status.OK;
        }
    }

    public status add_teacher(@NonNull Company company, @NonNull Course course, @NonNull Teacher teacher) {
        try (Session session = sessionFactory.openSession()) {
            val rel = is_member(teacher, company);
            if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
            if (rel.getKey() != status.OK) return rel.getKey();
            if (rel.getValue() != TRUE) return status.FORBIDDEN;

            val rel1 = is_admin(teacher, course);
            if (rel1.getKey() == status.OK) return status.ALREADY_MEMBER;
            if (rel1.getKey() != status.RELATION_NOT_FOUND) return rel1.getKey();

            session.save(new TeacherCourse(course.getCourse_id(), teacher.getTeacher_id(), false));
            return status.OK;
        }
    }

    public status delete_teacher(@NonNull Company company, @NonNull Course course, @NonNull Teacher teacher) {
        try (Session session = sessionFactory.openSession()) {
            val rel = is_member(teacher, company);
            if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
            if (rel.getKey() != status.OK) return rel.getKey();
            if (rel.getValue() != TRUE) return status.FORBIDDEN;

            val rel1 = is_admin(teacher, course);
            if (rel1.getKey() != status.OK) return rel1.getKey();

            session.delete(new TeacherCourse(course.getCourse_id(), teacher.getTeacher_id(), false));
            return status.OK;
        }
    }

    // Teacher-lesson
    public status create_lesson(@NonNull Teacher teacher,
                                @NonNull Course course,
                                @NonNull LocalTime time_from,
                                @NonNull LocalTime time_till,
                                String description) {
        val rel = is_admin(teacher, course);
        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel.getKey() != status.OK) return rel.getKey();
        if (rel.getValue() != TRUE) return status.FORBIDDEN;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(new Lesson((long) course.getLessons().size() + 1,
                                    course,
                                    teacher,
                                    time_from,
                                    time_till,
                                    description));
            session.getTransaction().commit();
            return status.OK;
        }
    }

    public status update_lesson(@NonNull Teacher teacher,
                                @NonNull Course course,
                                @NonNull LocalTime time_from,
                                @NonNull LocalTime time_till,
                                String description) {
        val rel = is_admin(teacher, course);
        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel.getKey() != status.OK) return rel.getKey();

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(new Lesson((long) course.getLessons().size() + 1,
                    course,
                    teacher,
                    time_from,
                    time_till,
                    description));
            session.getTransaction().commit();
            return status.OK;
        }
    }

//    private status update_lesson(@NonNull Teacher teacher,
//                                Lesson lesson) {
//        val rel = is_admin(teacher, course);
//        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
//        if (rel.getKey() != status.OK) return rel.getKey();
//        if (rel.getValue() != TRUE) return status.FORBIDDEN;
//
//        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//            session.saveOrUpdate(lesson);
//            session.getTransaction().commit();
//            return status.OK;
//        }
//    }

    public status delete_lesson(@NonNull Teacher teacher,
                                @NonNull Course course,
                                Long lesson_id) {
        val rel = is_admin(teacher, course);
        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel.getKey() != status.OK) return rel.getKey();
        if (rel.getValue() != TRUE) return status.FORBIDDEN;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(new Lesson(lesson_id,
                    course,
                    teacher));
            session.getTransaction().commit();
            return status.OK;
        }
    }

//    public status reassign(Teacher admin,
//                         Teacher teacher,
//                         Lesson lesson) {
//    }

    public status rate(Teacher teacher,
                       Lesson lesson,
                       Student student,
                       Long score) {
        val rel = lesson_dao.get_teacher(lesson);
//        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel.getKey() != status.OK) return rel.getKey();
        if (rel.getValue() != teacher) return status.FORBIDDEN;

        val rel1 = is_student(student, lesson_dao.get_course(lesson).getValue());
        if (rel1.getKey() != status.OK) return rel1.getKey();
//        if (rel1.getValue() != teacher) return status.FORBIDDEN;
        if (rel1.getValue() != TRUE) return status.RELATION_NOT_FOUND;

        try (Session session = sessionFactory.openSession()) {
            session.saveOrUpdate(new StudentLesson(lesson.getLesson_id(),
                                                    lesson.getCourse_id(),
                                                    student.getStudent_id(),
                                                    score));
        }
        return status.OK;
    }

}