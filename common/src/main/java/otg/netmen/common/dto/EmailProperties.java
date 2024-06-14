package otg.netmen.common.dto;

import lombok.Data;
import lombok.Value;

@Componet
@CP(prefix = "email")
@Data
public class EmailProperties {
    @Value("${email.user}")
    //发件人邮箱
    public String user;
    //发件人邮箱授权码
    public String code = "";
    //发件人邮箱对应的服务器域名，如果是163邮箱：smtp.163.com   qq邮箱：smtp.qq.com
    public String host = "smtp.qq.com";
    //身份验证开关
    public boolean auth = true;
}
