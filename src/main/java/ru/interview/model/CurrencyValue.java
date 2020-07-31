package ru.interview.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "сurrency_value")
public class CurrencyValue implements Comparable<CurrencyValue>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private double value;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @Override
    public int compareTo(CurrencyValue o) {
        return o.getDate().compareTo(this.date);
    }
}
