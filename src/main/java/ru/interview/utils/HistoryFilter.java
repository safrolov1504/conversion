package ru.interview.utils;

import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import ru.interview.model.Currency;
import ru.interview.model.History;
import ru.interview.model.User;
import ru.interview.repositories.HistorySpecifications;
import ru.interview.services.CurrencyService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Getter
public class HistoryFilter {
    private Specification<History> spec;
    private String error;
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


    public HistoryFilter(Map<String, String> requestParam, CurrencyService currencyService, User user) {
        error = null;
        //добавялем фильтр дата от
        spec = Specification.where(null);
        try {
            if(requestParam.containsKey("date_from_search")){
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(requestParam.get("date_from_search"));
                spec = spec.and(HistorySpecifications.fromThisDate(date));
            }
        } catch (ParseException e) {
            if(!requestParam.get("date_from_search").equals("")){
                error = "Проверьте правильность введенных данных. Формат ввода даты YYYY-MM-DD";
            }
        }

        //добавялем проверку дата до
        try {
            if(requestParam.containsKey("date_to_search")){
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(requestParam.get("date_to_search"));
                spec = spec.and(HistorySpecifications.fromTillThisDate(date));
            }
        } catch (ParseException e) {
            if(!requestParam.get("date_to_search").equals("")){
                error = "Проверьте правильность введенных данных. Формат ввода даты YYYY-MM-DD";
            }
        }

        //добавляем проверку валюта из
        Currency currencyFrom = null;
        if(requestParam.containsKey("currency_from_search")){
            //достаем валюты из которой переводят
            String currencyFromS = requestParam.get("currency_from_search");
            String[] s = currencyFromS.split(" ");
            currencyFromS = s[0];
            currencyFrom = currencyService.findOneByCharCode(currencyFromS);
        }
        if(currencyFrom != null){
            spec = spec.and(HistorySpecifications.equalCurrencyFrom(currencyFrom));
        }

        //добавляем проверку валюта в
        Currency currencyTo = null;
        if(requestParam.containsKey("currency_to_search")){
            //достаем валюты из которой переводят
            String currencyToS = requestParam.get("currency_to_search");
            String[] s = currencyToS.split(" ");
            currencyToS = s[0];
            currencyTo = currencyService.findOneByCharCode(currencyToS);
        }
        if(currencyTo != null){
            spec = spec.and(HistorySpecifications.equalCurrencyTo(currencyTo));
        }

        //добавялем проверку пользователя
        if(user != null){
            spec = spec.and(HistorySpecifications.equalUser(user));
        }
    }
}
