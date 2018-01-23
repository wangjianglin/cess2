package io.cess.sample; /**
 * Created by lin on 8/31/16.
 */
import io.cess.core.spring.JsonBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
public class SampleController {

    @Autowired
    private ServiceComponent service;

    @RequestMapping("/a.action")
//    @ResponseBody
    @JsonBody
    public User home() {
//        return "sample.Hello World!";
        User user = service.addUser();
        user.setEmail("===");
        return user;
    }

    @RequestMapping("/a2")
    @ResponseBody
    public User home2() {
//        return "sample.Hello World!";
        User user = service.addUser2();
        user.setEmail("===");
        return user;
    }

    @RequestMapping("/upload")
    @JsonBody
//    public String handleFileUpload(@RequestParam("file")MultipartFile file){
    public String handleFileUpload(@RequestParam("file")MultipartFile file){
        if(!file.isEmpty()){
            try {
              /*
               * 这段代码执行完毕之后，图片上传到了工程的跟路径；
               * 大家自己扩散下思维，如果我们想把图片上传到 d:/files大家是否能实现呢？
               * 等等;
               * 这里只是简单一个例子,请自行参考，融入到实际中可能需要大家自己做一些思考，比如：
               * 1、文件路径；
               * 2、文件名；
               * 3、文件格式;
               * 4、文件大小的限制;
               */
              System.out.println("================ upload ================");
              File outFile = new File("build/"+file.getOriginalFilename());
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return"上传失败,"+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return"上传失败,"+e.getMessage();
            }
            return"上传成功";
        }else{
            return"上传失败，因为文件是空的.";
        }
    }

    @RequestMapping("/jsp")
    public String test2() {
        return "test2";
    }
}
