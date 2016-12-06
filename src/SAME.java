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
public class Same{
    public static List<LineItem> lines = new ArrayList<LineItem>();//Список вывода результата
    public List<String>          list1 = new ArrayList<String>();//Список строк из файла №1
    public List<String>          list2 = new ArrayList<String>();//Список строк из файла №2
    public boolean  permitAddedRemoved = true;//permitAdded_Removed = true - pазрешить ADDED или REMOVED
    public boolean             mistake = false;//mistake = true - ошибка - операции ADDED - REMOVED невозможно разделить операцией SAME
    public static void main(String[] args) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName1 = reader.readLine();
        String fileName2 = reader.readLine();
        reader.close();
        Same same = new Same();
        same.list1 = same.createList(fileName1);
        same.list2 = same.createList(fileName2);
        same.updateList();
        /*same.printResult(lines)*/;
    }
    public List<String> createList(String fileName) throws IOException{
        ArrayList<String> list = new ArrayList<>();
        BufferedReader filereader = new BufferedReader(new FileReader(fileName));
        String line;
        while((line = filereader.readLine()) != null)
        {
            list.add(line);
        }
        filereader.close();
        return list;
    }
    public void updateList(){
        String line1 = "";
        String line2 = "";
        while (list1.size() > 0 && list2.size() > 0 && !mistake){
            line1 = list1.get(0);
            line2 = list2.get(0);
            boolean permitRemoved = permitAddedRemoved && list1.size() > 1 && list1.get(1).equals(line2);
            boolean permitAdded   = permitAddedRemoved && list2.size() > 1 && list2.get(1).equals(line1);
            boolean permitSame    = line1.equals(line2);
            if (permitRemoved) {
                makeAddedRemoved(Type.REMOVED);
            }
            else if (permitAdded) {
                makeAddedRemoved(Type.ADDED);
            }
            else if (permitSame) {
                makeSame();
            }
            else {
                mistake = true;
            }
        }
        /*Если один из листов пуст, еще возможна одна операция ADDED или REMOVED*/
        removeLast();
    }
    public void removeLast(){
        //Если пуст лист2, а лист1 нет
        if (permitAddedRemoved && !list1.isEmpty() && !mistake){
            makeAddedRemoved(Type.REMOVED);
        }
        //Если пуст лист1, а лист2 нет
        if (permitAddedRemoved && !list2.isEmpty() && !mistake){
            makeAddedRemoved(Type.ADDED);
        }
        /*Если после этого один из листов все еще не пуст - ошибка, т.к. SAME уже невозможно (один лист пуст, другой - нет)*/
        if (!list1.isEmpty() || !list2.isEmpty()){
            mistake = true;
        }
    }
    public void makeSame(){
        lines.add(new LineItem(Type.SAME, list1.get(0)));
        list1.remove(0);
        list2.remove(0);
        permitAddedRemoved = true;
    }
    public void makeAddedRemoved(Type type){
        if (type.equals(Type.ADDED)){
            lines.add(new LineItem(Type.ADDED, list2.get(0)));
            list2.remove(0);
        }
        else{
            lines.add(new LineItem(Type.REMOVED, list1.get(0)));
            list1.remove(0);
        }
        permitAddedRemoved = false;
    }
    public void printResult(List<LineItem> list){
        for (LineItem l : list){
            System.out.println(l.type + " " + l.line);
        }
        if (mistake) System.out.println("Mistake file");
    }
    public static enum Type{
        ADDED,        //добавлена новая строка
        REMOVED,      //удалена строка
        SAME          //без изменений
    }
    public static class LineItem{
        public Type type;
        public String line;
        public LineItem(Type type, String line){
            this.type = type;
            this.line = line;
        }
    }
}