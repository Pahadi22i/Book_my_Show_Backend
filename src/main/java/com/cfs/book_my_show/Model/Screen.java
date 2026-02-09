package com.cfs.book_my_show.Model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "screens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Screen {

    @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
    
     @Column(nullable = false)
     private String name;

     private Integer totalSeats;

     @ManyToOne
     @JoinColumn(name = "theater_id", nullable = false)
     private Theater theater;

     @OneToMany(mappedBy = "screen" ,cascade = CascadeType.ALL)
     private List<Show> shows;

    @OneToMany(mappedBy = "screen" ,cascade = CascadeType.ALL)
     private List<Seat> seats;

}
