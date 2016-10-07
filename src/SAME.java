import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*
 Файлы содержат строки, file2 является обновленной версией file1, часть строк совпадают.
 Нужно создать объединенную версию строк, записать их в список lines
 Операции ADDED и REMOVED не могут идти подряд, они всегда разделены SAME
 Пример:
 оригинальный   редактированный    общий
 file1:         file2:             результат:(lines)

 строка1        строка1            SAME строка1
 строка2                           REMOVED строка2
 строка3        строка3            SAME строка3
 строка4                           REMOVED строка4
 строка5        строка5            SAME строка5
 строка0                           ADDED строка0
 строка1        строка1            SAME строка1
 строка2                           REMOVED строка2
 строка3        строка3            SAME строка3
 строка5                           ADDED строка5
 строка4        строка4            SAME строка4
 строка5                           REMOVED строка5
 */
public class Same
{
    public static List<LineItem> lines = new ArrayList<LineItem>();
    public static List<String> list1 = new ArrayList<String>();//Список строк из файла №1
    public static List<String> list2 = new ArrayList<String>();//Список строк из файла №2
    public static boolean operation = true;//operation = true - pазрешить ADDED или REMOVED
    public static void main(String[] args) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String filename1 = reader.readLine();
        String filename2 = reader.readLine();
        reader.close();

        list1 = makelist(filename1);
        list2 = makelist(filename2);

        String line1 = "";
        String line2 = "";

        for (int i = 0; i < list1.size() || i < list2.size();)
        {
            line1 = list1.isEmpty()? null   : list1.get(i);
            line2 = list2.isEmpty()? ""     : list2.get(i);
            boolean listsnotempty = !list1.isEmpty() && !list2.isEmpty();

            boolean permitREMOVED = operation
                    && (list2.isEmpty()
                    || (listsnotempty && ((i + 1) < list1.size() && list1.get(i + 1).equals(line2))));
            boolean permitADD     = operation
                    && (list1.isEmpty()
                    || (listsnotempty && ((i + 1) < list2.size() && list2.get(i + 1).equals(line1))));
            boolean permitSAME    = listsnotempty && line1.equals(line2);

            if (permitREMOVED) {
                makeREMOVED(i);
            }
            else if (permitADD) {
                makeADDED(i);
            }
            else if (permitSAME) {
                makeSAME(i);
            }
            else {
                System.out.println("Данные в файлах не корректны");
                break;
            }
        }
        for (LineItem l : lines) {
            System.out.println(l.type + " " + l.line);
        }
    }
    public static ArrayList<String> makelist(String filename) throws IOException
    {
        ArrayList<String> list = new ArrayList<String>();
        BufferedReader filereader = new BufferedReader(new FileReader(filename));
        String line;
        while((line = filereader.readLine()) != null)
        {
            list.add(line);
        }
        filereader.close();
        return list;
    }
    public static void makeSAME(int j)
    {
        lines.add(new LineItem(Type.SAME, list1.get(j)));
        list1.remove(j);
        list2.remove(j);
        operation = true;
    }
    public static void makeADDED(int j)
    {
        lines.add(new LineItem(Type.ADDED, list2.get(j)));
        list2.remove(j);
        operation = false;
    }
    public static void makeREMOVED(int j)
    {
        lines.add(new LineItem(Type.REMOVED, list1.get(j)));
        list1.remove(j);
        operation = false;
    }
    public static enum Type
    {
        ADDED,        //добавлена новая строка
        REMOVED,      //удалена строка
        SAME          //без изменений
    }
    public static class LineItem
    {
        public Type type;
        public String line;
        public LineItem(Type type, String line)
        {
            this.type = type;
            this.line = line;
        }
    }
}
