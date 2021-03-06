import java.io.*;
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
public class Same {
    public static List<LineItem> lines = new ArrayList<LineItem>();//Список вывода результата
    public List<String>          list1;//Список строк из файла №1
    public List<String>          list2;//Список строк из файла №2
    public Same() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName1 = reader.readLine();
        String fileName2 = reader.readLine();
        reader.close();
        list1 = createList(fileName1);
        list2 = createList(fileName2);
    }
    public static void main(String[] args) throws IOException {
        Same same = new Same();
        boolean mistakeInFiles = same.updateList();
        /*same.printResult(lines, mistakeInFiles);*/
    }
    public List<String> createList(String fileName) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        BufferedReader filereader = new BufferedReader(new FileReader(fileName));
        String line;
        while((line = filereader.readLine()) != null) {
            list.add(line);
        }
        filereader.close();
        return list;
    }
    public boolean updateList() {
        String line1 = "";
        String line2 = "";
        boolean permitAddedRemoved = true;//permitAdded_Removed = true - pазрешить ADDED или REMOVED
        boolean            mistake = false;//mistake = true - ошибка - операции ADDED - REMOVED невозможно разделить операцией SAME
        while (list1.size() > 0 && list2.size() > 0) {
            line1 = list1.get(0);
            line2 = list2.get(0);
            boolean permitRemoved = permitAddedRemoved && list1.size() > 1 && list1.get(1).equals(line2);
            boolean permitAdded   = permitAddedRemoved && list2.size() > 1 && list2.get(1).equals(line1);
            boolean permitSame    = line1.equals(line2);
            if (!(permitRemoved || permitAdded || permitSame))
                return true; //mistake = true
            if (permitSame) {
                makeSame();
                permitAddedRemoved = true;
            }
            else {
                makeAddedRemoved(permitRemoved ? false : true);//Type.ADDED = true, Type.REMOVED = false
                permitAddedRemoved = false;
            }
        }
        /*Если один из листов пуст, еще возможна одна операция ADDED или REMOVED*/
        if (permitAddedRemoved)
            mistake = removeLast();
        return mistake;
    }
    public boolean removeLast() {
        //Если пуст лист2, а лист1 нет, - операция REMOVED (false), иначе - ADDED (true)
        if (!list1.isEmpty())
            makeAddedRemoved(false);
        else makeAddedRemoved(true);
        /*Если после этого один из листов все еще не пуст - ошибка, т.к. SAME уже невозможно (один лист пуст, другой - нет)*/
        if (!list1.isEmpty() || !list2.isEmpty()) {
            return true;
        }
        return false;
    }
    public void makeSame() {
        lines.add(new LineItem(Type.SAME, list1.get(0)));
        list1.remove(0);
        list2.remove(0);
    }
    public void makeAddedRemoved(boolean actualType) {
        //actualType = true - ADDED, actualType = false - REMOVED
        if (actualType){
            lines.add(new LineItem(Type.ADDED, list2.get(0)));
            list2.remove(0);
        }
        else {
            lines.add(new LineItem(Type.REMOVED, list1.get(0)));
            list1.remove(0);
        }
    }
    public void printResult(List<LineItem> list, boolean mistake) {
        for (LineItem l : list){
            System.out.println(l.type + " " + l.line);
        }
        if (mistake) System.out.println("Mistake file");
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