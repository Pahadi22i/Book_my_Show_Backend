package com.cfs.book_my_show.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {


     private Long id;

     private String title;

     private String language;

     private Integer durationMins;

     private String PosterUrl;

     private String genre;

     private String releaseDate;

     private String description;
    
}
