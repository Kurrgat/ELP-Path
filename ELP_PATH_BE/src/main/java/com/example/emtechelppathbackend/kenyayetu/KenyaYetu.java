package com.example.emtechelppathbackend.kenyayetu;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class KenyaYetu {
    public static void main(String[] args) {
        String inputFile = "C:\\Users\\Sato\\Desktop\\txt\\country.txt"; // Path to the input text file
        String outputFile = "C:\\Users\\Sato\\Desktop\\txt\\insert_data.sql"; // Path to the output MySQL file

        try {
            List<String> insertStatements = generateInsertStatements(inputFile);
            writeInsertStatementsToFile(insertStatements, outputFile);
            System.out.println("SQL file created successfully!");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<String> generateInsertStatements(String inputFile) throws IOException, JSONException {
        List<String> insertStatements = new ArrayList<>();

        StringBuilder jsonStringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        String line;
        while ((line = reader.readLine()) != null) {
            jsonStringBuilder.append(line);
        }
        reader.close();

        JSONArray jsonArray = new JSONArray(jsonStringBuilder.toString());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString("name");
            String country_code = jsonObject.getString("code");
            String phone_code = jsonObject.getString("dial_code");

            String insertStatement = String.format("INSERT INTO country (name, country_code, dial_code) VALUES ('%s', '%s', '%s');",
                    name, country_code, phone_code);
            insertStatements.add(insertStatement);
        }

        return insertStatements;
    }

    private static void writeInsertStatementsToFile(List<String> insertStatements, String outputFile) throws IOException {
        FileWriter writer = new FileWriter(outputFile);
        for (String statement : insertStatements) {
            writer.write(statement + "\n");
        }
        writer.close();
    }
}
