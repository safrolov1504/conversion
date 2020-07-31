package ru.interview.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "history")
public class History implements Comparable<History>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cur1_id")
    private Currency currency1;

    @ManyToOne
    @JoinColumn(name = "cur2_id")
    private Currency currency2;

    @Column(name = "count_cur1")
    private Double countCur1;

    @Column(name = "count_cur2")
    private Double countCur2;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public int compareTo(History o) {
        return o.getDate().compareTo(this.date);
    }
}
