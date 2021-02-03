package com.company;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XMLParser {
    // Имя файла
    public static void parse(String file) {

        DatabaseHandler databaseHandler = new DatabaseHandler();
        final String fileName = file;

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // Здесь мы определили анонимный класс, расширяющий класс DefaultHandler
            DefaultHandler handler = new DefaultHandler() {
                // Поле для указания, что тэг NAME начался
                boolean record = false;
                boolean obl = false;
                boolean region = false;
                boolean city = false;
                boolean city_region = false;
                boolean street = false;


                // Метод вызывается когда SAXParser "натыкается" на начало тэга
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    // Если тэг имеет имя ???, то мы этот момент отмечаем - начался тэг ???
                    if (record) {
                        if (qName.equalsIgnoreCase("OBL_NAME")) {
                            obl = true;
                        }
                        if (qName.equalsIgnoreCase("REGION_NAME")) {
                            region = true;
                        }
                        if (qName.equalsIgnoreCase("CITY_NAME")) {
                            city = true;
                        }
                        if (qName.equalsIgnoreCase("CITY_REGION_NAME")) {
                            city_region = true;
                        }
                        if (qName.equalsIgnoreCase("STREET_NAME")) {
                            street = true;
                        }
                    }
                }

                // Метод вызывается когда SAXParser считывает текст между тэгами
                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    // Если перед этим мы отметили, что имя тэга NAME - значит нам надо текст использовать.
                    if (obl) {
                        String oblName = new String(ch, start, length);
                        if(oblName.equals("")){}
                        else {
                            if(databaseHandler.check(Const.OBL_TABLE ,oblName)){
                                databaseHandler.insert(Const.OBL_TABLE, Const.OBL_VALUE, oblName);
                                System.out.println("Добавлено в бд "+ oblName);
                            }
                        }
                        obl = false;
                    }
                    if (region) {
                        String regionName = new String(ch, start, length);
                        if(regionName.equals("")){}
                        else{
                            if(databaseHandler.check(Const.REGION_TABLE, regionName));{
                                databaseHandler.insert(Const.REGION_TABLE, Const.REGION_VALUE, regionName);
                                System.out.println("Добавлено в бд: " +  regionName);
                            }
                        }
                        region = false;
                    }
                    if (city) {
                        String cityName = new String(ch, start, length);
                        if(cityName.equals("")){}
                        else {
                            if(databaseHandler.check(Const.CITY_TABLE, cityName)){
                                databaseHandler.insert(Const.CITY_TABLE, Const.CITYS_VALUE, cityName);
                                System.out.println("Добавлено в бд: " + cityName);
                            }
                        }
                        city = false;
                    }
                    if (city_region) {
                        String cityRegionName = new String(ch, start, length);
                        if(cityRegionName.equals("")){}
                        else{
                            if(databaseHandler.check(Const.CITY_REGION_TABLE, cityRegionName)){
                                databaseHandler.insert(Const.CITY_REGION_TABLE, Const.CITY_REGIONS_VALUE, cityRegionName);
                                System.out.println("Добавлено в бд: " + cityRegionName);
                            }
                        }
                        city_region = false;
                    }
                    if (street) {
                        String streetName = new String(ch, start, length);
                        if(streetName.equals("")){}
                        else{
                            if(databaseHandler.check(Const.STREET_TABLE, streetName)){
                                databaseHandler.insert(Const.STREET_TABLE, Const.STREET_VALUE, streetName);
                                System.out.println("Добавлено в бд: " + streetName);
                            }
                        }
                        street = false;
                    }
                }
            };

            // Стартуем разбор методом parse, которому передаем наследника от DefaultHandler, который будет вызываться в нужные моменты
            saxParser.parse(fileName, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
