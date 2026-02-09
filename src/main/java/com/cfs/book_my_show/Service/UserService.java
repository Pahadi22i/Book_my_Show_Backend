package com.cfs.book_my_show.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfs.book_my_show.DTO.UserDto;
import com.cfs.book_my_show.Exceptions.ResourseNotFound;
import com.cfs.book_my_show.Model.User;
import com.cfs.book_my_show.Repository.UserRepository;

// @Service batata hai Spring Boot ko ki ye class business logic handle karegi
// Aur iska object Spring khud banayega (Bean).
@Service
public class UserService {

    // @Autowired ka matlab: Hame UserRepository ka object chahiye.
    // Spring Boot automatically repository ka object yahan inject kar dega.
    @Autowired
    private UserRepository userRepository;

    // --- CREATE USER ---
    public UserDto createUser(UserDto userDto) {
        // Step 1: Frontend se DTO aaya, usko Entity mein convert kiya
        // kyuki Repository sirf Entity ko database mein save kar sakta hai.
        User user = mapToEntity(userDto);

        // Step 2: Database mein save kiya. 'saveUser' wo object hai jo save hone ke
        // baad mila.
        User saveUser = userRepository.save(user);

        // Step 3: Wapas Frontend ko bhejne ke liye, Entity ko wapas DTO banaya.
        return mapToDto(saveUser);
    }

    // --- UPDATE USER ---
    public UserDto updateUser(Long id, UserDto userDto) {
        // Step 1: Pehle check karo user database mein hai ya nahi.
        // findById optional return karta hai, agar nahi mila to Error feko
        // (ResourseNotFound).
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourseNotFound("User not Found with id " + id));

        // Step 2: Purane user (Entity) mein naya data (DTO se) set karo.
        // Hum ID change nahi karte, sirf details update karte hain.
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());

        // Step 3: Wapas save karo. Save method update ka kaam bhi karta hai agar ID
        // same ho.
        User updatedUser = userRepository.save(user);

        // Step 4: Updated data ko DTO bana ke wapas bhejo.
        return mapToDto(updatedUser);
    }

    // --- DELETE USER ---
    public void deleteUser(Long id) {
        // Step 1: Pehle dhundo ki user exist karta hai ya nahi.
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourseNotFound("User not Found with id " + id));

        // Step 2: Agar mil gaya, toh delete kar do.
        userRepository.delete(user);
    }

    // --- GET SINGLE USER ---
    public UserDto getUserDto(Long id) {
        // Step 1: Database se Entity nikalo ID ke basis pe.
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourseNotFound("User not Found with id " + id));

        // Step 2: Entity ko DTO mein convert karke return karo.
        return mapToDto(user);
    }

    // --- GET ALL USERS ---
    public List<UserDto> getAllUsers() {
        // Step 1: Saare users (Entities) ko database se nikalo.
        List<User> users = userRepository.findAll();

        // Step 2: Ye thoda advanced Java 8 concept hai (Stream API).
        // Hum har ek Entity ko ek-ek karke DTO mein convert kar rahe hain.
        return users.stream()
                .map(this::mapToDto) // Har user pe mapToDto function call hoga
                .collect(Collectors.toList()); // End mein wapas List bana diya
    }

    // --- HELPER METHOD: DTO -> ENTITY ---
    // Iska use tab hota hai jab data ANDAR (Database ki taraf) ja raha ho.
    private User mapToEntity(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        // Note: Hum yahan ID set nahi karte, kyuki ID database auto-generate karta hai.
        return user;
    }

    // --- HELPER METHOD: ENTITY -> DTO ---
    // Iska use tab hota hai jab data BAHAR (User ki taraf) ja raha ho.
    private UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId()); // Yahan ID zaruri hai taaki user ko pata chale uski ID kya hai.
        userDto.setEmail(user.getEmail());
        userDto.setName(user.getName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }

}