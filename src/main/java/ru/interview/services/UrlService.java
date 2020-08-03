package ru.interview.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.interview.model.Url;
import ru.interview.repositories.UrlRepository;

import java.util.*;

@Service
public class UrlService {
    private UrlRepository urlRepository;
    private CurrencyService currencyService;

    @Autowired
    public void setUrlRepository(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public String getOneByDate() {
        List<Url> urls = urlRepository.findAll();
        Collections.sort(urls);
        return urls.get(urls.size()-1).getName();
    }


    public void setNewUrl(String newUrl) {
        Url url = new Url(newUrl, Calendar.getInstance().getTime());
        urlRepository.save(url);
        currencyService.initiation(Calendar.getInstance().getTime());
    }

    public List<Url> findAll() {
        return urlRepository.findAll();
    }
}
