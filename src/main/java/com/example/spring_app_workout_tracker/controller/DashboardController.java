package com.example.spring_app_workout_tracker.controller;

import com.example.spring_app_workout_tracker.dto.workout.WorkoutRequest;
import com.example.spring_app_workout_tracker.entity.User;
import com.example.spring_app_workout_tracker.entity.workout.Workout;
import com.example.spring_app_workout_tracker.service.WorkoutService;
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
@RequestMapping("api/v1/dashboard")
public class DashboardController {

    public final WorkoutService workoutService;

    @Autowired
    public DashboardController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping
    public String showDashboard(Model model, @AuthenticationPrincipal User user) {
        // Get user's workouts
        List<Workout> workouts = workoutService.getWorkoutsByUser(user);
        model.addAttribute("workouts", workouts);
        model.addAttribute("workoutRequest", new WorkoutRequest());
        return "dashboard";
    }

    @PostMapping("/workouts")
    public String createWorkout(@ModelAttribute WorkoutRequest workoutRequest,
                                BindingResult result,
                                @AuthenticationPrincipal User user,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Invalid workout data");
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
