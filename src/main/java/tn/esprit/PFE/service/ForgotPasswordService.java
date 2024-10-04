package tn.esprit.PFE.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.PFE.controller.UserNotFoundException;
import tn.esprit.PFE.entities.User;
import tn.esprit.PFE.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import tn.esprit.PFE.utils.EmailServiceImpl;
import tn.esprit.PFE.utils.ForgotPasswordDto;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IUserservice userService;
    @Autowired
    private EmailServiceImpl emailService;

    public void resetPassword(ForgotPasswordDto forgotPasswordDto) {
       User user = userRepository.findByEmail(forgotPasswordDto.getEmail());
        if (user == null) {
    throw new UserNotFoundException("User not found");
        }

         //generate new password
        String newPassword = generateNewPassword();

         //update user password
        userService.updatePassword(Math.toIntExact(user.getId()), newPassword);

        // send password reset email
        sendPasswordResetEmail(user.getEmail(), newPassword);
    }

    public String generateNewPassword() {
        // generate new password
        //random password that consists of 10 alphanumeric characters
        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        return newPassword;
    }

    public void sendPasswordResetEmail(String email, String newPassword) {
        // send password reset email
        emailService.sendSimpleEmail(email, "Password Reset","Your new password is: " + newPassword);
    }
}
