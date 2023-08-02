package org.example.consul;


import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class KVValue {
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
        try {
            var dec= Base64.getDecoder();
            var strdec=dec.decode(value);
            return new String(strdec,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
