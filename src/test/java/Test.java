import com.mj.xr.bean.Contact;
import com.mj.xr.dao.ContactDao;
import com.mj.xr.dao.impl.ContactDaoImpl;
import com.mj.xr.util.DBUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Test {
    @org.junit.Test
    public  void init(){
        ContactDao test = new ContactDaoImpl();
        Random random = new Random();
        String sql = "UPDATE contact SET created_time=? WHERE id=?";
        Calendar time = Calendar.getInstance();
        for (int i = 1; i <= 52; i++) {
//            Contact contact = new Contact();
//            contact.setName("张三"+random.nextInt(100));
//            contact.setEmail(random.nextInt(1000)+"123@qq.com");
//            contact.setSubject("招聘"+random.nextInt(3000));
//            contact.setComment("代码搬运工"+random.nextInt(5000));
//            Date date = new Date();
//            date.setYear(2001+random.nextInt(12));
//            date.setMinutes(2+random.nextInt(30));
//            date.setDate(1+random.nextInt(30));
//            contact.setCreatedTime(date);
//            test.save(contact);

            time.set(2001+random.nextInt(30),
                    1+random.nextInt(12),
                    1+random.nextInt(30),
                    1+random.nextInt(60),
                    1+random.nextInt(60),
                    1+random.nextInt(60)
            );
            DBUtil.getTpl().update(sql, time, i);
        }
    }
}
