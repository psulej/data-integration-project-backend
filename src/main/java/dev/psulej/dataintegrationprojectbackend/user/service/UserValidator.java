//package dev.psulej.dataintegrationprojectbackend.user.service;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserValidator {
//    private final UserRepository userRepository;
//
//    public UserValidator(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    void validate(RegisterRequest request) {
//        List<ApplicationError> errors = new ArrayList<>();
//
//        if (userRepository.findByLoginIgnoreCase(request.login()).isPresent()) {
//            errors.add(ApplicationError.LOGIN_EXISTS);
//        }
//        if (userRepository.findByEmailIgnoreCase(request.email()).isPresent()) {
//            errors.add(ApplicationError.EMAIL_EXISTS);
//        }
//        if (!errors.isEmpty()) {
//            throw new ApplicationException(errors);
//        }
//    }
//}
