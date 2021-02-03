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
                boolean obl = false;
                boolean region = false;
                boolean city = false;
                boolean city_region = false;
                boolean street = false;

                // Метод вызывается когда SAXParser "натыкается" на начало тэга
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    // Если тэг имеет имя NAME, то мы этот момент отмечаем - начался тэг NAME
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

                // Метод вызывается когда SAXParser считывает текст между тэгами
                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    // Если перед этим мы отметили, что имя тэга NAME - значит нам надо текст использовать.
                    if (obl) {

                        databaseHandler.insertObl(new String(ch, start, length));
                        System.out.println("Obl Name: " + new String(ch, start, length));
                        obl = false;
                    }
                    if (region) {
                        System.out.println("Region: " + new String(ch, start, length));
                        region = false;
                    }
                    if (city) {
                        System.out.println("City: " + new String(ch, start, length));
                        city = false;
                    }
                    if (city_region) {
                        System.out.println("City Region: " + new String(ch, start, length));
                        city_region = false;
                    }
                    if (street) {
                        System.out.println("Street: " + new String(ch, start, length));
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
