import random
from datetime import datetime, timedelta

data = open('Populate.SQL', 'w')


def populate_courses():
    # All courses names
    college_courses = [
        "Mathematics",
        "Chemistry",
        "Biology",
        "Physics",
        "Computer Science",
        "Environmental Science",
        "Psychology",
        "Sociology",
        "History",
        "Political Science",
        "Economics",
        "English Literature",
        "Creative Writing",
        "Art History",
        "Music Theory",
        "Theater Arts",
        "Dance",
        "Physical Education",
        "Nutrition",
        "Geology",
        "Astronomy",
        "Philosophy",
        "Religious Studies",
        "Anthropology",
        "Communication Studies",
        "Statistics",
        "Calculus",
        "Algebra",
        "Linear Algebra",
        "Discrete Mathematics",
        "French",
        "Spanish",
        "German",
        "Chinese",
        "Japanese",
        "Russian",
        "Italian",
        "Arabic",
        "Linguistics",
        "Criminal Justice",
        "Law",
        "Public Administration",
        "Business Administration",
        "Marketing",
        "Finance",
        "Management",
        "Human Resources",
        "International Relations",
        "Information Technology",
        "Web Development",
        "Database Management",
        "Networking",
        "Cybersecurity",
        "Data Science",
        "Machine Learning",
        "Robotics",
        "Biotechnology",
        "Medical Ethics",
        "Nursing",
        "Dental Hygiene",
        "Physical Therapy",
        "Social Work",
        "Education",
        "Early Childhood Development",
        "Special Education",
        "Engineering",
        "Civil Engineering",
        "Mechanical Engineering",
        "Electrical Engineering",
        "Chemical Engineering",
        "Materials Science",
        "Architecture",
        "Urban Planning",
        "Fashion Design",
        "Graphic Design",
        "Interior Design",
        "Journalism",
        "Broadcasting",
        "Public Relations",
        "Meteorology",
        "Oceanography",
        "Sports Management",
        "Kinesiology",
        "Outdoor Recreation",
        "Hospitality Management",
        "Culinary Arts",
        "Tourism",
        "Veterinary Science",
        "Animal Science",
        "Plant Science",
        "Math Education",
        "Science Education",
        "Art Education",
        "Music Education",
        "Physical Education",
        "Health Education"
    ]
    # Construct Insert Statements
    for i in range(len(college_courses)):
        duration = int(random.gauss(50, 10))
        row = f"INSERT INTO COURSES VALUES ({i}, '{college_courses[i]}', {duration});\n"
        data.write(row)


def populate_departments():
    # All courses names
    college_departments = [
        "Mathematics",
        "Physics",
        "Chemistry",
        "Biology",
        "Computer Science",
        "Engineering",
        "Psychology",
        "Sociology",
        "History",
        "Political Science",
        "Economics",
        "English",
        "Languages",
        "Art",
        "Music"
    ]
    # Construct Insert Statements
    for i in range(len(college_departments)):
        years = random.randint(0, 12)
        row = f"INSERT INTO CDepartments VALUES ({i}, '{college_departments[i]}', NULL, SYSDATE - INTERVAL '{years}' YEAR);\n"
        data.write(row)


def populate_instructors():
    # All courses names
    college_instructors = [
        "Alice", "Bob", "Charlie", "David", "Emma",
        "Frank", "Grace", "Henry", "Isabel", "Jack",
        "Katherine", "Liam", "Mia", "Noah", "Olivia",
        "Penelope", "Quinn", "Ryan", "Sophia", "Thomas",
        "Ursula", "Victor", "Wendy", "Xander", "Yvonne",
        "Zachary", "Abigail", "Benjamin", "Catherine", "Daniel",
        "Eva", "Frederick", "Gemma", "Harper", "Isaac",
        "Jasmine", "Kai", "Lily", "Mason", "Nora",
        "Oscar", "Poppy", "Quentin", "Rachel", "Samuel",
        "Tessa", "Ulysses", "Violet", "William", "Ximena",
        "Yasmine", "Zane", "Amelia", "Brooklyn", "Caleb",
        "Diana", "Elijah", "Fiona", "Gabriel", "Hazel",
        "Ian", "Julia", "Kevin", "Luna", "Max",
        "Natalie", "Owen", "Piper", "Quincy", "Riley",
        "Sofia", "Theodore", "Uma", "Vincent", "Willow",
        "Xavier", "Yara", "Zara", "Aiden", "Bella",
        "Christopher", "Delilah", "Ezra", "Faith", "George",
        "Hannah", "Isaiah", "Joseph", "Kylie", "Leo",
        "Madison", "Nathan", "Oliver", "Penelope", "Quinn",
        "Rebecca", "Samantha", "Tyler", "Ursula", "Valentine",
        "Walter", "Xander", "Yvonne", "Zoey"
    ]
    # Construct Insert Statements
    for i in range(len(college_instructors)):
        dep_id = random.randint(0, 14)
        years = random.randint(0, 12)
        age = random.randint(25, 65)
        row = f"INSERT INTO Instructors VALUES ('{college_instructors[i]}', {i}, '0100200', '{college_instructors[i]}@yahoo.com', SYSDATE - INTERVAL '{years}' YEAR, SYSDATE - INTERVAL '{age}' YEAR, {dep_id});\n"
        data.write(row)


def populate_ins_courses():
    # Construct Insert Statements
    for i in range(1000):
        code = random.randint(0, 95)
        instructor = random.randint(0, 103)
        year = random.randint(0, 5)
        row = f"INSERT INTO Ins_Courses VALUES ({code}, {instructor}, SYSDATE - INTERVAL '{year}' YEAR);\n"
        data.write(row)


def populate_dep_courses():
    # Construct Insert Statements
    for i in range(250):
        code = random.randint(0, 95)
        department = random.randint(0, 14)
        row = f"INSERT INTO Dep_Courses VALUES ({code}, {department});\n"
        data.write(row)


def populate_prerequisites():
    # Construct Insert Statements
    for i in range(100):
        code = random.randint(0, 95)
        code2 = random.randint(0, 95)
        if code == code2:
            continue
        row = f"INSERT INTO Prerequisites VALUES ({code}, {code2});\n"
        data.write(row)


def populate_student():
    human_names = [
        "Alice", "Bob", "Charlie", "David", "Emma",
        "Frank", "Grace", "Henry", "Isabel", "Jack",
        "Katherine", "Liam", "Mia", "Noah", "Olivia",
        "Penelope", "Quinn", "Ryan", "Sophia", "Thomas",
        "Ursula", "Victor", "Wendy", "Xander", "Yvonne",
        "Zachary", "Abigail", "Benjamin", "Catherine", "Daniel",
        "Eva", "Frederick", "Gemma", "Harper", "Isaac",
        "Jasmine", "Kai", "Lily", "Mason", "Nora",
        "Oscar", "Poppy", "Quentin", "Rachel", "Samuel",
        "Tessa", "Ulysses", "Violet", "William", "Ximena",
        "Yasmine", "Zane", "Amelia", "Brooklyn", "Caleb",
        "Diana", "Elijah", "Fiona", "Gabriel", "Hazel",
        "Ian", "Julia", "Kevin", "Luna", "Max",
        "Natalie", "Owen", "Piper", "Quincy", "Riley",
        "Sofia", "Theodore", "Uma", "Vincent", "Willow",
        "Xavier", "Yara", "Zara", "Aiden", "Bella",
        "Christopher", "Delilah", "Ezra", "Faith", "George",
        "Hannah", "Isaiah", "Joseph", "Kylie", "Leo",
        "Madison", "Nathan", "Oliver", "Penelope", "Quinn",
        "Rebecca", "Samantha", "Tyler", "Ursula", "Valentine",
        "Walter", "Xander", "Yvonne", "Zoey"
    ]
    # Construct Insert Statements
    for i in range(1000):
        age = random.randint(18, 26)
        department = random.randint(0, 14)
        row = f"INSERT INTO Student VALUES ({i}, '{human_names[i%104]}',  SYSDATE - INTERVAL '{age}' YEAR, '{human_names[i%104]}@yahoo.com', 'Cairo', '6-October', {department});\n"
        data.write(row)


def populate_enrollments():
    enrollments = open('Enrollments.SQL', 'w')
    grades = ['A', 'B', 'C', 'D', 'F', 'A+']
    current_date = datetime.now()
    # Construct Insert Statements
    for i in range(211546):
        id_ = random.randint(0, 999)
        code = random.randint(0, 95)
        year = random.randint(0, 20)
        year = current_date - timedelta(days=365 * year)
        year = year.strftime("%Y-%m-%d")
        grade = random.choice(grades)

        row = f"{grade},{id_},{code},{year}\n"
        enrollments.write(row)


def populate_mobiles():
    mobiles = [
        "+1 555-123-4567",
        "+1 555-234-5678",
        "+1 555-345-6789",
        "+1 555-456-7890",
        "+1 555-567-8901",
        "+1 555-678-9012",
        "+1 555-789-0123",
        "+1 555-890-1234",
        "+1 555-901-2345",
        "+1 555-012-3456",
        "+1 555-123-7890",
        "+1 555-234-8901",
        "+1 555-345-9012",
        "+1 555-456-0123",
        "+1 555-567-1234",
        "+1 555-678-2345",
        "+1 555-789-3456",
        "+1 555-890-4567",
        "+1 555-901-5678",
        "+1 555-012-6789",
        "+1 555-123-8901",
        "+1 555-234-9012",
        "+1 555-345-0123",
        "+1 555-456-1234",
        "+1 555-567-2345",
        "+1 555-678-3456",
        "+1 555-789-4567",
        "+1 555-890-5678",
        "+1 555-901-6789",
        "+1 555-012-7890",
        "+1 555-123-8901",
        "+1 555-234-9012",
        "+1 555-345-0123",
        "+1 555-456-1234",
        "+1 555-567-2345",
        "+1 555-678-3456",
        "+1 555-789-4567",
        "+1 555-890-5678",
        "+1 555-901-6789",
        "+1 555-012-7890",
        "+1 555-123-8901",
        "+1 555-234-9012",
        "+1 555-345-0123",
        "+1 555-456-1234",
        "+1 555-567-2345",
        "+1 555-678-3456",
        "+1 555-789-4567",
        "+1 555-890-5678",
        "+1 555-901-6789",
        "+1 555-012-7890",
        "+1 555-123-8901",
        "+1 555-234-9012",
        "+1 555-345-0123",
        "+1 555-456-1234",
        "+1 555-567-2345",
        "+1 555-678-3456",
        "+1 555-789-4567",
        "+1 555-890-5678",
        "+1 555-901-6789",
        "+1 555-012-7890",
        "+1 555-123-8901",
        "+1 555-234-9012",
        "+1 555-345-0123",
        "+1 555-456-1234",
        "+1 555-567-2345",
        "+1 555-678-3456",
        "+1 555-789-4567",
        "+1 555-890-5678",
        "+1 555-901-6789",
        "+1 555-012-7890",
        "+1 555-123-8901",
        "+1 555-234-9012",
        "+1 555-345-0123",
        "+1 555-456-1234",
        "+1 555-567-2345",
        "+1 555-678-3456",
        "+1 555-789-4567",
        "+1 555-890-5678",
        "+1 555-901-6789",
        "+1 555-012-7890",
        "+1 555-123-8901",
        "+1 555-234-9012",
        "+1 555-345-0123",
        "+1 555-456-1234",
        "+1 555-567-2345",
        "+1 555-678-3456",
        "+1 555-789-4567",
        "+1 555-890-5678",
        "+1 555-901-6789"
    ]
    # Construct Insert Statements
    for i in range(1800):
        id_ = random.randint(0, 999)
        mobile = random.choice(mobiles)
        row = f"INSERT INTO Student_mobile VALUES ('{mobile}', {id_});\n"
        data.write(row)


def update_departments():
    for i in range(15):
        id_ = random.randint(0, 103)
        row = f"UPDATE CDepartments set manager_id = {id_} where id = {i};\n"
        data.write(row)


populate_courses()
populate_departments()
populate_instructors()
update_departments()
populate_ins_courses()
populate_dep_courses()
populate_prerequisites()
populate_student()
populate_mobiles()
populate_enrollments()
