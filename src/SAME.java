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
public class SAME
{
    public static List<LineItem> lines = new ArrayList<LineItem>();
    public static void main(String[] args) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String filename1 = reader.readLine();
        String filename2 = reader.readLine();
        reader.close();

        ArrayList<String> list1 = makelist(filename1);
        ArrayList<String> list2 = makelist(filename2);
        String line1;
        String line2;
        Type type;

        boolean operation = true;
        for (int i = 0; i < list1.size() || i < list2.size();)
        {
            if (list1.isEmpty() && operation)
            {
                type = Type.ADDED;
                lines.add(new LineItem(type, list2.get(i)));
                list2.remove(i);
                operation = false;
            }
            else if (list2.isEmpty() && operation)
            {
                type = Type.REMOVED;
                lines.add(new LineItem(type, list1.get(i)));
                list1.remove(i);
                operation = false;
            }
            else if (!list1.isEmpty() && !list2.isEmpty())
            {
                line1 = list1.get(i);
                line2 = list2.get(i);
                if (line1.equals(line2))
                {
                    type = Type.SAME;
                    lines.add(new LineItem(type, line1));
                    list1.remove(i);
                    list2.remove(i);
                    operation = true;
                }
                else if ((i + 1) < list1.size() && operation && list1.get(i + 1).equals(line2))
                {
                    type = Type.REMOVED;
                    lines.add(new LineItem(type, line1));
                    list1.remove(i);
                    operation = false;
                }
                else if ((i + 1) < list2.size() && operation && list2.get(i + 1).equals(line1))
                {
                    type = Type.ADDED;
                    lines.add(new LineItem(type, line2));
                    list2.remove(i);
                    operation = false;
                }
                else
                {
                    System.out.println("BREAK");
                    break;
                }
            }
        }
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
}
