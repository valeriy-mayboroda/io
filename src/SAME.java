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
    public static List<LineItem> lines = new ArrayList<LineItem>();//Список вывода результата
    public static List<String>   list1 = new ArrayList<String>();//Список строк из файла №1
    public static List<String>   list2 = new ArrayList<String>();//Список строк из файла №2
    public static boolean        permitAdded_Removed = true;//permitAdded_Removed = true - pазрешить ADDED или REMOVED
    public static void main(String[] args) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName1 = reader.readLine();
        String fileName2 = reader.readLine();
        reader.close();
        list1 = makelist(fileName1);
        list2 = makelist(fileName2);
        String line1 = "";
        String line2 = "";
        while (list1.size() > 0 || list2.size() > 0) {
            line1 = list1.isEmpty()? null : list1.get(0);
            line2 = list2.isEmpty()? null : list2.get(0);
            boolean listsNotempty = !list1.isEmpty() && !list2.isEmpty();
            boolean permitRemoved = permitAdded_Removed
                    && (list2.isEmpty()
                    || (listsNotempty && (list1.size() > 1 && list1.get(1).equals(line2))));
            boolean permitAdded   = permitAdded_Removed
                    && (list1.isEmpty()
                    || (listsNotempty && (list2.size() > 1 && list2.get(1).equals(line1))));
            boolean permitSame    = listsNotempty && line1.equals(line2);
            if (permitRemoved) {
                makeRemoved();
            }
            else if (permitAdded) {
                makeAdded();
            }
            else if (permitSame) {
                makeSame();
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
    public static ArrayList<String> makelist(String fileName) throws IOException {
        ArrayList<String> list = new ArrayList<String>();
        BufferedReader filereader = new BufferedReader(new FileReader(fileName));
        String line;
        while((line = filereader.readLine()) != null)
        {
            list.add(line);
        }
        filereader.close();
        return list;
    }
    public static void makeSame() {
        lines.add(new LineItem(Type.SAME, list1.get(0)));
        list1.remove(0);
        list2.remove(0);
        permitAdded_Removed = true;
    }
    public static void makeAdded() {
        lines.add(new LineItem(Type.ADDED, list2.get(0)));
        list2.remove(0);
        permitAdded_Removed = false;
    }
    public static void makeRemoved() {
        lines.add(new LineItem(Type.REMOVED, list1.get(0)));
        list1.remove(0);
        permitAdded_Removed = false;
    }
    public static enum Type {
        ADDED,        //добавлена новая строка
        REMOVED,      //удалена строка
        SAME          //без изменений
    }
    public static class LineItem {
        public Type type;
        public String line;
        public LineItem(Type type, String line) {
            this.type = type;
            this.line = line;
        }
    }
}