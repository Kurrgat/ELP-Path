package com.example.emtechelppathbackend.scholars;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * BulkUpload
 */
record ScholarMapper(
    String scholarCode,
    String gender,
    String branch,
    String county,
    String school,
    String course,
    String institution,
    String scholar_category,
    String yearOfJoiningTertiaryProgram,
    String scholarFirstName,
    String scholarLastName,
    String donor,
    String year
) {}

public class BulkUpload {

    public static void main(String[] args) {
        List<ScholarMapper> scholars =readExcel("scholars.csv");
        String stringBuilder = getStringBuilder(scholars);
//        saveSQL("bulk_upload.sql", scholars);
    }

    public static void saveSQL(String filename, List<ScholarMapper> scholars) {
        String stringBuilder = getStringBuilder(scholars);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename));) {
            writer.write(stringBuilder);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }finally{
            System.out.println("DONE!");
        }
    }

    public static String getStringBuilder(List<ScholarMapper> scholars) {
        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("BEGIN TRANSACTION;\n");
        scholars.forEach(s ->{
            String begin = """
                INSERT INTO scholar(
                    donor,scholar_category,scholar_code,scholar_first_name,scholar_last_name,scholar_type,year_of_joining_high_school_program,year_of_joining_tertiary_program,scholar_branch,
                    scholar_tertiary_institution,scholar_high_school,gender,country_of_origin_id,home_county_id,scholardob
                  )
                    """;

            String mid = "VALUES('"+s.donor()+"','"+s.scholar_category()+"','"+s.scholarCode()+"',\""+s.scholarFirstName()+"\",\""+s.scholarLastName()+"\",'LOCAL','"+s.year()+"','"+s.yearOfJoiningTertiaryProgram()+"',(SELECT id FROM branch_details WHERE branch_name LIKE \"%"+s.branch().replaceAll("%\"", "").strip()+"\" LIMIT 1),";
            String end = "(SELECT id FROM institution WHERE name LIKE \"%" + s.institution().replaceAll("\"", "").strip() + "%\" LIMIT 1), " +
            "(SELECT id FROM school_details WHERE school_name LIKE \"%" + s.school().replaceAll("\"", "") + "%\" LIMIT 1), '" + s.gender() + "', " +
            "(SELECT id FROM country WHERE name = 'Kenya' LIMIT 1), " +
            "(SELECT id FROM kenyan_county WHERE name LIKE \"%" + s.county() + "%\" LIMIT 1), " +
            "(SELECT NOW()));\n";
            stringBuilder.append(begin).append(mid).append(end);
        });
//        stringBuilder.append("COMMIT;");
        return stringBuilder.toString();
    }

    public static String convertCSVToSQL(InputStream inputStream){
        return getStringBuilder(handleFileUpload(inputStream));
    }

    public static List<ScholarMapper> handleFileUpload(InputStream inputStream) {
        List<ScholarMapper> scholars = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String scholar_category = "WTF_Alumni"; // Default value for scholar_category
            List<String> scholarCodes = new ArrayList<>();
            String line;
            int idx = 0;
            while ((line = reader.readLine()) != null) {
                if (idx > 0) {

                    String[] data = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
                    String name = data[1];
                    String scholar_first_name = "";
                    String scholar_last_name = "";
                    if (Objects.nonNull(name) && !"".equals(line) && !name.contains("-") && !name.contains("/")) {
                        scholar_first_name = name.substring(0,name.indexOf(" ")).strip();
                        scholar_last_name = name.substring(name.indexOf(" ")).strip();
                    }

                    String yearOfJoiningTertiaryInstitution = (Integer.parseInt(data[8])+1)+"-08-01";
                    String scholarCode = data[0];
                    String donor = "";
                    String year = "";
                    if (scholarCode.contains("-") && scholarCode.split("-").length == 4) {
                        String[] codes = scholarCode.split("-");
                        donor = codes[0];
                        year = codes[1]+"-01-03";

                    }else if (scholarCode.contains("-") && scholarCode.split("-").length == 5) {
                        String[] codes = scholarCode.split("-");
                        donor = codes[0];
                        year = codes[2]+"-01-03";
                    }else if (scholarCode.contains("/")) {
                        scholarCode = scholarCode.replaceAll("/", "-");
                        year = scholarCode.substring(0,scholarCode.indexOf("-"))+"-01-03";
                    }



                    ScholarMapper scholar = new ScholarMapper(
                            scholarCode,
                            data[2],
                            data[3],
                            data[4].replaceAll("[cC]ounty","").replaceAll("\\xa0", "").strip(),
                            data[5],
                            data[9].replaceAll("Not Found", ""),
                            data[10].replaceAll("Not Found", "").strip(),

                            scholar_category,
                            yearOfJoiningTertiaryInstitution,
                            scholar_first_name.replaceAll("\"", "").strip(),
                            scholar_last_name.replaceAll("\"", "").strip(),
                            donor,
                            year
                    );

                    if (!scholarCodes.contains(scholarCode)){
                        scholars.add(scholar);
                        scholarCodes.add(scholarCode);

                    }

                }

                idx++;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return scholars;
    }
    public static List<ScholarMapper> readExcel(String filename) {
        List<ScholarMapper> scholars = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String scholar_category = "WTF_Alumni"; // Default value for scholar_category

            String line;
            int idx = 0;
            while ((line = reader.readLine()) != null) {
                if (idx > 0) {
                
                String[] data = line.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
                String name = data[1];
                String scholar_first_name = "";
                String scholar_last_name = "";
                if (Objects.nonNull(name) && !"".equals(line) && !name.contains("-") && !name.contains("/")) {
                    scholar_first_name = name.substring(0,name.indexOf(" ")).strip();
                    scholar_last_name = name.substring(name.indexOf(" ")).strip();
                }
                
                String yearOfJoiningTertiaryInstitution = (Integer.parseInt(data[8])+1)+"-08-01";
                String scholarCode = data[0];
                String donor = "";
                String year = "";
                if (scholarCode.contains("-") && scholarCode.split("-").length == 4) {
                    String[] codes = scholarCode.split("-");
                    donor = codes[0];
                    year = codes[1]+"-01-03";

                }else if (scholarCode.contains("-") && scholarCode.split("-").length == 5) {
                    String[] codes = scholarCode.split("-");
                    donor = codes[0];
                    year = codes[2]+"-01-03";
                }else if (scholarCode.contains("/")) {
                    scholarCode = scholarCode.replaceAll("/", "-");
                    year = scholarCode.substring(0,scholarCode.indexOf("-"))+"-01-03";
                }

                

                ScholarMapper scholar = new ScholarMapper(
                        scholarCode,  
                        data[2], 
                        data[3], 
                        data[4].replaceAll("[cC]ounty","").replaceAll("\\xa0", "").strip(),
                        data[5],
                        data[9].replaceAll("Not Found", ""), 
                        data[10].replaceAll("Not Found", "").strip(),  
                    
                        scholar_category, 
                        yearOfJoiningTertiaryInstitution,
                        scholar_first_name.replaceAll("\"", "").strip(),
                        scholar_last_name.replaceAll("\"", "").strip(),
                        donor,
                        year
                );
                scholars.add(scholar);
            }

                idx++;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return scholars;
    }
}
