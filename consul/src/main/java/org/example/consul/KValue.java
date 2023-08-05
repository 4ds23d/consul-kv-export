package org.example.consul;


import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@With
public class KValue {
    @SerializedName("CreateIndex")
    private Integer index;
    @SerializedName("ModifyIndex")
    private Integer modifyIndex;
    @SerializedName("LockIndex")
    private Integer lockIndex;
    @SerializedName("Flags")
    private Integer flags;
    @SerializedName("Key")
    private Key key;
    @SerializedName("Value")
    private String value;

    public String getValueAsString() {
        if (value == null) {
            return null;
        }
        try {
            var dec = Base64.getDecoder();
            var strdec = dec.decode(value);
            return new String(strdec, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean hasValue() {
        return value != null;
    }
}
