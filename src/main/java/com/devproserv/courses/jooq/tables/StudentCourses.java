/*
 * This file is generated by jOOQ.
 */
package com.devproserv.courses.jooq.tables;


import com.devproserv.courses.jooq.Coursedb;
import com.devproserv.courses.jooq.Indexes;
import com.devproserv.courses.jooq.Keys;
import com.devproserv.courses.jooq.tables.records.StudentCoursesRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.7"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StudentCourses extends TableImpl<StudentCoursesRecord> {

    private static final long serialVersionUID = 1426635257;

    /**
     * The reference instance of <code>coursedb.student_courses</code>
     */
    public static final StudentCourses STUDENT_COURSES = new StudentCourses();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<StudentCoursesRecord> getRecordType() {
        return StudentCoursesRecord.class;
    }

    /**
     * The column <code>coursedb.student_courses.course_id</code>.
     */
    public final TableField<StudentCoursesRecord, Integer> COURSE_ID = createField("course_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>coursedb.student_courses.stud_id</code>.
     */
    public final TableField<StudentCoursesRecord, Integer> STUD_ID = createField("stud_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>coursedb.student_courses.status</code>.
     */
    public final TableField<StudentCoursesRecord, String> STATUS = createField("status", org.jooq.impl.SQLDataType.VARCHAR(20).nullable(false), this, "");

    /**
     * Create a <code>coursedb.student_courses</code> table reference
     */
    public StudentCourses() {
        this(DSL.name("student_courses"), null);
    }

    /**
     * Create an aliased <code>coursedb.student_courses</code> table reference
     */
    public StudentCourses(String alias) {
        this(DSL.name(alias), STUDENT_COURSES);
    }

    /**
     * Create an aliased <code>coursedb.student_courses</code> table reference
     */
    public StudentCourses(Name alias) {
        this(alias, STUDENT_COURSES);
    }

    private StudentCourses(Name alias, Table<StudentCoursesRecord> aliased) {
        this(alias, aliased, null);
    }

    private StudentCourses(Name alias, Table<StudentCoursesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> StudentCourses(Table<O> child, ForeignKey<O, StudentCoursesRecord> key) {
        super(child, key, STUDENT_COURSES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Coursedb.COURSEDB;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.STUDENT_COURSES_FK_STUDENT_COURSES_STUDENTS1_IDX, Indexes.STUDENT_COURSES_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<StudentCoursesRecord> getPrimaryKey() {
        return Keys.KEY_STUDENT_COURSES_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<StudentCoursesRecord>> getKeys() {
        return Arrays.<UniqueKey<StudentCoursesRecord>>asList(Keys.KEY_STUDENT_COURSES_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<StudentCoursesRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<StudentCoursesRecord, ?>>asList(Keys.FK_STUDENT_COURSES_COURSES1, Keys.FK_STUDENT_COURSES_STUDENTS1);
    }

    public Courses courses() {
        return new Courses(this, Keys.FK_STUDENT_COURSES_COURSES1);
    }

    public Students students() {
        return new Students(this, Keys.FK_STUDENT_COURSES_STUDENTS1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentCourses as(String alias) {
        return new StudentCourses(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StudentCourses as(Name alias) {
        return new StudentCourses(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public StudentCourses rename(String name) {
        return new StudentCourses(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public StudentCourses rename(Name name) {
        return new StudentCourses(name, null);
    }
}
