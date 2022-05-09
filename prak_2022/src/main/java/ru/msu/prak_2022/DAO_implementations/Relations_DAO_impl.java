package ru.msu.prak_2022.DAO_implementations;

import lombok.NonNull;
import lombok.val;
import org.apache.tomcat.jni.Local;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import ru.msu.prak_2022.DAO_interfaces.Relations_DAO;
import ru.msu.prak_2022.gl_session;
import ru.msu.prak_2022.models.*;
import ru.msu.prak_2022.status;

import javax.crypto.Cipher;
import java.time.LocalTime;
import java.util.AbstractMap.SimpleEntry;
//import static java.lang.Boolean.*;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;

@Repository
public class Relations_DAO_impl implements Relations_DAO {
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

//    private SessionFactory sessionFactory;

//    @Autowired
//    public void setSessionFactory(LocalSessionFactoryBean session_factory) {
//        this.sessionFactory = session_factory.getObject();
//    }

    //Student-course relation
    public status enroll(@NonNull Student student, @NonNull Course course) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(new StudentCourse(student.getStudent_id(), course.getCourse_id()));
        gl_session.commit();
        return status.OK;
    }

    public status unenroll(@NonNull Student student, @NonNull Course course) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val courses = student_dao.courses(student);
        if (courses.getKey() != status.OK) return courses.getKey();
        if (!courses.getValue().contains(course)) return status.RELATION_NOT_FOUND;
        session.close();
        gl_session.open();
        gl_session.beginTransaction();
        session = gl_session.sessionFactory.getCurrentSession();
        session.delete(new StudentCourse(student.getStudent_id(), course.getCourse_id()));
        gl_session.commit();
        return status.OK;
    }

    public SimpleEntry<status, Boolean> is_student(@NonNull Student student, @NonNull Course course) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val courses = student_dao.courses(student);
        if (courses.getKey() != status.OK) return new SimpleEntry<>(courses.getKey(), null);
        if (!courses.getValue().contains(course)) return new SimpleEntry<>(status.OK, FALSE);
        return new SimpleEntry<>(status.OK, TRUE);
    }

    // Teacher-company
    public SimpleEntry<status, Boolean> is_member(@NonNull Teacher teacher, @NonNull Company company) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val companies = teacher_dao.companies(teacher);
        if (companies.getKey() != status.OK) return new SimpleEntry<>(companies.getKey(), null);
        if (!companies.getValue().contains(company)) return new SimpleEntry<>(status.OK, FALSE);
        return new SimpleEntry<>(status.OK, TRUE);
    }

    public SimpleEntry<status, Boolean> is_invited(@NonNull Teacher teacher, @NonNull Company company) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val companies = teacher_dao.invites(teacher);
        if (companies.getKey() != status.OK) return new SimpleEntry<>(companies.getKey(), null);
        if (!companies.getValue().contains(company)) return new SimpleEntry<>(status.OK, FALSE);
        return new SimpleEntry<>(status.OK, TRUE);
    }

    public status approve(@NonNull Teacher teacher, @NonNull Company company) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val companies = teacher_dao.invites(teacher);
        if (companies.getKey() != status.OK) return companies.getKey();
        if (!companies.getValue().contains(company)) return status.RELATION_NOT_FOUND;
        gl_session.close();
        gl_session.open();
        session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(new CompanyTeacher(teacher.getTeacher_id(), company.getCompany_id(), teacher, company, true));
        gl_session.commit();
        return status.OK;
    }

    public status reject(@NonNull Teacher teacher, @NonNull Company company) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val companies = teacher_dao.invites(teacher);
        if (companies.getKey() != status.OK) return companies.getKey();
        if (!companies.getValue().contains(company)) return status.RELATION_NOT_FOUND;
        gl_session.close();
        gl_session.open();
        session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.delete(new CompanyTeacher(teacher.getTeacher_id(), company.getCompany_id(), teacher, company, false));
        gl_session.commit();
        return status.OK;
    }

    public status retire(@NonNull Teacher teacher, @NonNull Company company) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val companies = teacher_dao.companies(teacher);
        if (companies.getKey() != status.OK) return companies.getKey();
        val invites = teacher_dao.invites(teacher);
        if (companies.getKey() != status.OK) return invites.getKey();
        if (!companies.getValue().contains(company) && !invites.getValue().contains(company)) return status.RELATION_NOT_FOUND;
        gl_session.close();
        gl_session.open();
        session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.delete(new CompanyTeacher(teacher.getTeacher_id(), company.getCompany_id(), teacher, company, true));
        gl_session.commit();
        return status.OK;
    }

    public status invite(@NonNull Teacher teacher, @NonNull Company company) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val companies = teacher_dao.companies(teacher);
        if (companies.getKey() != status.OK) return companies.getKey();
        if (companies.getValue().contains(company)) return status.ALREADY_MEMBER;
        gl_session.close();
        gl_session.open();
        session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(new CompanyTeacher(teacher.getTeacher_id(), company.getCompany_id(), false));
        gl_session.commit();
        return status.OK;
    }

    public status fire(@NonNull Teacher teacher, @NonNull Company company) {
        return retire(teacher, company);
    }

    // Teacher-course
    public SimpleEntry<status, Boolean> is_admin(@NonNull Teacher teacher, @NonNull Course course) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val companies = teacher_dao.courses(teacher);
        if (companies.getKey() != status.OK) return new SimpleEntry<>(companies.getKey(), null);
        if (!companies.getValue().contains(course)) return new SimpleEntry<>(status.RELATION_NOT_FOUND, null);

        session = gl_session.sessionFactory.getCurrentSession();
        gl_session.beginTransaction();
        TeacherCourse rel = session.get(TeacherCourse.class, new TeacherCourseId(teacher.getTeacher_id(), course.getCourse_id()));
        gl_session.commit();
        return new SimpleEntry<>(status.OK, rel.is_admin());
    }

    // Company-course
    public status create_course(@NonNull Company company, @NonNull Course course) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(course);
        session.save(new CompanyCourse(company.getCompany_id(), course.getCourse_id(), true));
        session.getTransaction().commit();
        return status.OK;
    }

    public SimpleEntry<status, Boolean> is_author(@NonNull Company company, @NonNull Course course) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
//        session.flush();
//        session.refresh(company);
        session.getTransaction().commit();
//        gl_session.close();
//        gl_session.open();
//        session = gl_session.sessionFactory.getCurrentSession();
        val courses = company_dao.courses(company);
        if (courses.getKey() != status.OK) return new SimpleEntry<>(courses.getKey(), null);
        if (!courses.getValue().stream().map(Course::getCourse_id).toList().contains(course.getCourse_id())) return new SimpleEntry<>(status.RELATION_NOT_FOUND, null);
        gl_session.close();
        gl_session.open();
        session = gl_session.sessionFactory.getCurrentSession();
        gl_session.beginTransaction();
        CompanyCourse rel = session.get(CompanyCourse.class, new CompanyCourseId(company.getCompany_id(), course.getCourse_id()));
        gl_session.commit();
        return new SimpleEntry<>(status.OK, TRUE);
    }

    public status update_course(@NonNull Company company, @NonNull Course course) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val rel = is_author(company, course);
        if (rel.getKey() != status.OK) return rel.getKey();
        if (rel.getValue() != TRUE) return status.FORBIDDEN;
        session.close();
        gl_session.open();
        gl_session.beginTransaction();
        gl_session.sessionFactory.getCurrentSession().update(course);
        gl_session.commit();
        return status.OK;
    }

    public status delete_course(@NonNull Company company, @NonNull Course course) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val rel = is_author(company, course);
        if (rel.getKey() != status.OK) return status.FORBIDDEN;
        if (rel.getValue() != TRUE) return status.FORBIDDEN;
        session.close();
        gl_session.open();
        gl_session.beginTransaction();
        gl_session.sessionFactory.getCurrentSession().delete(course);
        gl_session.commit();
        return status.OK;
    }

    public status grant_admin(@NonNull Company company, @NonNull Course course, @NonNull Teacher teacher) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val rel = is_member(teacher, company);
        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel.getKey() != status.OK) return rel.getKey();
        if (rel.getValue() != TRUE) return status.FORBIDDEN;

        val rel1 = is_admin(teacher, course);
        if (rel1.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel1.getKey() != status.OK) return rel1.getKey();

        session = gl_session.sessionFactory.getCurrentSession();
        gl_session.beginTransaction();
        session.update(new TeacherCourse(course.getCourse_id(), teacher.getTeacher_id(), true));
        gl_session.commit();
        return status.OK;
    }

    public status revoke_admin(@NonNull Company company, @NonNull Course course, @NonNull Teacher teacher) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val rel = is_member(teacher, company);
        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel.getKey() != status.OK) return rel.getKey();
        if (rel.getValue() != TRUE) return status.FORBIDDEN;

        val rel1 = is_admin(teacher, course);
        if (rel1.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel1.getKey() != status.OK) return rel1.getKey();


        session = gl_session.sessionFactory.getCurrentSession();
        gl_session.beginTransaction();
        session.update(new TeacherCourse(course.getCourse_id(), teacher.getTeacher_id(), false));
        gl_session.commit();
        return status.OK;
    }

    public status add_teacher(@NonNull Company company, @NonNull Course course, @NonNull Teacher teacher) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val rel = is_member(teacher, company);
        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel.getKey() != status.OK) return rel.getKey();
        if (rel.getValue() != TRUE) return status.FORBIDDEN;

        val rel1 = is_admin(teacher, course);
        if (rel1.getKey() == status.OK) return status.ALREADY_MEMBER;
        if (rel1.getKey() != status.RELATION_NOT_FOUND) return rel1.getKey();

        session = gl_session.sessionFactory.getCurrentSession();
        gl_session.beginTransaction();
        session.save(new TeacherCourse(course.getCourse_id(), teacher.getTeacher_id(), false));
        gl_session.commit();
        return status.OK;
    }

    public status delete_teacher(@NonNull Company company, @NonNull Course course, @NonNull Teacher teacher) {
        Session session = gl_session.sessionFactory.getCurrentSession();
        val rel = is_member(teacher, company);
        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel.getKey() != status.OK) return rel.getKey();
        if (rel.getValue() != TRUE) return status.FORBIDDEN;

        val rel1 = is_admin(teacher, course);
        if (rel1.getKey() != status.OK) return rel1.getKey();

        session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.delete(new TeacherCourse(course.getCourse_id(), teacher.getTeacher_id(), false));
        gl_session.commit();
        return status.OK;
    }

    // Teacher-lesson
    public status update_lesson(@NonNull Teacher teacher,
                                @NonNull Course course,
                                @NonNull Lesson lesson) {
        val rel = is_admin(teacher, course);
        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel.getKey() != status.OK) return rel.getKey();

        Session session = gl_session.sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.saveOrUpdate(lesson);
        session.getTransaction().commit();
        return status.OK;
    }


/*
    private status update_lesson(@NonNull Teacher teacher,
                                Lesson lesson) {
        val rel = is_admin(teacher, course);
        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel.getKey() != status.OK) return rel.getKey();
        if (rel.getValue() != TRUE) return status.FORBIDDEN;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(lesson);
            session.getTransaction().commit();
            return status.OK;
        }
    }
*/
public status create_lesson(@NonNull Teacher teacher,
                            @NonNull Course course,
                            @NonNull java.sql.Timestamp time_from,
                            @NonNull java.sql.Timestamp time_till,
                            String description) {
    val rel = is_admin(teacher, course);
    if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
    if (rel.getKey() != status.OK) return rel.getKey();
//    if (rel.getValue() != TRUE) return status.FORBIDDEN;

    Session session = gl_session.sessionFactory.getCurrentSession();
    session.beginTransaction();
    session.save(new Lesson(
            course,
            teacher,
            time_from,
            time_till,
            description));
    session.getTransaction().commit();
    return status.OK;
}

    public status delete_lesson(@NonNull Teacher teacher,
                                @NonNull Course course,
                                Long lesson_id) {
        val rel = is_admin(teacher, course);
        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel.getKey() != status.OK) return rel.getKey();
        if (rel.getValue() != TRUE) return status.FORBIDDEN;

        try (Session session = gl_session.sessionFactory.getCurrentSession()) {
            session.beginTransaction();
            session.delete(new Lesson(lesson_id,
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
        gl_session.beginTransaction();
        gl_session.sessionFactory.getCurrentSession().refresh(teacher);
        gl_session.commit();
//        if (rel.getKey() == status.RELATION_NOT_FOUND) return status.FORBIDDEN;
        if (rel.getKey() != status.OK) return rel.getKey();
        if (!rel.getValue().equals(teacher)) return status.FORBIDDEN;

        val rel1 = is_student(student, lesson_dao.get_course(lesson).getValue());
        if (rel1.getKey() != status.OK) return rel1.getKey();
//        if (rel1.getValue() != teacher) return status.FORBIDDEN;
        if (rel1.getValue() != TRUE) return status.RELATION_NOT_FOUND;

        Session session = gl_session.sessionFactory.getCurrentSession();
        gl_session.beginTransaction();
        if (score != null) {
            session.saveOrUpdate(new StudentLesson(lesson.getLesson_id(),
                    lesson.getCourse_id(),
                    student.getStudent_id(),
                    score));
        } else {
            session.delete(new StudentLesson(lesson.getLesson_id(),
                    lesson.getCourse_id(),
                    student.getStudent_id(),
                    score));
        }
        gl_session.commit();
        return status.OK;
    }

}