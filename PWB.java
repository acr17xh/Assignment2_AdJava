import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PWB {

    public static void main(String[] args){
        try {
//            BufferedReader input1 = new BufferedReader(new InputStreamReader(System.in));
//            System.out.println("Please input the first file name: ");
//            String instr1 = input1.readLine();
            BufferedReader br1 = new BufferedReader(new FileReader("src/winequality-red.csv"));

//            BufferedReader input2 = new BufferedReader(new InputStreamReader(System.in));
//            System.out.println("Please input the second file name: ");
//            String instr2 = input2.readLine();
            BufferedReader br2 = new BufferedReader(new FileReader("src/winequality-white.csv"));


//            BufferedReader input3 = new BufferedReader(new InputStreamReader(System.in));
//            System.out.println("Please input the query file name: ");
//            String instr3 = input3.readLine();
            BufferedReader br3 = new BufferedReader(new FileReader("src/queries.txt"));


            ArrayList<Wine> wines = new ArrayList<>();
            int id = 1;

            String str1 = br1.readLine();
            while (str1 != null){

                str1 = br1.readLine();
                if (str1 != null) {
                    //This if is used for prevent str1 become null when read to the end of the stream

                    String spl = str1;
                    String[] item;
                    item = spl.split(";");
                    Double[] item_double = new Double[item.length];
                    //Use split to divide every single line with ";"
                    //define item_double to store the Double type array


                    for (int i = 0; i < 12; ) {
                        double value = Double.valueOf(item[i]);
                        item_double[i] = value;
                        i++;
                    }
                    //Transform String type to Double type in item array, then store in a new array item_double

                    Wine wine = new Wine();
                    wine.setF_acid(item_double[0]);
                    wine.setV_acid(item_double[1]);
                    wine.setC_acid(item_double[2]);
                    wine.setR_sugar(item_double[3]);
                    wine.setChlorid(item_double[4]);
                    wine.setF_sulf(item_double[5]);
                    wine.setT_sulf(item_double[6]);
                    wine.setDens(item_double[7]);
                    wine.setpH(item_double[8]);
                    wine.setSulph(item_double[9]);
                    wine.setAlc(item_double[10]);
                    wine.setQual(item_double[11]);

                    wine.setID(id);
                    wine.setWineKind("red");

                    wines.add(wine);
                    id = id + 1;
                }
                else {
                    break;
                }
            }

            String str2 = br2.readLine();
            while (str2 != null){
                str2 = br2.readLine();

                if (str2 != null) {
                    String spl = str2;
                    String[] item;
                    item = spl.split(";");
                    Double[] item_double = new Double[item.length];

                    for (int i = 0; i < 12; ) {
                        double value = Double.valueOf(item[i]);
                        item_double[i] = value;
                        i++;
                    }

                    Wine wine = new Wine();
                    wine.setF_acid(item_double[0]);
                    wine.setV_acid(item_double[1]);
                    wine.setC_acid(item_double[2]);
                    wine.setR_sugar(item_double[3]);
                    wine.setChlorid(item_double[4]);
                    wine.setF_sulf(item_double[5]);
                    wine.setT_sulf(item_double[6]);
                    wine.setDens(item_double[7]);
                    wine.setpH(item_double[8]);
                    wine.setSulph(item_double[9]);
                    wine.setAlc(item_double[10]);
                    wine.setQual(item_double[11]);

                    wine.setID(id);
                    wine.setWineKind("white");

                    wines.add(wine);
                    id = id + 1;
                }
                else {
                    break;
                }
            }


            ArrayList<String> queries1 = new ArrayList<>();
            String str3 = br3.readLine();
            while (str3 != null){
                str3 = str3.toLowerCase();
                queries1.add(str3);
                str3 = br3.readLine();
            }
            //read every single search conditions line and save in a ArrayList

            Query query = new Query();
            query.setQueries_array(queries1);
            ArrayList<String> queries_search = new ArrayList<>();
            queries_search = query.ConvertFormat();
            //Get converted searching conditions syntax
            //Clear wrong syntax and return conditions which is available

            System.out.println("\n"+"The final searching query is here: "+"\n");
            for (int i=0;i<queries_search.size();){
                System.out.println(queries_search.get(i));
                i++;
            }
            System.out.println("*******************************************");
            System.out.println("\n"+"The result of searching is here: ");



            for (int i=0;i<queries_search.size();){
                //This for loop for searching condition
                Query qry = new Query();
                qry.setScope(qry.ConvertScope(queries_search.get(i)));
                //Convert the queries scope to Query object
                qry.setConditions(qry.ConvertConditions(queries_search.get(i)));
                //Convert the queries conditions to Query object

                ArrayList<Wine> RightScopeWine = new ArrayList<>();
                //used for save wine sample which meet the scope from query
                ArrayList<Wine> temp = new ArrayList<>();

                for (int j=0;j<wines.size();){
                    //This loop is used for get wine sample with specific scope
                    if (qry.getScope().contains(wines.get(j).getWineKind())){
                        RightScopeWine.add(wines.get(j));
                    }
                    j++;
                }

                for (int j=0;j<RightScopeWine.size();){
                    //This loop is used for searching wine sample with specific conditions

                    ArrayList<ArrayList<Wine>> SamplesOfSamples = new ArrayList<>();


                    for (int n=0;n<qry.getConditions().size();){
                        Pattern pattern = Pattern.compile("\\d+\\.\\d+");
                        Matcher matcher = pattern.matcher(qry.getConditions().get(n));
                        String num_String ="";
                        if (matcher.find()) {
                            num_String = matcher.group(0);
                        }
                        //Use RegEX detect number is double number or not, then save it in num
                        else {
                            Pattern pattern1 = Pattern.compile("\\d+");
                            Matcher matcher1 = pattern1.matcher(qry.getConditions().get(n));
                            if (matcher1.find()){
                                num_String = matcher1.group(0);
                            }
                        }
                        //if not, detect number is integer or not, then save it in num
                        double num;
                        num = Double.valueOf(num_String);
                        //Transfer String to double


                        ArrayList<Wine> SamplesMeetCondition = new ArrayList<>();
                        //This ArrayList is used for save the samples meet the conditions from query
                        //Attention: need first search <=, then<; and first >=, then >.
                        //because first meeting < condition will not judge whether meeting <= or not.
                        if (qry.getConditions().get(n).contains("qual")){
                            if (qry.getConditions().get(n).contains(">=")){
                                if (RightScopeWine.get(j).getQual() >= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains(">")){
                                if (RightScopeWine.get(j).getQual() > num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("!=")){
                                if (RightScopeWine.get(j).getQual() != num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("=")){
                                if (RightScopeWine.get(j).getQual() == num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<=")){
                                if (RightScopeWine.get(j).getQual() <= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<")){
                                if (RightScopeWine.get(j).getQual() < num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                        }

                        else if (qry.getConditions().get(n).contains("alc")){
                            if (qry.getConditions().get(n).contains(">=")){
                                if (RightScopeWine.get(j).getAlc() >= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains(">")){
                                if (RightScopeWine.get(j).getAlc() > num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("!=")){
                                if (RightScopeWine.get(j).getAlc() != num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("=")){
                                if (RightScopeWine.get(j).getAlc() == num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<=")){
                                if (RightScopeWine.get(j).getAlc() <= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<")){
                                if (RightScopeWine.get(j).getAlc() < num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                        }

                        else if (qry.getConditions().get(n).contains("sulph")){
                            if (qry.getConditions().get(n).contains(">=")){
                                if (RightScopeWine.get(j).getSulph() >= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains(">")){
                                if (RightScopeWine.get(j).getSulph() > num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("!=")){
                                if (RightScopeWine.get(j).getSulph() != num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("=")){
                                if (RightScopeWine.get(j).getSulph() == num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<=")){
                                if (RightScopeWine.get(j).getSulph() <= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<")){
                                if (RightScopeWine.get(j).getSulph() < num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                        }

                        else if (qry.getConditions().get(n).contains("ph")){
                            //pay attention on the uppercase and lowercase of pH and ph
                            //This ph is after convert original to lowercase
                            if (qry.getConditions().get(n).contains(">=")){
                                if (RightScopeWine.get(j).getpH() >= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains(">")){
                                if (RightScopeWine.get(j).getpH() > num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("!=")){
                                if (RightScopeWine.get(j).getpH() != num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("=")){
                                if (RightScopeWine.get(j).getpH() == num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<=")){
                                if (RightScopeWine.get(j).getpH() <= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<")){
                                if (RightScopeWine.get(j).getpH() < num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                        }

                        else if (qry.getConditions().get(n).contains("dens")){
                            if (qry.getConditions().get(n).contains(">=")){
                                if (RightScopeWine.get(j).getDens() >= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains(">")){
                                if (RightScopeWine.get(j).getDens() > num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("!=")){
                                if (RightScopeWine.get(j).getDens() != num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("=")){
                                if (RightScopeWine.get(j).getDens() == num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<=")){
                                if (RightScopeWine.get(j).getDens() <= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<")){
                                if (RightScopeWine.get(j).getDens() < num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                        }

                        else if (qry.getConditions().get(n).contains("t_sulf")){
                            if (qry.getConditions().get(n).contains(">=")){
                                if (RightScopeWine.get(j).getT_sulf() >= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains(">")){
                                if (RightScopeWine.get(j).getT_sulf() > num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("!=")){
                                if (RightScopeWine.get(j).getT_sulf() != num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("=")){
                                if (RightScopeWine.get(j).getT_sulf() == num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<=")){
                                if (RightScopeWine.get(j).getT_sulf() <= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<")){
                                if (RightScopeWine.get(j).getT_sulf() < num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                        }

                        else if (qry.getConditions().get(n).contains("f_sulf")){
                            if (qry.getConditions().get(n).contains(">=")){
                                if (RightScopeWine.get(j).getF_sulf() >= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains(">")){
                                if (RightScopeWine.get(j).getF_sulf() > num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("!=")){
                                if (RightScopeWine.get(j).getF_sulf() != num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("=")){
                                if (RightScopeWine.get(j).getF_sulf() == num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<=")){
                                if (RightScopeWine.get(j).getF_sulf() <= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<")){
                                if (RightScopeWine.get(j).getF_sulf() < num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                        }

                        else if (qry.getConditions().get(n).contains("chlorid")){
                            if (qry.getConditions().get(n).contains(">=")){
                                if (RightScopeWine.get(j).getChlorid() >= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains(">")){
                                if (RightScopeWine.get(j).getChlorid() > num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("!=")){
                                if (RightScopeWine.get(j).getChlorid() != num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("=")){
                                if (RightScopeWine.get(j).getChlorid() == num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<=")){
                                if (RightScopeWine.get(j).getChlorid() <= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<")){
                                if (RightScopeWine.get(j).getChlorid() < num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                        }

                        else if (qry.getConditions().get(n).contains("r_sugar")){
                            if (qry.getConditions().get(n).contains(">=")){
                                if (RightScopeWine.get(j).getR_sugar() >= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains(">")){
                                if (RightScopeWine.get(j).getR_sugar() > num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("!=")){
                                if (RightScopeWine.get(j).getR_sugar() != num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("=")){
                                if (RightScopeWine.get(j).getR_sugar() == num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<=")){
                                if (RightScopeWine.get(j).getR_sugar() <= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<")){
                                if (RightScopeWine.get(j).getR_sugar() < num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                        }

                        else if (qry.getConditions().get(n).contains("c_acid")){
                            if (qry.getConditions().get(n).contains(">=")){
                                if (RightScopeWine.get(j).getC_acid() >= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains(">")){
                                if (RightScopeWine.get(j).getC_acid() > num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("!=")){
                                if (RightScopeWine.get(j).getC_acid() != num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("=")){
                                if (RightScopeWine.get(j).getC_acid() == num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<=")){
                                if (RightScopeWine.get(j).getC_acid() <= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<")){
                                if (RightScopeWine.get(j).getC_acid() < num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                        }

                        else if (qry.getConditions().get(n).contains("v_acid")){
                            if (qry.getConditions().get(n).contains(">=")){
                                if (RightScopeWine.get(j).getV_acid() >= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains(">")){
                                if (RightScopeWine.get(j).getV_acid() > num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("!=")){
                                if (RightScopeWine.get(j).getV_acid() != num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("=")){
                                if (RightScopeWine.get(j).getV_acid() == num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<=")){
                                if (RightScopeWine.get(j).getV_acid() <= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<")){
                                if (RightScopeWine.get(j).getV_acid() < num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                        }

                        else if (qry.getConditions().get(n).contains("f_acid")){
                            if (qry.getConditions().get(n).contains(">=")){
                                if (RightScopeWine.get(j).getF_acid() >= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains(">")){
                                if (RightScopeWine.get(j).getF_acid() > num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("!=")){
                                if (RightScopeWine.get(j).getF_acid() != num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("=")){
                                if (RightScopeWine.get(j).getF_acid() == num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<=")){
                                if (RightScopeWine.get(j).getF_acid() <= num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                            else if (qry.getConditions().get(n).contains("<")){
                                if (RightScopeWine.get(j).getF_acid() < num){
                                    SamplesMeetCondition.add(RightScopeWine.get(j));
                                }
                            }
                        }

                        if (SamplesMeetCondition.size() != 0){
                            SamplesOfSamples.add(SamplesMeetCondition);
                        }
                        n++;
                    }

                    if (SamplesOfSamples.size() == qry.getConditions().size()){
                        temp.add(RightScopeWine.get(j));
                    }
                    j++;
                }

                System.out.println("\n"+"--------------------------------------");
                System.out.println("No "+(i+1));
                System.out.println("Query: "+queries_search.get(i));
                System.out.println("Scope: "+qry.getScope());
                System.out.println("Searching Result: ");
                if (temp.size()==0){
                    System.out.println("No wine sample meeting this conditions");
                }
                else {
                    for (int m = 0; m < temp.size(); ) {
                        System.out.println(temp.get(m).getID() + " " + temp.get(m).getWineKind()
                                + " [Qual: " + temp.get(m).getQual() + "]"
                                + " [Alc: " + temp.get(m).getAlc() + "]"
                                + " [Sulph: " + temp.get(m).getSulph() + "]"
                                + " [pH: " + temp.get(m).getpH() + "]"
                                + " [Dens: " + temp.get(m).getDens() + "]"
                                + " [T_sulf: " + temp.get(m).getT_sulf() + "]"
                                + " [F_sulf: " + temp.get(m).getF_sulf() + "]"
                                + " [Chlorid: " + temp.get(m).getChlorid() + "]"
                                + " [R_sugar: " + temp.get(m).getR_sugar() + "]"
                                + " [C_acid: " + temp.get(m).getC_acid() + "]"
                                + " [V_acid: " + temp.get(m).getV_acid() + "]"
                                + " [F_acid: " + temp.get(m).getF_acid() + "]");
                        m++;
                    }
                }
                i++;
            }
            System.out.println("\n"+"Searching Ended.");

        } catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
        } catch (IOException io){
            io.printStackTrace();
        }
    }
}