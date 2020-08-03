package ru.interview.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "url")
public class Url implements Comparable<Url>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "update_date")
    private Date updateDate;

    @Override
    public int compareTo(Url o) {
        return this.id.compareTo(o.getId());
    }

    public Url(String name, Date updateDate) {
        this.name = name;
        this.updateDate = updateDate;
    }
}
