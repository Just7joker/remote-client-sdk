package com.kkk.client;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 调用第三方接口服务端
 * @author lonelykkk
 * @email 2765314967@qq.com
 * @date 2024/10/27 11:03
 * @Version V1.0
 */
public class RemoteClient {
    private static final String TRANSLATION_URL = "https://api.lolimi.cn/API/qqfy/api.php";
    private static final String AI_CHAT_URL = "https://api.lolimi.cn/API/AI/wx.php";
    private static final String IMG_CODE_URL = "https://api.lolimi.cn/API/qrcode/api.php";

    RestTemplate restTemplate = new RestTemplate();

    /**
     * 英汉互译
     * @param msg 输入你需要翻译的内容
     * @return
     */
    public String getTranslation(String msg) {
        if (!StringUtils.hasText(msg)) {
            throw new RuntimeException("字符串不能为空");
        }
        try {
            String url = TRANSLATION_URL + "?msg=" + msg + "&type=json";
            String response = restTemplate.getForObject(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode textNode = rootNode.path("text");
            String text = textNode.asText();
            return text;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 二维码生成接口
     * @param msg 输入你要填入二维码的信息
     * @param size 输入二维码的图片大小
     * @return
     */
    public String getImgCode(String msg, Integer size) {
        try {
            String url = IMG_CODE_URL + "?text=" + msg + "&size="+size+"px";
            ResponseEntity<Resource> responseEntity = restTemplate.getForEntity(url, Resource.class);
            // 获取响应体中的资源
            Resource resource = responseEntity.getBody();
            if (resource != null) {
                // 获取resource目录路径
                Path resourcePath = Paths.get("src", "main", "resources", "imgCode/");
                // 如果imgCode目录不存在，则创建它
                if (!Files.exists(resourcePath)) {
                    Files.createDirectories(resourcePath);
                }
                // 创建文件输出流，用于保存图片
                Path imgPath = resourcePath.resolve(UUID.randomUUID().toString().replace("-","")+".png");
                InputStream inputStream = resource.getInputStream();
                FileOutputStream outputStream = new FileOutputStream(imgPath.toFile());

                // 缓冲区，用于存储读取的数据
                byte[] buffer = new byte[1024];
                int bytesRead;

                // 读取数据并写入文件
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // 数据传输完成,保存
                return imgPath.toAbsolutePath().toString();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * GPT远程调用接口
     * @param msg 出入你的问题
     * @return
     */
    public String getAiChat(String msg) {
        try {

            String url = "https://api.openai-hk.com/v1/chat/completions";

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");

            String json = "{\n" +
                    "  \"max_tokens\": 1200,\n" +
                    "  \"model\": \"gpt-3.5-turbo\",\n" +
                    "  \"temperature\": 0.8,\n" +
                    "  \"top_p\": 1,\n" +
                    "  \"presence_penalty\": 1,\n" +
                    "  \"messages\": [\n" +
                    "    {\n" +
                    "      \"role\": \"system\",\n" +
                    "      \"content\": \"You are ChatGPT, a large language model trained by OpenAI. Answer as concisely as possible.\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"role\": \"user\",\n" +
                    "      \"content\": "+ "\"" + msg + "\"" +"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            RequestBody body = RequestBody.create(mediaType, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "hk-l0ik8q100004543128fa47fb2113ad7a48be4de270945998")
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();
            String result = response.body().string();

            JSONObject jsonObject = new JSONObject(result);
            // 获取choices数组中的第一个元素
            JSONObject firstChoice = jsonObject.getJSONArray("choices").getJSONObject(0);

            // 从第一个元素中获取message对象
            JSONObject messageObject = firstChoice.getJSONObject("message");

            // 从message对象中获取content字段
            String content = messageObject.getStr("content");

            // 输出获取到的content内容
            System.out.println(content);
            return content;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
