package com.newbiechen.classscheduledemo.net;

import com.newbiechen.classscheduledemo.R;
import com.newbiechen.classscheduledemo.entity.Course;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PC on 2016/9/26.
 * 解析HTML文档的类
 */
public class CourseParse {
    private static final String TAG = "LoginParse";
    private static final int [] COLOR = {R.color.hole_blue,R.color.light_blue,
    R.color.light_green,R.color.light_pink};

    public static boolean parseIsLoginSucceed(String data){
        Document doc = Jsoup.parse(data);
        //查看登陆文档。
        Element element = doc.getElementById("xhxm");
        if (element != null){
            return true;
        }
        return false;
    }

    /*解析个人课表*/
    public static List<Course> parsePersonal(String data){
        List<Course> courses = new ArrayList<>();
        Document doc = Jsoup.parse(data);
        //首先获取Table
        Element table = doc.getElementById("Table1");
        //然后获取table中的td节点
        Elements trs = table.select("tr");
        //移除不需要的参数，这里表示移除前两个数值。
        trs.remove(0);
        trs.remove(0);
        //遍历td节点
        for (int i=0; i<trs.size(); ++i){
            Element tr = trs.get(i);
            //获取tr下的td节点，要求
            Elements tds = tr.select("td[align]");
            //遍历td节点
            for(int j=0; j<tds.size(); ++j){
                Element td = tds.get(j);
                String str = td.text();
                //如果数值为空则不计算。
                if (str.length() != 1){
                    //解析文本数据
                    str = parsePersonalCourse(str);
                    Course course = new Course();
                    course.setClsName(str);
                    course.setDay(j+1);
                    course.setClsCount(Integer.valueOf(td.attr("rowspan")));
                    course.setClsNum(i+1);
                    Random random = new Random();
                    int num = random.nextInt(COLOR.length);
                    course.setColor(COLOR[num]);
                    courses.add(course);
                }
            }
        }
        return courses;
    }

    private static String parsePersonalCourse(String text){
        //正则表达式获取课名，和教室
        Pattern courseNamePattern = Pattern.compile("^.+?(\\s{1})");
        Matcher courseNameMatcher = courseNamePattern.matcher(text);
        courseNameMatcher.find();
        String str = courseNameMatcher.group(0);

        Pattern courseLocPattern = Pattern.compile("\\s{1}(\\d+)");
        Matcher courseLocMatcher = courseLocPattern.matcher(text);
        courseLocMatcher.find();
        String data = courseLocMatcher.group(0);

        return str+"@"+data;
    }
}
