package com.ftf.volunteeringproject.volunteer;

import com.ftf.volunteeringproject.exception.ResourceNotFoundException;
import com.ftf.volunteeringproject.registration.RegistrationDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Volunteer getVolunteerByEmail(String email) {
        return volunteerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "volunteer with email \"%s\" not found".formatted(email)
                ));
    }

    public Volunteer getCurrentVolunteer() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getVolunteerByEmail(authentication.getName());
    }

    @Transactional
    public void addVolunteer(RegistrationDto registrationDto) {
        Volunteer volunteer = Volunteer.builder()
                .email(registrationDto.email())
                .password(bCryptPasswordEncoder.encode(registrationDto.password()))
                .firstname(registrationDto.firstname())
                .surname(registrationDto.surname())
                .build();
        volunteerRepository.save(volunteer);
    }

    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }
}
