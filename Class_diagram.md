``` mermaid
classDiagram
    class Person {
        +String dni
        +String name
        +String surname
        +String username
        +String password
        +String cellphone
        +Date birthdate
        +String email
    }
    class Professor {
        +String degree
        +String graduate_univ
        +String position
    }
    class Student {
        +String birthplace
        +String town_of_residence
        +String contact_relative
        +String contact_cellphone
    }
    class Administrator {
        
    }
    class Period {
        +Date start_date
        +Date end_date
    }
    class Registration_Subject {
        +Date date
    }
    class Exam {
        +Date date
        +Float grade
    }
    class Degree {
        +String name
    }
    class Registration_Degree {
        +Date date
    }
    class Subject {
        +int code
        +String name
        +String course_syllabus
        +int hours
    }
    class Study_Plan {
        +String version
        +String plan
    }
    class Requirement {
        Condition condition
    }
    class Condition {
        <<enumeration>>
        REGULAR
        APPROVED
    }

    %% Inheritance
    Administrator --|> Person
    Professor --|> Person
    Student --|> Person

    %% Degree Relationships
    Study_Plan "1..*" *-- "1..*" Subject
    Degree "1" -- "1..*" Study_Plan

    %% Professor-Subject Relationships
    Professor "1" -- "1..*" Period
    Period "1..*" -- "1" Subject

    %% Student-Subject Relationships
    Student "1" -- "*" Registration_Subject
    Registration_Subject "*" -- "1" Subject
    Student "1" -- "*" Exam
    Exam "*" -- "1" Subject
    
    %% Correlation Relationship
    Subject "1" -- "*" Requirement
    Requirement "*" -- "1" Subject

    %% Student-Degree Relationship
    Student "1" -- "1..*" Registration_Degree
    Registration_Degree "*" -- "1" Degree
```
