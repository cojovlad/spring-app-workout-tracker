package com.example.spring_app_workout_tracker.controller;

import com.example.spring_app_workout_tracker.dto.workout.WorkoutRequest;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.entity.workout.Workout;
import com.example.spring_app_workout_tracker.repository.workout.MusclePartRepository;
import com.example.spring_app_workout_tracker.service.WorkoutService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final WorkoutService workoutService;
    private final MusclePartRepository musclePartRepository;

    @GetMapping
    @Transactional
    public String showDashboard(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("muscleParts", musclePartRepository.findAll());
        model.addAttribute("workouts", workoutService.getWorkoutsByUser(user));
        model.addAttribute("workoutRequest", new WorkoutRequest());
        return "dashboard";
    }

    @PostMapping("/workouts")
    public String createWorkout(@Valid @ModelAttribute WorkoutRequest workoutRequest,
                                BindingResult result,
                                @AuthenticationPrincipal User user,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.workoutRequest", result);
            redirectAttributes.addFlashAttribute("workoutRequest", workoutRequest);
            return "redirect:/api/v1/dashboard";
        }

        try {
            workoutService.createWorkoutTemplate(workoutRequest, user);
            redirectAttributes.addFlashAttribute("success", "Workout created successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/api/v1/dashboard";
    }
}
