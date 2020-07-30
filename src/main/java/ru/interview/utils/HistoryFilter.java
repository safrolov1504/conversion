package ru.interview.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import ru.interview.model.Currency;
import ru.interview.model.History;
import ru.interview.repositories.HistorySpecifications;
import ru.interview.services.CurrencyService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Getter
public class HistoryFilter {
    private Specification<History> spec;

    public HistoryFilter(Map<String, String> requestParam) throws ParseException {
        spec = Specification.where(null);

        //добавить проверку правильности ввода
        if(requestParam.containsKey("date_from_search")){
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(requestParam.get("date_from_search"));
            spec = spec.and(HistorySpecifications.fromThisDate(date));
        }

        if(requestParam.containsKey("date_to_search")){
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(requestParam.get("date_to_search"));
            spec = spec.and(HistorySpecifications.fromTillThisDate(date));
        }

    }




}
