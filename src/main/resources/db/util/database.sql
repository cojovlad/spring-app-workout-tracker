CREATE DATABASE workout_tracker;

-- Create languages table
CREATE TABLE IF NOT EXISTS languages (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         code VARCHAR(5) NOT NULL UNIQUE,
                                         name VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

-- Create roles table
CREATE TABLE IF NOT EXISTS roles (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     first_name VARCHAR(50) NOT NULL,
                                     last_name VARCHAR(50) NOT NULL UNIQUE,
                                     username VARCHAR(50) NOT NULL UNIQUE,
                                     email VARCHAR(100) NOT NULL UNIQUE,
                                     password_hash VARCHAR(255) NOT NULL,
                                     language_id BIGINT,
                                     profile_image_url VARCHAR(255),
                                     is_active BOOLEAN DEFAULT TRUE,
                                     email_verified BOOLEAN DEFAULT FALSE,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     FOREIGN KEY (language_id) REFERENCES languages(id)
) ENGINE=InnoDB;

-- Create user_roles join table
CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BIGINT NOT NULL,
                                          role_id BIGINT NOT NULL,
                                          PRIMARY KEY (user_id, role_id),
                                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                          FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 1. Muscle Parts (Normalized)
CREATE TABLE muscle_parts (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              name VARCHAR(50) NOT NULL UNIQUE,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Exercises (Many-to-Many with Muscle Parts)
CREATE TABLE exercises (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           description TEXT,
                           created_by_user_id BIGINT NOT NULL,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (created_by_user_id) REFERENCES users(id),
                           UNIQUE KEY uniq_exercise_name (name, created_by_user_id)
) ENGINE=InnoDB;

-- 2a. Exercise-Muscle Targets (Junction Table)
CREATE TABLE exercise_muscle_targets (
                                         exercise_id BIGINT NOT NULL,
                                         muscle_part_id BIGINT NOT NULL,
                                         PRIMARY KEY (exercise_id, muscle_part_id),
                                         FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE CASCADE,
                                         FOREIGN KEY (muscle_part_id) REFERENCES muscle_parts(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 3. Workouts (Templates/Instances)
CREATE TABLE workouts (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          description TEXT,
                          type ENUM('TEMPLATE','INSTANCE') NOT NULL DEFAULT 'TEMPLATE',
                          created_by_user_id BIGINT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          parent_template_id BIGINT NULL,
                          FOREIGN KEY (created_by_user_id) REFERENCES users(id),
                          FOREIGN KEY (parent_template_id) REFERENCES workouts(id)
) ENGINE=InnoDB;

-- 4. Workout Exercises (Ordered)
CREATE TABLE workout_exercises (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   workout_id BIGINT NOT NULL,
                                   exercise_id BIGINT NOT NULL,
                                   sort_order INT NOT NULL,
                                   notes TEXT,
                                   FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE,
                                   FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE RESTRICT,
                                   UNIQUE KEY uniq_workout_order (workout_id, sort_order)
) ENGINE=InnoDB;

-- 5. Exercise Sets (With Validation)
CREATE TABLE exercise_sets (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               workout_exercise_id BIGINT NOT NULL,
                               set_number INT NOT NULL,
                               repetitions INT NOT NULL,
                               weight_kg DECIMAL(5,2) NOT NULL,
                               rest_seconds INT,
                               FOREIGN KEY (workout_exercise_id)
                                   REFERENCES workout_exercises(id) ON DELETE CASCADE,
                               CONSTRAINT chk_reps CHECK (repetitions BETWEEN 1 AND 100),
                               CONSTRAINT chk_weight CHECK (weight_kg BETWEEN 0.25 AND 500),
                               UNIQUE KEY uniq_set_number (workout_exercise_id, set_number)
) ENGINE=InnoDB;

-- 6. User Workouts (Instance Tracking)
CREATE TABLE user_workouts (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               workout_id BIGINT NOT NULL,
                               scheduled_date DATE NOT NULL,
                               status ENUM('PLANNED','COMPLETED','SKIPPED')
                                   NOT NULL DEFAULT 'PLANNED',
                               completed_at DATETIME NULL,
                               immutable BOOLEAN NOT NULL DEFAULT FALSE,
                               FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                               FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE,
                               UNIQUE KEY uniq_user_workout_date (user_id, workout_id, scheduled_date)
) ENGINE=InnoDB;

-- 7. Recurring Schedules (Advanced)
CREATE TABLE recurring_schedules (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     user_id BIGINT NOT NULL,
                                     workout_id BIGINT NOT NULL,
                                     recurrence_rule VARCHAR(100)
                                         COMMENT 'RFC 5545 RRULE format',
                                     start_date DATE NOT NULL,
                                     end_date DATE NULL,
                                     active BOOLEAN NOT NULL DEFAULT TRUE,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                     FOREIGN KEY (workout_id) REFERENCES workouts(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Indexes for Performance
CREATE INDEX idx_user_workouts_date ON user_workouts(user_id, scheduled_date);
CREATE INDEX idx_recurring_active ON recurring_schedules(user_id, active);
CREATE INDEX idx_exercise_creator ON exercises(created_by_user_id);

ALTER TABLE workout_exercises
    ADD COLUMN muscle_part_id BIGINT NOT NULL,
    ADD FOREIGN KEY (muscle_part_id) REFERENCES muscle_parts(id);

-- Insert default muscle parts
INSERT INTO muscle_parts (name) VALUES
                                    ('Chest'),
                                    ('Back'),
                                    ('Legs'),
                                    ('Shoulders'),
                                    ('Biceps'),
                                    ('Triceps'),
                                    ('Abs'),
                                    ('Glutes'),
                                    ('Hamstrings'),
                                    ('Calves'),
                                    ('Forearms'),
                                    ('Traps'),
                                    ('Lats'),
                                    ('Quadriceps'),
                                    ('Obliques');