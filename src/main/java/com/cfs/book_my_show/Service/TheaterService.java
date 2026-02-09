package com.cfs.book_my_show.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfs.book_my_show.DTO.TheaterDto;
import com.cfs.book_my_show.Exceptions.ResourseNotFound;
import com.cfs.book_my_show.Model.Theater;
import com.cfs.book_my_show.Repository.TheaterRepository;

@Service // Spring ko batata hai ki ye business logic wali class hai
public class TheaterService {

    @Autowired // Repository ka object automatic mil jayega (Dependency Injection)
    private TheaterRepository theaterRepository;

    // --- 1. CREATE THEATER (Naya Theater add karna) ---
    public TheaterDto createTheater(TheaterDto theaterDto) {
        // Step 1: DTO (jo user ne bheja) ko Entity (jo DB samjhta hai) mein badla
        Theater theater = mapToEntity(theaterDto);

        // Step 2: Database mein save kiya
        Theater savedTheater = theaterRepository.save(theater);

        // Step 3: Save hone ke baad wapas DTO banakar return kiya
        return mapToDto(savedTheater);
    }

    // --- 2. GET THEATER BY ID (Id se ek theater dhundna) ---
    public TheaterDto getTheaterById(Long id) {
        // Step 1: Database mein dhundo. Agar nahi mila to Error feko.
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourseNotFound("Theater not found with id: " + id));

        // Step 2: Mil gaya to DTO mein convert karke return karo
        return mapToDto(theater);
    }

    // --- 3. GET ALL THEATERS (Saare theaters ki list lana) ---
    public List<TheaterDto> getAllTheaters() {
        // Step 1: DB se saari entities nikali
        List<Theater> theaters = theaterRepository.findAll();

        // Step 2: Stream use karke ek-ek Theater ko DTO mein badla
        return theaters.stream()
                .map(this::mapToDto) // Har theater par mapToDto function chalega
                .collect(Collectors.toList()); // Wapas List bana di
    }

    // --- 4. GET THEATERS BY CITY (City ke hisab se theaters lana) ---
    // Note: Iske liye Repository mein findByCity method hona zaroori hai
    public List<TheaterDto> getAllTheaterByCity(String city) {
        List<Theater> theaters = theaterRepository.findByCity(city);

        // Agar list khali hai, to aap chaho to error fek sakte ho, ya khali list return
        // kar sakte ho.
        // Filhal hum convert karke return kar rahe hain.
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // --- 5. UPDATE THEATER (Theater ki details update karna) ---
    public TheaterDto updateTheater(Long id, TheaterDto theaterDto) {
        // Step 1: Pehle check karo theater exist karta hai ya nahi
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourseNotFound("Theater not found with id: " + id));

        // Step 2: Purane object mein naya data set karo
        theater.setName(theaterDto.getName());
        theater.setAddress(theaterDto.getAddress());
        theater.setCity(theaterDto.getCity());
        theater.setTotalScreen(theaterDto.getTotalScreen());

        // Step 3: Wapas save karo (Hibernate update query chalayega)
        Theater updatedTheater = theaterRepository.save(theater);

        return mapToDto(updatedTheater);
    }

    // --- 6. DELETE THEATER (Theater delete karna) ---
    public void deleteTheater(Long id) {
        // Step 1: Pehle dhundo
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new ResourseNotFound("Theater not found with id: " + id));

        // Step 2: Delete kar do
        theaterRepository.delete(theater);
    }

    // --- HELPER METHOD: Entity -> DTO (Database se bahar aate waqt) ---
    private TheaterDto mapToDto(Theater theater) {
        TheaterDto theaterDto = new TheaterDto();
        theaterDto.setId(theater.getId());
        theaterDto.setName(theater.getName());
        theaterDto.setAddress(theater.getAddress());
        theaterDto.setCity(theater.getCity());
        theaterDto.setTotalScreen(theater.getTotalScreen()); // Method name check kar lena Model mein
        return theaterDto;
    }

    // --- HELPER METHOD: DTO -> Entity (Database mein jate waqt) ---
    private Theater mapToEntity(TheaterDto theaterDto) {
        Theater theater = new Theater();
        theater.setName(theaterDto.getName());
        theater.setAddress(theaterDto.getAddress());
        theater.setCity(theaterDto.getCity());
        theater.setTotalScreen(theaterDto.getTotalScreen());
        // ID set nahi karte, wo Database khud generate karta hai
        return theater;
    }
}