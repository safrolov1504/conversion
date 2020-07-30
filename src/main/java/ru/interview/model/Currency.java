package ru.interview.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name = "сurrency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "num_code")
    private Integer numCode;

    @Column(name = "char_code")
    private String charCode;

    @Column(name = "nominal")
    private Integer nominal;

    @Column(name = "name")
    private String name;

//    @ManyToMany
//    @JoinTable(name = "currency_сurrency_value",
//            joinColumns = @JoinColumn (name = "currency_id"),
//            inverseJoinColumns = @JoinColumn(name = "сurrency_value_id"))
//    private Collection<CurrencyValue> currencyValues;
//
}
