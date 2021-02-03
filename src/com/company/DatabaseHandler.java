package com.company;

import java.sql.*;

public class DatabaseHandler extends Configs{
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException{
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);

        return dbConnection;
    }

    public boolean checkObl(String value){
        String insert = "SELECT * FROM " + Const.OBL_TABLE + " WHERE value=(?)";
        boolean isAvailable = false;
        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, value);
            ResultSet resultSet;
            resultSet = preparedStatement.executeQuery();


            if(!resultSet.next()){
                isAvailable = true;
            }//Missing in your code

        }catch (Exception e){
            System.out.println("Ошибка в поиске обл");
        }

        return isAvailable;
    }

    public void insertObl(String value){

        String insert = "INSERT INTO " + Const.OBL_TABLE + "(" + Const.OBL_VALUE + ") VALUES(?)";

        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, value);
            preparedStatement.executeUpdate();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }
}
