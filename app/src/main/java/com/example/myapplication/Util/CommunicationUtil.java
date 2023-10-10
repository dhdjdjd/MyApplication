package com.example.myapplication.Util;

import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;

import com.example.myapplication.bean.AppInfo;
import com.example.myapplication.bean.CallLogRecord;
import com.example.myapplication.bean.Contact;
import com.example.myapplication.bean.SmsContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressLint("DefaultLocale")
public class CommunicationUtil {
    private final static String TAG = "CommunicationUtil";
    private static Uri mContactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private static String[] mContactColumn = new String[]{
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

    // 读取手机保存的联系人数量
    public static int readPhoneContacts(ContentResolver resolver) {
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = resolver.query(mContactUri, mContactColumn, null, null, null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.phone = cursor.getString(0).replace("+86", "").replace(" ", "");
            contact.name = cursor.getString(1);
            Log.d(TAG, contact.name + " " + contact.phone);
            contactList.add(contact);
        }
        cursor.close();
        return contactList.size();
    }

    // 读取sim卡保存的联系人数量
    public static int readSimContacts(ContentResolver resolver) {
        Uri simUri = Uri.parse("content://icc/adn");
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        Cursor cursor = resolver.query(simUri, mContactColumn, null, null, null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.phone = cursor.getString(0).replace("+86", "").replace(" ", "");
            contact.name = cursor.getString(1);
            Log.d(TAG, contact.name + " " + contact.phone);
            contactList.add(contact);
        }
        cursor.close();
        return contactList.size();
    }

    // 往手机通讯录添加一个联系人信息（包括姓名、电话号码、电子邮箱）
    /*public static void addContacts(ContentResolver resolver, Contact contact) {
        // 构建一个指向系统联系人提供器的Uri对象
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentValues values = new ContentValues(); // 创建新的配对
        // 往 raw_contacts 添加联系人记录，并获取添加后的联系人编号
        long contactId = ContentUris.parseId(resolver.insert(raw_uri, values));
        // 构建一个指向系统联系人数据的Uri对象
        Uri uri = Uri.parse("content://com.android.contacts/data");
        ContentValues name = new ContentValues(); // 创建新的配对
        name.put("raw_contact_id", contactId); // 往配对添加联系人编号
        // 往配对添加“姓名”的数据类型
        name.put("mimetype", "vnd.android.cursor.item/name");
        name.put("data2", contact.name); // 往配对添加联系人的姓名
        resolver.insert(uri, name); // 往提供器添加联系人的姓名记录
        ContentValues phone = new ContentValues(); // 创建新的配对
        phone.put("raw_contact_id", contactId); // 往配对添加联系人编号
        // 往配对添加“电话号码”的数据类型
        phone.put("mimetype", "vnd.android.cursor.item/phone_v2");
        phone.put("data1", contact.phone); // 往配对添加联系人的电话号码
        phone.put("data2", "2"); // 联系类型。1表示家庭，2表示工作
        resolver.insert(uri, phone); // 往提供器添加联系人的号码记录
        ContentValues email = new ContentValues(); // 创建新的配对
        email.put("raw_contact_id", contactId); // 往配对添加联系人编号
        // 往配对添加“电子邮箱”的数据类型
        email.put("mimetype", "vnd.android.cursor.item/email_v2");
        email.put("data1", contact.email); // 往配对添加联系人的电子邮箱
        email.put("data2", "2"); // 联系类型。1表示家庭，2表示工作
        resolver.insert(uri, email); // 往提供器添加联系人的邮箱记录
    }*/

    // 往手机通讯录一次性添加一个联系人信息（包括主记录、姓名、电话号码、电子邮箱）
    /*public static void addFullContacts(ContentResolver resolver, Contact contact) {
        // 构建一个指向系统联系人提供器的Uri对象
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        // 构建一个指向系统联系人数据的Uri对象
        Uri uri = Uri.parse("content://com.android.contacts/data");
        // 创建一个插入联系人主记录的内容操作器
        ContentProviderOperation op_main = ContentProviderOperation
                .newInsert(raw_uri).withValue("account_name", null).build();
        // 创建一个插入联系人姓名记录的内容操作器
        ContentProviderOperation op_name = ContentProviderOperation
                .newInsert(uri).withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/name")
                .withValue("data2", contact.name).build();
        // 创建一个插入联系人电话号码记录的内容操作器
        ContentProviderOperation op_phone = ContentProviderOperation
                .newInsert(uri).withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/phone_v2")
                .withValue("data2", "2").withValue("data1", contact.phone).build();
        // 创建一个插入联系人电子邮箱记录的内容操作器
        ContentProviderOperation op_email = ContentProviderOperation
                .newInsert(uri).withValueBackReference("raw_contact_id", 0)
                .withValue("mimetype", "vnd.android.cursor.item/email_v2")
                .withValue("data2", "2").withValue("data1", contact.email).build();
        // 声明一个内容操作器的列表，并将上面四个操作器添加到该列表中
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        operations.add(op_main);
        operations.add(op_name);
        operations.add(op_phone);
        operations.add(op_email);
        try {
            // 批量提交四个内容操作器所做的修改
            resolver.applyBatch("com.android.contacts", operations);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    //private static Uri mSmsUri = Uri.parse("content://sms"); //该地址表示所有短信，包括收件箱和发件箱
    //private static Uri mSmsUri = Uri.parse("content://sms/inbox"); //该地址为收件箱

    private static Uri mSmsUri;
    private static String[] mSmsColumn;

    // 读取指定号码发来的短信记录
    /*public static int readSms(ContentResolver resolver, String phone, int gaps) {
        mSmsUri = Telephony.Sms.Inbox.CONTENT_URI;
        mSmsColumn = new String[]{
                Telephony.Sms.ADDRESS, Telephony.Sms.PERSON,
                Telephony.Sms.BODY, Telephony.Sms.DATE,
                Telephony.Sms.TYPE};
        ArrayList<SmsContent> smsList = new ArrayList<SmsContent>();
        String selection = "";
        if (phone != null && phone.length() > 0) {
            selection = String.format("address='%s'", phone);
        }
        if (gaps > 0) {
            selection = String.format("%s%sdate>%d", selection,
                    (selection.length() > 0) ? " and " : "", System.currentTimeMillis() - gaps * 1000);
        }
        Cursor cursor = resolver.query(mSmsUri, mSmsColumn, selection, null, "date desc");
        while (cursor.moveToNext()) {
            SmsContent sms = new SmsContent();
            sms.address = cursor.getString(0);
            sms.person = cursor.getString(1);
            sms.body = cursor.getString(2);
            sms.date = DateUtil.formatDate(cursor.getLong(3));
            sms.type = cursor.getInt(4);  //type=1表示收到的短信，type=2表示发送的短信
            Log.d(TAG, sms.address + " " + sms.person + " " + sms.date + " " + sms.type + " " + sms.body);
            smsList.add(sms);
        }
        cursor.close();
        return smsList.size();
    }*/

    private static Uri mRecordUri = CallLog.Calls.CONTENT_URI;
    private static String[] mRecordColumn = new String[]{
            CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.TYPE,
            CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.NEW};

    // 读取所有的联系人信息
    public static List<Contact> readAllContacts(ContentResolver resolver) {
        List<Contact> contactList = new ArrayList<Contact>();
        // 查询结果按照联系人id降序排列，也就是越新的记录会排在越前面
        Cursor cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, "_id desc");
        int contactIdIndex = 0;
        int nameIndex = 0;

        if (cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.contactId = cursor.getString(contactIdIndex);
            contact.name = cursor.getString(nameIndex);
            contactList.add(contact);
        }
        cursor.close();

        for (int i = 0; i < contactList.size(); i++) {
            Contact contact = contactList.get(i);
            contact.phone = getColumn(resolver, contact.contactId,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.NUMBER);
            contact.email = getColumn(resolver, contact.contactId,
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Email.DATA);
            contactList.set(i, contact);
            Log.d(TAG, contact.contactId + " " + contact.name + " " + contact.phone + " " + contact.email);
        }
        return contactList;
    }

    private static String getColumn(ContentResolver resolver, String contactId,
                                    Uri uri, String selection, String column) {
        Cursor cursor = resolver.query(uri, null, selection + "=" + contactId, null, null);
        int index = 0;
        if (cursor.getCount() > 0) {
            index = cursor.getColumnIndex(column);
        }
        String value = "";
        while (cursor.moveToNext()) {
            value = cursor.getString(index);
        }
        cursor.close();
        return value;
    }

    public static List<String> getPkgListNew(Context context) {
        List<String> packages = new ArrayList<String>();
        try {
            List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES |
                    PackageManager.GET_SERVICES);
            for (PackageInfo info : packageInfos) {
                String pkg = info.packageName;
                packages.add(pkg);
            }
        } catch (Throwable t) {
            t.printStackTrace();;
        }
        return packages;
    }

    public static ArrayList<AppInfo> getAllAppInfo(Context ctx, boolean isFilterSystem) {
        ArrayList<AppInfo> appBeanList = new ArrayList<>();
        AppInfo bean = null;

        PackageManager packageManager = ctx.getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        for (PackageInfo p : list) {
            bean = new AppInfo();
            //bean.setIcon(p.applicationInfo.loadIcon(packageManager));
            bean.setLabel(packageManager.getApplicationLabel(p.applicationInfo).toString());
            bean.setPackage_name(p.applicationInfo.packageName);
            int flags = p.applicationInfo.flags;
            // 判断是否是属于系统的apk
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0 && isFilterSystem) {
//                bean.setSystem(true);
            } else {
                appBeanList.add(bean);
            }
        }
        return appBeanList;
    }

    public static List<CallLogRecord> getDataList(Context context) {
        // 1.获得ContentResolver
        ContentResolver resolver = context.getContentResolver();
        // 2.利用ContentResolver的query方法查询通话记录数据库
        /**
         * @param uri 需要查询的URI，（这个URI是ContentProvider提供的）
         * @param projection 需要查询的字段
         * @param selection sql语句where之后的语句
         * @param selectionArgs ?占位符代表的数据
         * @param sortOrder 排序方式
         *
         */
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                new String[] { CallLog.Calls.CACHED_NAME// 通话记录的联系人
                        , CallLog.Calls.NUMBER// 通话记录的电话号码
                        , CallLog.Calls.DATE// 通话记录的日期
                        , CallLog.Calls.DURATION// 通话时长
                        , CallLog.Calls.TYPE
                        , CallLog.Calls.GEOCODED_LOCATION}// 通话类型
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        // 3.通过Cursor获得数据
        List<CallLogRecord> list = new ArrayList<CallLogRecord>();

        int NumberIndex = 0;
        int NameIndex = 0;
        int DateIndex = 0;
        int DurationIndex = 0;
        int TypeIndex = 0;
        int GFLIndex = 0;

        if (cursor.getCount() > 0) {
             NumberIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
             NameIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
             DateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
             DurationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);
             TypeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
             GFLIndex = cursor.getColumnIndex(CallLog.Calls.GEOCODED_LOCATION);
        }

        while (cursor.moveToNext()) {
            CallLogRecord callLogRecord = new CallLogRecord();
            callLogRecord.name = cursor.getString(NameIndex);
            callLogRecord.number = cursor.getString(NumberIndex);
            long dateLong = cursor.getLong(DateIndex);
            callLogRecord.date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(dateLong));
            int Sduration = cursor.getInt(DurationIndex);
            callLogRecord.duration = (Sduration / 60) + "分钟";
            int type = cursor.getInt(TypeIndex);
            String typeString = "";
            switch (type) {
                case CallLog.Calls.INCOMING_TYPE:
                    typeString = "打入";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    typeString = "打出";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    typeString = "未接";
                    break;
                case CallLog.Calls.REJECTED_TYPE:
                    typeString = "挂断";
                    break;
                default:
                    break;
            }
            callLogRecord.type = typeString;
            callLogRecord.GfromLoc = cursor.getString(GFLIndex);
            list.add(callLogRecord);
        }
        return list;
    }
}
