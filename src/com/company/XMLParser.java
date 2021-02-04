package com.company;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.regex.Pattern;

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
                    // Если тэг имеет имя ???, то мы этот момент отмечаем - начался тэг ???
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

                Integer oblId = null;
                Integer regionId = null;
                Integer cityId = null;
                Integer cityRegionId = null;
                Integer streetId = null;

                // Метод вызывается когда SAXParser считывает текст между тэгами
                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    // Если перед этим мы отметили, что имя тэга NAME - значит нам надо текст использовать.

                        if (obl) {
                            String oblName = new String(ch, start, length);
                            if (check(oblName)) {
                                if (databaseHandler.check(Const.OBL_TABLE, oblName)) {
                                    databaseHandler.insert(Const.OBL_TABLE, Const.OBL_VALUE, oblName);
                                    System.out.println("Добавлено в бд " + oblName);
                                }
                            }

                            oblId = databaseHandler.searchId(Const.OBL_TABLE, oblName);
                            System.out.println("Obl ID: " + oblId);
                            obl = false;
                        }

                        if (region) {
                            String regionName = new String(ch, start, length);
                            if (check(regionName)) {
                                if (databaseHandler.check(Const.REGION_TABLE, regionName)) {
                                    databaseHandler.insert(Const.REGION_TABLE, Const.REGION_VALUE, regionName);
                                    System.out.println("Добавлено в бд: " + regionName);
                                }
                            }

                            regionId = databaseHandler.searchId(Const.REGION_TABLE, regionName);
                            System.out.println("Region ID: " + regionId);
                            region = false;
                        }
                        if (city) {
                            String cityName = new String(ch, start, length);
                            if (check(cityName)) {
                                if (databaseHandler.check(Const.CITY_TABLE, cityName)) {
                                    databaseHandler.insert(Const.CITY_TABLE, Const.CITYS_VALUE, cityName);
                                    System.out.println("Добавлено в бд: " + cityName);
                                }
                            }

                            cityId = databaseHandler.searchId(Const.CITY_TABLE, cityName);
                            System.out.println("City ID: " + cityId);
                            city = false;
                        }
                        if (city_region) {
                            String cityRegionName = new String(ch, start, length);
                            if (check(cityRegionName)) {
                                if (databaseHandler.check(Const.CITY_REGION_TABLE, cityRegionName)) {
                                    databaseHandler.insert(Const.CITY_REGION_TABLE, Const.CITY_REGIONS_VALUE, cityRegionName);
                                    System.out.println("Добавлено в бд: " + cityRegionName);
                                }
                            }

                            cityRegionId = databaseHandler.searchId(Const.CITY_REGION_TABLE, cityRegionName);
                            System.out.println("CityRegion ID: " + cityRegionId);
                            city_region = false;
                        }
                        if (street) {
                            String streetName = new String(ch, start, length);
                            if (check(streetName)) {
                                if (databaseHandler.check(Const.STREET_TABLE, streetName)) {
                                    databaseHandler.insert(Const.STREET_TABLE, Const.STREET_VALUE, streetName);
                                    System.out.println("Добавлено в бд: " + streetName);
                                }
                            }

                            streetId = databaseHandler.searchId(Const.STREET_TABLE, streetName);
                            System.out.println("Street ID: " + streetId);
                            street = false;

                            if(oblId!=null||regionId!=null||cityId!=null||cityRegionId!=null||streetId!=null){
                                databaseHandler.record(oblId, regionId, cityId, cityRegionId, streetId);
                                oblId=null;
                                regionId=null;
                                cityId=null;
                                cityRegionId=null;
                                streetId=null;
                            }
                        }
                }
            };

            // Стартуем разбор методом parse, которому передаем наследника от DefaultHandler, который будет вызываться в нужные моменты
            saxParser.parse(fileName, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static boolean check(String str) {

        boolean result = false;
        if (str.contains("а") || str.contains("б") || str.contains("в") || str.contains("г") || str.contains("д") || str.contains("е") || str.contains("ё") || str.contains("ж") || str.contains("з") || str.contains("и") || str.contains("й") || str.contains("к") || str.contains("л") || str.contains("м") || str.contains("н") || str.contains("о") || str.contains("п") || str.contains("р") || str.contains("с") || str.contains("т") || str.contains("у") || str.contains("ф") || str.contains("х") || str.contains("ц") || str.contains("ч") || str.contains("ш") || str.contains("щ") || str.contains("э"))
            result = true;

        return result;

    }

}
