-- =============================================================
-- PERSONS — base table (superclass)
-- Contains all common attributes including authentication fields
-- =============================================================
CREATE TABLE IF NOT EXISTS persons (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    dni         TEXT    NOT NULL UNIQUE,
    name        TEXT    NOT NULL,
    surname     TEXT    NOT NULL,
    username    TEXT    NOT NULL UNIQUE,
    password    TEXT    NOT NULL,
    cellphone   TEXT,
    birthdate   DATE,
    email       TEXT    NOT NULL UNIQUE,

    created_at  DATETIME,
    updated_at  DATETIME
);

-- =============================================================
-- PERSON_ROLES — many roles per person
-- =============================================================
CREATE TABLE IF NOT EXISTS person_roles (
    person_id   INTEGER NOT NULL,
    role        TEXT    NOT NULL CHECK(role IN ('ADMIN', 'PROFESSOR', 'STUDENT')),

    PRIMARY KEY (person_id, role),
    FOREIGN KEY (person_id) REFERENCES persons(id)
    ON DELETE CASCADE
);

-- =============================================================
-- PROFESSORS — subclass of Person
-- =============================================================
CREATE TABLE IF NOT EXISTS professors (
    person_id       INTEGER PRIMARY KEY,
    degree          TEXT,
    graduate_univ   TEXT,
    position        TEXT,

    created_at      DATETIME,
    updated_at      DATETIME,

    FOREIGN KEY (person_id) REFERENCES persons(id)
    ON DELETE CASCADE
);

-- =============================================================
-- STUDENTS — subclass of Person
-- =============================================================
CREATE TABLE IF NOT EXISTS students (
    id                  INTEGER PRIMARY KEY AUTOINCREMENT,
    person_id           INTEGER NOT NULL UNIQUE,

    birthplace          TEXT,
    town_of_residence   TEXT,
    contact_relative    TEXT,
    contact_cellphone   TEXT,

    created_at          DATETIME,
    updated_at          DATETIME,

    FOREIGN KEY(person_id)
        REFERENCES persons(id)
        ON DELETE CASCADE
);

-- =============================================================
-- ADMINISTRATORS — subclass of Person
-- =============================================================
CREATE TABLE IF NOT EXISTS administrators (
    person_id   INTEGER PRIMARY KEY,

    created_at  DATETIME,
    updated_at  DATETIME,

    FOREIGN KEY (person_id) REFERENCES persons(id)
    ON DELETE CASCADE
);

-- =============================================================
-- SUBJECTS
-- =============================================================
CREATE TABLE IF NOT EXISTS subjects (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    code            INTEGER NOT NULL UNIQUE,
    name            TEXT    NOT NULL,
    course_syllabus TEXT,
    hours           INTEGER NOT NULL,

    created_at      DATETIME,
    updated_at      DATETIME
);

-- =============================================================
-- CAREERS
-- =============================================================
CREATE TABLE IF NOT EXISTS careers (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    code        INTEGER NOT NULL UNIQUE,
    name        TEXT    NOT NULL,

    created_at  DATETIME,
    updated_at  DATETIME
);

-- =============================================================
-- PLANS
-- =============================================================
CREATE TABLE IF NOT EXISTS plans (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        TEXT    NOT NULL,
    version     TEXT    NOT NULL,

    created_at  DATETIME,
    updated_at  DATETIME
);

-- =============================================================
-- CAREER_SUBJECTS — junction entre carreras y materias (M:N)
-- =============================================================
CREATE TABLE IF NOT EXISTS career_subjects (
    career_id   INTEGER NOT NULL,
    subject_id  INTEGER NOT NULL,

    PRIMARY KEY (career_id, subject_id),

    FOREIGN KEY (career_id)  REFERENCES careers(id)  ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- =============================================================
-- PROFESSOR_SUBJECTS — junction entre profesores y materias (M:N)
-- =============================================================
CREATE TABLE IF NOT EXISTS professor_subjects (
    professor_person_id  INTEGER NOT NULL,
    subject_id           INTEGER NOT NULL,

    PRIMARY KEY (professor_person_id, subject_id),

    FOREIGN KEY (professor_person_id) REFERENCES professors(person_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id)          REFERENCES subjects(id)          ON DELETE CASCADE
);

-- =============================================================
-- REGISTRATION_SUBJECTS — asociación Student <-> Subject
-- Un alumno no puede inscribirse dos veces a la misma materia.
-- =============================================================
CREATE TABLE IF NOT EXISTS registration_subjects (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id  INTEGER NOT NULL,
    subject_id  INTEGER NOT NULL,
    date        DATE    NOT NULL,

    created_at  DATETIME,
    updated_at  DATETIME,

    FOREIGN KEY (student_id) REFERENCES students(person_id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id)        ON DELETE CASCADE,

    UNIQUE (student_id, subject_id)
);
-- =============================================================
-- CAREER_STUDENTS — join table Many2Many Career ↔ Student
-- Un estudiante puede estar en más de una carrera.
-- =============================================================
CREATE TABLE IF NOT EXISTS career_students (
    career_id   INTEGER NOT NULL,
    student_id  INTEGER NOT NULL,

    PRIMARY KEY (career_id, student_id),

    FOREIGN KEY (career_id)  REFERENCES careers(id)           ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(person_id)   ON DELETE CASCADE
);
-- =============================================================
-- EXAMS — association between Student and Subject
-- Stores exam records including date and grade.
-- =============================================================
CREATE TABLE IF NOT EXISTS exams (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,

    student_id  INTEGER NOT NULL,
    subject_id  INTEGER NOT NULL,

    date        DATE NOT NULL,
    grade       REAL NOT NULL,

    created_at  DATETIME,
    updated_at  DATETIME,

    FOREIGN KEY(student_id)
        REFERENCES students(person_id)
        ON DELETE CASCADE,

    FOREIGN KEY(subject_id)
        REFERENCES subjects(id)
        ON DELETE CASCADE
);
-- =============================================================
-- PERIODS — association between Professor and Subject
-- Represents academic teaching periods for a subject.
-- =============================================================
CREATE TABLE IF NOT EXISTS periods (
    id                  INTEGER PRIMARY KEY AUTOINCREMENT,

    professor_person_id INTEGER NOT NULL,
    subject_id          INTEGER NOT NULL,

    start_date          DATE NOT NULL,
    end_date            DATE NOT NULL,

    created_at          DATETIME,
    updated_at          DATETIME,

    FOREIGN KEY(professor_person_id)
        REFERENCES professors(person_id)
        ON DELETE CASCADE,

    FOREIGN KEY(subject_id)
        REFERENCES subjects(id)
        ON DELETE CASCADE
);
-- =============================================================
-- REQUIREMENTS — subject prerequisites
-- Defines prerequisite subjects and required completion conditions.
-- =============================================================
CREATE TABLE IF NOT EXISTS requirements (
    id                  INTEGER PRIMARY KEY AUTOINCREMENT,

    subject_id          INTEGER NOT NULL,
    required_subject_id INTEGER NOT NULL,

    condition           TEXT NOT NULL
        CHECK(condition IN ('REGULAR', 'APPROVED')),

    created_at          DATETIME,
    updated_at          DATETIME,

    FOREIGN KEY(subject_id)
        REFERENCES subjects(id)
        ON DELETE CASCADE,

    FOREIGN KEY(required_subject_id)
        REFERENCES subjects(id)
        ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS grades (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    student_id  INTEGER NOT NULL,
    subject_id  INTEGER NOT NULL,
    professor_id INTEGER NOT NULL,
    grade       REAL,
    description TEXT,
    date        TEXT
);
