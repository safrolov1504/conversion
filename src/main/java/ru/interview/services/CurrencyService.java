package ru.interview.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.interview.model.Currency;
import ru.interview.model.CurrencyValue;
import ru.interview.model.Url;
import ru.interview.repositories.CurrencyRepository;
import ru.interview.repositories.CurrencyValueRepository;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CurrencyService  {
    private CurrencyRepository currencyRepository;
    private CurrencyValueRepository currencyValueRepository;
    private RoleService roleService;
    private UrlService urlService;
    private String urlFirstPart;
    private String errorUrl;

    @Autowired
    public void setCurrencyRepository(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }
    @Autowired
    public void setCurrencyValueRepository(CurrencyValueRepository currencyValueRepository) {
        this.currencyValueRepository = currencyValueRepository;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setUrlService(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostConstruct
    public void init() {

        initiation(Calendar.getInstance().getTime());
        initUser();
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    private void initUrl(){
        if(urlService.findAll().isEmpty()){
            urlService.setNewUrl("null");
        }
    }

    private void initUser() {
        if(roleService.findAll().isEmpty()){
            //если при включении база данных ролей пуская, то значит нет администратора и ролей в БД
            //добавляим роли  ROLE_ADMIN и ROLE_USER и админа по-умолчанию с паролем 100 и именем admin
            roleService.addRoleAndAdmin();
        }
    }

    public void initiation(Date date){
        //проверяем если информация в БД о валюте сегодня.
        // Возвращает true если информации нет и ее надо обновить
        initUrl();
        errorUrl = null;
        this.urlFirstPart = urlService.getOneByDate();
        if(checkCurrencyToday(date)){

            //создаем ссылку в ЦБ с текущей датой
            StringBuilder urlCbr = new StringBuilder();

            //this.date = Calendar.getInstance().getTime();
//            urlCbr.append("http://www.cbr.ru/scripts/XML_daily.asp?date_req=");
            urlCbr.append(urlFirstPart);
            urlCbr.append(new SimpleDateFormat("dd/MM/yyyy").format(date));

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;

            try {
                URL url = new URL(urlCbr.toString());
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(url.openStream()));


                document.getDocumentElement().normalize();
                    // теперь XML полностью загружен в память
                    // в виде объекта Document
                NodeList nodeList = document.getElementsByTagName("Valute");

                    //обрабатываем всю остальную валюту
                CurrencyValue currencyValue;
                Currency currency;

                if(nodeList.getLength() != 0) {
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            Integer numCode = Integer.parseInt(element.getElementsByTagName("NumCode").item(0).getTextContent());

                            //проверяем если ли такая валюта уже в БД
                            currency = currencyRepository.findOneByNumCode(numCode);
                            if (currency == null) {
                                //добавляем новую валюту в БД, если ее нет
                                currency = new Currency();
                                currency.setNumCode(Integer.parseInt(element.getElementsByTagName("NumCode").item(0).getTextContent()));
                                currency.setCharCode(element.getElementsByTagName("CharCode").item(0).getTextContent());
                                currency.setNominal(Integer.parseInt(element.getElementsByTagName("Nominal").item(0).getTextContent()));
                                currency.setName(element.getElementsByTagName("Name").item(0).getTextContent());
                                currencyRepository.save(currency);
                            }
                            //добавялем курсы валют
                            currencyValue = new CurrencyValue();
                            currencyValue.setDate(date);
                            currencyValue.setValue(Double.parseDouble(element.getElementsByTagName("Value").item(0).getTextContent()
                                    .replace(',', '.')));
                            currencyValue.setCurrency(currency);
                            currencyValueRepository.save(currencyValue);
                        }

                    }

                    //обрабатываем рубль
                    addRub(date);
                } else {
                    //должна быть какая то ссылка на ошибку вввода ссылки
                    addErrorUrl();
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                addErrorUrl();
            } catch (IOException e) {
                e.printStackTrace();
                addErrorUrl();
            } catch (SAXException e) {
                e.printStackTrace();
                addErrorUrl();
            }
        }
    }

    private void addErrorUrl(){
        errorUrl = "Ошибка доступа к сайту ЦБ. Сообщите администратору";
    }

    //метод добавляет всю информацию о рубле
    private void addRub(Date date) {
        Currency currency = currencyRepository.findOneByNumCode(643);
        if(currency == null) {
            currency = new Currency();
            currency.setNumCode(643);
            currency.setCharCode("RUB");
            currency.setNominal(1);
            currency.setName("Российский рубль");
            currencyRepository.save(currency);
        }

        CurrencyValue currencyValue = new CurrencyValue();
        currencyValue.setCurrency(currency);
        currencyValue.setValue(1);
        currencyValue.setDate(date);
        currencyValueRepository.save(currencyValue);
    }

    //проверяем, если актуальная информация о валютах в БД
    public boolean checkCurrencyToday(Date date){
        //запрашиваем все данные о валюте сегодня
        List<CurrencyValue> currencyValues = currencyValueRepository.findAllByDate(date);

        if (currencyValues.size() == 0){
            return true;
        } else {
            return false;
        }
    }

    //считает результат перевода
    public Double result(String currencyFrom, String currencyTo, Double countFrom){
        Date date = Calendar.getInstance().getTime();
        Double currencyValueFrom = getCurrencyValue(currencyFrom,date);
        Double currencyValueTo = getCurrencyValue(currencyTo,date);
        Double result = (currencyValueFrom * countFrom)/currencyValueTo;

        return result;
    }

    private Double getCurrencyValue(String currencyCahrCode, Date date){
        Currency currency = findOneByCharCode(currencyCahrCode);
        CurrencyValue currencyValue = currencyValueRepository.findOneByCurrencyAndDate(currency,date);
        return currencyValue.getValue()/currency.getNominal();
    }

    public List<Currency> getAllCurrencies(){
        return currencyRepository.findAll();
    }

    public Currency findOneByCharCode(String currencyCahrCode){
        return currencyRepository.findOneByCharCode(currencyCahrCode);
    }

    public List<Currency> findAllByCharCode(String currency_statistic) {
        return currencyRepository.findAllByCharCode(currency_statistic);
    }
}
