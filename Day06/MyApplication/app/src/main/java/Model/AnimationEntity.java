package Model;


import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class AnimationEntity {
    private String name;
    private String gif;

    // Dùng annotation nếu tên JSON khác tên biến
    @SerializedName("static")
    private String staticUrl;

    public String getName() {
        return name;
    }
    private String decodeUrl(String encodedUrl) {
        if (encodedUrl == null) {
            return null;
        }

        try {
            return URLDecoder.decode(encodedUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String getStaticUrl() {
        return decodeUrl(staticUrl);
    }

    public String getGif() {
        return decodeUrl(gif);
    }
}
