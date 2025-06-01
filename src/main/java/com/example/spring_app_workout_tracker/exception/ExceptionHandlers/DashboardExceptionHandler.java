package com.example.spring_app_workout_tracker.exception.ExceptionHandlers;

import com.example.spring_app_workout_tracker.dto.ErrorResponse;
import com.example.spring_app_workout_tracker.exception.CustomAppException;
import com.example.spring_app_workout_tracker.exception.workout.ExerciseNotFoundException;
import com.example.spring_app_workout_tracker.exception.workout.MusclePartNotFoundException;
import com.example.spring_app_workout_tracker.exception.workout.WorkoutExerciseNotFound;
import com.example.spring_app_workout_tracker.exception.workout.WorkoutNotFoundException;
import com.example.spring_app_workout_tracker.util.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.internal.build.AllowNonPortable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

import static com.example.spring_app_workout_tracker.util.MessageKeys.ERROR;

@ControllerAdvice
public class DashboardExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public DashboardExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CustomAppException.class)
    public String handleCustomException(CustomAppException ex,
                                        RedirectAttributes redirectAttributes,
                                        Locale locale) {

        String message = messageSource.getMessage(ex.getMessageKey(), ex.getArgs(), locale);

        redirectAttributes.addFlashAttribute(ERROR, message);

        return "redirect:/api/v1/dashboard";
    }

    @ExceptionHandler(WorkoutNotFoundException.class)
    public String handleWorkoutNotFoundException(WorkoutNotFoundException workoutNotFoundException,
                                                 RedirectAttributes redirectAttributes,
                                                 Locale locale) {

        String message = messageSource.getMessage(workoutNotFoundException.getMessage(), null, locale);
        redirectAttributes.addFlashAttribute(ERROR, message);

        return "redirect:/api/v1/dashboard";
    }

    @ExceptionHandler(ExerciseNotFoundException.class)
    public String handleExerciseNotFoundException(ExerciseNotFoundException exerciseNotFoundException,
                                                 RedirectAttributes redirectAttributes,
                                                 Locale locale) {

        String message = messageSource.getMessage(exerciseNotFoundException.getMessage(), null, locale);
        redirectAttributes.addFlashAttribute(ERROR, message);

        return "redirect:/api/v1/dashboard";
    }

    @ExceptionHandler(MusclePartNotFoundException.class)
    public String handleMusclePartNotFoundException(MusclePartNotFoundException musclePartNotFoundException,
                                                 RedirectAttributes redirectAttributes,
                                                 Locale locale) {

        String message = messageSource.getMessage(musclePartNotFoundException.getMessage(), null, locale);
        redirectAttributes.addFlashAttribute(ERROR, message);

        return "redirect:/api/v1/dashboard";
    }

    @ExceptionHandler(WorkoutExerciseNotFound.class)
    public String handleWorkoutNotFoundException(WorkoutExerciseNotFound workoutExerciseNotFound,
                                                 RedirectAttributes redirectAttributes,
                                                 Locale locale){

        String message = messageSource.getMessage(workoutExerciseNotFound.getMessage(), null, locale);
        redirectAttributes.addFlashAttribute(ERROR, message);
        return "redirect:/api/v1/dashboard";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex,
                                RedirectAttributes redirectAttributes,
                                Locale locale) {

        String localizedMessage;

        if (ex.getMessage() != null) {
            localizedMessage = messageSource.getMessage("error.unexpected", new Object[]{ex.getMessage()}, locale);
        } else {
            localizedMessage = messageSource.getMessage("error.unexpected", null, locale);
        }

        redirectAttributes.addFlashAttribute(ERROR, localizedMessage);

        return "redirect:/api/v1/dashboard";
    }
}
