import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 阿里云短信验证
 *
 * @author Administrator
 * @create 2018-01-18 10:57
 */
public class SendMsg {

    //初始化ascClient需要的几个参数
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAIH7ml2xwoFB3f";//你的accessKeyId,参考本文档步骤2
    static final String accessKeySecret = "qy9HfdKbr5yCpZXx6VUoLpb6YxTbUM";//你的accessKeySecret，参考本文档步骤2

    //短信签名,例：云通信

    static String SignName = "宋仁杰";

    //TemplateCode(必须):短信模板ID
    static String TemplateCode = "SMS_122296182";

    /*
     * 入参列表
     * PhoneNumbers(必须):短信接收号码,支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
     * SignName(必须):短信签名,例：云通信
     * TemplateCode(必须):短信模板ID
     * TemplateParam(可选):短信模板变量替换JSON串,友情提示:如果JSON中需要带换行符,请参照标准的JSON协议。
     * SmsUpExtendCode:上行短信扩展码,无特殊需要此字段的用户请忽略此字段
     * OutId:外部流水扩展字段
     */
    public static SendSmsResponse sendSms(String phoneNum, String random){
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNum);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(SignName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(TemplateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //  request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
        request.setTemplateParam("{\"code\":\""+random+"\"}");
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = new SendSmsResponse();
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
        } catch (ServerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (ClientException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return sendSmsResponse;
    }




    public static void main(String[] args) {
        String phoneNum = "18855105006";
        //生成6位随机数
        String random = Integer.toString((int)((Math.random()*9+1)*100000));
        //返回给客户端的数据
        String dataJson = "{\"code\":\""+random+"\"}";

        SendSmsResponse response = sendSms(phoneNum,random);

        if(null!=response.getCode() && ("OK").equals(response.getCode())) {
            //请求成功，将dataJson数据返回给客户端就可以了
            System.out.println(dataJson);
        }

        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());

    }
}
