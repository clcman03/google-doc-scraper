import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.*;

class TableEntry {
    int x;
    int y;
    String entry;
    
    public TableEntry(int aX, int aY, String aEntry){
        x = aX;
        y = aY;
        entry = aEntry;
    }
}

class Table {
    ArrayList<TableEntry> entries;

    public Table(){
        entries = new ArrayList<>();
    }

    public Table urlToTable(String inputUrl) throws Exception {
        Table retTable = new Table();

        URL requestUrl = new URL(inputUrl);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(requestUrl.openStream()))) {
            String line;
            String text = "";

            while ((line = in.readLine()) != null) {
                if(line.contains("<td")){
                    text += line;
                }
            }

            String rowhead = "<tr class=";

            String[] rowArr = text.split(rowhead);

            String variableExtractionRegex = "(\\d+)</span></p></td><td class=\"c[0-9]\" colspan=\"1\" rowspan=\"1\"><p class=\"c[0-9]\"><span class=\"c[0-9]\">(.)</span></p></td><td class=\"c[0-9]\" colspan=\"1\" rowspan=\"1\"><p class=\"c[0-9]\"><span class=\"c[0-9]\">(\\d+)";
            Pattern regexPattern = Pattern.compile(variableExtractionRegex);

            for (String rStr : rowArr){
                if (!rStr.contains("google")){
                    Matcher matcher = regexPattern.matcher(rStr);
                    while (matcher.find()){
                        int xCoord = Integer.parseInt(matcher.group(1));
                        int yCoord = Integer.parseInt(matcher.group(3));

                        retTable.entries.add(new TableEntry(xCoord, yCoord, matcher.group(2)));
                    }
                }
                
            }

        }

        

        return retTable;
    }

    public Table sortTable(){
        class TableComparator implements Comparator<TableEntry>{

         public int compare(TableEntry t1, TableEntry t2){
            if (t1.x == t2.x){
                return (t1.y - t2.y);
            }
              return (t1.x - t2.x);
         }
        }

        this.entries.sort(new TableComparator());

        return this;
    }

    public void printTable(){
        int maxX = 0;
        int maxY = 0;

        for (TableEntry te : this.entries){
            maxX = Math.max(maxX, te.x);
            maxY = Math.max(maxY, te.y);
        }

        String[][] printStrings = new String[maxX+1][maxY+1];
        
        for(int i = 0; i <= maxX; i++){
            for(int j = 0; j <= maxY; j++){
                printStrings[i][j] = " ";
            }
        }

        for (TableEntry te : this.entries){
            printStrings[te.x][te.y] = te.entry;
        }

        for(int i = 0; i <= maxY; i++){
            for(int j = 0; j <= maxX; j++){
                System.out.print(printStrings[j][i]);
            }
            System.out.println("");
        }

    }
}

class fun {
    public static void main(String[] args) throws Exception {
        String url = " https://docs.google.com/document/d/e/2PACX-1vSvM5gDlNvt7npYHhp_XfsJvuntUhq184By5xO_pA4b_gCWeXb6dM6ZxwN8rE6S4ghUsCj2VKR21oEP/pub";
        Table t = new Table();
        t = t.urlToTable(url);
        //t = t.sortTable();
        t.printTable();
        
    }
    
}

